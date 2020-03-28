package ottawa.ventilator;

/**
 * Class for directly interfacing with the low-level hardware.
 */
class Hardware implements HardwareAPI {

    // ------------------------------
    // Messages sent down to hardware
    // ------------------------------

    public float getMinuteVentilationActual() {
        return 0;
    }

    public int getTidalVolumeActual() {
        return 0;
    }

    public int getFiO2Actual() {
        return 0;
    }

    public void requestNewBreathingRateTarget(int value) {

    }

    public void requestNewFio2Target(int value) {

    }

    public void requestNewInspirationPressureTarget(int value) {

    }

    public void requestNewTidalVolumeTarget(int value) {

    }

    public void requestNewPeepTarget(int value) {

    }

    public void requestNewIeRatioTarget(int value) {

    }

    public void requestRun() {

    }

    public void requestPause() {

    }

    public void requestSilenceAlarm() {

    }

    // ------------------------------
    // Messages sent up from hardware
    // These may need to be wrapped in Runnables or something to prevent a crash on the UI thread
    // ------------------------------

    public void runConfirmed() {

    }

    public void pauseConfirmed() {

    }

    public void setMinuteVentilationActual(float value) {

    }

    public void setTidalVolumeActual(int value) {

    }

    public void setFiO2Actual(int value) {

    }

    public void fireDisconnectionAlarm(boolean enable) {

    }

    public void fireMinuteVentilationHighAlarm(boolean enable) {

    }

    public void fireMinuteVentilationLowAlarm(boolean enable) {

    }

    public void clearAlarms() {

    }

    public void setBreathingRateTarget(int value) {

    }

    public void setInspirationPressureTarget(int value) {

    }

    public void setFiO2Target(int value) {

    }

    public void setPeepTarget(int value) {

    }

    public void setTidalVolumeTarget(int value) {

    }

    public void setIeRatioTarget(int value) {

    }

    public void allowPatientTriggering(boolean allow) {

    }

    public void setPatientTriggeredLight(boolean allow) {

    }

    public void enableRunPauseButton(boolean enable) {

    }

    public void setRunPauseButtonToRun() {

    }

    public void setRunPauseButtonToPause() {

    }

    public void enableSilenceAlarmButton(boolean enable) {

    }

}
