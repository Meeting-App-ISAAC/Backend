package Settings;

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

        InputStream in = SettingsHandler.class.getResourceAsStream("/config.properties");

        properties.load(in);
        return properties;
    }

    public static String getProperty(String key) throws IOException {
        return properties().getProperty(key);
    }

    public void fileHasChanged(){
        setChanged();
        notifyObservers();
    }
    
}
