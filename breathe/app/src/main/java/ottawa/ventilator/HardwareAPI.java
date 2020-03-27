package ottawa.ventilator;

public interface HardwareAPI {

    // Messages sent down to hardware

    float getMinuteVentilationActual();
    int getTidalVolumeActual();
    int getFiO2Actual();

    int requestNewBreathingRateTarget(int value);
    int requestNewFio2Target(int value);
    int requestNewInspirationPressureTarget(int value);
    int requestNewTidalVolumeTarget(int value);
    int requestNewPeepTarget(int value);
    int requestNewIeRatioTarget(int value);

    void requestRun();
    void requestPause();

    // Messages sent up from hardware

    void runConfirmed();
    void pauseConfirmed();

    void setMinuteVentilationActual(float value);
    void setTidalVolumeActual(int value);
    void setFiO2Actual(int value);
    void fireDisconnectionAlarm(boolean enable);
    void fireMinuteVentilationHighAlarm(boolean enable);
    void fireMinuteVentilationLowAlarm(boolean enable);
    void clearAlarms();

    void setBreathingRateTarget(int value);
    void setInspirationPressureTarget(int value);
    void setFiO2Target(int value);
    void setPeepTarget(int value);
    void setTidalVolumeTarget(int value);
    void setIeRatioTarget(int value);

    void enableRunPauseButton(boolean enable);
    void setRunPauseButtonToRun();
    void setRunPauseButtonToPause();
    void enableSilenceAlarmButton(boolean enable);

}
