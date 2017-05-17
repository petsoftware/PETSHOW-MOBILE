package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import br.com.petshow.R;
import br.com.petshow.google.PetFirebaseInstanceIdService;
import br.com.petshow.model.SmartphoneREG;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.GoogleUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestGetEntity;

public class PrincipalActivity extends PetActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle(this.getString(R.string.app_name));
        MenuUtil.loadToolBar(this,R.id.toolbar_activity_principal);
        progressDialog = new ProgressDialog(this);
        CriationUtil.openProgressBar(progressDialog);
        Bundle bundle= getIntent().getExtras();

        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_IS_LOGIN) && ((String)bundle.getSerializable(AtributosUtil.PAR_IS_LOGIN)).equals("S")){

            while (true) {
                if (FirebaseInstanceId.getInstance().getToken() != null && FacebookUtil.usuarioLogado != null) {
                    PetFirebaseInstanceIdService.sendRegistrationToServer();
                    break;
                } else {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            CriationUtil.closeProgressBar(progressDialog);
        }else{


            new RequestGetEntity(new CallBack(this) {
                @Override
                public void successWithReturn(String json) {
                    SmartphoneREG smartphoneREG= null;
                    try {
                        smartphoneREG = JsonUtil.transformObject(json, SmartphoneREG.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(smartphoneREG ==null){

                        MessageUtil.messageWarning(getContext(),getContext().getString(R.string.erroLoginNovamente));

                        LoginManager.getInstance().logOut();
                        Intent it = new Intent(getContext(),LoginActivity.class);
                        startActivity(it);
                        finish();
                    }else if(!smartphoneREG.getIdSmartPhoneFCM().equals(FirebaseInstanceId.getInstance().getToken())){
                        MessageUtil.messageWarning(getContext(),getContext().getString(R.string.erroLoginOutroCelular));

                        LoginManager.getInstance().logOut();
                        Intent it = new Intent(getContext(),LoginActivity.class);
                        startActivity(it);
                        finish();

                    }
                    CriationUtil.closeProgressBar(progressDialog);
                }

                @Override
                public void successNoReturn() {

                }

                @Override
                public void predictedError(MapErroRetornoRest map) {

                }
            }).execute("fcm/smartreg/facebook/"+FacebookUtil.usuarioLogado.getIdFacebook());

        }


        EditText edt = (EditText) findViewById(R.id.editText);
        edt.setText(FirebaseInstanceId.getInstance().getToken()==null?"ele esta nulo":FirebaseInstanceId.getInstance().getToken());
        //FacebookUtil.loadProfileFacebook();
        startComponents();
        //Futuro sair ou dar um aviso que o servico não esta no ar ou fazer o tratamendo devido verificar tambem se é necessario instalar o servico no celular
        //https://developers.google.com/android/guides/setup
        if(GoogleUtil.isApiEnable(this)){
            Toast.makeText(this,"Enable",Toast.LENGTH_LONG);
        }else{
            Toast.makeText(this,"Disable",Toast.LENGTH_LONG);
        }

        //PetFirebaseInstanceIdService.sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());



    }
    @Override
    protected  void onResume(){
        super.onResume();
        //Futuro sair ou dar um aviso que o servico não esta no ar ou fazer o tratamendo devido verificar tambem se é necessario instalar o servico no celular
        //https://developers.google.com/android/guides/setup
        if(GoogleUtil.isApiEnable(this)){
            Toast.makeText(this,"Enable",Toast.LENGTH_LONG);
        }else{
            Toast.makeText(this,"Disable",Toast.LENGTH_LONG);
        }
    }


    public void startComponents() {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // carregar menu Global
        MenuUtil.loadMenuGlobal(this, menu);

        MenuItem itemMenuAdicionar= menu.add(0,1,1,"criar post"); //   (int groupId,        int itemId,        int order,        int titleRes)
        itemMenuAdicionar.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavegationUtil.goToMenu(this);
            return true;
        }
        if(id==1){
            Intent it = new Intent(this,CriarPostActivity.class);
            startActivity(it);
        }


        return super.onOptionsItemSelected(item);
    }

}

