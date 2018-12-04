package Communication.server;

import Communication.SessionProvider;
import Settings.FrontendSettings;

import java.util.Observable;
import java.util.Observer;

public class SettingsListener implements Observer {

    // Listens for changes in the config file
    // Broadcasts updated settings to clients when the config file has changed

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("SettingsListener called");
        MessageSender messageSender = new MessageSender();
        FrontendSettings frontendSettings = new FrontendSettings();
        messageSender.broadcast(frontendSettings);
    }
}
