package ottawa.ventilator.hardware;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

import ottawa.ventilator.application.Application;

/**
 * https://github.com/mik3y/usb-serial-for-android
 *
 * Usb is created and owned by Hardware.
 */
public class Usb implements SerialInputOutputManager.Listener {

    final private AppCompatActivity activity;
    final private Application application;
    private UsbSerialPort port;
    final String ACTION_USB_PERMISSION = "permission";
    private String theUsbMessage = "";

    public Usb(final Application application, final AppCompatActivity activity) {
        this.activity = activity;
        this.application = application;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Write a command string and get an immediate response.
     * Called from Hardware.
     */
    void write(String message) {
        try {
            port.write(message.getBytes(), 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initialize() {
        SerialInputOutputManager serialInputOutputManager;
        UsbManager manager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);

        if (availableDrivers.isEmpty()) {
            return;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());

        if (connection == null) {
            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            return;
        }

        port = driver.getPorts().get(0); // Most devices have just one port (port 0)

        try {
            port.open(connection);
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serialInputOutputManager = new SerialInputOutputManager(port, this);
        Executors.newSingleThreadExecutor().submit(serialInputOutputManager);
    }

    void stop() {

    }

    @Override
    public void onNewData(final byte[] data) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                theUsbMessage = theUsbMessage + (new String(data));
            }
        });
    }

    @Override
    public void onRunError(Exception e) {

    }
}
