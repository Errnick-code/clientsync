package dev.errnicraft.clientsync.installer;

import dev.errnicraft.clientsync.model.DiffEntry;
import dev.errnicraft.clientsync.model.ManifestFile;
import dev.errnicraft.clientsync.model.ManifestMod;
import dev.errnicraft.clientsync.model.SyncManifest;
import dev.errnicraft.clientsync.sync.DiffCalculator;
import dev.errnicraft.clientsync.sync.LocalIndex;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncPanel extends JPanel {

    private static class NodeInfo {
        final String name;
        final boolean isFile;
        final long size;
        DiffEntry.Type changeType;
        String diffKey;
        boolean selected;
        boolean excludedForever;
        String lang;

        NodeInfo(String name, boolean isFile, long size, String lang) {
            this.name = name;
            this.isFile = isFile;
            this.size = size;
            this.changeType = null;
            this.lang = lang;
        }

        @Override
        public String toString() {
            if (isFile) {
                String suffix = changeType == null ? "" : "  [" + changeLabel() + "]";
                String excludedSuffix = excludedForever ? I18n.get(lang, "sp_exclude_forever_suffix") : "";
                return name + "  (" + humanSize(size) + ")" + suffix + excludedSuffix;
            }
            return name;
        }

        private String changeLabel() {
            return switch (changeType) {
                case MISSING -> I18n.get(lang, "sp_change_new");
                case UPDATE, REPAIR -> I18n.get(lang, "sp_change_update");
                case STALE -> I18n.get(lang, "sp_change_stale");
                default -> "";
            };
        }
    }

    private final JTree tree;
    private final String lang;
    private final DefaultMutableTreeNode root;
    private final DefaultListModel<String> diffListModel = new DefaultListModel<>();
    private final JLabel summaryLabel = new JLabel(" ");
    private final JLabel diskSpaceLabel = new JLabel(" ");
    private final JButton installButton = new JButton();
    private final JButton installMenuButton = new JButton("▾");
    private final java.util.Set<String> permanentlyExcluded;
    private final java.util.function.BiConsumer<String, Boolean> onPermanentExcludeChanged;

    private final SyncManifest manifest;
    private final Path gameDir;
    private final LocalIndex localIndex;
    private List<DiffEntry> currentDiff;
    private boolean cleanInstallMode = false;
    private final java.util.function.BiConsumer<java.util.Set<String>, Boolean> onInstallWithMode;

    public SyncPanel(SyncManifest manifest, List<DiffEntry> diff,
                      java.util.function.BiConsumer<java.util.Set<String>, Boolean> onInstall,
                      java.util.Set<String> permanentlyExcluded,
                      java.util.function.BiConsumer<String, Boolean> onPermanentExcludeChanged,
                      Path gameDir, LocalIndex localIndex, String lang) {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        this.lang = lang;
        this.root = new DefaultMutableTreeNode(new NodeInfo(I18n.get(lang, "sp_root_server"), false, 0, lang));
        this.manifest = manifest;
        this.gameDir = gameDir;
        this.localIndex = localIndex;
        this.currentDiff = diff;
        this.onInstallWithMode = onInstall;
        this.permanentlyExcluded = permanentlyExcluded;
        this.onPermanentExcludeChanged = onPermanentExcludeChanged;
        installButton.setText(I18n.get(lang, "sp_install"));

        buildTree(manifest, diff);

        tree = new JTree(new DefaultTreeModel(root));
        tree.setRootVisible(true);
        CheckboxTreeRenderer renderer = new CheckboxTreeRenderer();
        tree.setCellRenderer(renderer);
        tree.addMouseListener(new CheckboxTreeMouseListener(tree));
        for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);

        JList<String> diffList = new JList<>(diffListModel);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree), new JScrollPane(diffList));
        split.setResizeWeight(0.55);

        JPanel left = new JPanel(new BorderLayout());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        diskSpaceLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        diskSpaceLabel.setForeground(Color.GRAY);
        diskSpaceLabel.setFont(diskSpaceLabel.getFont().deriveFont(11f));

        installMenuButton.setMargin(new Insets(4, 6, 4, 6));
        installButton.setMargin(new Insets(4, 10, 4, 10));
        installMenuButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        installButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        JPopupMenu installMenu = new JPopupMenu();
        JMenuItem cleanItem = new JMenuItem(I18n.get(lang, "sp_clean_install_item"));
        cleanItem.addActionListener(e -> enableCleanInstallMode());
        installMenu.add(cleanItem);
        installMenuButton.addActionListener(e -> installMenu.show(installMenuButton, 0, installMenuButton.getHeight()));

        installButton.addActionListener(e -> onInstallWithMode.accept(collectExcludedKeys(root), cleanInstallMode));

        JPanel installButtonsRow = new JPanel(new BorderLayout(0, 0));
        installButtonsRow.add(installButton, BorderLayout.CENTER);
        installButtonsRow.add(installMenuButton, BorderLayout.EAST);

        JPanel installButtons = new JPanel(new BorderLayout());
        installButtons.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        installButtons.add(installButtonsRow, BorderLayout.SOUTH);

        JPanel bottomLeft = new JPanel(new GridLayout(2, 1));
        bottomLeft.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        bottomLeft.add(summaryLabel);
        bottomLeft.add(diskSpaceLabel);

        bottom.add(bottomLeft, BorderLayout.WEST);
        bottom.add(installButtons, BorderLayout.EAST);

        add(headerLabels(), BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        refreshDiffPanel();
    }

    private void enableCleanInstallMode() {
        int result = JOptionPane.showConfirmDialog(this,
                I18n.get(lang, "sp_clean_install_confirm_message"),
                I18n.get(lang, "sp_clean_install_confirm_title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        cleanInstallMode = true;
        List<DiffEntry> extras = DiffCalculator.calculateCleanExtras(manifest, localIndex);
        List<DiffEntry> merged = new java.util.ArrayList<>(currentDiff);
        java.util.Set<String> existingStaleKeys = new java.util.HashSet<>();
        for (DiffEntry d : currentDiff) {
            if (d.type == DiffEntry.Type.STALE) existingStaleKeys.add(d.category + ":" + d.key);
        }
        for (DiffEntry extra : extras) {
            if (existingStaleKeys.add(extra.category + ":" + extra.key)) {
                merged.add(extra);
            }
        }
        currentDiff = merged;

        onInstallWithMode.accept(collectExcludedKeys(root), cleanInstallMode);
    }

    private void updateDiskSpace(List<DiffEntry> diff) {
        long required = diff.stream()
                .filter(d -> d.type != DiffEntry.Type.STALE)
                .mapToLong(d -> d.size)
                .sum();
        long free = -1;
        try {
            free = Files.getFileStore(gameDir).getUsableSpace();
        } catch (Exception ignored) {}

        if (free < 0) {
            diskSpaceLabel.setText(I18n.get(lang, "sp_required_free_unknown").replace("{required}", humanSize(required)));
            installButton.setEnabled(!diff.isEmpty());
            return;
        }

        boolean enough = free >= required;
        diskSpaceLabel.setText(I18n.get(lang, "sp_required_free")
                .replace("{required}", humanSize(required))
                .replace("{free}", humanSize(free)));
        diskSpaceLabel.setForeground(enough ? Color.GRAY : Color.RED);
        installButton.setEnabled(!diff.isEmpty() && enough);
        if (!enough) {
            installButton.setToolTipText(I18n.get(lang, "sp_low_disk_space"));
        } else {
            installButton.setToolTipText(null);
        }
    }

    private java.util.Set<String> collectExcludedKeys(DefaultMutableTreeNode node) {
        java.util.Set<String> excluded = new java.util.HashSet<>();
        collectExcludedKeys(node, excluded);
        return excluded;
    }

    private void collectExcludedKeys(DefaultMutableTreeNode node, java.util.Set<String> excluded) {
        Object userObj = node.getUserObject();
        if (userObj instanceof NodeInfo info) {
            if (info.isFile && info.diffKey != null && !info.selected) {
                excluded.add(info.diffKey);
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            collectExcludedKeys((DefaultMutableTreeNode) node.getChildAt(i), excluded);
        }
    }

    private JComponent headerLabels() {
        JPanel header = new JPanel(new GridLayout(1, 2));
        header.add(new JLabel(I18n.get(lang, "sp_header_files")));
        header.add(new JLabel(I18n.get(lang, "sp_header_changes")));
        return header;
    }

    private void updateSummary(List<DiffEntry> diff) {
        long missing = diff.stream().filter(d -> d.type == DiffEntry.Type.MISSING).count();
        long update = diff.stream().filter(d -> d.type == DiffEntry.Type.UPDATE || d.type == DiffEntry.Type.REPAIR).count();
        long stale = diff.stream().filter(d -> d.type == DiffEntry.Type.STALE).count();
        if (diff.isEmpty()) {
            summaryLabel.setText(I18n.get(lang, "sp_all_up_to_date"));
        } else {
            summaryLabel.setText(I18n.get(lang, "sp_summary")
                    .replace("{missing}", String.valueOf(missing))
                    .replace("{update}", String.valueOf(update))
                    .replace("{stale}", String.valueOf(stale)));
        }
    }

    private void buildTree(SyncManifest manifest, List<DiffEntry> diff) {
        Map<String, DiffEntry.Type> changedFiles = new HashMap<>();
        Map<String, DiffEntry.Type> changedMods = new HashMap<>();
        for (DiffEntry d : diff) {
            if (d.category == DiffEntry.Category.MOD) changedMods.put(d.fileName, d.type);
            else changedFiles.put(d.key, d.type);
        }

        Map<String, DefaultMutableTreeNode> folders = new HashMap<>();

        for (ManifestMod mod : manifest.mods) {
            String folder = mod.folder == null || mod.folder.isEmpty() ? "mods" : mod.folder;
            DefaultMutableTreeNode folderNode = folders.computeIfAbsent(folder,
                    f -> addFolderNode(f));
            NodeInfo info = new NodeInfo(mod.fileName, true, mod.size, lang);
            info.changeType = changedMods.get(mod.fileName);
            info.diffKey = mod.modId;
            info.excludedForever = permanentlyExcluded.contains(mod.modId);
            info.selected = info.changeType != null && !info.excludedForever;
            folderNode.add(new DefaultMutableTreeNode(info));
        }

        for (ManifestFile file : manifest.files) {
            String folder = file.folder == null || file.folder.isEmpty() ? "files" : file.folder;
            DefaultMutableTreeNode folderNode = folders.computeIfAbsent(folder,
                    f -> addFolderNode(f));

            int slash = file.path.indexOf('/');
            String relativePath = slash >= 0 ? file.path.substring(slash + 1) : file.path;
            String[] segments = relativePath.split("/");

            DefaultMutableTreeNode parent = folderNode;
            String pathKey = folder;
            for (int i = 0; i < segments.length - 1; i++) {
                pathKey = pathKey + "/" + segments[i];
                String segment = segments[i];
                DefaultMutableTreeNode dirNode = folders.computeIfAbsent(pathKey, k -> {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodeInfo(segment, false, 0, lang));
                    return node;
                });
                if (dirNode.getParent() == null) {
                    parent.add(dirNode);
                }
                parent = dirNode;
            }

            String fileName = segments[segments.length - 1];
            NodeInfo info = new NodeInfo(fileName, true, file.size, lang);
            info.changeType = changedFiles.get(file.path);
            info.diffKey = file.path;
            info.excludedForever = permanentlyExcluded.contains(file.path);
            info.selected = info.changeType != null && !info.excludedForever;
            parent.add(new DefaultMutableTreeNode(info));
        }

    }

    private void refreshDiffPanel() {
        java.util.Set<String> excludedKeys = collectExcludedKeys(root);

        List<DiffEntry> visibleDiff = currentDiff.stream()
                .filter(d -> !excludedKeys.contains(d.key))
                .collect(java.util.stream.Collectors.toList());

        diffListModel.clear();
        for (DiffEntry d : visibleDiff) {
            String label = switch (d.type) {
                case MISSING -> "+ " + (d.fileName.isEmpty() ? d.key : d.fileName);
                case UPDATE, REPAIR -> "~ " + (d.fileName.isEmpty() ? d.key : d.fileName);
                case STALE -> "- " + d.fileName;
                default -> d.key;
            };
            diffListModel.addElement(label + "  (" + humanSize(d.size) + ")");
        }

        updateSummary(visibleDiff);
        updateDiskSpace(visibleDiff);
    }

    private DefaultMutableTreeNode addFolderNode(String folder) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodeInfo(folder, false, 0, lang));
        root.add(node);
        return node;
    }

    private static String humanSize(long bytes) {
        if (bytes <= 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB"};
        int unit = (int) (Math.log(bytes) / Math.log(1024));
        unit = Math.min(unit, units.length - 1);
        double value = bytes / Math.pow(1024, unit);
        return String.format("%.1f %s", value, units[unit]);
    }

    private static class CheckboxTreeRenderer extends JPanel implements javax.swing.tree.TreeCellRenderer {
        private final JCheckBox checkBox = new JCheckBox();
        private final JLabel label = new JLabel();
        private final DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
        private final Icon excludedIcon;

        private static String escapeHtml(String s) {
            return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }

        CheckboxTreeRenderer() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setOpaque(false);
            checkBox.setOpaque(false);
            checkBox.setBorder(BorderFactory.createEmptyBorder());
            checkBox.setMargin(new Insets(0, 0, 0, 0));
            checkBox.setAlignmentY(Component.CENTER_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            excludedIcon = new ExcludedCheckIcon(checkBox);
            add(checkBox);
            add(Box.createHorizontalStrut(2));
            add(label);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                        boolean leaf, int row, boolean hasFocus) {
            Object userObj = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObj instanceof NodeInfo info) {
                Icon icon = leaf ? defaultRenderer.getLeafIcon() : (expanded ? defaultRenderer.getOpenIcon() : defaultRenderer.getClosedIcon());
                label.setIcon(icon);
                if (info.excludedForever) {
                    label.setText("<html><strike>" + escapeHtml(info.toString()) + "</strike></html>");
                } else {
                    label.setText(info.toString());
                }
                CheckState state = checkState((DefaultMutableTreeNode) value);
                if (info.isFile && info.excludedForever) {
                    checkBox.setSelected(false);
                    checkBox.setIcon(excludedIcon);
                    checkBox.setSelectedIcon(excludedIcon);
                } else {
                    checkBox.setIcon(null);
                    checkBox.setSelectedIcon(null);
                    checkBox.setSelected(state != CheckState.UNSELECTED);
                }
                checkBox.setEnabled(true);
                Color fg = getForeground();
                if (info.changeType != null) {
                    fg = switch (info.changeType) {
                        case MISSING -> new Color(0, 128, 0);
                        case UPDATE, REPAIR -> new Color(200, 120, 0);
                        case STALE -> Color.RED;
                        default -> fg;
                    };
                }
                if (state == CheckState.PARTIAL) {
                    fg = Color.GRAY;
                } else if (state == CheckState.UNSELECTED && info.isFile) {
                    fg = Color.GRAY;
                }
                if (info.excludedForever) {
                    fg = Color.GRAY;
                }
                label.setForeground(fg);
            }
            setBackground(sel ? new Color(180, 210, 250) : tree.getBackground());
            invalidate();
            validate();
            return this;
        }
    }

    private static class ExcludedCheckIcon implements Icon {
        private final int size;

        ExcludedCheckIcon(JCheckBox reference) {
            Icon uiIcon = UIManager.getIcon("CheckBox.icon");
            if (uiIcon != null && uiIcon.getIconWidth() > 0) {
                this.size = uiIcon.getIconWidth();
            } else {
                JCheckBox probe = new JCheckBox();
                probe.setText("");
                probe.setBorder(BorderFactory.createEmptyBorder());
                probe.setMargin(new Insets(0, 0, 0, 0));
                int probeSize = probe.getPreferredSize().height;
                this.size = probeSize > 0 ? probeSize : 16;
            }
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            int w = getIconWidth();
            int h = getIconHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.RED);
            g2.fillRect(x, y, w, h);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            int pad = Math.max(3, w / 4);
            g2.drawLine(x + pad, y + pad, x + w - pad, y + h - pad);
            g2.drawLine(x + w - pad, y + pad, x + pad, y + h - pad);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, y, w - 1, h - 1);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private enum CheckState { SELECTED, UNSELECTED, PARTIAL }

    private static CheckState checkState(DefaultMutableTreeNode node) {
        Object userObj = node.getUserObject();
        if (!(userObj instanceof NodeInfo info)) return CheckState.SELECTED;
        if (info.isFile) {
            return info.selected ? CheckState.SELECTED : CheckState.UNSELECTED;
        }
        boolean anySelected = false;
        boolean anyUnselected = false;
        for (int i = 0; i < node.getChildCount(); i++) {
            CheckState childState = checkState((DefaultMutableTreeNode) node.getChildAt(i));
            if (childState == CheckState.PARTIAL) return CheckState.PARTIAL;
            if (childState == CheckState.SELECTED) anySelected = true;
            if (childState == CheckState.UNSELECTED) anyUnselected = true;
        }
        if (anySelected && anyUnselected) return CheckState.PARTIAL;
        if (anyUnselected) return CheckState.UNSELECTED;
        return CheckState.SELECTED;
    }

    private static void toggleNode(JTree tree, DefaultMutableTreeNode node) {
        CheckState current = checkState(node);
        boolean newValue = current == CheckState.UNSELECTED || current == CheckState.PARTIAL;
        setSelectedRecursive(node, newValue);
        tree.repaint();
    }

    private static void setSelectedRecursive(DefaultMutableTreeNode node, boolean value) {
        Object userObj = node.getUserObject();
        if (userObj instanceof NodeInfo info) {
            info.selected = value;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            setSelectedRecursive((DefaultMutableTreeNode) node.getChildAt(i), value);
        }
    }

    private class CheckboxTreeMouseListener extends java.awt.event.MouseAdapter {
        private final JTree tree;

        CheckboxTreeMouseListener(JTree tree) {
            this.tree = tree;
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            int row = tree.getRowForLocation(e.getX(), e.getY());
            if (row < 0) return;

            javax.swing.tree.TreePath path = tree.getPathForRow(row);
            if (path == null) return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

            if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                toggleForeverExclude(tree, node);
                e.consume();
                return;
            }

            Rectangle bounds = tree.getRowBounds(row);
            if (e.getX() > bounds.x + 20) return;

            Object userObj = node.getUserObject();
            if (userObj instanceof NodeInfo info && info.isFile && info.excludedForever) {
                clearForeverExclude(node);
                e.consume();
                return;
            }

            toggleNode(tree, node);
            refreshDiffPanel();
            e.consume();
        }

        private void clearForeverExclude(DefaultMutableTreeNode node) {
            Object userObj = node.getUserObject();
            if (!(userObj instanceof NodeInfo info)) return;
            info.excludedForever = false;
            info.selected = info.changeType != null;
            if (onPermanentExcludeChanged != null && info.diffKey != null) {
                onPermanentExcludeChanged.accept(info.diffKey, false);
            }
            refreshDiffPanel();
            tree.repaint();
        }
    }

    private void toggleForeverExclude(JTree tree, DefaultMutableTreeNode node) {
        Object userObj = node.getUserObject();
        if (!(userObj instanceof NodeInfo info) || !info.isFile || info.diffKey == null) return;

        info.excludedForever = !info.excludedForever;
        if (info.excludedForever) {
            info.selected = false;
        } else {
            info.selected = info.changeType != null;
        }
        if (onPermanentExcludeChanged != null) {
            onPermanentExcludeChanged.accept(info.diffKey, info.excludedForever);
        }
        refreshDiffPanel();
        tree.repaint();
    }
}
