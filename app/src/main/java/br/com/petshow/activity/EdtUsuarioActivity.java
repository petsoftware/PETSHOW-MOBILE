package br.com.petshow.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.facebook.Profile;



import java.util.ArrayList;
import java.util.List;

import br.com.petshow.R;
import br.com.petshow.adapters.BairroAdapterToArray;
import br.com.petshow.adapters.CidadeAdapterToArray;
import br.com.petshow.adapters.EstadoAdapterToArray;

import br.com.petshow.model.Bairro;
import br.com.petshow.model.Cidade;
import br.com.petshow.model.Estado;
import br.com.petshow.model.Usuario;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.view.util.PhoneEditText;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.CallBackBitMap;
import br.com.petshow.web.util.LoadImageTask;

import br.com.petshow.web.util.RequestListObjects;
import br.com.petshow.web.util.RequestPostEntity;


public class EdtUsuarioActivity extends PetActivity {
    ProgressDialog progressDialog;
    TextView txtNome ;
    ImageView imgProfileFacebook;
    PhoneEditText txtTelefone;
    Spinner spEstado;
    Spinner spCidade;
    Spinner spBairro;
    TextView lblEstadoAtual;
    TextView lblCidadeAtual;
    TextView lblBairroAtual;
    Usuario usuarioLogado;
    Profile profile;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_usuario);
        setTitle(this.getString(R.string.title_activity_edt_usuario));

        startVariables();
        startComponents();




    }
    public void startVariables(){
        usuarioLogado = FacebookUtil.usuarioLogado;
        profile = Profile.getCurrentProfile();

    }
    public void startComponents(){
        MenuUtil.loadToolBar(this,R.id.toolbar_activity_edt_usuario);
        progressDialog = new ProgressDialog(this);

        txtNome = (TextView) findViewById(R.id.edtUsuario_txtNome);
        imgProfileFacebook=(ImageView)  findViewById(R.id.edtUsuario_imgProfileFacebook);
        txtTelefone =(PhoneEditText) findViewById(R.id.edtUsuario_txtTelefone);

        spEstado=(Spinner) findViewById(R.id.edtUsuario_spEstado);
        spCidade=(Spinner) findViewById(R.id.edtUsuario_spCidade);
        spBairro=(Spinner) findViewById(R.id.edtUsuario_spBairro);

        lblEstadoAtual=(TextView)findViewById(R.id.edtUsuario_lblEstadoAtual) ;
        lblCidadeAtual=(TextView)findViewById(R.id.edtUsuario_lblCidadeAtual) ;
        lblBairroAtual=(TextView)findViewById(R.id.edtUsuario_lblBairroAtual) ;

        spEstado.setOnItemSelectedListener( new EdtUsuarioActivity.EstadoListener(this));
        spCidade.setOnItemSelectedListener( new EdtUsuarioActivity.CidadeListener(this));

        String photoUrl = profile.getProfilePictureUri(200, 200).toString();

        txtNome.setText(profile.getName());
        loadFieldInvisible(usuarioLogado);
        loadImgFacebook( photoUrl);

        loadSpinners();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);

        MenuItem itemMenuSalvar= menu.add(0,1,1,getString(R.string.lblSalvar)); //   (int groupId,        int itemId,        int order,        int titleRes)
        itemMenuSalvar.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        if(usuarioLogado.getBairro()!=null && !usuarioLogado.getBairro().trim().equals("")) {
            MenuItem itemMenuAlterarEndereco = menu.add(0, 2, 1, getString(R.string.lblTrocarEndereco)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemMenuAlterarEndereco.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }



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
            onClickSalvar();
        }
        if( id == 2){
            onClickTrocarEndereco();
        }

        return super.onOptionsItemSelected(item);
    }




    private class EstadoListener implements AdapterView.OnItemSelectedListener {

        EdtUsuarioActivity edtUsuarioActivity =null;
        public EstadoListener(EdtUsuarioActivity edtUsuarioActivity){
            this.edtUsuarioActivity=edtUsuarioActivity;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position>0) {
               loadSpinnerCidade((Estado) parent.getItemAtPosition(position));
            }else{
               edtUsuarioActivity.loadCleanListCidade();
               edtUsuarioActivity.loadCleanListBairro();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CidadeListener implements AdapterView.OnItemSelectedListener {

        EdtUsuarioActivity edtUsuarioActivity =null;
        public CidadeListener(EdtUsuarioActivity edtUsuarioActivity){
            this.edtUsuarioActivity=edtUsuarioActivity;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position>0) {
                loadSpinnerBairros((Cidade) parent.getItemAtPosition(position));
            }else{
                edtUsuarioActivity.loadCleanListBairro();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void loadSpinners(){

        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<EstadoAdapterToArray> lista=null;
                try {

                    lista=JsonUtil.jsonToList(new EstadoAdapterToArray(), json,EstadoAdapterToArray.class);

                    EstadoAdapterToArray objSelecione = new EstadoAdapterToArray();
                    objSelecione.setNome(getContext().getString(R.string.selEstado));
                    lista.add(0, objSelecione);

                    CriationUtil.createArrayAdapter(this.getContext(),spEstado, lista);


                } catch (Exception e) {
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
       }
            public void successNoReturn() {}
            public void predictedError(MapErroRetornoRest map) {
                MessageUtil.messageWarning(getContext(),map.getMessage());
            }

        }).execute("endereco/consulta/estados");
    }

    private void loadSpinnerCidade(Estado estadoSelecionado){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<CidadeAdapterToArray> lista=null;
                try {
                    lista=JsonUtil.jsonToList(new CidadeAdapterToArray(), json,CidadeAdapterToArray.class);
                    CidadeAdapterToArray objSelecione = new CidadeAdapterToArray();
                    objSelecione.setNome(getContext().getString(R.string.selCidade));
                    //objSelecione.setNome("merdaaaaaaa");
                    lista.add(0, objSelecione);

                    CriationUtil.createArrayAdapter(this.getContext(),spCidade, lista);

                } catch (Exception e) {
                    MessageUtil.messageErro(this.getContext(),e.getMessage());
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
   }
            public void successNoReturn() {}
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(this.getContext(),map.getMessage());
            }

        }).execute("endereco/consulta/cidade/estado/id/"+estadoSelecionado.getId());
    }
    private void loadSpinnerBairros(Cidade cidadeSelecionado){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<BairroAdapterToArray> lista=null;
                try {
                    lista=JsonUtil.jsonToList(new BairroAdapterToArray(), json,BairroAdapterToArray.class);

                    BairroAdapterToArray objSelecione = new BairroAdapterToArray();
                    objSelecione.setNome(getContext().getString(R.string.selBairro));
                    lista.add(0, objSelecione);

                    CriationUtil.createArrayAdapter(this.getContext(),spBairro, lista);

                } catch (Exception e) {

                    MessageUtil.messageErro(this.getContext(),e.getMessage());
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
            }
            public void successNoReturn() {}
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(this.getContext(),map.getMessage());

            }

        }).execute("endereco/consulta/bairro/cidade/"+cidadeSelecionado.getId());


    }


    public void loadCleanListCidade(){
            List<CidadeAdapterToArray> lista=new ArrayList<CidadeAdapterToArray>();

             CidadeAdapterToArray objSelecione = new CidadeAdapterToArray();
            objSelecione.setNome(this.getString(R.string.selCidade));
            lista.add(0, objSelecione);

            CriationUtil.createArrayAdapter(this,spCidade, lista);

    }
    public void loadCleanListBairro(){
        List<BairroAdapterToArray> lista=new ArrayList<BairroAdapterToArray>();

        BairroAdapterToArray objSelecione = new BairroAdapterToArray();
        objSelecione.setNome(this.getString(R.string.selBairro));
        lista.add(0, objSelecione);

        CriationUtil.createArrayAdapter(this,spBairro, lista);

    }
   private  void loadFieldInvisible(Usuario usuario){

       if(usuario.getBairro()!=null && !usuario.getBairro().trim().equals("")){
           lblBairroAtual.setVisibility(View.VISIBLE);
           lblCidadeAtual.setVisibility(View.VISIBLE);
           lblEstadoAtual.setVisibility(View.VISIBLE);
           lblEstadoAtual.setText(usuario.getEstado());
           lblCidadeAtual.setText(usuario.getCidade());
           lblBairroAtual.setText(usuario.getBairro());
           txtTelefone.setText(usuario.getDdd()+""+usuario.getTelefone());
       }else{
           spEstado.setVisibility(View.VISIBLE);
           spCidade.setVisibility(View.VISIBLE);
           spBairro.setVisibility(View.VISIBLE);
           loadCleanListCidade();
           loadCleanListBairro();
           if(usuario.getDdd()+usuario.getTelefone()>0) {
               txtTelefone.setText(usuario.getDdd() + "" + usuario.getTelefone());
           }

       }


   }

    public void onClickSalvar(){

        if(usuarioLogado.getBairro()==null || usuarioLogado.getBairro().trim().equals("")) {
            if (spEstado.getSelectedItemPosition() == 0) {
                MessageUtil.messageWarning(this, this.getString(R.string.selEstado));
                //ValidationAndroidUtil.setFocus(this,spEstado);
                return;
            }
            if (spCidade.getSelectedItemPosition() == 0) {
                MessageUtil.messageWarning(this, this.getString(R.string.selCidade));
               // ValidationAndroidUtil.setFocus(this,spCidade);
                return;
            }
            if (spBairro.getSelectedItemPosition() == 0) {
                MessageUtil.messageWarning(this, this.getString(R.string.selBairro));
               // ValidationAndroidUtil.setFocus(this,spBairro);
                return;
            }
        }

        if(txtTelefone.getText().toString().length() != 15 && txtTelefone.getText().toString().replace("(","").replace(")","").replace("-","").trim().length()!=0){
            MessageUtil.messageWarning(this, this.getString(R.string.validacaoTelefone));
           // ValidationAndroidUtil.setFocus(this,txtTelefone);
            return;
        }




        salvarUsuario();


    }

    public void salvarUsuario(){

        if(usuarioLogado.getBairro() ==null || usuarioLogado.getBairro().trim().equals("")) {
            usuarioLogado.setEstado(((Estado) spEstado.getSelectedItem()).getNome());
            usuarioLogado.setCidade(((Cidade) spCidade.getSelectedItem()).getNome());
            usuarioLogado.setBairro(((Bairro) spBairro.getSelectedItem()).getNome());
        }
        usuarioLogado.setDdd(Integer.parseInt(txtTelefone.getText().toString().substring(1,3)));
        usuarioLogado.setTelefone(Integer.parseInt(txtTelefone.getText().toString().substring(4,15).trim().replace("-","")));
        CriationUtil.openProgressBar(progressDialog);
        new RequestPostEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {
                Intent it = new Intent(getContext(),EdtUsuarioActivity.class);

                startActivity(it);
                finish();
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(), map.getMessage());

            }
        }).execute("usuario/salvar",usuarioLogado);
    }

    public void onClickTrocarEndereco(){
        //Futuro: validacao definir politica de permissao de alteraçao de dados tempo que poderá ser alterado

        alterarDados();
    }




        public void alterarDados(){
        //Futuro: definir politica de permissao de alteraçao de dados tempo que poderá ser alterado
            usuarioLogado.setEstado(null);
            usuarioLogado.setCidade(null);
            usuarioLogado.setBairro(null);
            //usuarioLogado.setDdd(0);
           // usuarioLogado.setTelefone(0);
            CriationUtil.openProgressBar(progressDialog);
            new RequestPostEntity(new CallBack(this) {
                @Override
                public void successWithReturn(String json) {
                    Intent it = new Intent(getContext(),EdtUsuarioActivity.class);
                    startActivity(it);
                    finish();
                }

                @Override
                public void successNoReturn() {

                }

                @Override
                public void predictedError(MapErroRetornoRest map) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageWarning(this.getContext(),map.getMessage());

                }
            }).execute("usuario/salvar",usuarioLogado);
    }







    public void goToMenu(View v) {
        Intent it = new Intent(this, MenuActivity.class);
        startActivity(it);

    }




    public  void loadImgFacebook(String photoUrl){
        CriationUtil.openProgressBar(progressDialog);
        new LoadImageTask(new CallBackBitMap() {
            @Override
            public void successWithReturn(Bitmap ob) {
                imgProfileFacebook.setImageBitmap(ob);
                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
            }
        }).execute(photoUrl);
    }



    // criar classes utilitarias



