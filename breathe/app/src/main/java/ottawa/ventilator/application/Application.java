package ottawa.ventilator.application;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ottawa.ventilator.hardware.IHardware;
import ottawa.ventilator.hardware.MockHardware;

/**
 * Application owns and controls the UI, scheduler, and hardware.
 */
public class Application implements ILifecycle {

    private Ui ui;
    private IHardware hardware;
    private Scheduler scheduler;

    final private AppCompatActivity activity;

    public Application(final AppCompatActivity activity) {
        this.activity = activity;
        // HardwareAPI hardware = new Hardware(this, activity);
        hardware = new MockHardware(this);
        ui = new Ui(activity, hardware);
        scheduler = new Scheduler(activity, hardware, ui);
    }

    // ---------------------------------------------------------------------------------------------
    // Application Lifecycle
    // ---------------------------------------------------------------------------------------------

    public void onCreate() {
        ui.initialize();
    }

    public void onStart() {
        hardware.onStart();
    }

    public void onPause() {

    }

    public void onResume() {

    }

    public void onStop() {
        hardware.onStop();
    }

    public void onRestart() {

    }

    public void onDestroy() {

    }

    // ---------------------------------------------------------------------------------------------
    // UI Events
    // ---------------------------------------------------------------------------------------------

    public void onDecrementTargetValue(View view) {
        ui.decrementTargetValue((TextView) view);
    }

    public void onIncrementTargetValue(View view) {
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

    // ---------------------------------------------------------------------------------------------
    // Other
    // ---------------------------------------------------------------------------------------------

    public void displayFatalErrorMessage(String message) {
        ui.displayFatalErrorMessage(message);
    }

}
