package Settings;

import java.io.File;
import java.util.TimerTask;

public class SettingsWatcher extends TimerTask {

    // Timer task looking for changes in the config file

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
        SettingsHandler.Instance().fileHasChanged();
    }

}
