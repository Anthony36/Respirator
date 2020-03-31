package ottawa.ventilator;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ottawa.ventilator.application.Application;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SerialInputOutputManager.Listener {

    final private Application application;
    final String ACTION_USB_PERMISSION = "permission";
    private String theUsbMessage;

    public MainActivity() {
        super();
        application = new Application(this);
        theUsbMessage = "";
    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        application.onStart();

        //String sMessage;
        //sMessage = usb.write("hello");
        //sMessage = usb.write("AB");
        SerialInputOutputManager serialInputOutputManager;
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
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

        UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            port.open(connection);
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serialInputOutputManager = new SerialInputOutputManager(port, this);
        Executors.newSingleThreadExecutor().submit(serialInputOutputManager);

        try {
            port.write("hello".getBytes(), 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        I can't make this bit work reliably. The event based code works reliably
                byte[] request;
                byte[] response;
                request = "hello".getBytes();
                //response = "".getBytes();
                StringBuilder sb = new StringBuilder();

                try {
                    port.write(request, 500);
                    while(true) {
                        response = new byte[4];
                        int len = port.read(response, 500);
                        sb.append(new String(response));
                        if (response.toString().contains("\n")){
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        */
    }

    @Override
    public void onPause() {
        super.onPause();
        application.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        application.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        application.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        application.onRestart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        application.onDestroy();
    }

    @Override
    public void onNewData(final byte[] data) {
        runOnUiThread(new Runnable() {
            public void run() {
                theUsbMessage = theUsbMessage + (new String(data));
            }
        });
    }

    @Override
    public void onRunError(Exception e) {

    }

    // ---------------------------------------------------------------------------------------------
    // UI Events
    // ---------------------------------------------------------------------------------------------

    public void onDecrementTarget(View view) {
        application.onDecrementTargetValue(view);
    }

    public void onIncrementTarget(View view) {
        application.onIncrementTargetValue(view);
    }

    public void onRunPauseButton(View view) {
        application.onRunPauseButton(view);
    }

    public void onSilenceAlarmButton(View view) {
        application.onSilenceAlarmButton(view);
    }

    public void onPatientTriggeringSwitch(View view) {
        application.onPatientTriggeringSwitch(view);
    }

}
