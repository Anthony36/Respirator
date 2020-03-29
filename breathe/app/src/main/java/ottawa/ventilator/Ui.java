package ottawa.ventilator;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for interfacing directly with the UI controls.
 */
class Ui {

    final private AppCompatActivity activity;
    final private Hardware hardware;

    // Controls
    private TextView minVentVal, tidalVolVal;

    private TextView alarmLbl;

    private TextView breathingRateTargetDec, breathingRateTarget, breathingRateTargetInc;
    private TextView fio2TargetDec, fio2Target, fio2TargetInc;
    private TextView peepTargetDec, peepTarget, peepTargetInc;
    private TextView pipTargetDec, pipTarget, pipTargetInc;
    private TextView ieRatioTargetDec, ieRatioTarget, ieRatioTargetInc;
    private TextView tidalVolTargetDec, tidalVolTarget, tidalVolTargetInc;

    private RadioGroup patientTriggerSwitch;
    private RadioButton patientTriggerSwitchOn, patientTriggerSwitchOff;

    private TextView patientTriggeredLight;

    private Button runPauseBtn, silenceAlarmBtn;

    // Map '-' and '+' views to the target controls
    private Map<TextView, TextView> decToTarget = new HashMap<>();
    private Map<TextView, Setting> targetToSettings = new HashMap<>();
    private Map<TextView, TextView> incToTarget = new HashMap<>();

    public Ui(AppCompatActivity appCompatActivity, Hardware hardware) {
        this.activity = appCompatActivity;
        this.hardware = hardware;
    }

    // ---------------------------------------------------------------------------------------------
    // Actuals
    // ---------------------------------------------------------------------------------------------

    void setMinuteVentilationActual(float value) {
        minVentVal.setText(String.format("%.1f", value));
    }

    void setTidalVolumeActual(int value) {
        tidalVolVal.setText("" + value);
    }

    // Called from Scheduler
    void pollMinuteVentilationActual() {
        setMinuteVentilationActual(hardware.getMinuteVentilationActual());
    }

    // Called from Scheduler
    void pollTidalVolumeActual() {
        setTidalVolumeActual(hardware.getTidalVolumeActual());
    }

    // Not used
    void hideActuals() {
        minVentVal.setAlpha(.1f);
        tidalVolVal.setAlpha(.1f);
    }

    // Used on start
    void showActuals() {
        minVentVal.setAlpha(1f);
        tidalVolVal.setAlpha(1f);
    }

    // ---------------------------------------------------------------------------------------------
    // Alarms
    // ---------------------------------------------------------------------------------------------

    // Called from Scheduler
    void pollAlarms() {
        int alarm = hardware.getAlarm();

        switch (alarm) {
            case 0: // No alarm
                clearAlarms();
                break;
            case 1: // Disconnect
                alarmLbl.setText("\u26a0 Disconnection \u26a0");
                enableSilenceAlarmButton(true);
                break;
            case 2: // Minute Vent. Low
                alarmLbl.setText("\u26a0 Minute Ventilation Low \u26a0");
                enableSilenceAlarmButton(true);
                break;
            case 3: // Minute vent. high
                alarmLbl.setText("\u26a0 Minute Ventilation High \u26a0");
                enableSilenceAlarmButton(true);
                break;
            default:
                System.out.println("Unexpected alarm value " + alarm);
        }
    }

    void clearAlarms() {
        alarmLbl.setText("");
        enableSilenceAlarmButton(false);
    }

    // ---------------------------------------------------------------------------------------------
    // Target Settings
    // ---------------------------------------------------------------------------------------------

    void setBreathingRateTarget(int value) {
        breathingRateTarget.setText("" + value);
    }

    void setPipTarget(int value) {
        pipTarget.setText("" + value);
    }

    void setFiO2Target(int value) {
        fio2Target.setText("" + value);
    }

    void setPeepTarget(int value) {
        peepTarget.setText("" + value);
    }

    void setTidalVolumeTarget(int value) {
        tidalVolTarget.setText("" + value);
    }

    void setIeRatioTarget(int value) {
        ieRatioTarget.setText("1:" + value);
    }

    int getTargetValue(TextView control) {
        String str = control.getText().toString();

        if (control == ieRatioTarget) {
            str = str.replace("1:", "");
        }

        return Integer.parseInt(str);
    }

    void setTargetValue(TextView control, int value) {
        if (control == ieRatioTarget) {
            control.setText("1:" + value);
        } else {
            control.setText("" + value);
        }
    }

