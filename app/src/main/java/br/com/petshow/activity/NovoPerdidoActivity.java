package br.com.petshow.activity;

import android.app.DatePickerDialog;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.petshow.R;
import br.com.petshow.adapters.BairroAdapterToArray;
import br.com.petshow.adapters.CidadeAdapterToArray;
import br.com.petshow.adapters.EstadoAdapterToArray;
import br.com.petshow.adapters.RacaAdapterToArray;
import br.com.petshow.enums.EnumCor;
import br.com.petshow.enums.EnumTipoAnimal;
import br.com.petshow.model.Bairro;
import br.com.petshow.model.Cidade;
import br.com.petshow.model.Estado;
import br.com.petshow.model.Perdido;
import br.com.petshow.model.Racas;
import br.com.petshow.model.Usuario;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.DateUtilsAndroid;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.ImagemUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.util.ValidationUtil;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.view.util.PhoneEditText;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestDelete;
import br.com.petshow.web.util.RequestListObjects;
import br.com.petshow.web.util.RequestPostEntity;

public class NovoPerdidoActivity extends PetActivity {

    ProgressDialog progressDialog;
    Usuario usuarioLogado;
    Profile profile;
    ImageView imageAnimal1;
    ImageView imageAnimal2;
    ImageView imageAnimal3;
    Spinner spTiposAnimais;
    Spinner spRacas;
    EditText txtTelefone;
    EditText txtaEdtMensagem;
    RadioButton radioMaxo;
    RadioButton radioFemea;
    RadioGroup radioGroupSexo;

    RadioButton radioPerdido;
    RadioButton radioAchado;
    RadioGroup radioGroupTipo;

    List<String> fotos;
    Perdido perdido;

    TextView lblEstadoAtual;
    Spinner spEstado;
    TextView lblCidadeAtual;
    Spinner spCidade;
    TextView lblBairro;
    Spinner spBairro;

    EditText txtdata;

    Spinner spCorPrincipal;
    Spinner spCorSegundaria;

    int chamadaChangeRestEstado;
    int chamadaChangeRestCidade;
    int chamadaChangeRestBairro;
    int chamadaChangeRestTipo;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadVariablesExternal();
        setContentView(R.layout.activity_novo_perdido);
        startComponents();
        startVariables();

