package ottawa.ventilator;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
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

public class Usb {

    final private AppCompatActivity activity;
    final private static String ACTION_USB_PERMISSION = "permission";
    private SerialInputOutputManager usbIoManager;

    // Making the assumption here that both port and connection can be held open for a long time
    // and that they don't need to be open and closed on a per-message basis
    private UsbDeviceConnection connection;
    private UsbSerialPort port;

    Usb(final AppCompatActivity activity) {
        this.activity = activity;
    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    // Called by MainActivity
    void onCreate(Bundle savedInstanceState) {
        initialize();
    }

    void onPause() {

    }

    void onResume() {

    }

    // Called by MainActivity
    void onStop() {
        try {
            port.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    /**
     * Write a command string and get an immediate response.
     * Called from Hardware.
     */
    String write(String message) {
        // TODO check for null message
        byte[] request = new byte[0];
        try {
            request = message.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
            // TODO Write error message to UI alarm message area
            return null;
        }

        return responseStr;
    }

    void initialize() {
        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);

        if (availableDrivers.isEmpty()) {
            // TODO Write error message to UI alarm message area
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
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Write error message to UI alarm message area
            return;
        }
    }

}
