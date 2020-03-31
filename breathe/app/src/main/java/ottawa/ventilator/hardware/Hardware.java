package ottawa.ventilator.hardware;

import androidx.appcompat.app.AppCompatActivity;

import ottawa.ventilator.application.Application;
import ottawa.ventilator.application.ILifecycle;

/**
 * Class for directly interfacing with the low-level hardware.
 */
public class Hardware implements IHardware {

    final private Usb usb;

    public Hardware(final Application application, final AppCompatActivity activity) {
        usb = new Usb(application, activity);
    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    public void onCreate() {
        usb.initialize();
    }

    public void onStart() {}
    public void onPause() {}
    public void onResume() {}

    public void onStop() {
        usb.stop();
    }

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
        return false;
    }

    public boolean isRunning() {
        return false;
    }

    public boolean isPaused() {
        return false;
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
