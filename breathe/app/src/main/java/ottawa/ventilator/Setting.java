package ottawa.ventilator;

import android.widget.TextView;

/**
 * Values for target settings.
 */
class Setting {

    final static Setting BREATHING_RATE = new Setting(6, 40, 12, 1);
    final static Setting FIO2 = new Setting(20, 100, 20, 10);
    final static Setting PIP = new Setting(5, 40, 20, 1);
    final static Setting TIDAL_VOLUME = new Setting(200, 600, 400, 10);
    final static Setting PEEP = new Setting(5, 20, 5, 1);
    final static Setting IE_RATIO = new Setting(1, 9, 4, 1);

    final int min;
    final int max;
    final int defalt;
    final int increment;

    private Setting(int min, int max, int defalt, int increment) {
        this.min = min;
        this.max = max;
        this.defalt = defalt;
        this.increment = increment;
    }

}
