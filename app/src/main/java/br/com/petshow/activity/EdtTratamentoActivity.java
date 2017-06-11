package br.com.petshow.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import br.com.petshow.R;
import br.com.petshow.enums.EnumFrequenciaTratamento;
import br.com.petshow.enums.EnumVacina;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Tratamento;
import br.com.petshow.model.Vacina;
import br.com.petshow.util.AtributosUtil;
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

public class EdtTratamentoActivity extends PetActivity {

    EditText txtNome;//=(EditText) edtTratamento_txtNome
    Spinner spFrequencia;        //edtTratamento_spFrequencia
    EditText  txtDtInicio; // edtTratamento_txtDtInicio
    EditText   txtDtFim;  //   edtTratamento_txtDtFim
    ImageView imgCalendario;
    TextView   vAte;
    EditText   txtHR;

    Animal animal;
    Tratamento tratamento;
    ProgressDialog progressDialog;
    int isCodeLoadDtInicio;
    int isCodeLoadDtFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_tratamento);
        ++isCodeLoadDtInicio;
        ++isCodeLoadDtFim;
        loadVariablesExternal();
        startComponents();
        startVariables();

        if(tratamento!=null  && tratamento.getId()>0) {
            loadTratamento();

        }else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String dt = DateUtilsAndroid.dateToString(  cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
            txtDtInicio.setText(dt);
            txtDtFim.setText(dt);
            tratamento.setDataInicio(new Date());
            tratamento.setDataTermino(new Date());

        }
    }
    public void startVariables(){
        tratamento= tratamento==null?new Tratamento():tratamento;

    }
    public void startComponents() {

        MenuUtil.loadToolBar(this, R.id.toolbar_activity_edt_tratamento);
        progressDialog = new ProgressDialog(this);

        spFrequencia=(Spinner) findViewById(R.id.edtTratamento_spFrequencia);
        txtNome=(EditText) findViewById(R.id.edtTratamento_txtNome);
        txtDtInicio=(EditText) findViewById(R.id.edtTratamento_txtDtInicio);
        txtDtFim=(EditText) findViewById(R.id.edtTratamento_txtDtFim);
        imgCalendario =(ImageView)  findViewById(R.id.edtTratamento_imgData);
        vAte =(TextView)   findViewById(R.id.edtTratamento_lblAte);
        txtHR =(EditText)   findViewById(R.id.edtTratamento_txtHR);

        spFrequencia.setAdapter(new ArrayAdapter<EnumFrequenciaTratamento>(this, android.R.layout.simple_list_item_1, EnumFrequenciaTratamento.values()));

        EdtTratamentoActivity.ExibeDataInicio exibeDataInicio =new EdtTratamentoActivity.ExibeDataInicio();
        txtDtInicio.setOnClickListener(exibeDataInicio);
        txtDtInicio.setOnFocusChangeListener(exibeDataInicio);

        EdtTratamentoActivity.ExibeDataFim exibeDataFim =new EdtTratamentoActivity.ExibeDataFim();
        txtDtFim.setOnClickListener(exibeDataFim);
        txtDtFim.setOnFocusChangeListener(exibeDataFim);
        spFrequencia.setOnItemSelectedListener(new FrequenciaListeners());

        ExibeHora exibeHora= new ExibeHora();
        txtHR.setOnClickListener(exibeHora);
        txtHR.setOnFocusChangeListener(exibeHora);






    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);
        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar));
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        if(tratamento!=null && tratamento.getId()>0){
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
    public void loadTratamento(){


        Calendar cal = Calendar.getInstance();
        cal.setTime(tratamento.getDataInicio());
        String dt = DateUtilsAndroid.dateToString(  cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
        txtDtInicio.setText(dt);

        Calendar calFim = Calendar.getInstance();
        calFim.setTime(tratamento.getDataTermino());
        String dtFim = DateUtilsAndroid.dateToString(  calFim.get(Calendar.YEAR),  calFim.get(Calendar.MONTH),  calFim.get(Calendar.DAY_OF_MONTH));
        txtDtInicio.setText(dtFim);
        setSpinnerFrequencia(spFrequencia,tratamento.getFrequencia().getId(),tratamento.getFrequencia().toString());
         txtDtInicio.setText(DateUtilsAndroid.hrToString(tratamento.getHrTratamento()));


    }

    private void setSpinnerFrequencia(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumFrequenciaTratamento enume = (EnumFrequenciaTratamento) adapter.getItem(i);
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

        }).execute("animal/tratamento/"+tratamento.getId());

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

        if(tratamento.getId()==0) {

            tratamento.setAnimal(animal);
        }
        tratamento.setNm_tratamento(txtNome.getText().toString());
        tratamento.setFrequencia(EnumFrequenciaTratamento.getEnum(spFrequencia.getSelectedItem().toString()));






        new RequestPostEntity(new CallBack(this) {

            public void successWithReturn(String json) {

                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageSucess(getContext(), getContext().getString(R.string.sucessVacinaAdd));
                try {
                    tratamento=  JsonUtil.transformObject(json,Tratamento.class);

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

        }).execute("animal/tratamento/salvar", tratamento);



    }

    private class ExibeDataInicio implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            exibeDataInicio();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //if(isCodeLoadDtInicio==0) {
                if (hasFocus) {
                    exibeDataInicio();
                }
            //}else{
             //   --isCodeLoadDtInicio;
            //}
        }
    }
    private class SelecionaDataInicioListener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtDtInicio.setText(dt);
            tratamento.setDataInicio(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));



        }
    }
    private void exibeDataInicio(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new EdtTratamentoActivity.SelecionaDataInicioListener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();

    }


    private class ExibeDataFim implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            exibeDataFim();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if(hasFocus){
                exibeDataFim();
            }
        }
    }
    private class SelecionaDataFimListener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtDtFim.setText(dt);

            tratamento.setDataTermino(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));

        }
    }
    private void exibeDataFim(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new EdtTratamentoActivity.SelecionaDataFimListener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();


    }
    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)) {
            animal = (Animal) bundle.getSerializable(AtributosUtil.PAR_ANIMAL);
        }
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_TRATAMENTO)) {
            tratamento = (Tratamento) bundle.getSerializable(AtributosUtil.PAR_TRATAMENTO);
        }
    }

    private class FrequenciaListeners implements AdapterView.OnItemSelectedListener {


        public FrequenciaListeners(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EnumFrequenciaTratamento enumF =(EnumFrequenciaTratamento) parent.getItemAtPosition(position);
            if(enumF.getId()>= 50){
                txtDtFim.setVisibility(View.INVISIBLE);
                txtDtInicio.setVisibility(View.INVISIBLE);
                imgCalendario.setVisibility(View.INVISIBLE);
                vAte.setVisibility(View.INVISIBLE);
            }else{
                txtDtFim.setVisibility(View.VISIBLE);
                txtDtInicio.setVisibility(View.VISIBLE);
                imgCalendario.setVisibility(View.VISIBLE);
                vAte.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

//    public static class TimePickerFragment extends DialogFragment
//            implements TimePickerDialog.OnTimeSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current time as the default values for the picker
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));
//        }
//
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            // Do something with the time chosen by the user
//        }
//    }
//
//    private class ExibeHora implements  View.OnClickListener, View.OnFocusChangeListener{
//
//
//        @Override
//        public void onClick(View v) {
//            DialogFragment newFragment = new TimePickerFragment();
//            newFragment.show(, "timePicker");
//            newFragment.
//        }
//
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//
//            if(hasFocus){
//                exibeDataFim();
//            }
//        }
//    }

    private class ExibeHora implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(EdtTratamentoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String hora=""+selectedHour;
                    String minuto=""+selectedMinute;
                    if(selectedHour <10){
                        hora="0"+hora;
                    }
                    if(selectedMinute <10){
                        minuto="0"+minuto;
                    }
                    txtHR.setText( hora + ":" + minuto);
                    tratamento.setHrTratamento(DateUtilsAndroid.getTime(selectedHour,selectedMinute));
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //if(isCodeLoadDtInicio==0) {
            if (hasFocus) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EdtTratamentoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hora=""+selectedHour;
                        String minuto=""+selectedMinute;
                        if(selectedHour <10){
                            hora="0"+hora;
                        }
                        if(selectedMinute <10){
                            minuto="0"+minuto;
                        }
                        txtHR.setText( hora + ":" + minuto);
                        tratamento.setHrTratamento(DateUtilsAndroid.getTime(selectedHour,selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
            //}else{
            //   --isCodeLoadDtInicio;
            //}
        }
    }


}
