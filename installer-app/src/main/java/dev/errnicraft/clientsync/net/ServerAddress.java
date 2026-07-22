package dev.errnicraft.clientsync.net;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class ServerAddress {

    private static final int DEFAULT_PORT = 25566;

    public final String host;
    public final int port;

    private ServerAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static ServerAddress resolve(String serverAddress) {
        int idx = serverAddress.lastIndexOf(':');
        if (idx >= 0) {
            String host = serverAddress.substring(0, idx);
            String portPart = serverAddress.substring(idx + 1);
            try {
                return new ServerAddress(host, Integer.parseInt(portPart));
            } catch (NumberFormatException e) {
                return new ServerAddress(serverAddress, DEFAULT_PORT);
            }
        }

        ServerAddress srv = lookupSrv("_clientsync._tcp." + serverAddress);
        if (srv != null) return srv;

        srv = lookupSrv("_minecraft._tcp." + serverAddress);
        if (srv != null) return srv;

        return new ServerAddress(serverAddress, DEFAULT_PORT);
    }

    private static ServerAddress lookupSrv(String query) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            InitialDirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(query, new String[]{"SRV"});
            Attribute srvAttr = attrs.get("SRV");
            if (srvAttr == null) return null;

            String record = (String) srvAttr.get();
            String[] parts = record.split(" ");
            if (parts.length != 4) return null;

            int srvPort = Integer.parseInt(parts[2]);
            String srvHost = parts[3];
            if (srvHost.endsWith(".")) srvHost = srvHost.substring(0, srvHost.length() - 1);

            return new ServerAddress(srvHost, srvPort);
        } catch (NamingException | NumberFormatException e) {
            return null;
        }
    }
}
