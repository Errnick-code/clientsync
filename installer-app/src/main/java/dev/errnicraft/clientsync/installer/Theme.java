package dev.errnicraft.clientsync.installer;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Тема оформления на базе FlatLaf: режимы SYSTEM/LIGHT/DARK.
 * Установка LAF и перекраска всех компонентов на лету.
 */
public final class Theme {

    public enum Mode { SYSTEM, LIGHT, DARK }

    /**
     * Панель, которая должна заново применить свои явные цвета при смене темы.
     */
    public interface ThemeAware {
        void retheme();
    }

    private static Mode mode = Mode.SYSTEM;

    private Theme() {}

    public static Mode mode() {
        return mode;
    }

    public static void setMode(Mode newMode) {
        mode = newMode != null ? newMode : Mode.SYSTEM;
    }

    public static boolean isDark() {
        return switch (resolvedMode()) {
            case DARK -> true;
            case LIGHT -> false;
            case SYSTEM -> detectSystemDark();
        };
    }

    private static Mode resolvedMode() {
        if (mode != Mode.SYSTEM) return mode;
        return detectSystemDark() ? Mode.DARK : Mode.LIGHT;
    }

    // ---------- Установка Look & Feel ----------

    /**
     * Устанавливает FlatLaf Light/Dark как активный LAF (меняем живой LAF,
     * чтобы все новые компоненты получали правильные цвета).
     */
    public static void apply() {
        try {
            if (isDark()) {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            }
        } catch (Exception e) {
            System.err.println("[ClientSync-Installer] Failed to init FlatLaf: " + e);
        }
    }

    /**
     * Перекрашивает все компоненты дерева под текущую тему.
     * FlatLaf.updateUI() обновляет и существующие компоненты.
     */
    public static void retheme(java.awt.Component root) {
        if (root == null) return;
        SwingUtilities.updateComponentTreeUI(root);
        traverseThemeAware(root);
        root.repaint();
    }

    public static void rethemeRecursive(java.awt.Component c) {
        retheme(c);
    }

    private static void traverseThemeAware(java.awt.Component c) {
        if (c instanceof ThemeAware aware) {
            aware.retheme();
        }
        if (c instanceof java.awt.Container container) {
            for (java.awt.Component child : container.getComponents()) {
                traverseThemeAware(child);
            }
        }
    }

    // ---------- Цвета для кастомных виджетов ----------

    public static Color accent() {
        return isDark() ? new Color(76, 154, 255) : UIManager.getColor("Component.accentColor");
    }

    public static Color accentDark() {
        return isDark() ? new Color(48, 116, 220) : new Color(20, 92, 200);
    }

    public static Color accentCyan() {
        return isDark() ? new Color(96, 210, 255) : new Color(0, 150, 200);
    }

    public static Color bg() {
        return UIManager.getColor("Panel.background");
    }

    public static Color panelBg() {
        return isDark() ? UIManager.getColor("Panel.background") : UIManager.getColor("Window.background");
    }

    public static Color border() {
        return UIManager.getColor("Component.borderColor");
    }

    public static Color textPrimary() {
        return UIManager.getColor("Label.foreground");
    }

    public static Color textSecondary() {
        return isDark() ? new Color(160, 166, 184) : new Color(110, 118, 132);
    }

    public static Color textOnAccent() {
        return Color.WHITE;
    }

    public static Color fieldBg() {
        return UIManager.getColor("TextField.background");
    }

    public static Color success() {
        return isDark() ? new Color(99, 198, 128) : new Color(39, 139, 69);
    }

    public static Color warn() {
        return isDark() ? new Color(224, 166, 64) : new Color(176, 122, 14);
    }

    public static Color danger() {
        return isDark() ? new Color(224, 100, 100) : new Color(200, 62, 52);
    }

    public static Color dangerBright() {
        return isDark() ? new Color(255, 90, 90) : new Color(235, 55, 45);
    }

    public static Color disabledText() {
        return UIManager.getColor("Label.disabledText");
    }

    public static Color selectionBg() {
        return UIManager.getColor("List.selectionBackground");
    }

    public static Color selectionFg() {
        return UIManager.getColor("List.selectionForeground");
    }

    public static Color treeCellBg(boolean selected) {
        return selected ? selectionBg() : fieldBg();
    }

    // ---------- Попапы комбобоксов ----------

    @SuppressWarnings("unchecked")
    private static javax.swing.ListCellRenderer<Object> castRenderer(JComboBox<?> box) {
        return (javax.swing.ListCellRenderer<Object>) box.getRenderer();
    }

    /**
     * Широкий попап: ширина по самому длинному элементу, минимум — ширина бокса.
     */
    public static void setupWidePopup(JComboBox<?> box) {
        box.putClientProperty("JComboBox.isWide", true);
        box.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                makePopupWide(box);
            }

            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
            }
        });
    }

    private static void makePopupWide(JComboBox<?> box) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Object child = box.getAccessibleContext().getAccessibleChild(0);
                if (!(child instanceof javax.swing.plaf.basic.ComboPopup popup)) return;
                var list = popup.getList();
                var scrollPane = (javax.swing.JScrollPane)
                        javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JScrollPane.class, list);
                if (scrollPane == null) return;
                int itemWidth = 0;
                for (int i = 0; i < box.getItemCount(); i++) {
                    Object item = box.getItemAt(i);
                    javax.swing.ListCellRenderer<Object> renderer = castRenderer(box);
                    var c = renderer.getListCellRendererComponent(list, item, i, false, false);
                    itemWidth = Math.max(itemWidth, c.getPreferredSize().width);
                }
                int targetWidth = Math.max(box.getWidth(), itemWidth + 8);
                var size = scrollPane.getPreferredSize();
                size.width = targetWidth;
                scrollPane.setPreferredSize(size);
                scrollPane.setMaximumSize(size);
            } catch (Exception ignored) {
            }
        });
    }

    // ---------- Определение системной темы ----------

    public static boolean detectSystemDark() {
        if (System.getProperty("os.name", "").toLowerCase().contains("win")) {
            String value = readWindowsReg("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                    "AppsUseLightTheme");
            if (value != null) {
                return value.equals("0");
            }
        }
        String uiMode = System.getProperty("user.apple.ui.root.interfaceStyle");
        if (uiMode != null) {
            return uiMode.contains("Dark");
        }
        return false;
    }

    private static String readWindowsReg(String key, String valueName) {
        try {
            ProcessBuilder pb = new ProcessBuilder("reg", "query", key, "/v", valueName);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            if (!process.waitFor(3, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return null;
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int idx = line.indexOf(valueName);
                    if (idx < 0) continue;
                    String rest = line.substring(idx + valueName.length()).trim();
                    for (String part : rest.split("\\s+")) {
                        if (part.startsWith("0x")) {
                            try {
                                long v = Long.parseLong(part.substring(2), 16);
                                return String.valueOf(v);
                            } catch (NumberFormatException ignored) {
                                return part;
                            }
                        }
                        if (part.equals("0") || part.equals("1")) {
                            return part;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}