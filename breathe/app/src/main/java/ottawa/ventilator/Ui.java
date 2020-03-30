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
 * Class for interfacing with the UI controls and some logic.
 */
class Ui {

    final private AppCompatActivity activity;
    final private Hardware hardware;

    // Controls
    private TextView minVentVal, tidalVolVal;

    private TextView alarmLbl;

    private TextView breathingRateTarget, fio2Target, peepTarget;
    private TextView pipTarget, ieRatioTarget, tidalVolTarget;

    private RadioGroup patientTriggerSwitch;
    private RadioButton patientTriggerSwitchOn, patientTriggerSwitchOff;

    private TextView patientTriggeredLight;

    private Button runPauseBtn, silenceAlarmBtn;

    // Map '-' and '+' views to the target controls
    private Map<TextView, TextView> decToTarget = new HashMap<>();
    private Map<TextView, Setting> targetToSettings = new HashMap<>();
    private Map<TextView, TextView> incToTarget = new HashMap<>();

    Ui(final AppCompatActivity activity, final Hardware hardware) {
        this.activity = activity;
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

    // Not used
    private void hideActuals() {
        minVentVal.setAlpha(.1f);
        tidalVolVal.setAlpha(.1f);
    }

    // Used on start
    private void showActuals() {
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

    private void clearAlarms() {
        alarmLbl.setText("");
        enableSilenceAlarmButton(false);
    }

    void onSilenceAlarmButton() {
        hardware.requestSilenceAlarm();
    }

    private void enableSilenceAlarmButton(boolean enable) {
        silenceAlarmBtn.setEnabled(enable);
        silenceAlarmBtn.setAlpha(enable ? 1 : 0.1f);
    }

    // Called from Scheduler
    void setAlarm(int alarm) {
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
    private void setTargetDefaults() {
        setBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        setFio2Target(Setting.FIO2.defalt);
        setPeepTarget(Setting.PEEP.defalt);
        setPipTarget(Setting.PIP.defalt);
        setIeRatioTarget(Setting.IE_RATIO.defalt);
        setTitalVolumeTarget(Setting.TIDAL_VOLUME.defalt);
    }

    // Called on can run
    private void requestTargetsToHardware() {
        hardware.requestNewBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        hardware.requestNewFio2Target(Setting.FIO2.defalt);
        hardware.requestNewPeepTarget(Setting.PEEP.defalt);
        hardware.requestNewPipTarget(Setting.PIP.defalt);
        hardware.requestNewIeRatioTarget(Setting.IE_RATIO.defalt);
        hardware.requestNewTidalVolumeTarget(Setting.TIDAL_VOLUME.defalt);
    }

    private int getTargetValue(TextView control) {
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
            value = Integer.parseInt(str) + setting.increment;
            if (value > setting.max) value = setting.max;
            control.setText("1:" + value);
        } else {
            value = Integer.parseInt(control.getText().toString()) + setting.increment;
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
            value = Integer.parseInt(str) - setting.increment;
            if (value < setting.min) value = setting.min;
            control.setText("1:" + value);
        } else {
            value = Integer.parseInt(control.getText().toString()) - setting.increment;
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

    // Called after '-' or '+'
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

    // Called after '-' or '+'
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

        new Timer("GetTargetValue").schedule(task, 50L);
    }

    // Called on Run button pressed
    private void waitForTargetChangeResponseAll() {
        TimerTask task = new TimerTask() {
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        setBreathingRateTarget(hardware.getBreathingRateTarget());
                        setFio2Target(hardware.getFio2Target());
                        setPeepTarget(hardware.getPeepTarget());
                        setPipTarget(hardware.getPipTarget());
                        setIeRatioTarget(hardware.getIeRatioTarget());
                        setTitalVolumeTarget(hardware.getTidalVolumeTarget());
                    }
                });
                }
        };

