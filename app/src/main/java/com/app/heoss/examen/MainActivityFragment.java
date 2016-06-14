package com.app.heoss.examen;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by HeosS on 12-06-2016.
 */
public class MainActivityFragment extends Fragment {

    private AccessToken accessToken;
    private TextView textView;
    private ProfilePictureView profilePicture;
    private CallbackManager callbackManager;

    //inicio de sesion Facebook
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            //se envia mensaje y imagen de perfil
            if (profile != null){
                textView.setText("¡Bienvenido " + profile.getName() + "!");
                profilePicture.setProfileId(profile.getId());
            }
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "¡Inicio de sesión exitoso!", Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        public void onCancel() {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "¡Inicio de sesión cancelado!", Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "¡No se pudo iniciar sesión!", Toast.LENGTH_LONG);
            toast.show();
        }
    };

    //constructor
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enlace Facebook
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.text_details);
        profilePicture = (ProfilePictureView) view.findViewById(R.id.profilePicture);

        Profile profile = Profile.getCurrentProfile();
        if (profile != null){
            textView.setText("¡Bienvenido " + profile.getName() + "!");
            profilePicture.setProfileId(profile.getId());
        }

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_facebook);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);

        //accion del botón de Facebook
        loginButton.registerCallback(callbackManager, callback);

        //sesión no iniciada
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    textView.setText("Iniciar sesión");
                    profilePicture.setProfileId("");
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
