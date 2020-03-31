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
import java.util.List;

import ottawa.ventilator.application.Application;

/**
 * https://github.com/mik3y/usb-serial-for-android
 *
 * Usb is created and owned by Hardware.
 */
public class Usb {

    final private AppCompatActivity activity;
    final private Application application;
    final private static String ACTION_USB_PERMISSION = "permission";
    private SerialInputOutputManager usbIoManager;

    // Making the assumption here that both port and connection can be held open for a long time
    // and that they don't need to be opened and closed on a per-message basis
    private UsbDeviceConnection connection;
    private UsbSerialPort port;

    public Usb(final Application application, final AppCompatActivity activity) {
        this.activity = activity;
        this.application = application;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Write a command string and get an immediate response.
     * Called from Hardware.
     */
    String write(String message) {
        if (message == null) {
            application.displayFatalErrorMessage("Fatal error: Usb.write() - message is null");
        }

        byte[] request = message.getBytes();
        byte[] response = new byte[0];
        int WRITE_WAIT_MILLIS = 30;
        int READ_WAIT_MILLIS = 30;
        int len;
        String responseStr;

        try {
            port.write(request, WRITE_WAIT_MILLIS);
            len = port.read(response, READ_WAIT_MILLIS);
            // I doubt the Arduino will be sending UTF-8
            //responseStr = new String(response, StandardCharsets.UTF_8);
            responseStr = response.toString();
            Log.i("Serial read", responseStr);
        } catch (IOException e) {
            e.printStackTrace();
            application.displayFatalErrorMessage("Fatal error: " + e.getMessage());
            return null;
        }

        return responseStr;
    }

    void initialize() {
        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);

        if (availableDrivers.isEmpty()) {
            application.displayFatalErrorMessage("Fatal error: No USB serial drivers found");
            return;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        connection = manager.openDevice(driver.getDevice());

        if (connection == null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(), pendingIntent);
            Log.i("serial", "connection successful");
        }

        port = driver.getPorts().get(0); // Most devices have just one port (port 0)

        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
            application.displayFatalErrorMessage("Fatal error: " + e.getMessage());
            return;
        }
    }

    void stop() {
        try {
            port.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
