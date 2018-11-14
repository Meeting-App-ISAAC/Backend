package Settings;

import Communication.SessionProvider;
import Communication.server.MessageSender;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

public class SettingsWatcher extends TimerTask {

    private long timeStamp;
    private File file;

    public SettingsWatcher(File file) {
        this.file = file;
        this.timeStamp = file.lastModified();
        System.out.println("[info] SettingsWatcher initialised");
    }

    public final void run() {
        long timeStamp = file.lastModified();

        if( this.timeStamp != timeStamp ) {
            this.timeStamp = timeStamp;
            onChange();
        }
    }

    private void onChange() {
        System.out.println("[info] Config has changed");

        //Create new FrontendSettings object
        FrontendSettings frontendSettings = null;
        try {
            frontendSettings = new FrontendSettings();
        }
        catch (IOException e) {
            System.out.println("[error] Could not get properties");
        }

        SessionProvider sessionProvider = SessionProvider.getInstance();
        MessageSender messageSender = new MessageSender();
        messageSender.setSessions(sessionProvider.getSessions());
        messageSender.broadcast(frontendSettings);
    }

}
