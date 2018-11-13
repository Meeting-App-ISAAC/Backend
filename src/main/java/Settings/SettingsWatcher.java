package Settings;

import java.io.File;
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
            onChange(file);
        }
    }

    private void onChange(File file) {
        // TODO: send settings to frontend
        System.out.println("[info] Config has changed");
    }

}