    void incrementTargetValue(TextView incControl) {
        TextView control = incToTarget.get(incControl);
        Setting setting = targetToSettings.get(control);

        if (control == ieRatioTarget) {
            String str = control.getText().toString();
            str = str.replace("1:", "");
            int value = Integer.valueOf(str) + setting.increment;
            if (value > setting.max) value = setting.max;
            control.setText("1:" + value);
        } else {
            int value = Integer.valueOf(control.getText().toString()) + setting.increment;
            if (value > setting.max) value = setting.max;

            if (control == peepTarget) {
                int pipVal = getTargetValue(pipTarget);
                if (value >= pipVal) {
                    flashText(pipTarget);
                    return; // PEEP can't be greater or or equal to PIP
                }
            }

            control.setText("" + value);
        }
    }

    void decrementTargetValue(TextView decControl) {
        TextView control = decToTarget.get(decControl);
        Setting setting = targetToSettings.get(control);
        int value;

        if (control == ieRatioTarget) {
            String str = control.getText().toString();
            str = str.replace("1:", "");
            value = Integer.valueOf(str) - setting.increment;
            if (value < setting.min) value = setting.min;
            control.setText("1:" + value);
        } else {
            value = Integer.valueOf(control.getText().toString()) - setting.increment;
            if (value < setting.min) value = setting.min;

            if (control == pipTarget) {
                int peepVal = getTargetValue(peepTarget);
                if (value <= peepVal) {
                    flashText(peepTarget);
                    return; // PIP can't be less than or equal to PEEP
                }
            }

            control.setText("" + value);
        }

        notifyHardwareOnTargetChange(control, value);
    }

