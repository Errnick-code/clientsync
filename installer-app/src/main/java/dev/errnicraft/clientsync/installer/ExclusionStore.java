package dev.errnicraft.clientsync.installer;

import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

public class ExclusionStore {

    private static final String KEY_PREFIX = "excluded_";
    private static final String SEPARATOR = "\n";

    private final Preferences prefs;

    public ExclusionStore(Preferences prefs) {
        this.prefs = prefs;
    }

    private String prefKey(String host) {
        return KEY_PREFIX + host;
    }

    public Set<String> load(String host) {
        String raw = prefs.get(prefKey(host), "");
        Set<String> result = new HashSet<>();
        if (raw.isBlank()) return result;
        for (String key : raw.split(SEPARATOR)) {
            if (!key.isBlank()) result.add(key);
        }
        return result;
    }

    private void save(String host, Set<String> keys) {
        prefs.put(prefKey(host), String.join(SEPARATOR, keys));
    }

    public void add(String host, String key) {
        Set<String> keys = load(host);
        keys.add(key);
        save(host, keys);
    }

    public void remove(String host, String key) {
        Set<String> keys = load(host);
        keys.remove(key);
        save(host, keys);
    }
}
