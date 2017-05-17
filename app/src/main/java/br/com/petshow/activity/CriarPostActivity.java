package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.*;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.com.petshow.exceptions.FacebookPermissionException;
import br.com.petshow.R;

import br.com.petshow.model.Usuario;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.FacebookUtil;

import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;

public class CriarPostActivity extends PetActivity {

    ProgressDialog progressDialog;
    Usuario usuarioLogado;
    Profile profile;

    EditText edtMsg;
    ImageView photoPost;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_post);
        startVariables();
        startComponents();





    }

    public void startComponents() {

        MenuUtil.loadToolBar(this,R.id.toolbar_activity_criar_post);
        progressDialog = new ProgressDialog(this);

        edtMsg = (EditText) findViewById(R.id.criarPost_edtMensagem);
        photoPost =(ImageView) findViewById(R.id.criarPost_image);
        photoPost.setOnClickListener(new ChangePictureListener());
    }


    public void startVariables(){
        usuarioLogado = FacebookUtil.usuarioLogado;
        profile = Profile.getCurrentProfile();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);

        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar)); //   (int groupId,        int itemId,        int order,        int titleRes)
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavegationUtil.goToMenu(this);
            return true;
        }
        if (id == 1) {
            onClicksalvar();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    public void onClicksalvar() {
        if (photo==null) {
            MessageUtil.messageWarning(this, this.getString(R.string.selFoto));
            return;
        }
        postarFacebook();
    }
    public void postarFacebook(){
        try {
            FacebookUtil.postPhoto(photo,edtMsg.getText().toString());
        } catch (FacebookPermissionException e) {
            // futuro testar se a permissao de post está ok caso nao esteja o usuario deverá dar permissao
            e.printStackTrace();
        }
    }


    private class ChangePictureListener implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            showOptionsPhoto ();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

        }
    }

    public void showOptionsPhoto (){

        android.support.v7.app.AlertDialog.Builder ad = new android.support.v7.app.AlertDialog.Builder(this);
        ad.setTitle(R.string.title_modal_tipo_get_photo);
        ad.setSingleChoiceItems(R.array.list_type_get_photo,-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    NavegationUtil.openGalleryPhoto(CriarPostActivity.this);
                    dialog.dismiss();
                }
                if(which==1){
                    NavegationUtil.openCamera(CriarPostActivity.this);
                    dialog.dismiss();
                }

            }
        });
        ad.setNeutralButton(R.string.lblCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.create();
        ad.show();




        ad.setSingleChoiceItems(R.array.list_type_get_photo,-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    NavegationUtil.openGalleryPhoto(CriarPostActivity.this);
                    dialog.dismiss();
                }
                if(which==1){
                    NavegationUtil.openCamera(CriarPostActivity.this);
                    dialog.dismiss();
                }

            }
        });
        ad.setNeutralButton(R.string.lblCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.create();
        ad.show();




    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){


        if (requestCode == AtributosUtil.INTENT_OPEN_GALLERY) {
            if(resultCode == RESULT_OK) {


                Uri imageUri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    photoPost.setImageBitmap(bitmap);
                    photo=bitmap;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "erro na abertura da imagem", Toast.LENGTH_SHORT).show();

                }
            }


        }
        if (requestCode == AtributosUtil.INTENT_OPEN_CAMERA) {

            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                photoPost.setImageBitmap(bitmap);
                photo=bitmap;

            }
        }
    }


}