    private void notifyHardwareOnTargetChange(View control, int newValue) {
        if (control == breathingRateTarget) {
            hardware.requestNewBreathingRateTarget(newValue);
        } else if (control == fio2Target) {
            hardware.requestNewFio2Target(newValue);
        } else if (control == peepTarget) {
            hardware.requestNewPeepTarget(newValue);
        } else if (control == pipTarget) {
            hardware.requestNewPipTarget(newValue);
        } else if (control == ieRatioTarget) {
            hardware.requestNewIeRatioTarget(newValue);
        } else if (control == tidalVolTarget) {
            hardware.requestNewTidalVolumeTarget(newValue);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Patient Triggering
    // ---------------------------------------------------------------------------------------------

    // Called from UI
    void onPatientTriggeringSwitch(View view) {

    }

    void allowPatientTriggering(boolean allow) {
        patientTriggerSwitchOn.setChecked(allow);
        patientTriggerSwitchOff.setChecked(!allow);
        if (!allow) setPatientTriggeredLight(false);
    }

    void setPatientTriggeredLight(boolean on) {
        patientTriggeredLight.setAlpha(on ? 1 : 0.1f);
    }

    // ---------------------------------------------------------------------------------------------
    // Run/Pause and Silence Buttons
    // ---------------------------------------------------------------------------------------------

    void onRunPauseButton(TextView control) {
        if (control.getText().toString().trim().toLowerCase().startsWith("run")) {
            hardware.requestRun();
        } else {
            hardware.requestPause();
        }
    }

    // Callback from hardware
    void runConfirmed() {
        clearAlarms();
        setRunPauseButtonToPause();
        showActuals();
    }

    void onSilenceAlarmButton() {
        hardware.requestSilenceAlarm();
    }

    void enableRunPauseButton(boolean enable) {
        runPauseBtn.setEnabled(enable);
    }

    void setRunPauseButtonToRun() {
        runPauseBtn.setText("Run \u25b6");
        runPauseBtn.setBackgroundColor(activity.getResources().getColor(R.color.clrGreen));
    }

    void setRunPauseButtonToPause() {
        runPauseBtn.setText("Pause  \u2759\u2759");
        runPauseBtn.setBackgroundColor(activity.getResources().getColor(R.color.clrBlue2));
    }

    void enableSilenceAlarmButton(boolean enable) {
        silenceAlarmBtn.setEnabled(enable);
        silenceAlarmBtn.setAlpha(enable ? 1 : 0.1f);
    }

    // ---------------------------------------------------------------------------------------------
    // Other
    // ---------------------------------------------------------------------------------------------

    private void flashText(final TextView view) {
        view.setTextColor(activity.getResources().getColor(R.color.clrOrange));

        TimerTask task = new TimerTask() {
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        view.setTextColor(activity.getResources().getColor(R.color.clrWhite));
                    }
                });
            }
        };

        Timer timer = new Timer("Timer");
        timer.schedule(task, 500L);
    }

    // ---------------------------------------------------------------------------------------------
    // Set up
    // ---------------------------------------------------------------------------------------------

    void initialize() {
        minVentVal = ((TextView) activity.findViewById(R.id.minVentVal));
        tidalVolVal = ((TextView) activity.findViewById(R.id.tidalVolVal));

        alarmLbl = ((TextView) activity.findViewById(R.id.alarmLbl));

        breathingRateTargetDec = ((TextView) activity.findViewById(R.id.breathingRateTargetDec));
        breathingRateTarget = ((TextView) activity.findViewById(R.id.breathingRateTarget));
        breathingRateTargetInc = ((TextView) activity.findViewById(R.id.breathingRateTargetInc));

        decToTarget.put(breathingRateTargetDec, breathingRateTarget);
        targetToSettings.put(breathingRateTarget, Setting.BREATHING_RATE);
        incToTarget.put(breathingRateTargetInc, breathingRateTarget);

        fio2TargetDec = ((TextView) activity.findViewById(R.id.fio2TargetDec));
        fio2Target = ((TextView) activity.findViewById(R.id.fio2Target));
        fio2TargetInc = ((TextView) activity.findViewById(R.id.fio2TargetInc));

        decToTarget.put(fio2TargetDec, fio2Target);
        targetToSettings.put(fio2Target,Setting.FIO2);
        incToTarget.put(fio2TargetInc, fio2Target);

        peepTargetDec = ((TextView) activity.findViewById(R.id.peepTargetDec));
        peepTarget = ((TextView) activity.findViewById(R.id.peepTarget));
        peepTargetInc = ((TextView) activity.findViewById(R.id.peepTargetInc));

        decToTarget.put(peepTargetDec, peepTarget);
        targetToSettings.put(peepTarget, Setting.PEEP);
        incToTarget.put(peepTargetInc, peepTarget);

        pipTargetDec = ((TextView) activity.findViewById(R.id.pipTargetDec));
        pipTarget = ((TextView) activity.findViewById(R.id.pipTarget));
        pipTargetInc = ((TextView) activity.findViewById(R.id.pipTargetInc));

        decToTarget.put(pipTargetDec, pipTarget);
        targetToSettings.put(pipTarget, Setting.PIP);
        incToTarget.put(pipTargetInc, pipTarget);

        ieRatioTargetDec = ((TextView) activity.findViewById(R.id.ieRatioTargetDec));
        ieRatioTarget = ((TextView) activity.findViewById(R.id.ieRatioTarget));
        ieRatioTargetInc = ((TextView) activity.findViewById(R.id.ieRatioTargetInc));

        decToTarget.put(ieRatioTargetDec, ieRatioTarget);
        targetToSettings.put(ieRatioTarget, Setting.IE_RATIO);
        incToTarget.put(ieRatioTargetInc, ieRatioTarget);

        tidalVolTargetDec = ((TextView) activity.findViewById(R.id.tidalVolTargetDec));
        tidalVolTarget = ((TextView) activity.findViewById(R.id.tidalVolTarget));
        tidalVolTargetInc = ((TextView) activity.findViewById(R.id.tidalVolTargetInc));

        decToTarget.put(tidalVolTargetDec, tidalVolTarget);
        targetToSettings.put(tidalVolTarget, Setting.TIDAL_VOLUME);
        incToTarget.put(tidalVolTargetInc, tidalVolTarget);

        patientTriggerSwitch = ((RadioGroup) activity.findViewById(R.id.patientTriggerSwitchGrp));
        patientTriggerSwitchOn = ((RadioButton) activity.findViewById(R.id.patientTriggerSwitchOn));
        patientTriggerSwitchOff = ((RadioButton) activity.findViewById(R.id.patientTriggerSwitchOff));

        patientTriggeredLight = ((TextView) activity.findViewById(R.id.patientTriggeredLight));

        runPauseBtn = ((Button) activity.findViewById(R.id.runPauseBtn));
        silenceAlarmBtn = ((Button) activity.findViewById(R.id.silenceAlarmBtn));

        setToDefaults();
        hideActuals();
        alarmLbl.setText("\u26a0 Waiting to Start \u26a0");
    }

    private void setToDefaults() {
        setMinuteVentilationActual(0f);
        setTidalVolumeActual(0);

        clearAlarms();

        setBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        setFiO2Target(Setting.FIO2.defalt);
        setPipTarget(Setting.PIP.defalt);
        setTidalVolumeTarget(Setting.TIDAL_VOLUME.defalt);
        setPeepTarget(Setting.PEEP.defalt);
        setIeRatioTarget(Setting.IE_RATIO.defalt);

        allowPatientTriggering(false);
        setPatientTriggeredLight(false);

        enableRunPauseButton(true);
        setRunPauseButtonToRun();
        enableSilenceAlarmButton(false);
    }

}
