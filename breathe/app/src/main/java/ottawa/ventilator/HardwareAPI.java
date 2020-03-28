package ottawa.ventilator;

public interface HardwareAPI {

    // Messages sent down to hardware

    float getMinuteVentilationActual();
    int getTidalVolumeActual();
    int getFiO2Actual();

    void requestNewBreathingRateTarget(int value);
    void requestNewFio2Target(int value);
    void requestNewInspirationPressureTarget(int value);
    void requestNewTidalVolumeTarget(int value);
    void requestNewPeepTarget(int value);
    void requestNewIeRatioTarget(int value);

    void requestRun();
    void requestPause();
    void requestSilenceAlarm();

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

    void allowPatientTriggering(boolean allow);
    void setPatientTriggeredLight(boolean allow);

    void enableRunPauseButton(boolean enable);
    void setRunPauseButtonToRun();
    void setRunPauseButtonToPause();
    void enableSilenceAlarmButton(boolean enable);

}
