package AdminConfiguration;

import AdminConfiguration.models.ClientConfigModel;

import java.io.*;
import java.util.Properties;

public class ReadClientConfig implements IClientConfig{


    InputStream getInputStream(){
        String path = "./auth.properties";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            return fis;
        } catch (FileNotFoundException e) {
            return ReadClientConfig.class.getClassLoader().getResourceAsStream("auth.properties");
        }

    }


    public ClientConfigModel getClientConfig(){
        InputStream authConfigStream = getInputStream();

        if (authConfigStream != null) {
            Properties authProps = new Properties();
            try {
                authProps.load(authConfigStream);
                ClientConfigModel clientConfigModel = new ClientConfigModel();
                clientConfigModel.setClientId(authProps.getProperty("clientId"));
                clientConfigModel.setClientSecret(authProps.getProperty("clientSecret"));
                clientConfigModel.setTenantId(authProps.getProperty("tenantId"));

                System.err.println(clientConfigModel.getTenantId());
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
