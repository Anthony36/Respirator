package ottawa.ventilator;

/**
 * Class for directly interfacing with the low-level hardware.
 */
public class Hardware {

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

    public int requestNewBreathingRateTarget(int value) {
        return 0;
    }

    public int requestNewFio2Target(int value) {
        return 0;
    }


    public int requestNewInspirationPressureTarget(int value) {
        return 0;
    }

    public int requestNewTidalVolumeTarget(int value) {
        return 0;
    }

    public int requestNewPeepTarget(int value) {
        return 0;
    }

   public int requestNewIeRatioTarget(int value) {
        return 0;
    }

    public void requestRun() {

    }

    public void requestPause() {

    }

    public void runConfirmed() {

    }

    // ------------------------------
    // Messages sent up from hardware
    // ------------------------------

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

    public void enableRunPauseButton(boolean enable) {

    }

    public void setRunPauseButtonToRun() {

    }

    public void setRunPauseButtonToPause() {

    }

    public void enableSilenceAlarmButton(boolean enable) {

    }
}
