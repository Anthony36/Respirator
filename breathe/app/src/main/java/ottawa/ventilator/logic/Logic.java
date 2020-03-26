package ottawa.ventilator.logic;

import ottawa.ventilator.hardware.HardwareApi;
import ottawa.ventilator.ui.UiApi;

/**
 * Responsible for the small amount of application logic we will perform.
 * Provides an important decoupling between hardware and UI.
 */
public class Logic implements UiApi, HardwareApi {

    // Message for hardware

    @Override
    public void requestPause() {

    }

    // Messages from hardware

    @Override
    public void pauseConfirmed() {

    }

    @Override
    public void setTidalVolumeActual(int value) {

    }
}