        new Timer("GetTargetValuesAll").schedule(task, 50L);
    }

    private void setBreathingRateTarget(int value) {
        breathingRateTarget.setText("" + value);
    }

    private void setFio2Target(int value) {
        fio2Target.setText("" + value);
    }

    private void setPeepTarget(int value) {
        peepTarget.setText("" + value);
    }

    private void setPipTarget(int value) {
        pipTarget.setText("" + value);
    }

    private void setIeRatioTarget(int value) {
        ieRatioTarget.setText("1:" + value);
    }

    private void setTitalVolumeTarget(int value) {
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

        // Fire the request down and forget it. The Scheduler will poll for the reponse in 200 ms or less.
        hardware.requestPatientTriggering(isAllowedDisplayed);
    }

    // Called on start
    private void allowPatientTriggering(boolean allow) {
        patientTriggerSwitchOn.setChecked(allow);
        patientTriggerSwitchOff.setChecked(!allow);
        if (!allow) patientTriggeredLight.setAlpha(0.1f);
    }

    // Called by Scheduler
    void setPatientTriggeredLight(boolean on) {
        patientTriggeredLight.setAlpha(on ? 1 : 0.1f);

        if (on) {
            allowPatientTriggering(true);
            // Keep the light on for 2 seconds, then turn off
            TimerTask task = new TimerTask() {
                public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        patientTriggeredLight.setAlpha(0.1f);
                    }
                });
                }
            };

            new Timer("PatientTriggeredLightOffTimer").schedule(task, 2000L);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Run/Pause Button
    // ---------------------------------------------------------------------------------------------

    // Called from Activity
    void onRunPauseButton() {
        // Not sure yet if the button text state is changed before or after this call from the system
        boolean isRunDisplayed = runPauseBtn.getText().toString().trim().toLowerCase().startsWith("run");

        if (isRunDisplayed) {
            hardware.requestRun();
        } else {
            hardware.requestPause();
        }

        // Obtain response in 200 ms
        TimerTask task = new TimerTask() {
            public void run() {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    setToRunning(hardware.isRunning());
                }
            });
            }
        };

        new Timer("WaitingIsRunningResponse").schedule(task, 200L);
    }

    // Called from Scheduler
    void setToRunning(boolean isRunning) {
        enableRunPauseButton(isRunning);

        if (isRunning) {
            setRunPauseButtonToRun();
            showActuals();
            requestTargetsToHardware();
            waitForTargetChangeResponseAll();
        } else {
            setRunPauseButtonToPause();
        }
    }

    private void enableRunPauseButton(boolean enable) {
        runPauseBtn.setEnabled(enable);
    }

    private void setRunPauseButtonToRun() {
        runPauseBtn.setText("Run \u25b6");
        runPauseBtn.setBackgroundColor(activity.getResources().getColor(R.color.clrGreen));
    }

    private void setRunPauseButtonToPause() {
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

        new Timer("FlashText").schedule(task, 500L);
    }

    // ---------------------------------------------------------------------------------------------
    // Set up
    // ---------------------------------------------------------------------------------------------

    void initialize() {
        minVentVal = activity.findViewById(R.id.minVentVal);
        tidalVolVal = activity.findViewById(R.id.tidalVolVal);

        alarmLbl = activity.findViewById(R.id.alarmLbl);

        TextView breathingRateTargetDec = activity.findViewById(R.id.breathingRateTargetDec);
        breathingRateTarget = activity.findViewById(R.id.breathingRateTarget);
        TextView breathingRateTargetInc = activity.findViewById(R.id.breathingRateTargetInc);

        decToTarget.put(breathingRateTargetDec, breathingRateTarget);
        targetToSettings.put(breathingRateTarget, Setting.BREATHING_RATE);
        incToTarget.put(breathingRateTargetInc, breathingRateTarget);

        TextView fio2TargetDec = activity.findViewById(R.id.fio2TargetDec);
        fio2Target = activity.findViewById(R.id.fio2Target);
        TextView fio2TargetInc = activity.findViewById(R.id.fio2TargetInc);

        decToTarget.put(fio2TargetDec, fio2Target);
        targetToSettings.put(fio2Target,Setting.FIO2);
        incToTarget.put(fio2TargetInc, fio2Target);

        TextView peepTargetDec = activity.findViewById(R.id.peepTargetDec);
        peepTarget = activity.findViewById(R.id.peepTarget);
        TextView peepTargetInc = activity.findViewById(R.id.peepTargetInc);

        decToTarget.put(peepTargetDec, peepTarget);
        targetToSettings.put(peepTarget, Setting.PEEP);
        incToTarget.put(peepTargetInc, peepTarget);

        TextView pipTargetDec = activity.findViewById(R.id.pipTargetDec);
        pipTarget = activity.findViewById(R.id.pipTarget);
        TextView pipTargetInc = activity.findViewById(R.id.pipTargetInc);

        decToTarget.put(pipTargetDec, pipTarget);
        targetToSettings.put(pipTarget, Setting.PIP);
        incToTarget.put(pipTargetInc, pipTarget);

        TextView ieRatioTargetDec = activity.findViewById(R.id.ieRatioTargetDec);
        ieRatioTarget = activity.findViewById(R.id.ieRatioTarget);
        TextView ieRatioTargetInc = activity.findViewById(R.id.ieRatioTargetInc);

        decToTarget.put(ieRatioTargetDec, ieRatioTarget);
        targetToSettings.put(ieRatioTarget, Setting.IE_RATIO);
        incToTarget.put(ieRatioTargetInc, ieRatioTarget);

        TextView tidalVolTargetDec = activity.findViewById(R.id.tidalVolTargetDec);
        tidalVolTarget = activity.findViewById(R.id.tidalVolTarget);
        TextView tidalVolTargetInc = activity.findViewById(R.id.tidalVolTargetInc);

        decToTarget.put(tidalVolTargetDec, tidalVolTarget);
        targetToSettings.put(tidalVolTarget, Setting.TIDAL_VOLUME);
        incToTarget.put(tidalVolTargetInc, tidalVolTarget);

        patientTriggerSwitch = activity.findViewById(R.id.patientTriggerSwitchGrp);
        patientTriggerSwitchOn = activity.findViewById(R.id.patientTriggerSwitchOn);
        patientTriggerSwitchOff = activity.findViewById(R.id.patientTriggerSwitchOff);

        patientTriggeredLight = activity.findViewById(R.id.patientTriggeredLight);

        runPauseBtn = activity.findViewById(R.id.runPauseBtn);
        silenceAlarmBtn = activity.findViewById(R.id.silenceAlarmBtn);

        setToDefaults();
        hideActuals();
        alarmLbl.setText("\u26a0 Waiting to Start \u26a0");
        waitForHardwareReady();
    }

    private void setToDefaults() {
        setMinuteVentilationActual(0f);
        setTidalVolumeActual(0);

        clearAlarms();

        setTargetDefaults();

        allowPatientTriggering(false);
        setPatientTriggeredLight(false);

        // Disable Run button until isRunAllowed() returns true
        enableRunPauseButton(false);
        setRunPauseButtonToRun();
        enableSilenceAlarmButton(false);
    }

    private void waitForHardwareReady() {
        // Poll every 200 ms until running is allowed
        final Timer timer = new Timer("WaitingOnRunAllowed");

        TimerTask task = new TimerTask() {
            public void run() {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    boolean canRun = hardware.isRunAllowed();
                    if (canRun) {
                        timer.cancel();
                        enableRunPauseButton(true);
                        requestTargetsToHardware();
                        waitForTargetChangeResponseAll();
                    }
                }
            });
            }
        };

        timer.scheduleAtFixedRate(task, 50L, 200L);
    }

}
