package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.Profile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.petshow.R;
import br.com.petshow.adapters.RacaAdapterToArray;
import br.com.petshow.enums.EnumFaseVida;
import br.com.petshow.enums.EnumTipoAnimal;
import br.com.petshow.model.Adocao;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Tutor;
import br.com.petshow.model.Usuario;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.DateUtils;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.ImagemUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.view.util.PhoneEditText;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestDelete;
import br.com.petshow.web.util.RequestListObjects;
import br.com.petshow.web.util.RequestPostEntity;

public class NovoAdocaoActivity extends PetActivity {

    ProgressDialog progressDialog;
    Usuario usuarioLogado;
    Profile profile;
    ImageView imageAnimal1;
    ImageView imageAnimal2;
    ImageView imageAnimal3;

    Spinner spTiposAnimais;
    Spinner spRacas;
    Spinner spFaixaIdade;
    Spinner spPorte;
    Switch  swCastrado;
    Switch  swVacinado;
    Switch  swVermifugado;
    EditText txtTelefone;
    EditText txtaEdtMensagem;

    RadioButton radioMaxo;
    RadioButton radioFemea;
    RadioGroup radioGroupSexo;
    List<String> fotos;
    Adocao adocao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_adocao);
        startComponents();
        startVariables();
        loadSpinners();
    }

    public void startVariables(){
        usuarioLogado = FacebookUtil.usuarioLogado;
        profile = Profile.getCurrentProfile();
        adocao= adocao==null?new Adocao():adocao;

    }

    public void startComponents() {

        MenuUtil.loadToolBar(this, R.id.toolbar_activity_novo_adocao);
        progressDialog = new ProgressDialog(this);

        imageAnimal1=(ImageView) findViewById(R.id.novoAdocao_image1);
        imageAnimal2=(ImageView) findViewById(R.id.novoAdocao_image2);
        imageAnimal3=(ImageView) findViewById(R.id.novoAdocao_image3);

        spTiposAnimais=(Spinner)  findViewById(R.id.novoAdocao_spTiposAnimais);
        spRacas=(Spinner) findViewById(R.id.novoAdocao_spRacas);
        spFaixaIdade=(Spinner) findViewById(R.id.novoAdocao_spFaixaIdade);
        spPorte=(Spinner) findViewById(R.id.novoAdocao_spPorte);
        swCastrado=(Switch) findViewById(R.id.novoAdocao_swCastrado);
        swVacinado=(Switch) findViewById(R.id.novoAdocao_swVacinado);
        swVermifugado=(Switch) findViewById(R.id.novoAdocao_swVermifugado);
        txtTelefone=(PhoneEditText) findViewById(R.id.novoAdocao_txtTelefone);
        txtaEdtMensagem=(EditText) findViewById(R.id.novoAdocao_edtMensagem);

        spFaixaIdade.setAdapter(new ArrayAdapter<EnumFaseVida>(this, android.R.layout.simple_list_item_1, EnumFaseVida.values()));

        radioMaxo=(RadioButton)  findViewById(R.id.novoAdocao_rMacho);
        radioFemea=(RadioButton)  findViewById(R.id.novoAdocao_rFemea);
        radioGroupSexo=(RadioGroup)  findViewById(R.id.novoAdocao_radioGroupSexo);
        imageAnimal1.setOnClickListener(new NovoAdocaoActivity.ChangePictureListener());
        imageAnimal2.setOnClickListener(new NovoAdocaoActivity.ChangePictureListener());
        imageAnimal3.setOnClickListener(new NovoAdocaoActivity.ChangePictureListener());
        fotos = new ArrayList<String>();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);

        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar)); //   (int groupId,        int itemId,        int order,        int titleRes)
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        if(adocao!=null && adocao.getId()>0){
            MenuItem itemMenu2= menu.add(0,3,3,getString(R.string.lblExcluir)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemMenu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            MenuItem itemMenu3= menu.add(0,2,2,getString(R.string.lblAdotado)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemMenu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
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
            salvar();
            return true;
        }
        if (id == 3) {
            excluir();
            return true;
        }
        if (id == 2) {
            adotado();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void loadSpinners(){

        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<String> lista=null;
                try {

                    lista= JsonUtil.jsonToList(new String(), json,String.class);

                    lista.add(0, getContext().getString(R.string.selAnimal));

                    CriationUtil.createArrayAdapter(this.getContext(),spTiposAnimais, lista);
                    loadAnimal();

                } catch (Exception e) {
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
            }
            public void successNoReturn() {}
            public void predictedError(MapErroRetornoRest map) {
                MessageUtil.messageWarning(getContext(),map.getMessage());
            }

        }).execute("animal/consulta/tipos");
    }


    public void showOptionsPhoto (){

        android.support.v7.app.AlertDialog.Builder ad = new android.support.v7.app.AlertDialog.Builder(this);
        ad.setTitle(R.string.title_modal_tipo_get_photo);

        ad.setSingleChoiceItems(R.array.list_type_get_photo,-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    NavegationUtil.openGalleryPhoto(NovoAdocaoActivity.this);
                    dialog.dismiss();
                }
                if(which==1){
                    NavegationUtil.openCamera(NovoAdocaoActivity.this);
                    dialog.dismiss();
                }

            }
        });
        ad.setNeutralButton(R.string.lblCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();;
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
                    addPhoto(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "erro na abertura da imagem", Toast.LENGTH_SHORT).show();

                }
            }else {

            }


        }
        if (requestCode == AtributosUtil.INTENT_OPEN_CAMERA) {

            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                addPhoto(bitmap);

            }else {


            }
        }
    }
    private void addPhoto(Bitmap bitmap){

        if(fotos.size()==0){
            imageAnimal1.setImageBitmap(bitmap);
        }else if(fotos.size()==1){
            imageAnimal2.setImageBitmap(bitmap);
        }else if(fotos.size()==2){
            imageAnimal3.setImageBitmap(bitmap);
        }
        fotos.add(ImagemUtil.transformBase64AsString(bitmap));

    }

    private void focusInCamposRetornoRest(String field){
//        if(field.equals("nome")){
//            txtNome.requestFocus();
//        }
//        if(field.equals("tipo")){
//            spTpAnimal.requestFocus();
//        }
//        if(field.equals("flSexo")){
//            spTpAnimal.requestFocus();
//        }
//        if(field.equals("dataNascimento")){
//            txtDataNascimento.requestFocus();
//        }

    }

    private class RacaListeners implements AdapterView.OnItemSelectedListener {


        public RacaListeners(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position>0) {
                loadSpinnerRaca((String) parent.getItemAtPosition(position),null);
            }else{
                loadCleanListRacas();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void loadSpinnerRaca(String tipoAnimal,String raca){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<RacaAdapterToArray> lista=null;
                try {
                    lista= JsonUtil.jsonToList(new RacaAdapterToArray(), json,RacaAdapterToArray.class);
                    lista.add(0, new RacaAdapterToArray());
                    CriationUtil.createArrayAdapter(this.getContext(),spRacas, lista);
                    if(raca !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).getDescricao()!=null && lista.get(i).getDescricao().equalsIgnoreCase(raca)){
                                spRacas.setSelection(i);
                                break;
                            }
                        }

                    }
                    // colocado neste local para evitar conflito com o metodo loadAnimal
                    spTiposAnimais.setOnItemSelectedListener(new NovoAdocaoActivity.RacaListeners());

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

        }).execute("animal/racas/"+tipoAnimal);
    }

    public void loadCleanListRacas(){
        List<RacaAdapterToArray> lista=new ArrayList<RacaAdapterToArray>();

        lista.add(0, new RacaAdapterToArray());

        CriationUtil.createArrayAdapter(this,spRacas, lista);

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

    private String getValueRadioSexo(){
        if(radioMaxo.isChecked()){
            return "M";
        }else if(radioFemea.isChecked()){
            return "F";
        }else{
            return "";
        }
    }

    private void setValueRadioSexo(String value){
        if(value.trim().equalsIgnoreCase("M")){
            radioMaxo.setChecked(true);
        }else if(value.trim().equalsIgnoreCase("f")){
            radioFemea.setChecked(true);
        }

    }

    public void loadAnimal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)){





        }else {
            // colocado neste local para evitar conflito com o metodo loadAnimal
            spTiposAnimais.setOnItemSelectedListener(new NovoAdocaoActivity.RacaListeners());
        }
    }

    private void salvar(){

        CriationUtil.openProgressBar(progressDialog);
        adocao.setTipo(EnumTipoAnimal.getEnum(spTiposAnimais.getSelectedItem().toString()));
        adocao.setRaca(spRacas.getSelectedItem().toString());
        adocao.setFase(EnumFaseVida.getEnum( spFaixaIdade.getSelectedItem().toString()));
        adocao.setFlSexo(getValueRadioSexo());
        adocao.setCastrado(swCastrado.isChecked());
        adocao.setFlVacinado(swVacinado.isChecked());
        adocao.setFlVermifugado(swVermifugado.isChecked());
        adocao.setDescAdocao(txtaEdtMensagem.getText().toString());
        adocao.setDddCelular(Integer.parseInt(txtTelefone.getText().toString().substring(1,3)));
        adocao.setTelefoneCelular(Long.parseLong(txtTelefone.getText().toString().substring(4,15).trim().replace("-","")));
        adocao.setFotos(fotos);
        adocao.setDataCadastro(new Date());
        adocao.setUsuario(usuarioLogado);
           // faltando fotos

            new RequestPostEntity(new CallBack(this) {

                public void successWithReturn(String json) {

                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageSucess(getContext(), getContext().getString(R.string.sucessPetAdd));
                    finish();
                }

                public void successNoReturn() {
                }

                public void predictedError(MapErroRetornoRest map) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageWarning(this.getContext(), map.getMessage());
                    focusInCamposRetornoRest(map.getFields()[0]);

                }

            }).execute("adocao/salvar", adocao);



    }


    private void excluir(){

        CriationUtil.openProgressBar(progressDialog);

        // faltando fotos

        new RequestDelete(new CallBack(this) {

            public void successWithReturn(String json) {


            }

            public void successNoReturn() {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageSucess(getContext(), getContext().getString(R.string.sucessPetExcluir));
                finish();
            }

            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(this.getContext(), map.getMessage());
                if(map.getFields() !=null) {
                    focusInCamposRetornoRest(map.getFields()[0]);
                }

            }

        }).execute("adocao/"+adocao.getId());

    }

    private void adotado(){

        CriationUtil.openProgressBar(progressDialog);
        adocao.setDataAdocao(new Date());

        new RequestPostEntity(new CallBack(this) {

            public void successWithReturn(String json) {

                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageSucess(getContext(), getContext().getString(R.string.sucessPetAdd));
                finish();
            }

            public void successNoReturn() {
            }

            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(this.getContext(), map.getMessage());
                if(map.getFields() !=null) {
                    focusInCamposRetornoRest(map.getFields()[0]);
                }

            }

        }).execute("adocao/salvar", adocao);



    }


    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)) {
            adocao = (Adocao) bundle.getSerializable(AtributosUtil.PAR_ANIMAL);
        }
    }


}
