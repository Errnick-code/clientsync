# ClientSync

ClientSync is a standalone Minecraft modpack synchronization system designed for servers that need reliable client management.

Unlike traditional in-game downloaders, ClientSync runs outside Minecraft as a dedicated installer. This allows it to safely update, replace, and remove files without fighting the game process.

The server generates a file manifest, and the installer compares the player's local files against the server state. Only required changes are applied.

ClientSync is not a one-time mod downloader — it keeps players synchronized with the current server setup.

## Features

### Modpack installation

Install a complete client setup directly from your server:

- Mods
- Config files
- Resource packs
- Shader packs
- Additional client files

The installer connects to the server, checks the required files, and downloads everything needed for the modpack.

### Updating existing installations

When the server pack changes, players simply run the installer again.

ClientSync detects:

- New files
- Changed files
- Removed files

Only required changes are applied.

No manual modpack archives.  
No searching through Discord messages.  
No manually deleting old files.

### File verification

All files are verified using SHA-256 hashes to ensure the installed client matches the server configuration.

This allows ClientSync to detect:

- Modified files
- Corrupted downloads
- Outdated versions

### Mod information detection

For Fabric mods, ClientSync reads `fabric.mod.json` and stores:

- Mod ID
- Mod version
- File name
- File size
- File hash

This allows the installer to properly track installed mods and detect removed or replaced versions.

## Installer workflow

1. Open ClientSync Installer
2. Enter your server address
3. Review detected changes
4. Start synchronization
5. Launch Minecraft

![Server connection screen](https://cdn.modrinth.com/data/cached_images/40c301ff778a9f53542163c519e07a27ea97ab2a.jpeg)

The installer shows the server connection interface where players enter the address of the server they want to synchronize with.

## File management

The installer displays the complete file structure that will be synchronized.

Players can see:

- Files to install
- Files to update
- Files to remove

![File tree view](https://cdn.modrinth.com/data/cached_images/3f158b2a1a755d35422942d0aa4f797f729fb3a8.jpeg)

<details>
<summary>Server setup</summary>

Install ClientSync on your Fabric server.

After the first server startup, ClientSync creates its own directory:

```
clientsync/
├── config.json
└── client/
```

The `clientsync` folder is used only by ClientSync and contains:

- Server configuration
- Client files distributed to players
- Synchronization data

</details>

<details>
<summary>Configuration</summary>

The server configuration is stored here:

```
clientsync/config.json
```

Example:

```json
{
  "port": 26886,
  "host": "example.com",
  "mc_version": "1.21.11",
  "loader": "fabric",
  "loader_version": "0.19.3",
  "max_concurrent_tasks": 20,
  "chunk_size_mb": 10
}
```

Most values are generated automatically by ClientSync and do not need to be edited manually.

The only values that normally require administrator configuration are:

```json
"host": "your-server-address",
"port": 26886
```

- `host` — public server address or domain used by the installer to connect
- `port` — TCP port used by the ClientSync installer

The following values are managed automatically:

- `mc_version` — Minecraft version required by the client pack
- `loader` — mod loader type
- `loader_version` — required loader version
- `max_concurrent_tasks` — number of simultaneous download tasks
- `chunk_size_mb` — file transfer chunk size

### Server address resolution

ClientSync supports:

```
example.com
```

and:

```
example.com:26886
```

When only a domain is provided, ClientSync automatically checks DNS SRV records:

```
_clientsync._tcp.example.com
```

If no ClientSync SRV record is found, it checks:

```
_minecraft._tcp.example.com
```

If no SRV record is found, the configured default port is used.

After changing the configuration, reload ClientSync without restarting the server:

```
/clientsync reload
```

</details>

<details>
<summary>Client files</summary>

The files distributed to players are stored separately from normal server files:

```
clientsync/client/
```

Only files inside this folder are synchronized by the installer.

Example:

```
clientsync/
└── client/
    ├── mods/
    ├── config/
    ├── resourcepacks/
    ├── shaderpacks/
    ├── options.txt
    └── servers.dat
```

Supported files include:

- `mods/` — client-side mods
- `config/` — mod configuration files
- `resourcepacks/` — resource packs
- `shaderpacks/` — shader packs
- Additional client files

Server-only files are not synchronized.

Example:

```
server/
├── mods/          ❌ Not synchronized
├── config/        ❌ Not synchronized
├── world/         ❌ Not synchronized
└── clientsync/
    └── client/    ✅ Distributed to players
```

This allows administrators to keep server files separate from the client installation.

</details>

<details>
<summary>Building the manifest</summary>

After adding, removing, or updating files inside:

```
clientsync/client/
```

rebuild the synchronization data:

```
/clientsync rebuild
```

ClientSync will:

- Scan all client files
- Calculate SHA-256 hashes
- Detect Fabric mods
- Read mod metadata
- Detect removed files
- Update installer data

The manifest is also generated automatically when the server starts.

</details>

## Why ClientSync?

Traditional in-game download systems have several limitations:

- They usually work only as one-time downloaders
- They cannot safely modify files while Minecraft is running
- They often focus only on downloading mods
- They usually do not handle removed files or full client synchronization

ClientSync is designed as a complete client management solution for Minecraft servers.

## Requirements

- Fabric Loader
- Fabric API

---

ClientSync is the server-side synchronization mod combined with a standalone installer application.

Players only need to run the installer, connect to the server, and keep their client synchronized with the current modpack.
