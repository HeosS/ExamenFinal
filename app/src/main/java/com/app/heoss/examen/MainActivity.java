package com.app.heoss.examen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity{

    private AdView adView;
    private Button btnYoutube, btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //enlace banner
        adView = (AdView)findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        //enlace botón actividad youtube
        btnYoutube = (Button)findViewById(R.id.button_youtube);
        btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, YoutubeFragment.class);
                startActivity(i);
            }
        });

        btnGoogle = (Button)findViewById(R.id.google);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGoogle = new Intent(MainActivity.this, GoogleLogin.class);
                startActivity(iGoogle);
            }
        });

        mostrarHashKey();
    }

    //método que muestra el hash
    public void mostrarHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.heoss.examen",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("MiHashKey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    //Remueve el banner
    @Override
    protected void onDestroy() {
        if(adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    //Se prepara el banner
    @Override
    protected void onResume() {
        if(adView != null) {
            adView.resume();
        }
        super.onResume();
    }

    //Detiene el banner
    @Override
    protected void onPause() {
        if(adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Realizado por Sebastian Bascuñan", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                salir();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Método que cierra la aplicación
    public void salir() {
        AlertDialog.Builder cuadroDialogo = new AlertDialog.Builder(this);
        cuadroDialogo.setTitle("Alerta");
        cuadroDialogo.setMessage("¿Esta seguro que quieres salir?");
        cuadroDialogo.setCancelable(false);
        cuadroDialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        cuadroDialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                seguirEnApp();
            }
        });
        cuadroDialogo.show();
    }

    //Método que permanece en la aplicación
    public void seguirEnApp() {
        Toast mensaje = Toast.makeText(this, "Gracias por permanecer en la aplicación", Toast.LENGTH_SHORT);
        mensaje.show();
    }
}
