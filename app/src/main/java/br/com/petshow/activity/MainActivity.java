package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.vision.face.Face;
import com.google.firebase.iid.FirebaseInstanceId;

import br.com.petshow.R;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;

public class MainActivity extends PetActivity {

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MenuUtil.loadToolBar(this,R.id.toolbar_activity_main);

        progressDialog = new ProgressDialog(this);
        CriationUtil.openProgressBar(progressDialog);

        if(AccessToken.getCurrentAccessToken() ==null ){
            goToLoginScreen();
        }else{
            //goToPrincipalScreen();
            FacebookUtil.loadProfileFacebook(this);
        }


    }

    private void goToLoginScreen() {
        Intent it = new Intent(this,LoginActivity.class);
        //it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();
    }
    public void goToPrincipalScreen() {
        Intent it = new Intent(this,PrincipalActivity.class);
        startActivity(it);
        finish();
    }





}
