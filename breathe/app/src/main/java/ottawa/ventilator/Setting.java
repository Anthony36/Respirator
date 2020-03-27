package ottawa.ventilator;

/**
 * Values for target settings.
 */
public class Setting {

    final static public Setting BREATHING_RATE = new Setting(6, 40, 12, 1);
    final static public Setting FIO2 = new Setting(20, 100, 20, 10);
    final static public Setting INSPIRATION_PRESSURE = new Setting(5, 40, 20, 1);
    final static public Setting TIDAL_VOLUME = new Setting(200, 600, 400, 10);
    final static public Setting PEEP = new Setting(5, 20, 5, 1);
    final static public Setting IE_RATIO = new Setting(1, 9, 4, 1);

    public final int min;
    public final int max;
    public final int defalt;
    public final int increment;

    public Setting(int min, int max, int defalt, int increment) {
        this.min = min;
        this.max = max;
        this.defalt = defalt;
        this.increment = increment;
    }

}
