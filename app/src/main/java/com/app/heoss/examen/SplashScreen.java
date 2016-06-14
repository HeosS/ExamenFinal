package com.app.heoss.examen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HeosS on 12-06-2016.
 */
public class SplashScreen extends Activity {

    private static final long SPLASH_SCREEN_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);
        animaciones(null);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("datos", Context.MODE_PRIVATE);
                String visto = settings.getString("Visto", "");

                if (visto.equals("")) {
                    Intent mainIntent = new Intent().setClass(SplashScreen.this, Bienvenido.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent().setClass(SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    public void animaciones(View v) {
        ImageView loader_1 = (ImageView) findViewById(R.id.loader2);
        Animation animacion_1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_loader);
        loader_1.startAnimation(animacion_1);

        ImageView loader_2 = (ImageView) findViewById(R.id.loader1);
        Animation animacion_2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_loader);
        loader_2.startAnimation(animacion_2);
    }
}
