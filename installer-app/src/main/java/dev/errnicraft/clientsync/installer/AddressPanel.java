package dev.errnicraft.clientsync.installer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddressPanel extends JPanel implements Theme.ThemeAware {

    private final JComboBox<String> addressBox = new JComboBox<>();
    private final JTextField addressField;
    private final JLabel statusLabel = new JLabel(" ");
    private final Ui.AccentButton connectButton = new Ui.AccentButton("");
    private final JComboBox<I18n.Lang> languageBox = new JComboBox<>(I18n.SUPPORTED);
    private final JComboBox<ThemeItem> themeBox;
    private final JLabel titleLabel = new JLabel();
    private final JLabel hintLabel = new JLabel();
    private final JLabel languageLabel = new JLabel();
    private final JLabel themeLabel = new JLabel();
    private final JCheckBox autoUpdateCheck = new JCheckBox();
    private final Consumer<Boolean> onAutoUpdateChange;

    private final List<String> history;
    private final Consumer<List<String>> onHistoryChanged;
    private final Consumer<Theme.Mode> onThemeChange;

    private String currentLang;

    private static final class ThemeItem {
        final Theme.Mode mode;
        final String key;

        ThemeItem(Theme.Mode mode) {
            this.mode = mode;
            this.key = "theme_" + mode.name().toLowerCase();
        }
    }

    public AddressPanel(String initialAddress, String initialLang, Theme.Mode initialTheme, List<String> initialHistory,
                         Consumer<String> onConnect, Consumer<String> onLangChange,
                         Consumer<Theme.Mode> onThemeChange,
                         Consumer<List<String>> onHistoryChanged, boolean initialAutoUpdate,
                         Consumer<Boolean> onAutoUpdateChange) {
        currentLang = initialLang != null ? initialLang : I18n.detectSystemLang();
        this.history = new ArrayList<>(initialHistory != null ? initialHistory : List.of());
        this.onHistoryChanged = onHistoryChanged;
        this.onThemeChange = onThemeChange;
        this.onAutoUpdateChange = onAutoUpdateChange;
        this.autoUpdateCheck.setSelected(initialAutoUpdate);
        this.autoUpdateCheck.addActionListener(e -> {
            if (onAutoUpdateChange != null) {
                onAutoUpdateChange.accept(autoUpdateCheck.isSelected());
            }
        });
        this.autoUpdateCheck.setOpaque(false);

        setLayout(new BorderLayout());

        JPanel card = new JPanel();
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.border(), 1, true),
                new EmptyBorder(18, 22, 18, 22)
        ));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 15f));

        hintLabel.setForeground(Theme.textSecondary());
        hintLabel.setFont(hintLabel.getFont().deriveFont(12f));

        addressBox.setEditable(true);
        addressBox.setRenderer(new HistoryItemRenderer());
        addressField = (JTextField) addressBox.getEditor().getEditorComponent();
        addressField.setText(initialAddress == null ? "" : initialAddress);
        refreshHistoryModel();
        installPopupClickHandler();

        connectButton.addActionListener(e -> tryConnect(onConnect));
        addressField.addActionListener(e -> tryConnect(onConnect));

        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.add(addressBox, BorderLayout.CENTER);
        row.add(connectButton, BorderLayout.EAST);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        languageLabel.setFont(languageLabel.getFont().deriveFont(12f));
        languageBox.setSelectedItem(I18n.findLang(currentLang));
        languageBox.setMaximumSize(new Dimension(160, 26));
        Theme.setupWidePopup(languageBox);
        languageBox.addActionListener(e -> {
            I18n.Lang selected = (I18n.Lang) languageBox.getSelectedItem();
            if (selected != null && !selected.code.equals(currentLang)) {
                currentLang = selected.code;
                applyLanguage();
                onLangChange.accept(currentLang);
            }
        });

        themeLabel.setFont(themeLabel.getFont().deriveFont(12f));
        themeBox = new JComboBox<>(new ThemeItem[]{
                new ThemeItem(Theme.Mode.SYSTEM),
                new ThemeItem(Theme.Mode.LIGHT),
                new ThemeItem(Theme.Mode.DARK)
        });
        themeBox.setMaximumSize(new Dimension(160, 26));
        Theme.setupWidePopup(themeBox);
        themeBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ThemeItem item) {
                    setText(I18n.get(currentLang, item.key));
                }
                return c;
            }
        });
        selectThemeItem(initialTheme != null ? initialTheme : Theme.Mode.SYSTEM);
        themeBox.addActionListener(e -> {
            ThemeItem selected = (ThemeItem) themeBox.getSelectedItem();
            if (selected != null && onThemeChange != null) {
                onThemeChange.accept(selected.mode);
            }
        });

        statusLabel.setForeground(Theme.danger());
        statusLabel.setFont(statusLabel.getFont().deriveFont(12f));

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(hintLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(row);
        card.add(Box.createVerticalStrut(8));
        card.add(statusLabel);

        autoUpdateCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        autoUpdateCheck.setForeground(Theme.textPrimary());
        autoUpdateCheck.setFont(autoUpdateCheck.getFont().deriveFont(12f));
        card.add(Box.createVerticalStrut(10));
        card.add(autoUpdateCheck);

        card.add(Box.createVerticalGlue());

        add(card, BorderLayout.CENTER);

        JPanel corner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        corner.setOpaque(false);
        corner.add(languageLabel);
        corner.add(languageBox);
        corner.add(themeLabel);
        corner.add(themeBox);
        add(corner, BorderLayout.SOUTH);

        applyLanguage();
    }

    @Override
    public void retheme() {
        hintLabel.setForeground(Theme.textSecondary());
        statusLabel.setForeground(Theme.danger());
        autoUpdateCheck.setForeground(Theme.textPrimary());
        connectButton.applyAccent();
        addressBox.getEditor().getEditorComponent()
                .setForeground(Theme.textPrimary());
        addressBox.getEditor().getEditorComponent()
                .setBackground(Theme.fieldBg());
    }

    private void selectThemeItem(Theme.Mode mode) {
        for (int i = 0; i < themeBox.getItemCount(); i++) {
            if (themeBox.getItemAt(i).mode == mode) {
                themeBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void applyLanguage() {
        titleLabel.setText(I18n.get(currentLang, "title"));
        hintLabel.setText(I18n.get(currentLang, "hint"));
        connectButton.setText(I18n.get(currentLang, "connect"));
        languageLabel.setText(I18n.get(currentLang, "language") + ":");
        themeLabel.setText(I18n.get(currentLang, "theme") + ":");
        autoUpdateCheck.setText(I18n.get(currentLang, "auto_update"));
        themeBox.repaint();
    }

    private void refreshHistoryModel() {
        String currentText = addressField.getText();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String entry : history) {
            model.addElement(entry);
        }
        addressBox.setModel(model);
        addressField.setText(currentText);
        Object renderer = addressBox.getRenderer();
        if (renderer instanceof HistoryItemRenderer itemRenderer) {
            itemRenderer.setHighlightIndex(-1);
        }
    }

    private void installPopupClickHandler() {
        Object child = addressBox.getAccessibleContext().getAccessibleChild(0);
        if (!(child instanceof javax.swing.plaf.basic.ComboPopup popup)) {
            return;
        }
        JList<?> list = popup.getList();
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index < 0 || index >= history.size()) {
                    return;
                }
                Rectangle cellBounds = list.getCellBounds(index, index);
                String value = history.get(index);
                int removeZoneStart = cellBounds.x + cellBounds.width - 28;
                if (e.getX() >= removeZoneStart) {
                    removeFromHistory(value);
                    e.consume();
                } else {
                    addressField.setText(value);
                    addressBox.hidePopup();
                }
            }
        });
        list.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                boolean highlight = false;
                if (index >= 0 && index < history.size()) {
                    Rectangle cellBounds = list.getCellBounds(index, index);
                    int removeZoneStart = cellBounds.x + cellBounds.width - 28;
                    highlight = e.getX() >= removeZoneStart && e.getX() < cellBounds.x + cellBounds.width;
                }
                Object renderer = list.getCellRenderer();
                if (renderer instanceof HistoryItemRenderer itemRenderer) {
                    itemRenderer.setHighlightIndex(highlight && index >= 0 && index < history.size() ? index : -1);
                    list.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Object renderer = list.getCellRenderer();
                if (renderer instanceof HistoryItemRenderer itemRenderer) {
                    itemRenderer.setHighlightIndex(-1);
                    list.repaint();
                }
            }
        });
    }

    private void addToHistoryInternal(String address) {
        history.remove(address);
        history.add(0, address);
        refreshHistoryModel();
        if (onHistoryChanged != null) {
            onHistoryChanged.accept(new ArrayList<>(history));
        }
    }

    public void addToHistory(String address) {
        if (address == null || address.isBlank()) {
            return;
        }
        addToHistoryInternal(address);
    }

    public boolean isAutoUpdateSelected() {
        return autoUpdateCheck.isSelected();
    }

    public void setAutoUpdateSelected(boolean selected) {
        autoUpdateCheck.setSelected(selected);
        if (onAutoUpdateChange != null) {
            onAutoUpdateChange.accept(selected);
        }
    }

    private void removeFromHistory(String address) {
        history.remove(address);
        refreshHistoryModel();
        if (onHistoryChanged != null) {
            onHistoryChanged.accept(new ArrayList<>(history));
        }
    }

    private void tryConnect(Consumer<String> onConnect) {
        String address = addressField.getText().trim();
        if (address.isEmpty()) {
            statusLabel.setForeground(Theme.danger());
            statusLabel.setText(I18n.get(currentLang, "empty_address"));
            return;
        }
        statusLabel.setForeground(Theme.textSecondary());
        statusLabel.setText(I18n.get(currentLang, "connecting"));
        connectButton.setEnabled(false);
        addressBox.setEnabled(false);
        languageBox.setEnabled(false);
        themeBox.setEnabled(false);
        onConnect.accept(address);
    }

    public void showError(String message) {
        statusLabel.setForeground(Theme.danger());
        statusLabel.setText(message);
        connectButton.setEnabled(true);
        addressBox.setEnabled(true);
        languageBox.setEnabled(true);
        themeBox.setEnabled(true);
    }

    public String currentAddress() {
        return addressField.getText().trim();
    }

    public String currentLang() {
        return currentLang;
    }

    private static class HistoryItemRenderer extends JPanel implements ListCellRenderer<String> {
        private final JLabel textLabel = new JLabel();
        private final JLabel removeLabel = new JLabel("✕");
        private int highlightIndex = -1;

        HistoryItemRenderer() {
            setLayout(new BorderLayout());
            setOpaque(true);
            textLabel.setBorder(new EmptyBorder(2, 6, 2, 6));
            removeLabel.setBorder(new EmptyBorder(2, 8, 2, 8));
            removeLabel.setForeground(Theme.danger());
            add(textLabel, BorderLayout.CENTER);
            add(removeLabel, BorderLayout.EAST);
        }

        void setHighlightIndex(int index) {
            this.highlightIndex = index;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String item,
                                                        int index, boolean isSelected, boolean cellHasFocus) {
            textLabel.setText(item);
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            textLabel.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            boolean highlight = index == highlightIndex;
            removeLabel.setForeground(highlight ? Theme.dangerBright() : Theme.danger());
            removeLabel.setFont(removeLabel.getFont().deriveFont(
                    highlight ? Font.BOLD : Font.PLAIN, 14f));
            return this;
        }
    }
}