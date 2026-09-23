package dev.errnicraft.clientsync.installer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class Ui {

    private Ui() {}

    /**
     * Акцентная кнопка на FlatLaf: фон залит тематической акцентной краской,
     * hover/pressed состояния FlatLaf вычисляет сам из заданного background.
     */
    public static final class AccentButton extends JButton {
        public AccentButton(String text) {
            super(text);
            setBorderPainted(true);
            setFocusPainted(false);
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setFont(getFont().deriveFont(Font.BOLD, 13f));
            putClientProperty("JButton.buttonType", "roundRect");
            applyAccent();
        }

        public void applyAccent() {
            setBackground(Theme.accent());
            setForeground(Theme.textOnAccent());
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(Math.max(d.width, 84), Math.max(d.height, 30));
        }
    }

    /**
     * Единая скруглённая кнопка-сплит: акцентная заливка рисуется как одно целое,
     * внутри лежат основная кнопка и кнопка-стрелка. Обе кнопки прозрачны,
     * фон и hover/pressed состояния рисует сама обёртка.
     */
    public static final class SplitAccentButton extends JPanel {
        private final JButton mainButton = new JButton();
        private final JButton menuButton = new JButton("▾");
        private boolean hovered;
        private boolean pressed;

        public SplitAccentButton() {
            setOpaque(false);
            setLayout(new BorderLayout(0, 0));
            setBorder(new EmptyBorder(0, 0, 0, 0));

            mainButton.setContentAreaFilled(false);
            mainButton.setOpaque(false);
            mainButton.setBorderPainted(false);
            mainButton.setFocusPainted(false);
            mainButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            mainButton.setFont(mainButton.getFont().deriveFont(Font.BOLD, 13f));
            mainButton.setForeground(Theme.textOnAccent());
            mainButton.setBorder(new EmptyBorder(6, 14, 6, 10));

            menuButton.setContentAreaFilled(false);
            menuButton.setOpaque(false);
            menuButton.setBorderPainted(false);
            menuButton.setFocusPainted(false);
            menuButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            menuButton.setFont(menuButton.getFont().deriveFont(Font.BOLD, 11f));
            menuButton.setForeground(Theme.textOnAccent());
            menuButton.setBorder(new EmptyBorder(6, 8, 6, 10));

            add(mainButton, BorderLayout.CENTER);
            add(menuButton, BorderLayout.EAST);

            bindHover(mainButton);
            bindHover(menuButton);
        }

        private void bindHover(JButton b) {
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    pressed = false;
                    repaint();
                }
            });
            b.getModel().addChangeListener(e -> {
                boolean isPressed = b.getModel().isPressed();
                if (isPressed != pressed) {
                    pressed = isPressed;
                    repaint();
                }
            });
        }

        public JButton mainButton() {
            return mainButton;
        }

        public JButton menuButton() {
            return menuButton;
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension a = mainButton.getPreferredSize();
            Dimension b = menuButton.getPreferredSize();
            return new Dimension(a.width + b.width, Math.max(a.height, b.height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int arc = Math.min(12, h);
            Color fill;
            Color divider;
            if (!isEnabled()) {
                fill = new Color(Theme.disabledText().getRed(), Theme.disabledText().getGreen(),
                        Theme.disabledText().getBlue(), 90);
                divider = new Color(255, 255, 255, 40);
            } else if (pressed) {
                fill = Theme.accentDark();
                divider = new Color(255, 255, 255, 70);
            } else if (hovered) {
                fill = Theme.accentCyan();
                divider = new Color(255, 255, 255, 90);
            } else {
                fill = Theme.accent();
                divider = new Color(255, 255, 255, 70);
            }
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
            g2.setColor(new Color(255, 255, 255, 50));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
            int mw = menuButton.getWidth();
            if (mw > 0) {
                g2.setColor(divider);
                g2.fillRect(w - 1 - mw, 4, 1, Math.max(1, h - 8));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}