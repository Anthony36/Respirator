package ottawa.ventilator;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Polls hardware on a timer. Any code that touches the UI must be spun off to run on the UI thread.
 *
 * What needs to be polled:
 *      IsRunning / IsPaused
 *      Actuals values
 *          Minute Ventilation
 *          Tidal volume
 *      Alarms
 *          0 = no alarms, 1 = Disconnect, 2 = Min. Vent. Low, 3 = Min. Vent. High
 *      Patient triggered activity
 */
class Scheduler {

    final private AppCompatActivity activity;
    final private Hardware hardware;
    private Ui ui;

    final private AtomicBoolean checkPatientTriggering = new AtomicBoolean(false);
    final private AtomicBoolean checkIsRunning = new AtomicBoolean(true);
    final private AtomicBoolean checkIsPaused = new AtomicBoolean(true);

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

    void includeIsRunningCheck(boolean include) {
        checkIsRunning.set(include);
    }

    void includeIsPausedCheck(boolean include) {
        checkIsPaused.set(include);
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
            if (checkIsRunning.get()) isRunning();
            if (checkIsPaused.get()) isPaused();
            if (checkPatientTriggering.get()) checkPatientTriggered();
            getAlarms();

            isBusy.set(false);
        }
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
        final boolean isPatientTriggered = hardware.getPatientTriggered();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                ui.setPatientTriggeredLight(isPatientTriggered);
            }
        });
    }

    void isRunning() {
        final boolean isRunning = hardware.isRunning();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                ui.setToRunning(isRunning);
            }
        });
    }

    void isPaused() {
        final boolean isPaused = hardware.isPaused();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                ui.setToRunning(!isPaused);
            }
        });
    }

    void getAlarms() {
        final int alarm = hardware.getAlarm();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                ui.setAlarm(alarm);
            }
        });
    }

}
