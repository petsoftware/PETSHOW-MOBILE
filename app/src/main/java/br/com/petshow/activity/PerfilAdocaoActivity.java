package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import java.io.IOException;

import br.com.petshow.R;
import br.com.petshow.enums.EnumFaseVida;
import br.com.petshow.enums.EnumPorteAnimal;
import br.com.petshow.enums.EnumSexo;
import br.com.petshow.enums.EnumTipoAnimal;
import br.com.petshow.model.PerfilAdocao;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestGetEntity;
import br.com.petshow.web.util.RequestPostEntity;

public class PerfilAdocaoActivity extends PetActivity {

    //Declaring the basic components
    Spinner spTipo;
    Spinner spFase;
    Spinner spPorte;
    Spinner spSexo;
    ImageView ivTipo;
    ImageView ivFase;
    ImageView ivPorte;
    ImageView ivSexo;
    //Object model from DataBase by REST
    PerfilAdocao perfilAdocao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_adocao);
        startComponents();
        startVariables();
        loadSpinnersValuesDefault();
        if(perfilAdocao!=null  && perfilAdocao.getId()>0) {
            loadSpinners(perfilAdocao);
        }
    }
    public void startComponents() {

        MenuUtil.loadToolBar(this, R.id.toolbar_activity_perfil_adocao);
        progressDialog = new ProgressDialog(this);

        spTipo  =(Spinner) findViewById(R.id.perfilAdocao_spTipo);
        spFase  =(Spinner) findViewById(R.id.perfilAdocao_spFase);
        spPorte =(Spinner) findViewById(R.id.perfilAdocao_spPorte);
        spSexo  =(Spinner) findViewById(R.id.perfilAdocao_spSexo);

        spTipo.setAdapter(new ArrayAdapter<EnumTipoAnimal>(this,R.layout.support_simple_spinner_dropdown_item,EnumTipoAnimal.values()));
        spFase.setAdapter(new ArrayAdapter<EnumFaseVida>(this,R.layout.support_simple_spinner_dropdown_item,EnumFaseVida.values()));
        spPorte.setAdapter(new ArrayAdapter<EnumPorteAnimal>(this,R.layout.support_simple_spinner_dropdown_item,EnumPorteAnimal.values()));
        spSexo.setAdapter(new ArrayAdapter<EnumSexo>(this,R.layout.support_simple_spinner_dropdown_item,EnumSexo.values()));

        spTipo.setOnItemSelectedListener(new TipoAnimalListner());
        spFase.setOnItemSelectedListener(new FaseVidaListner());
        spPorte.setOnItemSelectedListener(new PorteAnimalListner());
        spSexo.setOnItemSelectedListener(new SexoListner());

        ivFase = (ImageView) findViewById(R.id.perfilAdocao_ivFase);
        ivPorte= (ImageView) findViewById(R.id.perfilAdocao_ivPorte);
        ivSexo = (ImageView) findViewById(R.id.perfilAdocao_ivSexo);
        ivTipo = (ImageView) findViewById(R.id.perfilAdocao_ivTipo);

        ivPorte.setImageResource(R.drawable.select_combo);
        ivFase.setImageResource(R.drawable.select_combo);
        ivSexo.setImageResource(R.drawable.select_combo);
        ivTipo.setImageResource(R.drawable.select_combo);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);
        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblSalvar));
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        if(perfilAdocao!=null && perfilAdocao.getId()>0){
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

    public void startVariables(){
        if(perfilAdocao != null) {
            if (perfilAdocao.getUsuario() == null) {
                perfilAdocao.setUsuario(FacebookUtil.usuarioLogado);
            }
        }else{
            perfilAdocao = new PerfilAdocao();
            if (perfilAdocao.getUsuario() == null) {
                perfilAdocao.setUsuario(FacebookUtil.usuarioLogado);
            }
        }
        consultaPerfilOnLoad();
        perfilAdocao        = perfilAdocao==null?new PerfilAdocao():perfilAdocao;

    }

    //Utilizaremos este metodo para buscar e preencher spnners de acordo com o banco de dados

    private void loadSpinners(PerfilAdocao perfilAdocao){
        setSpinnerFaseVida(spFase,perfilAdocao.getFaseVida().getId(),perfilAdocao.getFaseVida().toString());
        setSpinnerPorteAnimal(spPorte,perfilAdocao.getPorteAnimal().getId(),perfilAdocao.getPorteAnimal().toString());
        setSpinnerTipoAnimal(spTipo,perfilAdocao.getTipoAnimal().getId(),perfilAdocao.getTipoAnimal().toString());
        setSpinnerSexoAnimal(spSexo,perfilAdocao.getSexo().getId(),perfilAdocao.getSexo().toString());

        setTipoAnimalComboImagem(perfilAdocao.getTipoAnimal());
        setFaseVidaComboImagem(perfilAdocao.getFaseVida());
        setSexoAnimalComboImagem(perfilAdocao.getSexo());
        setPorteAnimalComboImagem(perfilAdocao.getPorteAnimal());
    }


    private void setPorteAnimalComboImagem(EnumPorteAnimal porteAnimal){
        if(porteAnimal == EnumPorteAnimal.MEDIO){
            ivPorte.setImageResource(R.drawable.ic_porte_med);
        }else if(porteAnimal == EnumPorteAnimal.GRANDE){
            ivPorte.setImageResource(R.drawable.ic_porte_gran);
        }else{
            ivPorte.setImageResource(R.drawable.ic_porte_peq);
        }
    }
    private void setSexoAnimalComboImagem(EnumSexo enumSexo){
        if(enumSexo == EnumSexo.MACHO){
            ivSexo.setImageResource(R.drawable.ic_macho);
        }else{
            ivSexo.setImageResource(R.drawable.ic_femea);
        }
    }
    private void setFaseVidaComboImagem(EnumFaseVida enumFaseVida){
        if(enumFaseVida == EnumFaseVida.ADULTO){
            ivFase.setImageResource(R.drawable.ic_adult_dog);
        }else if(enumFaseVida == EnumFaseVida.FILHOTE){
            ivFase.setImageResource(R.drawable.ic_puppy);
        }else{
            ivFase.setImageResource(R.drawable.ic_idoso);
        }
    }

    private void setTipoAnimalComboImagem(EnumTipoAnimal tipoAnimal){

        if(tipoAnimal == EnumTipoAnimal.CACHORRO){
            ivTipo.setImageResource(R.drawable.ic_dog);
        }else if(tipoAnimal == EnumTipoAnimal.CAVALO){
            ivTipo.setImageResource(R.drawable.ic_horse);
        }else if(tipoAnimal == EnumTipoAnimal.COBRA){
            ivTipo.setImageResource(R.drawable.ic_snake);
        }else if(tipoAnimal == EnumTipoAnimal.GATO){
            ivTipo.setImageResource(R.drawable.ic_cat_perfil);
        }else if(tipoAnimal == EnumTipoAnimal.LAGARTO){
            ivTipo.setImageResource(R.drawable.ic_lizard);
        }else if(tipoAnimal == EnumTipoAnimal.PASSARO){
            ivTipo.setImageResource(R.drawable.ic_bird);
        }else if(tipoAnimal == EnumTipoAnimal.PEIXE){
            ivTipo.setImageResource(R.drawable.ic_fish);
        }else if(tipoAnimal == EnumTipoAnimal.SUINO){
            ivTipo.setImageResource(R.drawable.ic_pig);
        }else{
            ivTipo.setImageResource(R.drawable.ic_paw);
        }
    }

    private void setSpinnerFaseVida(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumFaseVida enume = (EnumFaseVida) adapter.getItem(i);
                if(enume.getId()== id && desc.equals(enume.toString())){
                    sp.setSelection(i);
                    break;
                }
            }catch (ClassCastException ex) {

            }
        }
    }

    private void setSpinnerPorteAnimal(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumPorteAnimal enume = (EnumPorteAnimal) adapter.getItem(i);
                if(enume.getId()== id && desc.equals(enume.toString())){
                    sp.setSelection(i);
                    break;
                }
            }catch (ClassCastException ex) {

            }
        }
    }

    private void setSpinnerSexoAnimal(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumSexo enume = (EnumSexo) adapter.getItem(i);
                if(enume.getId()== id && desc.equals(enume.toString())){
                    sp.setSelection(i);
                    break;
                }
            }catch (ClassCastException ex) {

            }
        }
    }

    private void setSpinnerTipoAnimal(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumTipoAnimal enume = (EnumTipoAnimal) adapter.getItem(i);
                if(enume.getId()== id && desc.equals(enume.toString())){
                    sp.setSelection(i);
                    break;
                }
            }catch (ClassCastException ex) {

            }
        }
    }


    public void  salvar(){
       CriationUtil.openProgressBar(progressDialog);
        new RequestPostEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {
                MessageUtil.messageSucess(getContext(),getContext().getString(R.string.msgSalvo));
                try {
                    perfilAdocao = JsonUtil.transformObject(json,PerfilAdocao.class );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CriationUtil.closeProgressBar(progressDialog);
            }

            @Override
            public void successNoReturn() {
                MessageUtil.messageSucess(getContext(),"Cadastrado com sucesso!");
            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                MessageUtil.messageWarning(getContext(),map.getMessage());
                CriationUtil.closeProgressBar(progressDialog);
            }
        }).execute("perfil/adocao/salvar",perfilAdocao);
    }



    private void onClickSalvar(){
        //TODO Por metodo para savar, aproveitar e por as validações.
        salvar();

    }



    private void excluir(){

    }

    private void loadSpinnersValuesDefault(){
    //spFaixaIdade.setAdapter(new ArrayAdapter<EnumFaseVida>(this, android.R.layout.simple_list_item_1, EnumFaseVida.values()));


    }

    private class TipoAnimalListner implements AdapterView.OnItemSelectedListener {
        public TipoAnimalListner(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EnumTipoAnimal enumTipoAnimal = EnumTipoAnimal.getEnum(spTipo.getSelectedItem().toString());
            perfilAdocao.setTipoAnimal(enumTipoAnimal);
            setTipoAnimalComboImagem(enumTipoAnimal);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class FaseVidaListner implements AdapterView.OnItemSelectedListener {
        public FaseVidaListner(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EnumFaseVida enumFaseVida = EnumFaseVida.getEnum(spFase.getSelectedItem().toString());
            perfilAdocao.setFaseVida(enumFaseVida);
            setFaseVidaComboImagem(enumFaseVida);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class PorteAnimalListner implements AdapterView.OnItemSelectedListener {
        public PorteAnimalListner(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EnumPorteAnimal enumPorteAnimal = EnumPorteAnimal.getEnum(spPorte.getSelectedItem().toString());
            perfilAdocao.setPorteAnimal(enumPorteAnimal);
            setPorteAnimalComboImagem(enumPorteAnimal);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class SexoListner implements AdapterView.OnItemSelectedListener {
        public SexoListner(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EnumSexo enumSexo = EnumSexo.getEnum(spSexo.getSelectedItem().toString());
            perfilAdocao.setSexo(enumSexo);
            setSexoAnimalComboImagem(enumSexo);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void  consultaPerfilOnLoad(){
        CriationUtil.openProgressBar(progressDialog);
        new RequestGetEntity(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {

                try {
                    perfilAdocao = JsonUtil.transformObject(json,PerfilAdocao.class );
                    loadSpinners(perfilAdocao);
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
                MessageUtil.messageWarning(getContext(),map.getMessage());
                CriationUtil.closeProgressBar(progressDialog);
            }
        }).execute("perfil/adocao/get/"+perfilAdocao.getUsuario().getId());
    }

}
