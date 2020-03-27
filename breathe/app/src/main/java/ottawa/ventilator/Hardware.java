package ottawa.ventilator;

/**
 * Class for directly interfacing with the low-level hardware.
 */
class Hardware {

    // ------------------------------
    // Messages sent down to hardware
    // ------------------------------

    float getMinuteVentilationActual() {
        return 0;
    }

    int getTidalVolumeActual() {
        return 0;
    }

    int getFiO2Actual() {
        return 0;
    }

    int requestNewBreathingRateTarget(int value) {
        return 0;
    }

    int requestNewFio2Target(int value) {
        return 0;
    }

    int requestNewInspirationPressureTarget(int value) {
        return 0;
    }

    int requestNewTidalVolumeTarget(int value) {
        return 0;
    }

    int requestNewPeepTarget(int value) {
        return 0;
    }

    int requestNewIeRatioTarget(int value) {
        return 0;
    }

    void requestRun() {

    }

    void requestPause() {

    }

    // ------------------------------
    // Messages sent up from hardware
    // ------------------------------

    void runConfirmed() {

    }

    void pauseConfirmed() {

    }

    void setMinuteVentilationActual(float value) {

    }

    void setTidalVolumeActual(int value) {

    }

    void setFiO2Actual(int value) {

    }

    void fireDisconnectionAlarm(boolean enable) {

    }

    void fireMinuteVentilationHighAlarm(boolean enable) {

    }

    void fireMinuteVentilationLowAlarm(boolean enable) {

    }

    void clearAlarms() {

    }

    void setBreathingRateTarget(int value) {

    }

    void setInspirationPressureTarget(int value) {

    }

    void setFiO2Target(int value) {

    }

    void setPeepTarget(int value) {

    }

    void setTidalVolumeTarget(int value) {

    }

    void setIeRatioTarget(int value) {

    }

    void enableRunPauseButton(boolean enable) {

    }

    void setRunPauseButtonToRun() {

    }

    void setRunPauseButtonToPause() {

    }

    void enableSilenceAlarmButton(boolean enable) {

    }

}
