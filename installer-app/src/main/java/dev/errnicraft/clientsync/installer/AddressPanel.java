package dev.errnicraft.clientsync.installer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddressPanel extends JPanel {

    private final JComboBox<String> addressBox = new JComboBox<>();
    private final JTextField addressField;
    private final JLabel statusLabel = new JLabel(" ");
    private final JButton connectButton = new JButton();
    private final JComboBox<I18n.Lang> languageBox = new JComboBox<>(I18n.SUPPORTED);
    private final JLabel titleLabel = new JLabel();
    private final JLabel hintLabel = new JLabel();
    private final JLabel languageLabel = new JLabel();

    private final List<String> history;
    private final Consumer<List<String>> onHistoryChanged;

    private String currentLang;

    public AddressPanel(String initialAddress, String initialLang, List<String> initialHistory,
                         Consumer<String> onConnect, Consumer<String> onLangChange,
                         Consumer<List<String>> onHistoryChanged) {
        currentLang = initialLang != null ? initialLang : I18n.detectSystemLang();
        this.history = new ArrayList<>(initialHistory != null ? initialHistory : List.of());
        this.onHistoryChanged = onHistoryChanged;

        setLayout(new BorderLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 40), 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));

        hintLabel.setForeground(Color.GRAY);
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
        row.add(addressBox, BorderLayout.CENTER);
        row.add(connectButton, BorderLayout.EAST);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        languageLabel.setFont(languageLabel.getFont().deriveFont(12f));
        languageBox.setSelectedItem(I18n.findLang(currentLang));
        languageBox.setMaximumSize(new Dimension(160, 26));
        languageBox.addActionListener(e -> {
            I18n.Lang selected = (I18n.Lang) languageBox.getSelectedItem();
            if (selected != null && !selected.code.equals(currentLang)) {
                currentLang = selected.code;
                applyLanguage();
                onLangChange.accept(currentLang);
            }
        });

        statusLabel.setForeground(Color.RED);
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
        card.add(Box.createVerticalGlue());

        add(card, BorderLayout.CENTER);

        JPanel langCorner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        langCorner.add(languageLabel);
        langCorner.add(languageBox);
        add(langCorner, BorderLayout.SOUTH);

        applyLanguage();
    }

    private void applyLanguage() {
        titleLabel.setText(I18n.get(currentLang, "title"));
        hintLabel.setText(I18n.get(currentLang, "hint"));
        connectButton.setText(I18n.get(currentLang, "connect"));
        languageLabel.setText(I18n.get(currentLang, "language") + ":");
    }

    private void refreshHistoryModel() {
        String currentText = addressField.getText();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String entry : history) {
            model.addElement(entry);
        }
        addressBox.setModel(model);
        addressField.setText(currentText);
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
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(I18n.get(currentLang, "empty_address"));
            return;
        }
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setText(I18n.get(currentLang, "connecting"));
        connectButton.setEnabled(false);
        addressBox.setEnabled(false);
        languageBox.setEnabled(false);
        onConnect.accept(address);
    }

    public void showError(String message) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(message);
        connectButton.setEnabled(true);
        addressBox.setEnabled(true);
        languageBox.setEnabled(true);
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

        HistoryItemRenderer() {
            setLayout(new BorderLayout());
            setOpaque(true);
            textLabel.setBorder(new EmptyBorder(2, 6, 2, 6));
            removeLabel.setBorder(new EmptyBorder(2, 8, 2, 8));
            removeLabel.setForeground(Color.GRAY);
            add(textLabel, BorderLayout.CENTER);
            add(removeLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String item,
                                                        int index, boolean isSelected, boolean cellHasFocus) {
            textLabel.setText(item);
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            textLabel.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }
    }
}
