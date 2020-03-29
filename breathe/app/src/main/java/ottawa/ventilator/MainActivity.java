package ottawa.ventilator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    final private Ui ui;
    final private Hardware hardware;
    final private Scheduler scheduler;
    final String ACTION_USB_PERMISSION = "permission";
    private SerialInputOutputManager usbIoManager;

    public MainActivity() {
        super();
        hardware = new Hardware();
        scheduler = new Scheduler(this, hardware);
        ui = new Ui(this, hardware, scheduler);
        scheduler.setUi(ui);

    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ui.initialize();
        scheduler.start();

        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            //UsbManager.requestPermission(driver.getDevice(),Intent.) handling here
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(), pendingIntent);
            Log.i("serial", "connection successful");
        }

        UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            port.open(connection);
        } catch (IOException e) {
             e.printStackTrace();
             return;
        }
        try {
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        byte[] request="o".getBytes();
        byte[] response="".getBytes();
        int WRITE_WAIT_MILLIS = 30;
        int READ_WAIT_MILLIS = 30;
        int len;

        try {
            port.write(request, WRITE_WAIT_MILLIS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            len = port.read(response, READ_WAIT_MILLIS);

            String m = new String(response,"UTF-8");
            Log.i("Serial read", m);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            port.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        scheduler.stop();
    }

    // ---------------------------------------------------------------------------------------------
    // UI
    // ---------------------------------------------------------------------------------------------

    public void onDecrementTarget(View view) {
        ui.decrementTargetValue((TextView) view);
    }

    public void onIncrementTarget(View view) {
        ui.incrementTargetValue((TextView) view);
    }

    public void onRunPauseButton(View view) {
        ui.onRunPauseButton();
    }

    public void onSilenceAlarmButton(View view) {
        ui.onSilenceAlarmButton();
    }

    public void onPatientTriggeringSwitch(View view) {
        ui.onPatientTriggeringSwitch();
    }


}
