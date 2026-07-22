package dev.errnicraft.clientsync.net;

import java.util.ArrayList;
import java.util.List;

public final class LoaderVersionMatcher {

    private LoaderVersionMatcher() {}

    public static boolean matches(String requirement, String actual) {
        if (requirement == null || requirement.isBlank()) return true;
        if (actual == null || actual.isBlank()) return false;

        String req = requirement.trim();
        String op;
        String reqVersion;

        if (req.startsWith(">=")) {
            op = ">=";
            reqVersion = req.substring(2).trim();
        } else if (req.startsWith("<=")) {
            op = "<=";
            reqVersion = req.substring(2).trim();
        } else if (req.startsWith(">")) {
            op = ">";
            reqVersion = req.substring(1).trim();
        } else if (req.startsWith("<")) {
            op = "<";
            reqVersion = req.substring(1).trim();
        } else if (req.startsWith("=")) {
            op = "=";
            reqVersion = req.substring(1).trim();
        } else {
            op = "=";
            reqVersion = req;
        }

        int cmp = compareVersions(actual, reqVersion);
        return switch (op) {
            case ">=" -> cmp >= 0;
            case "<=" -> cmp <= 0;
            case ">" -> cmp > 0;
            case "<" -> cmp < 0;
            default -> cmp == 0;
        };
    }

    public static String formatRequirement(String requirement) {
        if (requirement == null || requirement.isBlank()) return "";
        return requirement.trim();
    }

    private static int compareVersions(String v1, String v2) {
        List<Long> a = parseVersionSegments(v1);
        List<Long> b = parseVersionSegments(v2);
        int len = Math.max(a.size(), b.size());
        for (int i = 0; i < len; i++) {
            long x = i < a.size() ? a.get(i) : 0;
            long y = i < b.size() ? b.get(i) : 0;
            if (x != y) return Long.compare(x, y);
        }
        return 0;
    }

    private static List<Long> parseVersionSegments(String version) {
        List<Long> segments = new ArrayList<>();
        if (version == null) return segments;
        String numericPart = version.split("[+\\-]")[0];
        for (String part : numericPart.split("\\.")) {
            String digits = part.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) continue;
            try {
                segments.add(Long.parseLong(digits));
            } catch (NumberFormatException ignored) {}
        }
        return segments;
    }
}
