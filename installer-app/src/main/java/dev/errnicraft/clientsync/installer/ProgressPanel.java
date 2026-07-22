package dev.errnicraft.clientsync.installer;

import javax.swing.*;
import java.awt.*;

public class ProgressPanel extends JPanel {

    private final String lang;
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel statusLabel = new JLabel();
    private final JTextArea logArea = new JTextArea();
    private final JButton doneButton = new JButton();
    private final JLabel speedLabel = new JLabel();
    private final JLabel etaLabel = new JLabel();

    public ProgressPanel(Runnable onDone, String lang) {
        this.lang = lang;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        statusLabel.setText(I18n.get(lang, "pp_preparing"));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        progressBar.setStringPainted(true);

        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(560, 240));

        doneButton.setText(I18n.get(lang, "pp_done"));
        doneButton.setEnabled(false);
        doneButton.addActionListener(e -> onDone.run());

        JPanel top = new JPanel(new BorderLayout(4, 4));
        top.add(statusLabel, BorderLayout.NORTH);
        top.add(progressBar, BorderLayout.SOUTH);

        speedLabel.setText(formatSpeed(0));
        speedLabel.setFont(speedLabel.getFont().deriveFont(12f));
        speedLabel.setForeground(Color.GRAY);
        etaLabel.setText(I18n.get(lang, "pp_eta_prefix") + I18n.get(lang, "pp_eta_dash"));
        etaLabel.setFont(etaLabel.getFont().deriveFont(12f));
        etaLabel.setForeground(Color.GRAY);

        JPanel speedEtaPanel = new JPanel(new GridLayout(2, 1));
        speedEtaPanel.setPreferredSize(new Dimension(140, speedEtaPanel.getPreferredSize().height));
        speedEtaPanel.add(speedLabel);
        speedEtaPanel.add(etaLabel);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomRight.add(doneButton);
        bottom.add(speedEtaPanel, BorderLayout.WEST);
        bottom.add(bottomRight, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(logScroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setStatus(String text) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(text));
    }

    public void setProgress(int done, int total) {
        int pct = total <= 0 ? 100 : Math.min(100, done * 100 / total);
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(pct);
            progressBar.setString(done + " / " + total);
        });
    }

    public void setSpeed(long bytesPerSecond) {
        SwingUtilities.invokeLater(() -> speedLabel.setText(formatSpeed(bytesPerSecond)));
    }

    public void setEta(long secondsRemaining) {
        SwingUtilities.invokeLater(() -> etaLabel.setText(I18n.get(lang, "pp_eta_prefix") + formatEta(secondsRemaining)));
    }

    private String formatEta(long seconds) {
        if (seconds < 0) return I18n.get(lang, "pp_eta_dash");
        if (seconds < 60) return I18n.get(lang, "pp_eta_seconds").replace("{s}", String.valueOf(seconds));
        long minutes = seconds / 60;
        long secs = seconds % 60;
        if (minutes < 60) {
            return I18n.get(lang, "pp_eta_min_sec")
                    .replace("{m}", String.valueOf(minutes))
                    .replace("{s}", String.format("%02d", secs));
        }
        long hours = minutes / 60;
        minutes = minutes % 60;
        return I18n.get(lang, "pp_eta_hour_min")
                .replace("{h}", String.valueOf(hours))
                .replace("{m}", String.format("%02d", minutes));
    }

    private String formatSpeed(long bytesPerSecond) {
        double value = bytesPerSecond;
        String[] unitKeys = {"pp_speed_b", "pp_speed_kb", "pp_speed_mb", "pp_speed_gb"};
        int unitIndex = 0;
        while (value >= 1024 && unitIndex < unitKeys.length - 1) {
            value /= 1024;
            unitIndex++;
        }
        return String.format("%.1f %s", value, I18n.get(lang, unitKeys[unitIndex]));
    }

    public void log(String line) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(line + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void complete(String finalMessage) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(finalMessage);
            progressBar.setValue(100);
            progressBar.setString(I18n.get(lang, "pp_done"));
            doneButton.setEnabled(true);
        });
    }
}
