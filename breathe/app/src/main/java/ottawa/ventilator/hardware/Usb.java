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
    final private Context context;

    private UsbSerialPort port;
    final String ACTION_USB_PERMISSION = "permission";
    private String theUsbMessage = "";

    private Object writeLock = new Object();

    public Usb(final Application application, final AppCompatActivity activity, Context context) {
        this.activity = activity;
        this.application = application;
        this.context = context;
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
            //This code has to run, otherwise the app gets permission denied on the USB device and never gets permission from the user to continue:
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(), pendingIntent);
            Log.i("serial", "connection successful");
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

    void write(String message) {
        synchronized (writeLock) {
            try {
                port.write((message + "\n").getBytes(), 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void stop() {
        // Close port?
    }

    // Data call back
    @Override
    public void onNewData(final byte[] data) {
        theUsbMessage = theUsbMessage + data.toString();

        if (theUsbMessage.contains("\n")) {
            String message = theUsbMessage;
            theUsbMessage = "";
            dispatchMessage(message);
        }
    }

    private void dispatchMessage(String message) {
        // ##_#######\n --> Response for get query, eg. "23 600\n"
        // ##\n         --> Response for command, eg "30\n"
        int commandCode = Integer.parseInt(message.substring(0, 1));

        // Call proper methods on the UI thread... TODO

        switch (commandCode) {
            case 02:
                break;
            default:
                // Unexpected command
        }
    }

    @Override
    public void onRunError(Exception e) {

    }

}
