package ottawa.ventilator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hoho.android.usbserial.util.SerialInputOutputManager;

public class MainActivity extends AppCompatActivity {

    final private Ui ui;
    final private Usb usb;
    final private Scheduler scheduler;
    final String ACTION_USB_PERMISSION = "permission";
    private SerialInputOutputManager usbIoManager;

    public MainActivity() {
        super();
        usb = new Usb(this);
        Hardware hardware = new Hardware(usb);
        ui = new Ui(this, hardware);
        scheduler = new Scheduler(this, hardware, ui);
    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usb.initialize();
        ui.initialize();
        scheduler.start();
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
        usb.onStop();
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
