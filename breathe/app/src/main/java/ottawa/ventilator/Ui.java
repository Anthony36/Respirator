package ottawa.ventilator;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class for interfacing directly with the UI controls.
 */
public class Ui {

    final private AppCompatActivity appCompatActivity;

    public Ui(AppCompatActivity appCompatActivity) {
        this.appCompatActivity =appCompatActivity;
    }

    public void setToDefaults() {
        setBreathingRateTarget(Setting.BREATHING_RATE.defalt);
        setFiO2Target(Setting.FIO2.defalt);
        setInspirationPressureTarget(Setting.INSPIRATION_PRESSURE.defalt);
//        setFiO2Target(Setting.FIO2.defalt);
//        setFiO2Target(Setting.FIO2.defalt);
//        setFiO2Target(Setting.FIO2.defalt);
    }

    public void pauseConfirmed() {

    }

    public void setMinuteVentilationActual(float value) {

    }

    public void setTidalVolumeActual(int value) {

    }

    public void setFiO2Actual(int value) {

    }

    public void fireDisconnectionAlarm(boolean enable) {

    }

    public void fireMinuteVentilationHighAlarm(boolean enable) {

    }

    public void fireMinuteVentilationLowAlarm(boolean enable) {

    }

    public void clearAlarms() {

    }

    public void setBreathingRateTarget(int value) {
        ((TextView) appCompatActivity.findViewById(R.id.breathingRateTarget)).setText(value);
    }

    public void setInspirationPressureTarget(int value) {

    }

    public void setFiO2Target(int value) {

    }

    public void setPeepTarget(int value) {

    }

    public void setTidalVolumeTarget(int value) {

    }

    public void setIeRatioTarget(int value) {

    }

    public void enableRunPauseButton(boolean enable) {

    }

    public void setRunPauseButtonToRun() {

    }

    public void setRunPauseButtonToPause() {

    }

    public void enableSilenceAlarmButton(boolean enable) {

    }

}
