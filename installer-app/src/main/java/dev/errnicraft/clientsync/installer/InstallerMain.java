package dev.errnicraft.clientsync.installer;

import dev.errnicraft.clientsync.model.DiffEntry;
import dev.errnicraft.clientsync.model.SyncManifest;
import dev.errnicraft.clientsync.sync.DiffCalculator;
import dev.errnicraft.clientsync.sync.FileInstaller;
import dev.errnicraft.clientsync.sync.InstallPlan;
import dev.errnicraft.clientsync.sync.LocalIndex;
import dev.errnicraft.clientsync.sync.ManifestFetcher;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.prefs.Preferences;

public class InstallerMain {

    private static final String CARD_ADDRESS = "address";
    private static final String CARD_SYNC = "sync";
    private static final String CARD_PROGRESS = "progress";
    private static final String CARD_VERSION_MISMATCH = "version_mismatch";

    private final Path gameDir = Path.of("").toAbsolutePath();
    private final Preferences prefs = Preferences.userNodeForPackage(InstallerMain.class);
    private final ExclusionStore exclusionStore = new ExclusionStore(prefs);
    private static final String PACK_VERSION_PREFIX = "pack_version_";

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    private AddressPanel addressPanel;
    private ProgressPanel progressPanel;

    private SyncManifest manifest;
    private List<DiffEntry> diff;
    private dev.errnicraft.clientsync.sync.LocalIndex localIndex;
    private String serverAddress;

    private enum Phase { IDLE, DOWNLOADING, APPLYING, DONE }
    private volatile Phase phase = Phase.IDLE;

    private String clientVersion = "unknown";
    private final String clientLoader;
    private final String clientLoaderVersion;

