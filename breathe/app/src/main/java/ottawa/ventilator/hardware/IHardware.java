package ottawa.ventilator.hardware;

import ottawa.ventilator.application.ILifecycle;

public interface IHardware extends ILifecycle {

    // Status displays
    float getMinuteVentilationActual();
    int getTidalVolumeActual();

    // Target settings
    void requestNewBreathingRateTarget(int value);
    void requestNewFio2Target(int value);
    void requestNewPipTarget(int value);
    void requestNewTidalVolumeTarget(int value);
    void requestNewPeepTarget(int value);
    void requestNewIeRatioTarget(int value);

    // Confirm target settings
    int getBreathingRateTarget();
    int getFio2Target();
    int getPipTarget();
    int getTidalVolumeTarget();
    int getPeepTarget();
    int getIeRatioTarget();

    // Command requests
    void requestRun();
    void requestPause();

    // Confirm command requests
    boolean isRunAllowed();
    boolean isRunning();
    boolean isPaused();

    // Get current alarm, otherwise 0 if no alarms
    int getAlarm();

    // Request change to allowing patient triggering
    void requestPatientTriggering(boolean onOff);

    // Confirm patient triggering allowed
    boolean isPatientTriggeringAllowed();

    // Is the patient currently triggering?
    boolean getPatientTriggered();

    // Request audible alarm to be silenced
    void requestSilenceAlarm();

}
