package by.lykashenko.demon.mirparfumanew;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * Created by Admin on 25.07.17.
 */

public class MirParfumaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
    }
}
