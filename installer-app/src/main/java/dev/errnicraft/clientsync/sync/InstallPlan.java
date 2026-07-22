package dev.errnicraft.clientsync.sync;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.errnicraft.clientsync.model.DiffEntry;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InstallPlan {

    public static class PlanEntry {
        public String category;
        public String key;
        public boolean delete;
        public String hash;
        public String version;
        public String cachedFile;
        public String destination;

        public static PlanEntry install(DiffEntry entry, String hash, String version, String cachedFile, String destination) {
            PlanEntry pe = new PlanEntry();
            pe.category = entry.category.name();
            pe.key = entry.key;
            pe.delete = false;
            pe.hash = hash;
            pe.version = version;
            pe.cachedFile = cachedFile;
            pe.destination = destination;
            return pe;
        }

        public static PlanEntry delete(DiffEntry entry) {
            PlanEntry pe = new PlanEntry();
            pe.category = entry.category.name();
            pe.key = entry.key;
            pe.delete = true;
            pe.hash = "";
            pe.version = "";
            pe.cachedFile = "";
            pe.destination = entry.staleLocalPath != null ? entry.staleLocalPath.toString() : "";
            return pe;
        }
    }

    private static final Gson GSON = new Gson();

    public static Path planPath(Path gameDir) {
        return gameDir.resolve("clientsync/install-plan.json");
    }

    public static void write(Path gameDir, List<PlanEntry> entries) throws IOException {
        Path path = planPath(gameDir);
        Files.createDirectories(path.getParent());
        Files.writeString(path, GSON.toJson(entries));
    }

    public static List<PlanEntry> read(Path gameDirOrPlanPath) throws IOException {
        Path path = gameDirOrPlanPath.toString().endsWith(".json")
                ? gameDirOrPlanPath
                : planPath(gameDirOrPlanPath);
        String json = Files.readString(path);
        Type type = new TypeToken<List<PlanEntry>>() {}.getType();
        return GSON.fromJson(json, type);
    }
}