        if(perdido!=null  && perdido.getId()>0) {
            loadAnimal();
        }else{
            loadSpinners(null, null,true,true);

        }

    }

    public void startVariables(){
        usuarioLogado = FacebookUtil.usuarioLogado;
        profile = Profile.getCurrentProfile();
        perdido= perdido==null?new Perdido():perdido;

    }
    public void startComponents() {

        MenuUtil.loadToolBar(this, R.id.toolbar_activity_novo_perdido);
        progressDialog = new ProgressDialog(this);

        imageAnimal1=(ImageView) findViewById(R.id.novoPerdido_image1);
        imageAnimal2=(ImageView) findViewById(R.id.novoPerdido_image2);
        imageAnimal3=(ImageView) findViewById(R.id.novoPerdido_image3);

        spTiposAnimais=(Spinner)  findViewById(R.id.novoPerdido_spTiposAnimais);
        spRacas=(Spinner) findViewById(R.id.novoPerdido_spRacas);

        txtTelefone=(PhoneEditText) findViewById(R.id.novoPerdido_txtTelefone);
        txtaEdtMensagem=(EditText) findViewById(R.id.novoPerdido_descPerdidoEncontrado);


        radioMaxo=(RadioButton)  findViewById(R.id.novoPerdido_rMacho);
        radioFemea=(RadioButton)  findViewById(R.id.novoPerdido_rFemea);
        radioGroupSexo=(RadioGroup)  findViewById(R.id.novoPerdido_radioGroupSexo);

        imageAnimal1.setOnClickListener(new NovoPerdidoActivity.ChangePictureListener());
        imageAnimal2.setOnClickListener(new NovoPerdidoActivity.ChangePictureListener());
        imageAnimal3.setOnClickListener(new NovoPerdidoActivity.ChangePictureListener());

        fotos = fotos==null?new ArrayList<String>():fotos;

        lblEstadoAtual=(TextView) findViewById(R.id.novoPerdido_lblEstadoAtual);
        spEstado=(Spinner) findViewById(R.id.novoPerdido_spEstado);

        lblCidadeAtual=(TextView) findViewById(R.id.novoPerdido_lblCidadeAtual);
        spCidade=(Spinner) findViewById(R.id.novoPerdido_spCidade);

        lblBairro=(TextView) findViewById(R.id.novoPerdido_lblBairroAtual);
        spBairro=(Spinner) findViewById(R.id.novoPerdido_spBairro);

        txtdata=(EditText) findViewById(R.id.novoPerdido_data);

        NovoPerdidoActivity.ExibeDataListener exibeDataListener =new NovoPerdidoActivity.ExibeDataListener();
        txtdata.setOnClickListener(exibeDataListener);
        txtdata.setOnFocusChangeListener(exibeDataListener);

        spCorPrincipal=(Spinner)  findViewById(R.id.novoPerdido_spCorPrincipal);
        spCorSegundaria=(Spinner)  findViewById(R.id.novoPerdido_spCorSegundaria);

        spCorPrincipal.setAdapter(new ArrayAdapter<EnumCor>(this, android.R.layout.simple_list_item_1, EnumCor.values()));

        spCorSegundaria.setAdapter(new ArrayAdapter<EnumCor>(this, android.R.layout.simple_list_item_1, EnumCor.values()));


        radioPerdido=(RadioButton)  findViewById(R.id.novoPerdido_Perdido);
        radioAchado=(RadioButton)  findViewById(R.id.novoPerdido_Achado);
        radioGroupTipo=(RadioGroup)  findViewById(R.id.novoPerdido_radioGroupTipo);

        spCidade.setOnItemSelectedListener( new NovoPerdidoActivity.CidadeListener(this));

        spEstado.setOnItemSelectedListener( new  NovoPerdidoActivity.EstadoListener(NovoPerdidoActivity.this));

        spTiposAnimais.setOnItemSelectedListener(new NovoPerdidoActivity.RacaListeners());

        loadFieldInvisible(null);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);
        this.menu= menu;
        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar));
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        if(perdido!=null && perdido.getId()>0){
            MenuItem itemMenu2= menu.add(0,3,3,getString(R.string.lblExcluir)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemMenu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            MenuItem itemMenu3= menu.add(0,2,2,getString(R.string.lblEncontradoDevolvido)); //   (int groupId,        int itemId,        int order,        int titleRes)
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
            onClickSalvar();
            return true;
        }
        if (id == 3) {
            excluir();
            return true;
        }
        if (id == 2) {
            finalizar();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void onClickSalvar() {

        if (perdido !=null && perdido.getId()>0 && perdido.getDtResolvido() !=null) {
            MessageUtil.messageWarning(this, this.getString(R.string.validacaoAnimalEntreguePerdido));
            // ValidationAndroidUtil.setFocus(this,spBairro);
            return;
        }


        if (spEstado.getSelectedItemPosition() == 0) {
            MessageUtil.messageWarning(this, this.getString(R.string.selEstado));
            //ValidationAndroidUtil.setFocus(this,spEstado);
            return;

        }
        if (spCidade.getSelectedItemPosition() == 0) {
            MessageUtil.messageWarning(this, this.getString(R.string.selCidade));
            //ValidationAndroidUtil.setFocus(this,spEstado);
            return;

        }
        if (spBairro.getSelectedItemPosition() == 0) {
            MessageUtil.messageWarning(this, this.getString(R.string.selBairro));
            //ValidationAndroidUtil.setFocus(this,spEstado);
            return;

        }
        if (spTiposAnimais.getSelectedItemPosition() == 0) {
            MessageUtil.messageWarning(this, this.getString(R.string.selAnimal));
            //ValidationAndroidUtil.setFocus(this,spEstado);
            return;

        }
        if (!ValidationUtil.isCampoComValor(txtdata.getText().toString())) {
            MessageUtil.messageWarning(this, this.getString(R.string.validacaoDataPerdidoAchado));
            // ValidationAndroidUtil.setFocus(this,spBairro);
            return;
        }
        if (!ValidationUtil.isCampoComValor(txtaEdtMensagem.getText().toString())) {
            MessageUtil.messageWarning(this, this.getString(R.string.validacaoDescPerdido));
            // ValidationAndroidUtil.setFocus(this,spBairro);
            return;
        }







        salvar();
    }

    private void loadSpinners(String descTipo,Long idEstado,boolean isCodeLoadTipo,boolean isCodeLoadEstado){

        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<String> lista=null;
                try {

                    lista= JsonUtil.jsonToList(new String(), json,String.class);

                    lista.add(0, getContext().getString(R.string.selAnimal));

                    CriationUtil.createArrayAdapter(this.getContext(),spTiposAnimais, lista);
                    if(descTipo !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).equals(descTipo)){
                                spTiposAnimais.setSelection(i);
                                if(isCodeLoadTipo)++chamadaChangeRestTipo;
                                break;
                            }
                        }

                    }



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


        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<EstadoAdapterToArray> lista=null;
                try {

                    lista=JsonUtil.jsonToList(new EstadoAdapterToArray(), json,EstadoAdapterToArray.class);

                    EstadoAdapterToArray objSelecione = new EstadoAdapterToArray();
                    objSelecione.setNome(getContext().getString(R.string.selEstado));
                    lista.add(0, objSelecione);

                    CriationUtil.createArrayAdapter(this.getContext(),spEstado, lista);

                    if(idEstado !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).getId() == idEstado){
                                spEstado.setSelection(i);
                                if(isCodeLoadEstado)++chamadaChangeRestEstado;
                                break;
                            }
                        }

                    }


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

    private void loadSpinnerCidade(Estado estadoSelecionado,Long idCidade,boolean isCodeLoad){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<CidadeAdapterToArray> lista=null;
                try {
                    lista=JsonUtil.jsonToList(new CidadeAdapterToArray(), json,CidadeAdapterToArray.class);
                    CidadeAdapterToArray objSelecione = new CidadeAdapterToArray();
                    objSelecione.setNome(getContext().getString(R.string.selCidade));

                    lista.add(0, objSelecione);
                    CriationUtil.createArrayAdapter(this.getContext(),spCidade, lista);
                    if(idCidade !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).getId() == idCidade){
                                spCidade.setSelection(i);
                                if(isCodeLoad)++chamadaChangeRestCidade;
                                break;
                            }
                        }

                    }



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
    private void loadSpinnerBairros(Cidade cidadeSelecionado,Long idBairro,boolean isCodeLoad) {
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json) {
                List<BairroAdapterToArray> lista = null;
                try {
                    lista = JsonUtil.jsonToList(new BairroAdapterToArray(), json, BairroAdapterToArray.class);

                    BairroAdapterToArray objSelecione = new BairroAdapterToArray();
                    objSelecione.setNome(getContext().getString(R.string.selBairro));
                    lista.add(0, objSelecione);
                    CriationUtil.createArrayAdapter(this.getContext(), spBairro, lista);
                    if(idBairro !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).getId() == idBairro){
                                spBairro.setSelection(i);
                                if(isCodeLoad)++chamadaChangeRestBairro;
                                break;
                            }
                        }

                    }


                } catch (Exception e) {

                    MessageUtil.messageErro(this.getContext(), e.getMessage());
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
            }

            public void successNoReturn() {
            }

            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(this.getContext(), map.getMessage());

            }

        }).execute("endereco/consulta/bairro/cidade/" + cidadeSelecionado.getId());

    }
    public void showOptionsPhoto (){

        android.support.v7.app.AlertDialog.Builder ad = new android.support.v7.app.AlertDialog.Builder(this);
        ad.setTitle(R.string.title_modal_tipo_get_photo);

        ad.setSingleChoiceItems(R.array.list_type_get_photo,-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    NavegationUtil.openGalleryPhoto(NovoPerdidoActivity.this);
                    dialog.dismiss();
                }
                if(which==1){
                    NavegationUtil.openCamera(NovoPerdidoActivity.this);
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
        String a="";

    }

    private class RacaListeners implements AdapterView.OnItemSelectedListener {


        public RacaListeners(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(chamadaChangeRestTipo ==0) {
                if (position > 0) {
                    loadSpinnerRaca((String) parent.getItemAtPosition(position), null);
                } else {
                    loadCleanListRacas();
                }
            }else{
                --chamadaChangeRestTipo;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void loadSpinnerRaca(String tipoAnimal,Long raca){
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

                            if(lista.get(i).getId()==raca){
                                spRacas.setSelection(i);
                                break;
                            }
                        }

                    }



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

        if(perdido != null && perdido.getId()>0){
            txtTelefone.setText(perdido.getDddCelular()+""+perdido.getTelefoneCelular());
            txtaEdtMensagem.setText(perdido.getDescAcontecimento());
            setValueRadioSexo(perdido.getFlSexo() );
            setValueRadioTipo(perdido.getFlAcontecimento());
            loadSpinners(perdido.getTpAnimal().toString(),perdido.getEstado().getId(),true,true);
            loadSpinnerCidade(perdido.getEstado(),perdido.getCidade().getId(),true);
            loadSpinnerBairros(perdido.getCidade(),perdido.getBairro().getId(),true);
            setSpinnerCor(spCorPrincipal,perdido.getTpCorPrincipal().getId(),perdido.getTpCorPrincipal().toString());
            setSpinnerCor(spCorSegundaria,perdido.getTpCorSegundaria().getId(),perdido.getTpCorSegundaria().toString());
            loadSpinnerRaca(perdido.getTpAnimal().toString(),perdido.getRaca().getId());


            Calendar cal = Calendar.getInstance();
            cal.setTime(perdido.getDtPerdidoAchado());
            String dt = DateUtilsAndroid.dateToString(  cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
            txtdata.setText(dt);

            this.fotos=perdido.getFotos();
            if(fotos!=null && fotos.size()>0){
                imageAnimal1.setImageBitmap(ImagemUtil.transformBase64Bitmap(fotos.get(0)));
                if(fotos.size()>1){
                    imageAnimal2.setImageBitmap(ImagemUtil.transformBase64Bitmap(fotos.get(1)));
                    if(fotos.size()>2){
                        imageAnimal3.setImageBitmap(ImagemUtil.transformBase64Bitmap(fotos.get(2)));
                    }
                }
            }

        }else {
            // colocado neste local para evitar conflito com o metodo loadAnimal
            spTiposAnimais.setOnItemSelectedListener(new NovoPerdidoActivity.RacaListeners());
        }
    }

    private void salvar(){

        CriationUtil.openProgressBar(progressDialog);

        if(perdido.getId()==0) {
            perdido.setDataCadastro(new Date());
        }
        perdido.setDddCelular(Integer.parseInt(txtTelefone.getText().toString().substring(1,3)));
        perdido.setTelefoneCelular(Long.parseLong(txtTelefone.getText().toString().substring(4,15).trim().replace("-","")));
        perdido.setDescAcontecimento(txtaEdtMensagem.getText().toString());
        perdido.setFlAcontecimento(getValueRadioTipo());
        perdido.setBairro(((Bairro) spBairro.getSelectedItem()));
        perdido.setCidade(((Cidade) spCidade.getSelectedItem()));
        perdido.setEstado(((Estado) spEstado.getSelectedItem()));
        perdido.setTpAnimal(EnumTipoAnimal.getEnum(spTiposAnimais.getSelectedItem().toString()));
        //perdido.setNome();
        perdido.setTpCorPrincipal(EnumCor.getEnum(spCorPrincipal.getSelectedItem().toString()));
        perdido.setTpCorSegundaria(EnumCor.getEnum(spCorSegundaria.getSelectedItem().toString()));
        perdido.setFlSexo( getValueRadioSexo());
        if(spRacas.getSelectedItem() !=null && ((Racas) spRacas.getSelectedItem()).getId()>0)
            perdido.setRaca(((Racas) spRacas.getSelectedItem()));
        perdido.setFotos(fotos);
        perdido.setUsuario(usuarioLogado);



        new RequestPostEntity(new CallBack(this) {

            public void successWithReturn(String json) {

                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageSucess(getContext(), getContext().getString(R.string.sucessPetAdd));
                try {
                    perdido=  JsonUtil.transformObject(json,Perdido.class);
                    onCreateOptionsMenu(menu);
                } catch (IOException e) {
                    e.printStackTrace();
                }


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

        }).execute("animal/perdido/salvar", perdido);



    }

    private class SelecionaDataListener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtdata.setText(dt);
            perdido.setDtPerdidoAchado(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));

        }
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


    private class EstadoListener implements AdapterView.OnItemSelectedListener {

        NovoPerdidoActivity novoPerdidoActivity =null;
        public EstadoListener(NovoPerdidoActivity novoPerdidoActivity){
            this.novoPerdidoActivity=novoPerdidoActivity;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(chamadaChangeRestEstado==0) {
                if (position > 0) {
                    loadSpinnerCidade((Estado) parent.getItemAtPosition(position), null,false);
                } else {
                    novoPerdidoActivity.loadCleanListCidade();
                    novoPerdidoActivity.loadCleanListBairro();
                }
            }else{
                --chamadaChangeRestEstado;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CidadeListener implements AdapterView.OnItemSelectedListener {

        NovoPerdidoActivity novoPerdidoActivity =null;
        public CidadeListener(NovoPerdidoActivity novoPerdidoActivity){
            this.novoPerdidoActivity=novoPerdidoActivity;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(chamadaChangeRestCidade==0) {
                if (position > 0) {
                    loadSpinnerBairros((Cidade) parent.getItemAtPosition(position), null,false);
                } else {
                    novoPerdidoActivity.loadCleanListBairro();
                }
            }else{
                --chamadaChangeRestCidade;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
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

    private  void loadFieldInvisible(Perdido perdido){

//        if(usuario.getBairro()!=null && !perdido.getBairro().trim().equals("")){
//            lblBairroAtual.setVisibility(View.VISIBLE);
//            lblCidadeAtual.setVisibility(View.VISIBLE);
//            lblEstadoAtual.setVisibility(View.VISIBLE);
//            lblEstadoAtual.setText(perdido.getEstado());
//            lblCidadeAtual.setText(perdido.getCidade());
//            lblBairroAtual.setText(perdido.getBairro());
//            txtTelefone.setText(perdido.getDdd()+""+perdido.getTelefone());
//        }else{
            spEstado.setVisibility(View.VISIBLE);
            spCidade.setVisibility(View.VISIBLE);
            spBairro.setVisibility(View.VISIBLE);
            //loadCleanListCidade();
            //loadCleanListBairro();

 //       }


    }

    private String getValueRadioTipo(){
        String retorno="";
        if(radioPerdido.isChecked()){
            retorno="P";
        }else if(radioAchado.isChecked()){
            retorno="A";
        }
        return retorno;
    }

    private void setValueRadioTipo(String value){
        if(value.trim().equalsIgnoreCase("P")){
            radioPerdido.setChecked(true);
        }else if(value.trim().equalsIgnoreCase("A")){
            radioAchado.setChecked(true);
        }

    }

    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_PERDIDO)) {
            perdido = (Perdido) bundle.getSerializable(AtributosUtil.PAR_PERDIDO);
        }
    }

    private class ExibeDataListener implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                exibeData();
            }
        }
    }

    private void exibeData(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new NovoPerdidoActivity.SelecionaDataListener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();

    }

    private void setSpinnerCor(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumCor enume = (EnumCor) adapter.getItem(i);
                if(enume.getId()== id && desc.equals(enume.toString())){
                    sp.setSelection(i);
                }
            }catch (ClassCastException ex){

            }


        }

    }

    private void finalizar(){
        this.perdido.setDtResolvido(new Date());
        CriationUtil.openProgressBar(progressDialog);

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

        }).execute("animal/perdido/salvar", perdido);


    }

    private void excluir(){

        CriationUtil.openProgressBar(progressDialog);



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

        }).execute("animal/perdido/"+perdido.getId());

    }


}
