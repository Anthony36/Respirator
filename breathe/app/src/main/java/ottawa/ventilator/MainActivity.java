package ottawa.ventilator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final private Ui ui;
    final private Hardware hardware;

    public MainActivity() {
        super();
        hardware = new Hardware();
        ui = new Ui(this, hardware);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ui.initialize();
    }

    public void onDecrementTarget(View view) {
        ui.decrementTargetValue((TextView) view);
    }

    public void onIncrementTarget(View view) {
        ui.incrementTargetValue((TextView) view);
    }

    public void onRunPauseButtonClick(View view) {
        ui.onRunPauseButtonClick((TextView) view);
    }

    public void onSilenceAlarmButtonClick(View view) {
        ui.onSilenceAlarmButtonClick();
    }

}
