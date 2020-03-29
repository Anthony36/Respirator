package ottawa.ventilator;

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

    AtomicBoolean checkPatientTriggering = new AtomicBoolean(false);

    // Called on application life cycle change
    void start() {

    }

    // Called on application life cycle change
    void pause() {

    }

    // Called on application life cycle change
    void resume() {

    }

    // Called on application life cycle change
    void stop() {

    }

    void includePatientTriggeringCheck(boolean include) {
        checkPatientTriggering.set(include);
    }

    /*
       TimerTask task = new TimerTask() {
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        view.setTextColor(activity.getResources().getColor(R.color.clrWhite));
                    }
                });
            }
        };

        Timer timer = new Timer("Timer");
        timer.schedule(task, 500L);
     */

}
