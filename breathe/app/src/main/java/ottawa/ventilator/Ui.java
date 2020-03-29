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
    final private Scheduler scheduler;

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

    public Ui(final AppCompatActivity activity, final Hardware hardware, final Scheduler scheduler) {
        this.activity = activity;
        this.hardware = hardware;
        this.scheduler = scheduler;
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

    // Called from Scheduler
    void pollMinuteVentilationActual() {
        setMinuteVentilationActual(hardware.getMinuteVentilationActual());
    }

    // Called from Scheduler
    void pollTidalVolumeActual() {
        setTidalVolumeActual(hardware.getTidalVolumeActual());
    }

    // ---------------------------------------------------------------------------------------------
    // Alarms
    // ---------------------------------------------------------------------------------------------

    void clearAlarms() {
        alarmLbl.setText("");
        enableSilenceAlarmButton(false);
    }

    void onSilenceAlarmButton() {
        hardware.requestSilenceAlarm();
    }

    void enableSilenceAlarmButton(boolean enable) {
        silenceAlarmBtn.setEnabled(enable);
        silenceAlarmBtn.setAlpha(enable ? 1 : 0.1f);
    }

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

    // ---------------------------------------------------------------------------------------------
    // Target Settings
    // ---------------------------------------------------------------------------------------------

    // Called on start
    void setTargetDefaults() {
        setBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        hardware.requestNewBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        setFio2Target(Setting.FIO2.defalt);
        hardware.requestNewFio2Target(Setting.FIO2.defalt);
        setPeepTarget(Setting.PEEP.defalt);
        hardware.requestNewPeepTarget(Setting.PEEP.defalt);
        setPipTarget(Setting.PIP.defalt);
        hardware.requestNewPipTarget(Setting.PIP.defalt);
        setIeRatioTarget(Setting.IE_RATIO.defalt);
        hardware.requestNewIeRatioTarget(Setting.IE_RATIO.defalt);
        setTitalVolumeTarget(Setting.TIDAL_VOLUME.defalt);
        hardware.requestNewTidalVolumeTarget(Setting.TIDAL_VOLUME.defalt);
    }

    int getTargetValue(TextView control) {
        String str = control.getText().toString();

        if (control == ieRatioTarget) {
            str = str.replace("1:", "");
        }

        return Integer.parseInt(str);
    }

    // Called from Activity
    void incrementTargetValue(TextView incControl) {
        TextView control = incToTarget.get(incControl);
        Setting setting = targetToSettings.get(control);
        int value;

        if (control == ieRatioTarget) {
            String str = control.getText().toString();
            str = str.replace("1:", "");
            value = Integer.valueOf(str) + setting.increment;
            if (value > setting.max) value = setting.max;
            control.setText("1:" + value);
        } else {
            value = Integer.valueOf(control.getText().toString()) + setting.increment;
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

        notifyHardwareOnTargetChange(control, value);
        waitForTargetChangeResponse(control);
    }

    // Called from Activity
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
        waitForTargetChangeResponse(control);
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

    private void waitForTargetChangeResponse(final View control) {
        TimerTask task = new TimerTask() {
            public void run() {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (control == breathingRateTarget) {
                        setBreathingRateTarget(hardware.getBreathingRateTarget());
                    } else if (control == fio2Target) {
                        setFio2Target(hardware.getFio2Target());
                    } else if (control == peepTarget) {
                        setPeepTarget(hardware.getPeepTarget());
                    } else if (control == pipTarget) {
                        setPipTarget(hardware.getPipTarget());
                    } else if (control == ieRatioTarget) {
                        setIeRatioTarget(hardware.getIeRatioTarget());
                    } else if (control == tidalVolTarget) {
                        setTitalVolumeTarget(hardware.getTidalVolumeTarget());
                    }
                }
            });
            }
        };

        Timer timer = new Timer("GetTargetValue");
        timer.schedule(task, 50L);
    }

    void setBreathingRateTarget(int value) {
        breathingRateTarget.setText("" + value);
    }

    void setFio2Target(int value) {
        fio2Target.setText("" + value);
    }

    void setPeepTarget(int value) {
        peepTarget.setText("" + value);
    }

    void setPipTarget(int value) {
        pipTarget.setText("" + value);
    }

    void setIeRatioTarget(int value) {
        ieRatioTarget.setText("1:" + value);
    }

    void setTitalVolumeTarget(int value) {
        tidalVolTarget.setText("" + value);
    }

    // ---------------------------------------------------------------------------------------------
    // Patient Triggering
    // ---------------------------------------------------------------------------------------------

    // Called from Activity
    void onPatientTriggeringSwitch() {
        // Get new switch value and make the request to hardware
        // We don't yet keep track of the old setting
        int selectedId = patientTriggerSwitch.getCheckedRadioButtonId();
        boolean isAllowedDisplayed = (selectedId == R.id.patientTriggerSwitchOn);

        // Make the request
        hardware.requestPatientTriggering(isAllowedDisplayed);

        // TODO set timer to obtain response
        // hardware.isPatientTriggeringAllowed();

        boolean isAllowed = true; // XXX TEMP

        scheduler.includePatientTriggeringCheck(isAllowed);
        allowPatientTriggering(isAllowed);
    }

    // Called on start
    void allowPatientTriggering(boolean allow) {
        patientTriggerSwitchOn.setChecked(allow);
        patientTriggerSwitchOff.setChecked(!allow);
        if (!allow) setPatientTriggeredLight(false);
    }

    // Called by Scheduler
    void setPatientTriggeredLight(boolean on) {
        patientTriggeredLight.setAlpha(on ? 1 : 0.1f);
    }

    // ---------------------------------------------------------------------------------------------
    // Run/Pause Button
    // ---------------------------------------------------------------------------------------------

    // Called from Activity
    void onRunPauseButton() {
        boolean isRunDisplayed = runPauseBtn.getText().toString().trim().toLowerCase().startsWith("run");
        boolean isRunAllowed = true; // XXX TEMP

        if (isRunDisplayed) {
            hardware.requestRun();
            // TODO set timer to obtain response
            // hardware.isRunning();
            isRunAllowed = true; // XXX TEMP
        } else {
            hardware.requestPause();
            // TODO set timer to obtain response
            // hardware.isPaused();
            isRunAllowed = false; // XXX TEMP
        }

        setToRunning(isRunAllowed);
    }

    // Called from Scheduler
    void setToRunning(boolean isRunning) {
        enableRunPauseButton(isRunning);

        if (isRunning) {
            setRunPauseButtonToRun();
            showActuals();
        } else {
            setRunPauseButtonToPause();
        }
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

        Timer timer = new Timer("FlashText");
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

        // TODO Disable Run button until isRunAllowed() returns true
    }

    private void setToDefaults() {
        setMinuteVentilationActual(0f);
        setTidalVolumeActual(0);

        clearAlarms();

        setTargetDefaults();

        allowPatientTriggering(false);
        setPatientTriggeredLight(false);

        enableRunPauseButton(true);
        setRunPauseButtonToRun();
        enableSilenceAlarmButton(false);
    }

}
