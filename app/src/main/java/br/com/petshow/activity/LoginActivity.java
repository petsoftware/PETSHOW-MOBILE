package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.Arrays;

import br.com.petshow.R;
import br.com.petshow.google.PetFirebaseInstanceIdService;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.view.util.CriationUtil;

public class LoginActivity extends PetActivity {


    CallbackManager callbackManager ;
    LoginButton btnLogin;
    ImageView image;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startComponents();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithPublishPermissions(this,Arrays.asList("publish_actions"));
        CriationUtil.openProgressBar(progressDialog);
        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //futuro: fazer teste se o servidor de rest está funcionando
                FacebookUtil.loadProfileFacebook(LoginActivity.this);

                //goToPrincipal();

            }

            @Override
            public void onCancel() {
                CriationUtil.closeProgressBar(progressDialog);
                Toast.makeText(getApplicationContext(),R.string.cancel_login,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                CriationUtil.closeProgressBar(progressDialog);
                Toast.makeText(getApplicationContext(),R.string.erro_login,Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void startComponents(){
        image = (ImageView) findViewById(R.id.imageView3);
        image.setImageResource(R.drawable.pet);

        btnLogin = (LoginButton) findViewById(R.id.btnLogin);
        btnLogin.setReadPermissions(AtributosUtil.FACEBOOK_ACESS);
        progressDialog = new ProgressDialog(this);


    }

    public void goToPrincipal() {
        Intent intent = new Intent(this,PrincipalActivity.class);
        intent.putExtra(AtributosUtil.PAR_IS_LOGIN,"S");
        startActivity(intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
