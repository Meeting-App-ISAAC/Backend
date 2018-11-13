package Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SettingsHandler {

    private static Properties properties() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("target/classes/config.properties");
        properties.load(inputStream);
        return properties;
    }

    public static String getProperty(String key) throws IOException {
        return properties().getProperty(key);
    }
    
}
