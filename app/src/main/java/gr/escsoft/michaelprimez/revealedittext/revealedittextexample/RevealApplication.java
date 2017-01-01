package gr.escsoft.michaelprimez.revealedittext.revealedittextexample;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by michael on 12/31/16.
 */

public class RevealApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
