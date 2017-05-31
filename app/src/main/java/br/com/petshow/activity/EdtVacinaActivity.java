package br.com.petshow.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import br.com.petshow.R;
import br.com.petshow.enums.EnumVacina;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Vacina;
import br.com.petshow.role.VacinaRole;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.DateUtil;
import br.com.petshow.util.DateUtilsAndroid;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.util.ValidationAndroidUtil;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestDelete;
import br.com.petshow.web.util.RequestPostEntity;

import static br.com.petshow.util.FacebookUtil.usuarioLogado;

public class EdtVacinaActivity extends PetActivity {

    ProgressDialog progressDialog;

    Spinner spTipos;
    EditText txtdata;
    Switch swichVacinada;
    TextView txtCadastro;
    EditText txtdataPrevisao;
    TextView txtDesc;
    Vacina vacina;
    Animal animal;

    int isCodeLoadPrevista=0;
    int isCodeLoadVacina=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // fazer pergunta sobre cadastrar a proxima...
        // validar se pode tomar a vacina de acordo com as anteriores? é necessario?

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_vacina);
        ++isCodeLoadPrevista;
        ++isCodeLoadVacina;
        loadVariablesExternal();
        startComponents();
        startVariables();

        if(vacina!=null  && vacina.getId()>0) {
            loadVacina();
            spTipos.setEnabled(false);
        }else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String dt = DateUtilsAndroid.dateToString(  cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
            txtdata.setText(dt);
            vacina.setData(new Date());
            loadDadosVacina();
        }

    }
    public void startVariables(){
       vacina= vacina==null?new Vacina():vacina;

    }

    public void startComponents() {

        MenuUtil.loadToolBar(this, R.id.toolbar_activity_edt_vacina);
        progressDialog = new ProgressDialog(this);

        spTipos=(Spinner) findViewById(R.id.edtVacina_spTipos);
        txtdata=(EditText) findViewById(R.id.edtVacina_Data);
        txtCadastro=(TextView) findViewById(R.id.edtVacina_txtCadastro);
        swichVacinada=(Switch) findViewById(R.id.edtVacina_swAplicada);
        txtdataPrevisao=(EditText) findViewById(R.id.edtVacina_DataPrevisao);
        txtDesc =(TextView) findViewById(R.id.edtVacina_txtDesc);

        spTipos.setAdapter(new ArrayAdapter<EnumVacina>(this, android.R.layout.simple_list_item_1, EnumVacina.values()));
        spTipos.setFocusable(true);
        ValidationAndroidUtil.setFocus(this,spTipos);

        EdtVacinaActivity.ExibeDataListener exibeDataListener =new EdtVacinaActivity.ExibeDataListener();
        txtdata.setOnClickListener(exibeDataListener);
        txtdata.setOnFocusChangeListener(exibeDataListener);

        EdtVacinaActivity.ExibeDataPrevisaoListener exibeDataPrevisaoListener =new EdtVacinaActivity.ExibeDataPrevisaoListener();
        txtdataPrevisao.setOnClickListener(exibeDataPrevisaoListener);
        txtdataPrevisao.setOnFocusChangeListener(exibeDataPrevisaoListener);

        spTipos.setOnItemSelectedListener(new EdtVacinaActivity.VacinaListeners());

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);
        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar));
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        if(vacina!=null && vacina.getId()>0){
            MenuItem itemMenu2= menu.add(0,3,3,getString(R.string.lblExcluir)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemMenu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

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




        return super.onOptionsItemSelected(item);
    }


    private void onClickSalvar() {

//        if (perdido !=null && perdido.getId()>0 && perdido.getDtResolvido() !=null) {
//            MessageUtil.messageWarning(this, this.getString(R.string.validacaoAnimalEntreguePerdido));
//            // ValidationAndroidUtil.setFocus(this,spBairro);
//            return;
//        }
//
//
//        if (spEstado.getSelectedItemPosition() == 0) {
//            MessageUtil.messageWarning(this, this.getString(R.string.selEstado));
//            //ValidationAndroidUtil.setFocus(this,spEstado);
//            return;
//
//        }
//        if (spCidade.getSelectedItemPosition() == 0) {
//            MessageUtil.messageWarning(this, this.getString(R.string.selCidade));
//            //ValidationAndroidUtil.setFocus(this,spEstado);
//            return;
//
//        }
//        if (spBairro.getSelectedItemPosition() == 0) {
//            MessageUtil.messageWarning(this, this.getString(R.string.selBairro));
//            //ValidationAndroidUtil.setFocus(this,spEstado);
//            return;
//
//        }
//        if (spTiposAnimais.getSelectedItemPosition() == 0) {
//            MessageUtil.messageWarning(this, this.getString(R.string.selAnimal));
//            //ValidationAndroidUtil.setFocus(this,spEstado);
//            return;
//
//        }
//        if (!ValidationUtil.isCampoComValor(txtdata.getText().toString())) {
//            MessageUtil.messageWarning(this, this.getString(R.string.validacaoDataPerdidoAchado));
//            // ValidationAndroidUtil.setFocus(this,spBairro);
//            return;
//        }
//        if (!ValidationUtil.isCampoComValor(txtaEdtMensagem.getText().toString())) {
//            MessageUtil.messageWarning(this, this.getString(R.string.validacaoDescPerdido));
//            // ValidationAndroidUtil.setFocus(this,spBairro);
//            return;
//        }



        salvar();
    }

    public void loadVacina(){

            if(vacina.getEstabelecimento().getId()!=usuarioLogado.getId()) {
                txtCadastro.setText(getString(R.string.lblCadastradoPor) + vacina.getEstabelecimento().getNome());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(vacina.getData());
            String dt = DateUtilsAndroid.dateToString(  cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
            txtdata.setText(dt);
            setSpinnerVacina(spTipos,vacina.getTpVacina().getId(),vacina.getTpVacina().toString());



        }


    private void setSpinnerVacina(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumVacina enume = (EnumVacina) adapter.getItem(i);
                if(enume.getId()== id && desc.equals(enume.toString())){
                    sp.setSelection(i);
                }
            }catch (ClassCastException ex){

            }


        }

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

        }).execute("animal/vacina/"+vacina.getId());

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


    private void salvar(){

        CriationUtil.openProgressBar(progressDialog);

        if(vacina.getId()==0) {
            vacina.setEstabelecimento(usuarioLogado);
            vacina.setAnimal(animal);
        }
        vacina.setTpVacina(EnumVacina.getEnum(spTipos.getSelectedItem().toString()));
        vacina.setFlAplicada(swichVacinada.isChecked());





        new RequestPostEntity(new CallBack(this) {

            public void successWithReturn(String json) {

                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageSucess(getContext(), getContext().getString(R.string.sucessVacinaAdd));
                try {
                    vacina=  JsonUtil.transformObject(json,Vacina.class);

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

        }).execute("animal/vacina/salvar", vacina);



    }

    private class ExibeDataListener implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            exibeData();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(isCodeLoadVacina==0) {
                if (hasFocus) {
                    exibeData();
                }
            }else{
                --isCodeLoadVacina;
            }
        }
    }
    private class SelecionaDataListener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtdata.setText(dt);
            vacina.setData(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));
            loadDadosVacina();

        }
    }
    private void exibeData(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new EdtVacinaActivity.SelecionaDataListener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();

    }



    private class ExibeDataPrevisaoListener implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            exibeDataPrevisao();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if(hasFocus){
                exibeDataPrevisao();
            }
        }
    }
    private class SelecionaDataPrevisaoListener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtdataPrevisao.setText(dt);
            vacina.setPrevisaoProxima(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));


        }
    }
    private void exibeDataPrevisao(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new EdtVacinaActivity.SelecionaDataPrevisaoListener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();

    }


    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)) {
            animal = (Animal) bundle.getSerializable(AtributosUtil.PAR_ANIMAL);
        }
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_VACINA)) {
            vacina = (Vacina) bundle.getSerializable(AtributosUtil.PAR_VACINA);
        }
    }

    public void loadDadosVacina(){
        Date data=vacina.getData();
        HashMap<String,String> dados= new VacinaRole().getRegrasVacina(EnumVacina.getEnum(spTipos.getSelectedItem().toString()),data);
        if(data !=null){
            txtDesc.setText(dados.get("desc"));
            String dt = DateUtilsAndroid.dateToString(   Integer.parseInt(dados.get("dtProx").substring(6,10)),  Integer.parseInt(dados.get("dtProx").substring(3,5))-1, Integer.parseInt(dados.get("dtProx").substring(0,2)));
            txtdataPrevisao.setText(dt);
            vacina.setPrevisaoProxima(DateUtilsAndroid.getDate(Integer.parseInt(dados.get("dtProx").substring(6,10)),  Integer.parseInt(dados.get("dtProx").substring(3,5))-1, Integer.parseInt(dados.get("dtProx").substring(0,2))));
        }else{
            txtDesc.setText(dados.get("desc"));
        }
    }

    private class VacinaListeners implements AdapterView.OnItemSelectedListener {


        public VacinaListeners(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            loadDadosVacina();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
