package CalendarResource.ExchangeCommunication;

import AdminConfiguration.IClientConfig;
import AdminConfiguration.models.ClientConfigModel;
import com.google.gson.Gson;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public class BearerProvider {

    private ClientConfigModel clientConfig;


    public BearerProvider(IClientConfig clientConfig) {
        this.clientConfig = clientConfig.getClientConfig();
    }

    public String getBearer(){

        System.out.println(clientConfig.getTenantId());
        Gson gson = new Gson();
        try {
            return  gson.fromJson(Unirest.post("https://login.microsoftonline.com/{tenant}/oauth2/token")
                    .routeParam("tenant", clientConfig.getTenantId())
                    .field("client_id", clientConfig.getClientId())
                    .field("client_secret", clientConfig.getClientSecret())
                    .field("grant_type", "client_credentials")
                    .field("resource", "https://graph.microsoft.com")
                    .asString().getBody(), ResponeAuthToken.class).getAccess_token();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "";
    }

    private class ResponeAuthToken{
        String token_type;
        String expires_in;
        String ext_expires_in;
        String expires_on;
        String not_before;
        String resource;
        String access_token;

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getExt_expires_in() {
            return ext_expires_in;
        }

        public void setExt_expires_in(String ext_expires_in) {
            this.ext_expires_in = ext_expires_in;
        }

        public String getExpires_on() {
            return expires_on;
        }

        public void setExpires_on(String expires_on) {
            this.expires_on = expires_on;
        }

        public String getNot_before() {
            return not_before;
        }

        public void setNot_before(String not_before) {
            this.not_before = not_before;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }
}
