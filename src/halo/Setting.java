package halo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Wini;

/**
 *
 * @author Phan Hieu
 */
public class Setting {

    private static File iniFile = new File("setting.ini");

    private static String DEFAULT_DATABASE_IP = "localhost";
    private static int DEFAULT_DATABASE_PORT = 1433;

    public static String Load(String section, String name) {
        try {
            iniFile.createNewFile();
            Wini wini = new Wini(iniFile);
            return wini.get(section, name, String.class);
        } catch (IOException ex) {
            return null;
        }
    }

    public static String GetServerIP() {
        try {
            iniFile.createNewFile();
            Wini wini = new Wini(iniFile);
            String ip = wini.get("Database", "IP", String.class);
            return ip == null ? DEFAULT_DATABASE_IP : ip;
        } catch (IOException ex) {
            return DEFAULT_DATABASE_IP;
        }
    }
    
    public static void SetServerIP(String ip) {
        try {
            Store("Database", "IP", ip);
        } catch (IOException ex) {}
    }
    
    public static int GetServerPort() {
        try {
            iniFile.createNewFile();
            Wini wini = new Wini(iniFile);
            String port = wini.get("Database", "port", String.class);
            return port==null ? DEFAULT_DATABASE_PORT : Integer.parseInt(port);
        } catch (IOException ex) {
            return DEFAULT_DATABASE_PORT;
        }
    }
    
    public static void SetServerPort(int port) {
        try {
            Store("Database", "port", port);
        } catch (IOException ex) {}
    }

    public static void Store(String section, String name, String value) throws IOException {
        iniFile.createNewFile();
        Wini wini = new Wini(iniFile);
        wini.put(section, name, value);
        wini.store();
    }
    
    public static void Store(String section, String name, int value) throws IOException {
        iniFile.createNewFile();
        Wini wini = new Wini(iniFile);
        wini.put(section, name, value);
        wini.store();
    }
}
