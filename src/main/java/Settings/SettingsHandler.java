package Settings;

import AdminConfiguration.ReadClientConfig;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Properties;
import java.util.stream.Collectors;

public class SettingsHandler extends Observable {

    // Utility class handling reading from the config file

    private SettingsHandler(){}
    private static SettingsHandler instance;
    public static SettingsHandler Instance() {
        if(instance == null){
            instance = new SettingsHandler();
        }
        return instance;
    }
    private static Properties properties() throws IOException {
        Properties properties = new Properties();

        InputStream in = getInputStream();

        properties.load(in);
        return properties;
    }

    static InputStream getInputStream(){
        String path = "./config.properties";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            return fis;
        } catch (FileNotFoundException e) {
            return SettingsHandler.class.getClassLoader().getResourceAsStream("config.properties");
        }

    }

    public static String getProperty(String key) throws IOException {
        return properties().getProperty(key);
    }

    public void fileHasChanged(){
        setChanged();
        notifyObservers();
    }
    
}
