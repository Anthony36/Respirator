package ottawa.ventilator.logic;

import ottawa.ventilator.hardware.HardwareApi;
import ottawa.ventilator.ui.UiApi;

/**
 * Responsible for the small amount of application logic we will perform.
 * Provides an important decoupling between hardware and UI.
 */
public class Logic implements UiApi, HardwareApi {

    @Override
    public void requestPause() {

    }

    @Override
    public void pauseConfirmed() {

    }

    @Override
    public void setMinuteVentilationValue(float value) {

    }

    @Override
    public void setTidalVolumeValue(int value) {

    }

    @Override
    public void setFiO2Value(int value) {

    }

    @Override
    public void setBreathingRateTarget(int value) {

    }

    @Override
    public void setInspirationTarget(int value) {

    }

    @Override
    public void setFiO2Target(int value) {

    }

    @Override
    public void setPeepTarget(int value) {

    }

    @Override
    public void setTidalVolumeTarget(int value) {

    }

    @Override
    public void setIeRatioTarget(int value) {

    }
}
