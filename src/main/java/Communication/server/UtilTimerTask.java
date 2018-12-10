package Communication.server;

import java.time.LocalDateTime;
import java.util.TimerTask;

class UtilTimerTask extends TimerTask {

    // Tasks that should be run on an interval (regardless of settings)

    private LocalDateTime time;

    public UtilTimerTask(LocalDateTime start) {
        System.out.println("[info] Util timer tasks have been initiated!");
        time = start;
    }

    public void run() {
        if (LocalDateTime.now().getDayOfYear() != time.getDayOfYear()) {
            System.out.println("[info] Date has changed! Broadcasting reservations for this day");
            MessageSender messageSender = new MessageSender();
            messageSender.sendReservationDump();
        }
    }

}
