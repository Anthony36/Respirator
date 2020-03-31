package ottawa.ventilator.hardware;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import ottawa.ventilator.R;
import ottawa.ventilator.application.Application;
import ottawa.ventilator.application.ILifecycle;

public class MockHardware implements IHardware {

    final Application application;

    private AtomicBoolean isRunAllowed = new AtomicBoolean(false);
    private float minuteVentilationActual = 0f;
    private int tidalVolumeActual = 0;

    public MockHardware(Application application) {
        this.application = application;
    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    public void onCreate() {}

    public void onStart() {
        // Allow running in 3 seconds
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                isRunAllowed.set(true);
            }
        };
        new Timer("MockHardware").schedule(task, 2000L);
    }

    public void onPause() {}
    public void onResume() {}
    public void onStop() {}
    public void onRestart() {}
    public void onDestroy() {}

    // ---------------------------------------------------------------------------------------------

    public float getMinuteVentilationActual() {
        return 0;
    }

    public int getTidalVolumeActual() {
        return 0;
    }

    public void requestNewBreathingRateTarget(int value) {

    }

    public void requestNewFio2Target(int value) {

    }

    public void requestNewPipTarget(int value) {

    }

    public void requestNewTidalVolumeTarget(int value) {

    }

    public void requestNewPeepTarget(int value) {

    }

    public void requestNewIeRatioTarget(int value) {

    }

    public int getBreathingRateTarget() {
        return 0;
    }

    public int getFio2Target() {
        return 0;
    }

    public int getPipTarget() {
        return 0;
    }

    public int getTidalVolumeTarget() {
        return 0;
    }

    public int getPeepTarget() {
        return 0;
    }

    public int getIeRatioTarget() {
        return 0;
    }

    public void requestRun() {

    }

    public void requestPause() {

    }

    public boolean isRunAllowed() {
        return isRunAllowed.get();
    }

    public boolean isRunning() {
        return true;
    }

    public boolean isPaused() {
        return true;
    }

    public int getAlarm() {
        return 0;
    }

    public void requestPatientTriggering(boolean onOff) {

    }

    public boolean isPatientTriggeringAllowed() {
        return false;
    }

    public boolean getPatientTriggered() {
        return false;
    }

    public void requestSilenceAlarm() {

    }

}
