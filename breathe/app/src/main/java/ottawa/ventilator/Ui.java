package ottawa.ventilator;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for interfacing directly with the UI controls.
 */
class Ui {

    final private AppCompatActivity activity;

    private TextView minVentVal, fio2Val, tidalVolVal;

    private TextView alarmLbl;

    private TextView breathingRateTargetDec, breathingRateTarget, breathingRateTargetInc;
    private TextView fio2TargetDec, fio2Target, fio2TargetInc;
    private TextView peepTargetDec, peepTarget, peepTargetInc;
    private TextView inspPresTargetDec, inspPresTarget, inspPresTargetInc;
    private TextView ieRatioTargetDec, ieRatioTarget, ieRatioTargetInc;
    private TextView tidalVolTargetDec, tidalVolTarget, tidalVolTargetInc;

    private RadioGroup patientTriggerSwitch;
    private RadioButton patientTriggerSwitchOn, patientTriggerSwitchOff;

    private TextView patientTriggeredLight;

    private Button runPauseBtn, silenceAlarmBtn;

    // Map increment and decrement views to the target controls
    private Map<TextView, TextView> decToTarget = new HashMap<>();
    private Map<TextView, Setting> targetToSettings = new HashMap<>();
    private Map<TextView, TextView> incToTarget = new HashMap<>();

    public Ui(AppCompatActivity appCompatActivity) {
        this.activity = appCompatActivity;
    }

    void pauseConfirmed() {
        setRunPauseButtonToRun();
    }

    void runConfirmed() {
        setRunPauseButtonToPause();
    }

    // Actuals

    void setMinuteVentilationActual(float value) {

    }

    void setTidalVolumeActual(int value) {

    }

    void setFiO2Actual(int value) {

    }

    // Alarms

    void fireDisconnectionAlarm(boolean enable) {
        alarmLbl.setText("\u26a0 Disconnection \u26a0");
    }

    void fireMinuteVentilationHighAlarm(boolean enable) {
        alarmLbl.setText("\u26a0 Minute Ventilation High \u26a0");
    }

    void fireMinuteVentilationLowAlarm(boolean enable) {
        alarmLbl.setText("\u26a0 Minute Ventilation Low \u26a0");
    }

    void clearAlarms() {
        alarmLbl.setText("");
    }

    // Target Settings

    void setBreathingRateTarget(int value) {
        breathingRateTarget.setText("" + value);
    }

    void setInspirationPressureTarget(int value) {
        inspPresTarget.setText("" + value);
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

    void setIeRatioTarget(String value) {
        ieRatioTarget.setText("" + value);
    }

    // Run/Pause and Silence Buttons

    void enableRunPauseButton(boolean enable) {
        runPauseBtn.setEnabled(enable);
    }

    void setRunPauseButtonToRun() {
        runPauseBtn.setText("Run \u25b6");
        runPauseBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
    }

    void setRunPauseButtonToPause() {
        runPauseBtn.setText("Pause \u2759 \u2759");
        runPauseBtn.setBackgroundColor(Color.parseColor("#808080"));
    }

    void enableSilenceAlarmButton(boolean enable) {
        silenceAlarmBtn.setEnabled(enable);
    }

    // Target Support Methods

    int getTargetValue(TextView control) {
        return Integer.parseInt(control.getText().toString());
    }

    void setTargetValue(TextView control, int value) {
        control.setText("" + value);
    }

    void incrementTargetValue(TextView incControl) {
        TextView control = incToTarget.get(incControl);
        Setting setting = targetToSettings.get(control);

        if (control.equals(ieRatioTarget)) {
            String str = control.getText().toString();
            str = str.replace("1:", "");
            int value = Integer.valueOf(str) + setting.increment;
            if (value > setting.max) value = setting.max;
            control.setText("1:" + value);
        } else {
            int value = Integer.valueOf(control.getText().toString()) + setting.increment;
            if (value > setting.max) value = setting.max;
            control.setText("" + value);
        }
    }

    void decrementTargetValue(TextView decControl) {
        TextView control = decToTarget.get(decControl);
        Setting setting = targetToSettings.get(control);

        if (control.equals(ieRatioTarget)) {
            String str = control.getText().toString();
            str = str.replace("1:", "");
            int value = Integer.valueOf(str) - setting.increment;
            if (value < setting.min) value = setting.min;
            control.setText("1:" + value);
        } else {
            int value = Integer.valueOf(control.getText().toString()) - setting.increment;
            if (value < setting.min) value = setting.min;
            control.setText("" + value);
        }
    }

    // Set up

    void initialize() {
        minVentVal = ((TextView) activity.findViewById(R.id.minVentVal));
        fio2Val = ((TextView) activity.findViewById(R.id.fio2Val));
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

        inspPresTargetDec = ((TextView) activity.findViewById(R.id.inspPresTargetDec));
        inspPresTarget = ((TextView) activity.findViewById(R.id.inspPresTarget));
        inspPresTargetInc = ((TextView) activity.findViewById(R.id.inspPresTargetInc));

            decToTarget.put(inspPresTargetDec, inspPresTarget);
            targetToSettings.put(inspPresTarget, Setting.INSPIRATION_PRESSURE);
            incToTarget.put(inspPresTargetInc, inspPresTarget);

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





        patientTriggerSwitch = ((RadioGroup) activity.findViewById(R.id.patientTriggerSwitch));
        patientTriggerSwitchOn = ((RadioButton) activity.findViewById(R.id.patientTriggerSwitchOn));
        patientTriggerSwitchOff = ((RadioButton) activity.findViewById(R.id.patientTriggerSwitchOff));

        patientTriggeredLight = ((TextView) activity.findViewById(R.id.patientTriggeredLight));

        runPauseBtn = ((Button) activity.findViewById(R.id.runPauseBtn));
        silenceAlarmBtn = ((Button) activity.findViewById(R.id.silenceAlarmBtn));

        setToDefaults();
        setListeners();
    }

    private void setToDefaults() {
        setMinuteVentilationActual(0f);
        setFiO2Actual(0);
        setTidalVolumeActual(0);

        clearAlarms();

        setBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        setFiO2Target(Setting.FIO2.defalt);
        setInspirationPressureTarget(Setting.INSPIRATION_PRESSURE.defalt);
        setTidalVolumeTarget(Setting.TIDAL_VOLUME.defalt);
        setPeepTarget(Setting.PEEP.defalt);
        setIeRatioTarget("1:" + Setting.IE_RATIO.defalt);

        enableRunPauseButton(true);
        setRunPauseButtonToRun();
        enableSilenceAlarmButton(false);
    }

    private void setListeners() {

    }

}
