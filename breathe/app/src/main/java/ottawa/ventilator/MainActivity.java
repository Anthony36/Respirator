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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

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
