package ottawa.ventilator.ui;

public interface UiApi {

    public void setMinuteVentilationValue(float value);
    public void setTidalVolumeValue(int value);
    public void setFiO2Value(int value);

    public void setBreathingRateTarget(int value);
    public void setInspirationTarget(int value);
    public void setFiO2Target(int value);
    public void setPeepTarget(int value);
    public void setTidalVolumeTarget(int value);
    public void setIeRatioTarget(int value);

    // ...etc.

}
