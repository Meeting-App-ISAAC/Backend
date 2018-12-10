package Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Properties;

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
        InputStream inputStream = new FileInputStream("target/classes/config.properties");
        properties.load(inputStream);
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
