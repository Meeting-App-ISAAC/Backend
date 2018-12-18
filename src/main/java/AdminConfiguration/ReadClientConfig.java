package AdminConfiguration;

import AdminConfiguration.models.ClientConfigModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadClientConfig implements IClientConfig{

    public ClientConfigModel getClientConfig(){
        String authConfigFile = "auth.properties";
        InputStream authConfigStream = ReadClientConfig.class.getClassLoader().getResourceAsStream(authConfigFile);

        if (authConfigStream != null) {
            Properties authProps = new Properties();
            try {
                authProps.load(authConfigStream);
                ClientConfigModel clientConfigModel = new ClientConfigModel();
                clientConfigModel.setClientId(authProps.getProperty("clientId"));
                clientConfigModel.setClientSecret(authProps.getProperty("clientSecret"));
                clientConfigModel.setTenantId(authProps.getProperty("tenantId"));
                return clientConfigModel;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    authConfigStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