//
//    public static <T> ArrayAdapter<T> createArrayAdapter(Context ctx, Spinner spinner, List<T> data){
//        return createArrayAdapter( ctx,  spinner,  data,android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);
//    }
//
//    public static <T> ArrayAdapter<T> createArrayAdapter(Context ctx, Spinner spinner, List<T> data,int layoutAdapter, int layoutDropDown){
//
//
//        ArrayAdapter<T> arrayAdapter = new ArrayAdapter<T> (ctx,layoutAdapter,data);
//        arrayAdapter.setDropDownViewResource(layoutDropDown);
//        spinner.setAdapter(arrayAdapter);
//        return arrayAdapter;
//    }
   // ArrayAdapter<EstadoAdapterToArray> arrayAdapter = new ArrayAdapter<EstadoAdapterToArray>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, listaEstado);

    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

   // spEstado.setAdapter(arrayAdapter);


//    private void getUsuario(long id){
//
//        new RequestGetEntity(new CallBack(this) {
//
//            public void successWithReturn(String json)  {
//
//                try {
//                    Usuario usuario = JsonUtil.transformObject(json,Usuario.class);
//                    usuarioLogado=usuario;
//                    loadFieldInvisible( usuario);
//                } catch (Throwable e) {
//
//                    MessageUtil.messageErro(this.getContext(),e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//            public void successNoReturn() {}
//            public void predictedError(MapErroRetornoRest map) {
//
//                MessageUtil.messageWarning(this.getContext(),map.getMessage());
//            }
//
//        }).execute("usuario/facebook/"+id);
//
//
//    }

}
