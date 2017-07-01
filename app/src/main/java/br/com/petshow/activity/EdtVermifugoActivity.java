package br.com.petshow.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import br.com.petshow.R;
import br.com.petshow.enums.EnumFrequenciaTratamento;
import br.com.petshow.enums.EnumFrequenciaVermifugacao;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Tratamento;
import br.com.petshow.model.Vermifugo;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.DateUtil;
import br.com.petshow.util.DateUtilsAndroid;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestDelete;
import br.com.petshow.web.util.RequestGetEntity;
import br.com.petshow.web.util.RequestPostEntity;

public class EdtVermifugoActivity extends PetActivity {

    Animal animal;

    ProgressDialog progressDialog;
    Vermifugo vermifugo;
    int isCodeLoadDt1;
    int isCodeLoadDtReforco;
    Spinner spFrequencia;
    EditText txtDt1Dose;
    Switch swTomou1;
    EditText txtDt2Dose;
    Switch swTomou2;
    TextView lbl1DoseDt;
    TextView lbl2DoseDt;
    TextView lblVer;

    boolean isAtualizaDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_vermifugo);
        ++isCodeLoadDt1;

        loadVariablesExternal();
        startComponents();
        startVariables();

        loadVermifugoRest();



    }
    public void startVariables(){
        vermifugo= vermifugo==null?new Vermifugo():vermifugo;

    }
    public void startComponents() {

        MenuUtil.loadToolBar(this, R.id.toolbar_activity_edt_vermifugo);
        progressDialog = new ProgressDialog(this);

        spFrequencia=(Spinner)  findViewById(R.id.edtVermifugo_spFrequencia);
        txtDt1Dose=(EditText)  findViewById(R.id.edtVermifugo_txtDt1Dose);
        swTomou1=(Switch)  findViewById(R.id.edtVermifugo_swTomou1);
        txtDt2Dose=(EditText)         findViewById(R.id.edtVermifugo_txtDt2Dose);
        swTomou2=(Switch)  findViewById(R.id.edtVermifugo_swTomou2);
        lbl1DoseDt=(TextView)  findViewById(R.id.edtVermifugo_lbl1DoseDt);
        lbl2DoseDt=(TextView)   findViewById(R.id.edtVermifugo_lbl2DoseDt);
        lblVer=(TextView)   findViewById(R.id.edtVermifugo_lblVer);
        spFrequencia.setAdapter(new ArrayAdapter<EnumFrequenciaVermifugacao>(this, android.R.layout.simple_list_item_1, EnumFrequenciaVermifugacao.values()));

        ExibeData1Dose dt1 =new ExibeData1Dose();
        txtDt1Dose.setOnClickListener(dt1);
        txtDt1Dose.setOnFocusChangeListener(dt1);

        ExibeDataReforco dt2 =new ExibeDataReforco();
        txtDt2Dose.setOnClickListener(dt2);
        txtDt2Dose.setOnFocusChangeListener(dt2);

        spFrequencia.setOnItemSelectedListener(new FrequenciaListeners());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);
        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar));
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        if(vermifugo!=null && vermifugo.getId()>0){
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
        salvar();
    }

    public void loadVermifugoRest(){

        CriationUtil.openProgressBar(progressDialog);
        new RequestGetEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {

                try {
                    if(json !=null){
                        vermifugo=  JsonUtil.transformObject(json,Vermifugo.class);
                        loadVermifugo();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {

                MessageUtil.messageWarning(this.getContext(), map.getMessage());
                if(map.getFields() !=null) {
                    focusInCamposRetornoRest(map.getFields()[0]);
                }
                CriationUtil.closeProgressBar(progressDialog);
            }
        }).execute("animal/vermifugo/animal/"+animal.getId());



    }

    public void loadVermifugo(){

        Calendar cal = Calendar.getInstance();
        cal.setTime(vermifugo.getDtVermifugoDose1());
        String dt = DateUtilsAndroid.dateToString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        txtDt1Dose.setText(dt);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(vermifugo.getDtVermifugoDoseReforco());
        String dt2 = DateUtilsAndroid.dateToString(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        txtDt2Dose.setText(dt2);

        if (vermifugo.getDtVermifugoDose1Ant() != null) {
            Calendar calAnt1 = Calendar.getInstance();
            calAnt1.setTime(vermifugo.getDtVermifugoDose1Ant());
            lbl1DoseDt.setText("1º Dose:" + DateUtilsAndroid.dateTo_ddMMYYYY(calAnt1.getTime()));
            lblVer.setVisibility(View.VISIBLE);
            lbl1DoseDt.setVisibility(View.VISIBLE);
        }

        if (vermifugo.getDtVermifugoDoseReforcoAnt() != null) {
            Calendar calAnt2 = Calendar.getInstance();
            calAnt2.setTime(vermifugo.getDtVermifugoDoseReforcoAnt());
            lbl2DoseDt.setText("2º Dose(Reforço):" + DateUtilsAndroid.dateTo_ddMMYYYY(calAnt2.getTime()));
            lblVer.setVisibility(View.VISIBLE);
            lbl2DoseDt.setVisibility(View.VISIBLE);
        }

        setSpinnerFrequencia(spFrequencia, vermifugo.getFrequencia().getId(), vermifugo.getFrequencia().toString());
        swTomou1.setChecked(vermifugo.isTomou1());
        swTomou2.setChecked(vermifugo.isReforco());
    }

    private void setSpinnerFrequencia(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumFrequenciaVermifugacao enume = (EnumFrequenciaVermifugacao) adapter.getItem(i);
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

        }).execute("animal/vermifugo/"+vermifugo.getId());

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

        if(vermifugo.getId()==0) {

            vermifugo.setAnimal(animal);
        }

        vermifugo.setFrequencia(EnumFrequenciaVermifugacao.getEnum(spFrequencia.getSelectedItem().toString()));
        vermifugo.setTomou1(swTomou1.isChecked());
        vermifugo.setTomouReforco(swTomou2.isChecked());
        if(swTomou1.isChecked() && swTomou2.isChecked()) isAtualizaDatas=true;

        new RequestPostEntity(new CallBack(this) {

            public void successWithReturn(String json) {

                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageSucess(getContext(), getContext().getString(R.string.sucessVermifugoAdd));
                try {
                    vermifugo=  JsonUtil.transformObject(json,Vermifugo.class);
                    loadVermifugo();
                    if(isAtualizaDatas) {
                        CriationUtil.openAlertDialog(getContext(), R.string.msgTelaSalvarVermifugo, R.string.title_alert_aviso);
                        isAtualizaDatas=false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



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

        }).execute("animal/vermifugo/salvar", vermifugo);



    }

    private class ExibeData1Dose implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            exibeData1Dose();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(isCodeLoadDt1==0) {
            if (hasFocus) {
                exibeData1Dose();
            }
            }else{
               --isCodeLoadDt1;
            }
        }
    }
    private class SelecionaData1Listener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtDt1Dose.setText(dt);
            vermifugo.setDtVermifugoDose1(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));



        }
    }
    private void exibeData1Dose(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new EdtVermifugoActivity.SelecionaData1Listener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();

    }

    private class ExibeDataReforco implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            exibeDataReforco();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(isCodeLoadDtReforco==0) {
                if (hasFocus) {
                    exibeDataReforco();
                }
            }else{
                --isCodeLoadDtReforco;
            }
        }
    }
    private class SelecionaDataReforcoListener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtDt2Dose.setText(dt);
            vermifugo.setDtVermifugoDoseReforco(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));



        }
    }
    private void exibeDataReforco(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new EdtVermifugoActivity.SelecionaDataReforcoListener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();

    }

    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)) {
            animal = (Animal) bundle.getSerializable(AtributosUtil.PAR_ANIMAL);
        }

    }

    private class FrequenciaListeners implements AdapterView.OnItemSelectedListener {


        public FrequenciaListeners(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EnumFrequenciaVermifugacao enumF =(EnumFrequenciaVermifugacao) parent.getItemAtPosition(position);
            if(vermifugo !=null && vermifugo.getDtVermifugoDose1Ant() !=null){
                Date dateNovaDose1 = new Date();
                dateNovaDose1.setTime(vermifugo.getDtVermifugoDose1Ant().getTime());
                dateNovaDose1= DateUtil.addDays(dateNovaDose1, DateUtil.getDaysFrequenciaVermifugo(enumF));
                vermifugo.setDtVermifugoDose1(dateNovaDose1);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateNovaDose1);
                String dt = DateUtilsAndroid.dateToString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                txtDt1Dose.setText(dt);

                Date dateNovaReforco = new Date();
                dateNovaReforco.setTime(vermifugo.getDtVermifugoDose1().getTime());
                dateNovaReforco=DateUtil.addDays(dateNovaReforco, 15);
                vermifugo.setDtVermifugoDoseReforco(dateNovaReforco);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(dateNovaReforco);
                String dt2 = DateUtilsAndroid.dateToString(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));
                txtDt2Dose.setText(dt2);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
