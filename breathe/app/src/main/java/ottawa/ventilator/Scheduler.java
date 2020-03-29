package ottawa.ventilator;

/**
 * Polls hardware on a timer. Any code that touches the UI must be spun off on a new thread.
 */
public class Scheduler {

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
