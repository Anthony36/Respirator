package ottawa.ventilator;

/**
 * Class for directly interfacing with the low-level hardware.
 */
class Hardware implements HardwareAPI {

    @Override
    public float getMinuteVentilationActual() {
        return 0;
    }

    @Override
    public int getTidalVolumeActual() {
        return 0;
    }

    @Override
    public void requestNewBreathingRateTarget(int value) {

    }

    @Override
    public void requestNewFio2Target(int value) {

    }

    @Override
    public void requestNewPipTarget(int value) {

    }

    @Override
    public void requestNewTidalVolumeTarget(int value) {

    }

    @Override
    public void requestNewPeepTarget(int value) {

    }

    @Override
    public void requestNewIeRatioTarget(int value) {

    }

    @Override
    public int getBreathingRateTarget() {
        return 0;
    }

    @Override
    public int getFio2Target() {
        return 0;
    }

    @Override
    public int getPipTarget() {
        return 0;
    }

    @Override
    public int getTidalVolumeTarget() {
        return 0;
    }

    @Override
    public int getPeepTarget() {
        return 0;
    }

    @Override
    public int getIeRatioTarget() {
        return 0;
    }

    @Override
    public void requestRun() {

    }

    @Override
    public void requestPause() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public int getAlarm() {
        return 0;
    }

    @Override
    public void requestPatientTriggering(boolean onOff) {

    }

    @Override
    public boolean isPatientTriggeringAllowed() {
        return false;
    }

    @Override
    public boolean getPatientTriggered() {
        return false;
    }

    @Override
    public void requestSilenceAlarm() {

    }

}
