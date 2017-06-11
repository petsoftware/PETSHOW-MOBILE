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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.Profile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.petshow.R;
import br.com.petshow.adapters.RacaAdapterToArray;
import br.com.petshow.enums.EnumTipoAnimal;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Tutor;
import br.com.petshow.model.Usuario;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.DateUtilsAndroid;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.ImagemUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestDelete;
import br.com.petshow.web.util.RequestListObjects;
import br.com.petshow.web.util.RequestPostEntity;



public class NovoPetActivity extends PetActivity {


    ProgressDialog progressDialog;
    Usuario usuarioLogado;
    Profile profile;

    EditText txtNome;
    RadioGroup radioGroupSexo;
    RadioButton radioMaxo;
    RadioButton radioFemea;
    Spinner spTpAnimal;
    Spinner spRaca;
    EditText txtDataNascimento;

    int chamadaChangeRestTipo;

    Animal animal;
    Tutor tutor;

    private ImageView imgPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadVariablesExternal();
        setContentView(R.layout.activity_novo_pet);
        setTitle(this.getString(R.string.title_activity_novo_pet));
        startComponents();
        startVariables();

        if(animal !=null && animal.getId()>0){
            loadAnimal();
        }else{
            loadSpinners(null,true);
        }






    }
    public void startVariables(){
        usuarioLogado = FacebookUtil.usuarioLogado;
        profile = Profile.getCurrentProfile();
        animal= animal==null? new Animal():animal;
        tutor = new Tutor();

    }
    public void startComponents() {

        MenuUtil.loadToolBar(this,R.id.toolbar_activity_novo_pet);
        progressDialog = new ProgressDialog(this);


        radioMaxo =(RadioButton) findViewById(R.id.novoPet_rMacho);
        radioFemea =(RadioButton) findViewById(R.id.novoPet_rFemea);
        txtNome =(EditText) findViewById(R.id.novoPet_txtNome);
        txtDataNascimento =(EditText) findViewById(R.id.novoPet_dtNascimento);
        spTpAnimal = (Spinner) findViewById(R.id.novoPet_spTiposAnimais);
        spRaca = (Spinner) findViewById(R.id.novoPet_spRacas);
        radioGroupSexo=(RadioGroup) findViewById(R.id.radioGroupSexo);

        ExibeDataListener exibeDataListener =new ExibeDataListener();
        txtDataNascimento.setOnClickListener(exibeDataListener);
        txtDataNascimento.setOnFocusChangeListener(exibeDataListener);

        imgPicture=(ImageView) findViewById(R.id.novoPet_imageProfile);
        imgPicture.setImageResource(R.drawable.perfil_no_photo);
        imgPicture.setOnClickListener(new ChangePictureListener());
        spTpAnimal.setOnItemSelectedListener(new NovoPetActivity.RacaListeners());

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);

        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar)); //   (int groupId,        int itemId,        int order,        int titleRes)
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        if(animal !=null && animal.getId()>0) {
           MenuItem itemExcluir = menu.add(0, 2, 5, getString(R.string.lblExcluir)); //   (int groupId,        int itemId,        int order,        int titleRes)
           itemExcluir.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

            MenuItem itemVacina = menu.add(0, 3, 2, getString(R.string.menuVacina)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemVacina.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

            MenuItem itemVermifugo = menu.add(0, 4, 3, getString(R.string.menuVermifugo)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemVermifugo.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

            MenuItem itemTratamento = menu.add(0, 5, 4, getString(R.string.menuTratamento)); //   (int groupId,        int itemId,        int order,        int titleRes)
            itemTratamento.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
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
        if (id == 2) {
            excluir();
            return true;
        }
        if (id == 3) {
            goToVacina();
            return true;
        }
        if (id == 4) {
            goToVermifugo();
            return true;
        }
        if (id == 5) {
            goToTratamento();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void goToTratamento() {
        Intent it = new Intent(this,ListaTratamentoActivity.class);
        it.putExtra(AtributosUtil.PAR_ANIMAL,animal);
        startActivityForResult(it,AtributosUtil.INTENT_NOVO_TRATAMENTO);
    }

    private void goToVermifugo() {
        Intent it = new Intent(this,EdtVermifugoActivity.class);
        it.putExtra(AtributosUtil.PAR_ANIMAL,animal);
        startActivityForResult(it,AtributosUtil.INTENT_NOVO_VERMIFUGO);
    }

    private void goToVacina() {
        Intent it = new Intent(this,ListaVacinaActivity.class);
        it.putExtra(AtributosUtil.PAR_ANIMAL,animal);
        startActivityForResult(it,AtributosUtil.INTENT_NOVO_VACINA);
    }


    public void showOptionsPhoto (){

        android.support.v7.app.AlertDialog.Builder ad = new android.support.v7.app.AlertDialog.Builder(this);
        ad.setTitle(R.string.title_modal_tipo_get_photo);

        ad.setSingleChoiceItems(R.array.list_type_get_photo,-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                   NavegationUtil.openGalleryPhoto(NovoPetActivity.this);
                    dialog.dismiss();
                }
                if(which==1){
                   NavegationUtil.openCamera(NovoPetActivity.this);
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
                        imgPicture.setImageBitmap(bitmap);
                        animal.setFotoPerfil(ImagemUtil.transformBase64AsString(bitmap));
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
                    imgPicture.setImageBitmap(bitmap);
                    animal.setFotoPerfil(ImagemUtil.transformBase64AsString(bitmap));

                }else {


                }
            }
        }



    private void salvar(){



        animal.setNome(txtNome.getText().toString());
        animal.setFlSexo(getValueRadioSexo());
        animal.setTipo(EnumTipoAnimal.getEnum(spTpAnimal.getSelectedItem().toString()));
        animal.setRaca(spRaca.getSelectedItem().toString());

        if(animal.getId()==0) {
            tutor.setAnimal(animal);
            tutor.setDonoAtual(true);
            tutor.setUsuario(usuarioLogado);
        }

        if(animal.getId()==0) {
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
                    focusInCamposRetornoRest(map.getFields()[0]);

                }

            }).execute("animal/tutor/salvar", tutor);

        }else{
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
                    focusInCamposRetornoRest(map.getFields()[0]);

                }

            }).execute("animal/salvar", animal);
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
                    if(map.getFields()!=null) {
                        focusInCamposRetornoRest(map.getFields()[0]);
                    }

                }

            }).execute("animal/"+ animal.getId());
    }

    private void focusInCamposRetornoRest(String field){
        if(field.equals("nome")){
            txtNome.requestFocus();
        }
        if(field.equals("tipo")){
            spTpAnimal.requestFocus();
        }
        if(field.equals("flSexo")){
            spTpAnimal.requestFocus();
        }
        if(field.equals("dataNascimento")){
            txtDataNascimento.requestFocus();
        }

    }



    private void loadSpinners(String tipo,boolean isCodeLoadTipo){

        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<String> lista=null;
                try {

                    lista= JsonUtil.jsonToList(new String(), json,String.class);

                    lista.add(0, getContext().getString(R.string.selAnimal));

                    CriationUtil.createArrayAdapter(this.getContext(),spTpAnimal, lista);
                    if(tipo !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).equals(tipo)){
                                spTpAnimal.setSelection(i);
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
    }

    private class RacaListeners implements AdapterView.OnItemSelectedListener {


        public RacaListeners(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           if(chamadaChangeRestTipo==0) {
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

    private void loadSpinnerRaca(String tipoAnimal,String raca){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<RacaAdapterToArray> lista=null;
                try {
                    lista=JsonUtil.jsonToList(new RacaAdapterToArray(), json,RacaAdapterToArray.class);
                    lista.add(0, new RacaAdapterToArray());
                    CriationUtil.createArrayAdapter(this.getContext(),spRaca, lista);
                    if(raca !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).getDescricao()!=null && lista.get(i).getDescricao().equalsIgnoreCase(raca)){
                                spRaca.setSelection(i);
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

        CriationUtil.createArrayAdapter(this,spRaca, lista);

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

    private class ChangePictureListener implements  View.OnClickListener, View.OnFocusChangeListener{


        @Override
        public void onClick(View v) {
            showOptionsPhoto ();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

        }
    }

    private void exibeData(){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,new SelecionaDataListener(),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();

    }

    private class SelecionaDataListener implements  DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = DateUtilsAndroid.dateToString( year,  month,  dayOfMonth);
            txtDataNascimento.setText(dt);
            animal.setDataNascimento(DateUtilsAndroid.getDate(year,  month,  dayOfMonth));

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
    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)) {
            animal = (Animal) bundle.getSerializable(AtributosUtil.PAR_ANIMAL);
        }
    }

    public void loadAnimal(){


            txtNome.setText(animal.getNome());
            setValueRadioSexo(animal.getFlSexo());
            spTpAnimal.setSelection(animal.getTipo().getId());
            spTpAnimal.setEnabled(false);
            if(animal.getFotoPerfil() != null) {
                imgPicture.setImageBitmap(ImagemUtil.transformBase64Bitmap(animal.getFotoPerfil()));
            }


            Calendar cal = Calendar.getInstance();
            cal.setTime(animal.getDataNascimento());
            String dt = DateUtilsAndroid.dateToString(  cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
            txtDataNascimento.setText(dt);
            loadSpinners(animal.getTipo().toString(),true);
            loadSpinnerRaca(animal.getTipo().toString(),animal.getRaca());






    }


}


