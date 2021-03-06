package com.hayashi.android_geogence;

import android.util.Log;

public class MyLog {

    private MainActivity activity;
    private Text text;
    private static MyLog instance = null;
    private final String filePath = "log.txt";
    private final String TAG = "----";

    private MyLog(MainActivity activity) {
        this.activity = activity;
        this.text = new Text(
                this.filePath,
                this.activity);
    }

    public static void init(MainActivity activity) {
        instance = new MyLog(activity);
    }

    public static MyLog getInstance() {
        return instance;
    }

    public void debug(String message) {
        Log.d(TAG, message);
        this.text.append(message + "\n");
    }

    public String getLogText() {
        return this.text.read();
    }

    public void deleteLogFile() {
        this.text.delete();
    }
}
