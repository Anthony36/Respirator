package ottawa.ventilator.application;

public interface ILifecycle {

    void onCreate();
    void onStart();
    void onPause();
    void onResume();
    void onStop();
    void onRestart();
    void onDestroy();

}
