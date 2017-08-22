package br.com.petshow.activity;


import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;

import com.facebook.login.LoginManager;



import br.com.petshow.R;

import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.web.util.CallBackBitMap;
import br.com.petshow.web.util.LoadImageTask;

public class MenuActivity extends PetActivity {


    TextView txtNome ;
    TextView txtEmail ;
    ImageView imgProfileFacebook;
    Button btnLogout;
    Button btnEdtUsuario;
    ImageButton btnSetaRetorno;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        startComponents();



    }

    public void startComponents(){
        txtNome = (TextView) findViewById(R.id.txtNome);
        imgProfileFacebook=(ImageView)  findViewById(R.id.imgProfileFacebook);
        btnLogout=(Button) findViewById(R.id.btnLogout);
        btnEdtUsuario=(Button) findViewById(R.id.btnEditarPerfil);
        btnSetaRetorno=(ImageButton) findViewById(R.id.btnSetaRetorno);

        Profile profile = Profile.getCurrentProfile();
        String photoUrl = profile.getProfilePictureUri(200, 200).toString();
        txtNome.setText(profile.getName());
        loadImgFacebook( photoUrl);


    }
    private void goToLoginScreen() {
        Intent it = new Intent(this,LoginActivity.class);
        startActivity(it);

    }
    public void gotoListPets(View view) {
        Intent it = new Intent(this,ListaPetsActivity.class);
        startActivity(it);

    }
    public void goToEditProfile(View view) {
        Intent it = new Intent(this,EdtUsuarioActivity.class);
        startActivity(it);

    }
    public void goToFriends(View view) {
        Intent it = new Intent(this,AmigoActivity.class);
        startActivity(it);

    }

    public void logout (View view){
        LoginManager.getInstance().logOut();
        goToLoginScreen();

    }
    public void close(View view){
        finish();
    }

    public  void loadImgFacebook(String photoUrl){
        new LoadImageTask(new CallBackBitMap() {
            @Override
            public void successWithReturn(Bitmap ob) {
                imgProfileFacebook.setImageBitmap(ob);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {

            }
        }).execute(photoUrl);
    }

}