    public static void main(String[] args) {
        String version = args.length > 0 ? args[0] : "unknown";
        String loader = args.length > 1 ? args[1] : "unknown";
        String loaderVersion = args.length > 2 ? args[2] : "unknown";
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("[ClientSync-Installer] Uncaught exception in thread " + t.getName() + ":");
            e.printStackTrace();
        });
        SwingUtilities.invokeLater(() -> {
            try {
                new InstallerMain(version, loader, loaderVersion).start();
            } catch (Throwable e) {
                System.err.println("[ClientSync-Installer] Failed to start installer UI:");
                e.printStackTrace();
            }
        });
    }

    public InstallerMain(String clientVersion, String clientLoader, String clientLoaderVersion) {
        this.clientVersion = clientVersion;
        this.clientLoader = clientLoader;
        this.clientLoaderVersion = clientLoaderVersion;
    }

    private void start() {
        String lang = prefs.get("language", null);
        if (lang == null) lang = I18n.detectSystemLang();
        String loaderLabel;
        boolean hasLoaderInfo = clientLoader != null && !clientLoader.isBlank() && !"unknown".equals(clientLoader)
                && clientLoaderVersion != null && !clientLoaderVersion.isBlank() && !"unknown".equals(clientLoaderVersion);
        if (hasLoaderInfo) {
            loaderLabel = clientLoader + " (" + clientLoaderVersion + ")" + I18n.get(lang, "im_for_suffix") + clientVersion;
        } else {
            loaderLabel = clientVersion;
        }
        frame = new JFrame(I18n.get(lang, "im_title").replace("{loader}", loaderLabel));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                onCloseRequested();
            }
        });
        frame.setSize(480, 320);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(420, 280));

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        String savedAddress = prefs.get("server_address", "");
        String savedLang = prefs.get("language", null);
        List<String> history = loadAddressHistory();
        addressPanel = new AddressPanel(savedAddress, savedLang, history, this::onConnect, this::onLangChange, this::onAddressHistoryChanged);
        cards.add(addressPanel, CARD_ADDRESS);

        frame.setContentPane(cards);
        cardLayout.show(cards, CARD_ADDRESS);
        frame.setVisible(true);
    }

    private void onLangChange(String lang) {
        prefs.put("language", lang);
    }

    private static final String HISTORY_SEPARATOR = "\n";
    private static final int HISTORY_MAX_SIZE = 10;

    private List<String> loadAddressHistory() {
        String raw = prefs.get("server_address_history", "");
        if (raw.isBlank()) {
            return new java.util.ArrayList<>();
        }
        return new java.util.ArrayList<>(java.util.Arrays.asList(raw.split(HISTORY_SEPARATOR)));
    }

    private void onAddressHistoryChanged(List<String> history) {
        List<String> trimmed = history.size() > HISTORY_MAX_SIZE
                ? history.subList(0, HISTORY_MAX_SIZE)
                : history;
        prefs.put("server_address_history", String.join(HISTORY_SEPARATOR, trimmed));
    }

    private void onCloseRequested() {
        String lang = addressPanel != null ? addressPanel.currentLang() : prefs.get("language", I18n.detectSystemLang());
        String title;
        String message;
        switch (phase) {
            case APPLYING -> {
                title = I18n.get(lang, "im_close_installing_title");
                message = I18n.get(lang, "im_close_installing_message");
            }
            case DOWNLOADING -> {
                title = I18n.get(lang, "im_close_downloading_title");
                message = I18n.get(lang, "im_close_downloading_message");
            }
            case DONE -> {
                frame.dispose();
                System.exit(0);
                return;
            }
            default -> {
                title = I18n.get(lang, "im_close_confirm_title");
                message = I18n.get(lang, "im_close_confirm_message");
            }
        }

        int result = JOptionPane.showConfirmDialog(
                frame, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            if (phase == Phase.DOWNLOADING || phase == Phase.IDLE) {
                cleanupPendingInstall();
            }
            frame.dispose();
            System.exit(0);
        }
    }

    private void cleanupPendingInstall() {
        try {
            Path cacheDir = gameDir.resolve("clientsync/cache");
            if (Files.isDirectory(cacheDir)) {
                try (java.util.stream.Stream<Path> stream = Files.walk(cacheDir)) {
                    stream.sorted((a, b) -> b.getNameCount() - a.getNameCount())
                          .forEach(p -> {
                              try {
                                  Files.deleteIfExists(p);
                              } catch (java.io.IOException ignored) {}
                          });
                }
            }
            Files.deleteIfExists(InstallPlan.planPath(gameDir));
        } catch (java.io.IOException ignored) {}
    }

    private void onConnect(String address) {
        new SwingWorker<Void, Void>() {
            String error;

            @Override
            protected Void doInBackground() {
                try {
                    System.out.println("[ClientSync] onConnect doInBackground start, address=" + address);
                    if (!ManifestFetcher.ping(address)) {
                        error = ManifestFetcher.getLastError() != null
                            ? ManifestFetcher.getLastError()
                            : I18n.get(addressPanel.currentLang(), "im_ping_failed");
                        System.out.println("[ClientSync] ping failed: " + error);
                        return null;
                    }
                    System.out.println("[ClientSync] ping ok, fetching manifest");
                    manifest = ManifestFetcher.fetch(address);

                    LocalIndex index = new LocalIndex(gameDir);
                    index.loadForManifest(manifest);
                    localIndex = index;
                    diff = new java.util.ArrayList<>(DiffCalculator.calculate(manifest, index));
                    diff.addAll(DiffCalculator.calculateRemoved(manifest, index));
                    System.out.println("[ClientSync] onConnect done, diff size=" + diff.size());
                } catch (Throwable e) {
                    error = I18n.get(addressPanel.currentLang(), "im_connect_failed").replace("{error}", String.valueOf(e.getMessage()));
                    System.out.println("[ClientSync] onConnect exception: " + e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                if (error != null) {
                    addressPanel.showError(error);
                    return;
                }
                serverAddress = address;
                prefs.put("server_address", address);
                addressPanel.addToHistory(address);

                String serverVersion = ManifestFetcher.lastPingInfo != null
                        ? ManifestFetcher.lastPingInfo.mcVersion : "";
                if (!serverVersion.isBlank() && !serverVersion.equals(clientVersion)) {
                    showVersionMismatch(clientVersion, serverVersion);
                    return;
                }

                String serverLoader = ManifestFetcher.lastPingInfo != null
                        ? ManifestFetcher.lastPingInfo.loader : "";
                String serverLoaderVersionReq = ManifestFetcher.lastPingInfo != null
                        ? ManifestFetcher.lastPingInfo.loaderVersion : "";
                if (!serverLoader.isBlank() && clientLoader != null && !clientLoader.isBlank()
                        && !"unknown".equals(clientLoader) && !serverLoader.equalsIgnoreCase(clientLoader)) {
                    showLoaderMismatch(clientLoader, clientLoaderVersion, serverLoader, serverLoaderVersionReq);
                    return;
                }
                if (!serverLoaderVersionReq.isBlank() && clientLoaderVersion != null
                        && !clientLoaderVersion.isBlank() && !"unknown".equals(clientLoaderVersion)
                        && !dev.errnicraft.clientsync.net.LoaderVersionMatcher.matches(serverLoaderVersionReq, clientLoaderVersion)) {
                    showLoaderMismatch(clientLoader, clientLoaderVersion, serverLoader, serverLoaderVersionReq);
                    return;
                }

                logPackVersionComparison();

                showSyncPanel();
            }
        }.execute();
    }

    private void showVersionMismatch(String clientVersion, String serverVersion) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        String lang = addressPanel.currentLang();
        String message = I18n.get(lang, "version_mismatch_message")
                .replace("{server}", serverVersion)
                .replace("{client}", clientVersion)
                .replace("\n", "<br>");

        JLabel label = new JLabel(
                "<html><div style='text-align:center'>" + message + "</div></html>",
                SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(14f));

        JButton exitButton = new JButton(I18n.get(lang, "exit"));
        exitButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(exitButton);

        panel.add(label, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        cards.add(panel, CARD_VERSION_MISMATCH);
        cardLayout.show(cards, CARD_VERSION_MISMATCH);
    }

    private void showLoaderMismatch(String clientLoader, String clientLoaderVersion,
                                     String serverLoader, String serverLoaderVersionReq) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        String lang = addressPanel.currentLang();
        String requirement = dev.errnicraft.clientsync.net.LoaderVersionMatcher.formatRequirement(serverLoaderVersionReq);
        String message = I18n.get(lang, "loader_mismatch_message")
                .replace("{server_loader}", serverLoader)
                .replace("{server_loader_version}", requirement)
                .replace("{client_loader}", clientLoader == null ? "" : clientLoader)
                .replace("{client_loader_version}", clientLoaderVersion == null ? "" : clientLoaderVersion)
                .replace("\n", "<br>");

        JLabel label = new JLabel(
                "<html><div style='text-align:center'>" + message + "</div></html>",
                SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(14f));

        JButton exitButton = new JButton(I18n.get(lang, "exit"));
        exitButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(exitButton);

        panel.add(label, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        cards.add(panel, CARD_VERSION_MISMATCH);
        cardLayout.show(cards, CARD_VERSION_MISMATCH);
    }

    private void logPackVersionComparison() {
        String host = dev.errnicraft.clientsync.net.ServerAddress.resolve(serverAddress).host;
        String serverPackVersion = ManifestFetcher.lastPingInfo != null
                ? ManifestFetcher.lastPingInfo.packVersion : "";
        String savedVersion = prefs.get(PACK_VERSION_PREFIX + host, "");

        if (savedVersion.isBlank()) {
            System.out.println("[ClientSync] Pack version for " + host + " not recorded yet. "
                    + "Current server version: " + serverPackVersion + ". Installing without removals.");
        } else if (serverPackVersion.isBlank()) {
            System.out.println("[ClientSync] Server " + host + " does not report a pack version.");
        } else if (savedVersion.equals(serverPackVersion)) {
            System.out.println("[ClientSync] Pack version for " + host + " unchanged: " + serverPackVersion);
        } else {
            System.out.println("[ClientSync] Pack version for " + host + " changed: "
                    + savedVersion + " -> " + serverPackVersion);
        }
    }

    private void savePackVersion() {
        String host = dev.errnicraft.clientsync.net.ServerAddress.resolve(serverAddress).host;
        String serverPackVersion = ManifestFetcher.lastPingInfo != null
                ? ManifestFetcher.lastPingInfo.packVersion : "";
        if (!serverPackVersion.isBlank()) {
            prefs.put(PACK_VERSION_PREFIX + host, serverPackVersion);
        }
    }

    private void showSyncPanel() {
        String host = dev.errnicraft.clientsync.net.ServerAddress.resolve(serverAddress).host;
        java.util.Set<String> excluded = exclusionStore.load(host);
        SyncPanel syncPanel = new SyncPanel(manifest, diff, this::onInstall, excluded,
                (key, isExcluded) -> {
                    if (isExcluded) {
                        exclusionStore.add(host, key);
                    } else {
                        exclusionStore.remove(host, key);
                    }
                }, gameDir, localIndex, addressPanel.currentLang());
        cards.add(syncPanel, CARD_SYNC);
        growWindow();
        cardLayout.show(cards, CARD_SYNC);
    }

    private void growWindow() {
        Dimension current = frame.getSize();
        if (current.width < 820 || current.height < 560) {
            frame.setMinimumSize(new Dimension(640, 420));
            frame.setSize(820, 560);
            frame.setLocationRelativeTo(null);
        }
    }

    private void onInstall(java.util.Set<String> excludedKeys, boolean cleanInstall) {
        List<DiffEntry> baseDiff = diff;
        if (cleanInstall) {
            List<DiffEntry> extras = DiffCalculator.calculateCleanExtras(manifest, localIndex);
            List<DiffEntry> merged = new java.util.ArrayList<>(diff);
            java.util.Set<String> existingStaleKeys = new java.util.HashSet<>();
            for (DiffEntry d : diff) {
                if (d.type == DiffEntry.Type.STALE) existingStaleKeys.add(d.category + ":" + d.key);
            }
            for (DiffEntry extra : extras) {
                if (existingStaleKeys.add(extra.category + ":" + extra.key)) {
                    merged.add(extra);
                }
            }
            baseDiff = merged;
        }

        List<DiffEntry> filteredDiff = baseDiff.stream()
                .filter(d -> !excludedKeys.contains(d.key))
                .collect(java.util.stream.Collectors.toList());

        if (filteredDiff.isEmpty()) {
            String lang = addressPanel.currentLang();
            JOptionPane.showMessageDialog(frame,
                    I18n.get(lang, "im_nothing_to_install_message"),
                    I18n.get(lang, "im_nothing_to_install_title"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        phase = Phase.DOWNLOADING;

        String lang = addressPanel.currentLang();
        progressPanel = new ProgressPanel(() -> System.exit(0), lang);
        cards.add(progressPanel, CARD_PROGRESS);
        cardLayout.show(cards, CARD_PROGRESS);

        new SwingWorker<Void, Void>() {
            String error;

            @Override
            protected Void doInBackground() {
                try {
                    progressPanel.setStatus(I18n.get(lang, "im_status_downloading"));

                    int maxConcurrentTasks = dev.errnicraft.clientsync.net.SyncProtocol.DEFAULT_MAX_CONCURRENT_TASKS;
                    long chunkBytes = dev.errnicraft.clientsync.net.SyncProtocol.DEFAULT_CHUNK_BYTES;
                    if (ManifestFetcher.lastPingInfo != null) {
                        maxConcurrentTasks = ManifestFetcher.lastPingInfo.maxConcurrentTasks;
                        chunkBytes = (long) ManifestFetcher.lastPingInfo.chunkSizeMb * 1024 * 1024;
                    }

                    FileInstaller installer = new FileInstaller(
                            gameDir, manifest, serverAddress, maxConcurrentTasks, chunkBytes);

                    long totalBytes = filteredDiff.stream()
                            .filter(d -> d.type != DiffEntry.Type.STALE)
                            .mapToLong(d -> d.size)
                            .sum();
                    long[] downloadedBytes = {0};

                    long[] bytesSinceLastTick = {0};
                    java.util.Timer speedTimer = new java.util.Timer(true);
                    speedTimer.scheduleAtFixedRate(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            long bytes;
                            synchronized (bytesSinceLastTick) {
                                bytes = bytesSinceLastTick[0];
                                bytesSinceLastTick[0] = 0;
                            }
                            progressPanel.setSpeed(bytes);
                            long remaining = totalBytes - downloadedBytes[0];
                            if (bytes <= 0 || remaining <= 0) {
                                progressPanel.setEta(remaining <= 0 ? 0 : -1);
                            } else {
                                progressPanel.setEta(remaining / bytes);
                            }
                        }
                    }, 1000, 1000);

                    try {
                        installer.downloadToCache(filteredDiff, new FileInstaller.ProgressCallback() {
                            @Override
                            public void onProgress(int done, int total, String currentKey) {
                                progressPanel.setProgress(done, total);
                                if (!currentKey.isEmpty()) {
                                    progressPanel.log(I18n.get(lang, "im_log_downloaded").replace("{key}", currentKey));
                                }
                            }

                            @Override
                            public void onError(String key, String message) {
                                progressPanel.log(I18n.get(lang, "im_log_error").replace("{key}", key).replace("{message}", String.valueOf(message)));
                            }

                            @Override
                            public void onBytesDownloaded(long bytes) {
                                synchronized (bytesSinceLastTick) {
                                    bytesSinceLastTick[0] += bytes;
                                }
                                synchronized (downloadedBytes) {
                                    downloadedBytes[0] += bytes;
                                }
                            }
                        });
                    } finally {
                        speedTimer.cancel();
                        progressPanel.setSpeed(0);
                        progressPanel.setEta(0);
                    }

                    progressPanel.setStatus(I18n.get(lang, "im_status_applying"));
                    phase = Phase.APPLYING;
                    List<InstallPlan.PlanEntry> plan = InstallPlan.read(gameDir);
                    PlanApplier applier = new PlanApplier(gameDir);
                    applier.apply(plan, new PlanApplier.ProgressCallback() {
                        @Override
                        public void onProgress(int done, int total, String currentKey) {
                            progressPanel.setProgress(done, total);
                            if (!currentKey.isEmpty()) {
                                progressPanel.log(I18n.get(lang, "im_log_installed").replace("{key}", currentKey));
                            }
                        }

                        @Override
                        public void onError(String key, String message) {
                            progressPanel.log(I18n.get(lang, "im_log_install_error").replace("{key}", key).replace("{message}", String.valueOf(message)));
                        }
                    });

                    java.nio.file.Files.deleteIfExists(InstallPlan.planPath(gameDir));
                } catch (Exception e) {
                    error = e.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                phase = Phase.DONE;
                if (error != null) {
                    progressPanel.log(I18n.get(lang, "im_status_done_error").replace("{error}", String.valueOf(error)));
                    progressPanel.complete(I18n.get(lang, "im_status_done_error_short"));
                } else {
                    savePackVersion();
                    progressPanel.complete(I18n.get(lang, "im_status_done_success"));
                }
            }
        }.execute();
    }
}
