package com.app.heoss.examen;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;

public class GoogleLogin extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

	private static final int SIGN_IN_REQUEST_CODE = 10;
	private static final int ERROR_DIALOG_REQUEST_CODE = 11;

	Button signOutButton;

	//comunicación con Google APIs
	private GoogleApiClient mGoogleApiClient;
	private boolean mSignInClicked;
	private boolean mIntentInProgress;

	//conexión con Google Play services
	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_google);

		android.app.ActionBar bar = getActionBar();
		bar.setDisplayShowHomeEnabled(true);

		findViewById(R.id.sign_in_button).setOnClickListener(this);
		signOutButton = (Button) findViewById(R.id.sign_out_button);
		signOutButton.setOnClickListener(this);

		//inicio cliente google plus api
		mGoogleApiClient = buildGoogleAPIClient();
	}

	//acceso a API para GoogleApiClient
	private GoogleApiClient buildGoogleAPIClient() {
		return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		/*new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();*/
	}

	@Override
	protected void onStart() {
		super.onStart();
		//inico de conexion connection
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//desconectar api si esta conectada
		if (mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
	}

	//evalua id de boton
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.sign_in_button:
			processSignIn();
			break;
		case R.id.sign_out_button:
			processSignOut();
			break;
		}

	}

	//update de la vista para el boton signOut
	private void processUIUpdate(boolean isUserSignedIn) {
		if (isUserSignedIn) {
			signOutButton.setEnabled(true);
		} else {
			signOutButton.setEnabled(false);
		}
	}

	//evalúa resultado
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SIGN_IN_REQUEST_CODE) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	//proceso de inicio de sesión
	private void processSignOut() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			processUIUpdate(false);
		}

	}

	//proceso de conexión
	private void processSignIn() {
		if (!mGoogleApiClient.isConnecting()) {
			processSignInError();
			mSignInClicked = true;
		}
	}

	//proceso de error de conexión
	private void processSignInError() {
		if (mConnectionResult != null && mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, SIGN_IN_REQUEST_CODE);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	//callback para fallo de conexión
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, ERROR_DIALOG_REQUEST_CODE).show();
			return;
		}
		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				processSignInError();
			}
		}
	}

	//conectado
	@Override
	public void onConnected(Bundle connectionHint) {
		mSignInClicked = false;
		Toast.makeText(getApplicationContext(), "Sesión iniciada", Toast.LENGTH_LONG).show();
		processUIUpdate(true);
	}

	//callback para la suspensión de conexión
	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}
}
