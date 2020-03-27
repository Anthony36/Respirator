package ottawa.ventilator;

import android.graphics.Color;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class for interfacing directly with the UI controls.
 */
class Ui {

    final private AppCompatActivity appCompatActivity;

    public Ui(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    void setToDefaults() {
        setMinuteVentilationActual(0f);
        setFiO2Actual(0);
        setTidalVolumeActual(0);

        clearAlarms();

        setBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        setFiO2Target(Setting.FIO2.defalt);
        setInspirationPressureTarget(Setting.INSPIRATION_PRESSURE.defalt);
        setTidalVolumeTarget(Setting.TIDAL_VOLUME.defalt);
        setPeepTarget(Setting.PEEP.defalt);
        setIeRatioTarget(Setting.IE_RATIO.defalt);

        enableRunPauseButton(true);
        setRunPauseButtonToRun();
        enableSilenceAlarmButton(false);
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
        ((TextView) appCompatActivity.findViewById(R.id.alarmLbl)).setText("\u26a0 Disconnection \u26a0");
    }

    void fireMinuteVentilationHighAlarm(boolean enable) {
        ((TextView) appCompatActivity.findViewById(R.id.alarmLbl)).setText("\u26a0 Minute Ventilation High \u26a0");
    }

    void fireMinuteVentilationLowAlarm(boolean enable) {
        ((TextView) appCompatActivity.findViewById(R.id.alarmLbl)).setText("\u26a0 Minute Ventilation Low \u26a0");
    }

    void clearAlarms() {
        ((TextView) appCompatActivity.findViewById(R.id.alarmLbl)).setText("");
    }

    // Target Settings

    void setBreathingRateTarget(int value) {
        ((TextView) appCompatActivity.findViewById(R.id.breathingRateTarget)).setText("" + value);
    }

    int getBreathingRateTarget() {
        String targetStr =  ((TextView) appCompatActivity.findViewById(R.id.breathingRateTarget)).getText().toString();
        return Integer.parseInt(targetStr);
    }

    void incBreathingRateTarget() {
        TextView control = (TextView) appCompatActivity.findViewById(R.id.breathingRateTarget);
        incrementSetting(control, Setting.BREATHING_RATE);
    }

    void decBreathingRateTarget() {
        TextView control = (TextView) appCompatActivity.findViewById(R.id.breathingRateTarget);
        decrementSetting(control, Setting.BREATHING_RATE);
    }

    void setInspirationPressureTarget(int value) {
        ((TextView) appCompatActivity.findViewById(R.id.inspPresTarget)).setText("" + value);
    }

    void setFiO2Target(int value) {
        ((TextView) appCompatActivity.findViewById(R.id.fio2Target)).setText("" + value);
    }

    void setPeepTarget(int value) {
        ((TextView) appCompatActivity.findViewById(R.id.peepTarget)).setText("" + value);
    }

    void setTidalVolumeTarget(int value) {
        ((TextView) appCompatActivity.findViewById(R.id.tidalVolTarget)).setText("" + value);
    }

    void setIeRatioTarget(int value) {
        ((TextView) appCompatActivity.findViewById(R.id.ieRatioTarget)).setText("" + value);
    }

    // Run/Pause and Silence Buttons

    void enableRunPauseButton(boolean enable) {
        ((TextView) appCompatActivity.findViewById(R.id.runPauseBtn)).setEnabled(enable);
    }

    void setRunPauseButtonToRun() {
        TextView btn = (TextView) appCompatActivity.findViewById(R.id.runPauseBtn);
        btn.setText("Run \u25b6");
        btn.setBackgroundColor(Color.parseColor("#4CAF50"));
    }

    void setRunPauseButtonToPause() {
        TextView btn = (TextView) appCompatActivity.findViewById(R.id.runPauseBtn);
        btn.setText("Pause \u2759 \u2759");
        btn.setBackgroundColor(Color.parseColor("#808080"));
    }

    void enableSilenceAlarmButton(boolean enable) {
        ((TextView) appCompatActivity.findViewById(R.id.silenceAlarmBtn)).setEnabled(enable);
    }
    // Support Methods

    private void incrementSetting(TextView control, Setting setting) {
        int value = Integer.valueOf(control.getText().toString()) + setting.increment;
        if (value > setting.max) value = setting.max;
        control.setText("" + value);
    }

    private void decrementSetting(TextView control, Setting setting) {
        int value = Integer.valueOf(control.getText().toString()) - setting.increment;
        if (value < setting.min) value = setting.min;
        control.setText("" + value);
    }

}
