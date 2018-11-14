package Communication.server;

import Communication.SessionProvider;
import Settings.FrontendSettings;

import java.util.Observable;
import java.util.Observer;

public class SettingsListener implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("SettingsListener called");
        MessageSender messageSender = new MessageSender();
        FrontendSettings frontendSettings = new FrontendSettings();
        messageSender.setSessions(SessionProvider.getInstance().getSessions());
        messageSender.broadcast(frontendSettings);
    }
}
