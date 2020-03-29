package ottawa.ventilator;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Polls hardware on a timer. Any code that touches the UI must be spun off to run on the UI thread.
 *
 * What needs to be polled:
 *      IsRunning
 *      Actuals values
 *          Minute Ventilation
 *          Tidal volume
 *      Alarms
 *          0 = no alarms, 1 = Disconnect, 2 = Min. Vent. Low, 3 = Min. Vent. High
 *      Patient triggered activity
 *
 * What needs to be confirmed after a request:
 *      Target settings '-' and '+'
 *      Patient trigger allow/off
 *      Running allowed
 *      Paused allowed
 */
class Scheduler {

    final private AppCompatActivity activity;
    final private Hardware hardware;
    private Ui ui;

    final private AtomicBoolean checkPatientTriggering = new AtomicBoolean(false);
    final private AtomicBoolean checkIsRunAllowed = new AtomicBoolean(false);
    final private AtomicBoolean checkIsRunning = new AtomicBoolean(false);
    final private AtomicBoolean checkIsPaused = new AtomicBoolean(false);

    final private AtomicBoolean isBusy = new AtomicBoolean(false);
    private Timer timer;
    private int timerTicks = 0;

    Scheduler(final AppCompatActivity activity, final Hardware hardware) {
        this.activity = activity;
        this.hardware = hardware;
    }

    void setUi(Ui ui) {
        this.ui = ui;
    }

    // ---------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // ---------------------------------------------------------------------------------------------

    // Called from Activity on application start
    void start() {
        TimerTask task = new TimerTask() {
            public void run() {
                onTimerEvent();
            }
        };

        timer = new Timer("Scheduler");

        // Delay 100 ms, fire every 200 ms
        timer.scheduleAtFixedRate(task,100L, 200L);
    }

    // Called from Activity on application stop
    void stop() {
        timer.cancel();
    }

    void includePatientTriggeringCheck(boolean include) {
        checkPatientTriggering.set(include);
    }

    // ---------------------------------------------------------------------------------------------
    // Hardware Polling
    // ---------------------------------------------------------------------------------------------

    // Called everytime the timer fires. Perform all the hardware polling here.
    void onTimerEvent() {
        if (!isBusy.get()) {
            isBusy.set(true);
            timerTicks++;

            if (timerTicks == 5) {
                // Poll 1000 ms events
                pollMinuteVentilationActual();
                pollTidalVolumeActual();
                timerTicks = 0;
            }

            // Poll 200 ms events
            if (checkIsRunAllowed.get()) isRunAllowed();
            if (checkIsRunning.get()) isRunning();
            if (checkIsPaused.get()) isPaused();
            if (checkPatientTriggering.get()) checkPatientTriggered();

            isBusy.set(false);
        }
    }

    void isRunAllowed() {

    }

    void isRunning() {

    }

    void isPaused() {

    }

    void pollMinuteVentilationActual() {
        final float minuteVentilationActual = hardware.getMinuteVentilationActual();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                ui.setMinuteVentilationActual(minuteVentilationActual);
            }
        });
    }

    void pollTidalVolumeActual() {
        final int tidalVolumeActual = hardware.getTidalVolumeActual();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                ui.setTidalVolumeActual(tidalVolumeActual);
            }
        });
    }

    void checkPatientTriggered() {

    }

}
