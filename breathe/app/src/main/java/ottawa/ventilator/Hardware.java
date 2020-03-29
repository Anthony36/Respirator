package ottawa.ventilator;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for directly interfacing with the low-level hardware.
 */
class Hardware implements HardwareAPI {

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
