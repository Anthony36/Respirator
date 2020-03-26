package ottawa.ventilator.hardware;

/**
 * Interface to the hardware.
 */
public interface HardwareApi {

    // Messages pushed down to hardware

    public void requestPause();


    // Messages pushed up from hardware

    public void pauseConfirmed();

    // ...etc.
}
