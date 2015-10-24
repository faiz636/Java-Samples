import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Muhammad Faizan Khan on 24/10/2015.
 */
public class Test {
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        beepForAnHour();
    }

    public static void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() {
                System.out.println("beep");
            }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 5, 2, TimeUnit.SECONDS);
        final Runnable beepTerminator = new Runnable() {
            public void run() {
                beeperHandle.cancel(true);
                System.out.println("terminator");
            }
        };
        scheduler.schedule(beepTerminator, 20, TimeUnit.SECONDS);
    }

}