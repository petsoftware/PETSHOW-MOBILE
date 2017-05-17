package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import br.com.petshow.R;
import br.com.petshow.model.Amigo;
import br.com.petshow.model.Usuario;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.ImagemUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestDelete;
import br.com.petshow.web.util.RequestGetEntity;

import static br.com.petshow.util.FacebookUtil.usuarioLogado;

public class PerfilActivity extends PetActivity {

    private ListView lstPublicacao;
    ProgressDialog progressDialog;
    Usuario usuario;
    Amigo amigo;

    private TextView txtNome;
    private TextView txtLink;
    private ImageView imgUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        MenuUtil.loadToolBar(this,R.id.toolbar_activity_perfil);
        progressDialog = new ProgressDialog(this);

        startVariables();
       // startComponents();
       // loadListAtividades();

    }

    public void startVariables() {
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_USUARIO_WITH_ANIMALS)){
            HashMap<String,Object> usuario=(HashMap<String,Object>) bundle.getSerializable(AtributosUtil.PAR_USUARIO_WITH_ANIMALS);
            loadUsuario(Long.parseLong(usuario.get("uID").toString()));

        }



    }

    public void loadListAtividades(){
//        CriationUtil.openProgressBar(progressDialog);
//
//        new RequestListObjects(new CallBack(this) {
//
//            public void successWithReturn(String json)  {
//
//                try{
//                    CriationUtil.closeProgressBar(progressDialog);
//
//
//                } catch (Exception e) {
//                    CriationUtil.closeProgressBar(progressDialog);
//                    MessageUtil.messageErro(getContext(),e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//            public void successNoReturn() {}
//            public void predictedError(MapErroRetornoRest map) {
//                CriationUtil.closeProgressBar(progressDialog);
//                MessageUtil.messageWarning(getContext(),map.getMessage());
//
//            }
//
//        }).execute();
    }

    public void startComponents() {


        txtNome = (TextView) findViewById(R.id.perfil_txtNome);
        lstPublicacao= (ListView) findViewById(R.id.perfil_lstPublicacao);
        //txtLink= (TextView) findViewById(R.id.perfil_txtLink);
        imgUsuario= (ImageView) findViewById(R.id.perfil_imgUsuario);

        imgUsuario.setImageBitmap(ImagemUtil.transformBase64Bitmap(usuario.getFoto()));
        txtNome.setText(usuario.getNome());
        //txtLink.setText("Ir para o FACEBOOK/ Ver o que colocar aqui");

        //txtLink.setMovementMethod(LinkMovementMethod.getInstance());

        //txtLink.setOnClickListener(new OpenFacebook());

    }

    public void loadUsuario(Long id){

        CriationUtil.openProgressBar(progressDialog);
        new RequestGetEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {
                try {
                    usuario = JsonUtil.transformObject(json, Usuario.class);
                    loadAmigo(usuarioLogado.getId(),usuario.getId());
                    startComponents();
                } catch (Exception e) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(),map.getMessage());
            }
        }).execute("usuario/"+id);
    }


    public void loadAmigo(Long idUsuarioLogado,Long idUsuarioAmigo){

        CriationUtil.openProgressBar(progressDialog);

        new RequestGetEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {
                try {
                    amigo = JsonUtil.transformObject(json, Amigo.class);
                    startComponents();
                } catch (Exception e) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(),map.getMessage());
            }
        }).execute("amigo/"+idUsuarioLogado+"/"+idUsuarioAmigo);
    }



    public class OpenFacebook implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            getOpenFacebookIntent(v.getContext());
        }
    }
    public  Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+usuario.getIdFacebook()));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/gabsslimmaa"));
        }
    }
    public  void addAmigo() {

        CriationUtil.openProgressBar(progressDialog);

        new RequestGetEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {
                try {
                    amigo = JsonUtil.transformObject(json, Amigo.class);
                    startComponents();
                } catch (Exception e) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(),map.getMessage());
            }
        }).execute("amigo/add",amigo);
    }

    public  void removeAmigo() {

        CriationUtil.openProgressBar(progressDialog);

        new RequestDelete(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {

                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(),map.getMessage());
            }
        }).execute("amigo/remove",amigo);
    }


    public  void seguirAmigo() {

        if(amigo==null){
            MessageUtil.messageWarning(this,getString(R.string.msgUserNotFriend));
            return;
        }

       // amigo.setFlSeguir( !amigo.getFlSeguir());
        CriationUtil.openProgressBar(progressDialog);

        new RequestGetEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {
                try {
                    amigo = JsonUtil.transformObject(json, Amigo.class);
                    startComponents();
                } catch (Exception e) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(),map.getMessage());
            }
        }).execute("amigo/add",amigo);
    }
}
