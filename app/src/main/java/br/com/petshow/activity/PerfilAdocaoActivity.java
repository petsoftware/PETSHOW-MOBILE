package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.io.IOException;
import java.util.List;
import br.com.petshow.R;
import br.com.petshow.enums.EnumCor;
import br.com.petshow.enums.EnumFaseVida;
import br.com.petshow.enums.EnumPorteAnimal;
import br.com.petshow.enums.EnumTipoAnimal;
import br.com.petshow.model.PerfilAdocao;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestListObjects;
import br.com.petshow.web.util.RequestPostEntity;

public class PerfilAdocaoActivity extends PetActivity {

    //Declaring the basic components
    Spinner spTipo;
    Spinner spFase;
    Spinner spPorte;
    //Declaring all used Adapters
    ArrayAdapter<EnumFaseVida> faseVidaAdapter;
    ArrayAdapter<EnumPorteAnimal> porteAnimalAdapter;
    ArrayAdapter<EnumTipoAnimal> tipoAnimalAdapter;
    //Object model from DataBase by REST
    PerfilAdocao perfilAdocao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_adocao);

        perfilAdocao = new PerfilAdocao();
        perfilAdocao.setId(1);
        perfilAdocao.setFaseVida(EnumFaseVida.ADULTO);
        perfilAdocao.setPorteAnimal(EnumPorteAnimal.MEDIO);
        perfilAdocao.setTipoAnimal(EnumTipoAnimal.LAGARTO);


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

        spTipo.setAdapter(new ArrayAdapter<EnumTipoAnimal>(this,R.layout.support_simple_spinner_dropdown_item,EnumTipoAnimal.values()));
        spFase.setAdapter(new ArrayAdapter<EnumFaseVida>(this,R.layout.support_simple_spinner_dropdown_item,EnumFaseVida.values()));
        spPorte.setAdapter(new ArrayAdapter<EnumPorteAnimal>(this,R.layout.support_simple_spinner_dropdown_item,EnumPorteAnimal.values()));
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
        //Initializing variable
        perfilAdocao        = perfilAdocao==null?new PerfilAdocao():perfilAdocao;
//        faseVidaAdapter     = new ArrayAdapter<EnumFaseVida>(this,R.layout.support_simple_spinner_dropdown_item,EnumFaseVida.values());
//        tipoAnimalAdapter   = new ArrayAdapter<EnumTipoAnimal>(this,R.layout.support_simple_spinner_dropdown_item,EnumTipoAnimal.values());
//        porteAnimalAdapter  = new ArrayAdapter<EnumPorteAnimal>(this,R.layout.support_simple_spinner_dropdown_item,EnumPorteAnimal.values());
    }

    //Utilizaremos este metodo para buscar e preencher spnners de acordo com o banco de dados

    private void loadSpinners(PerfilAdocao perfilAdocao){

        setSpinnerFaseVida(spFase,perfilAdocao.getFaseVida().getId(),perfilAdocao.getFaseVida().toString());
        setSpinnerPorteAnimal(spPorte,perfilAdocao.getPorteAnimal().getId(),perfilAdocao.getPorteAnimal().toString());
        setSpinnerTipoAnimal(spTipo,perfilAdocao.getTipoAnimal().getId(),perfilAdocao.getTipoAnimal().toString());
//       if(perfilAdocao!=null){
//           if(perfilAdocao.getFaseVida() != null){
//               String value = perfilAdocao.getFaseVida().name();
//               int positionFaseVida = faseVidaAdapter.getPosition(perfilAdocao.getFaseVida());
//               spFase.setSelection(positionFaseVida);
//           }
//           if(perfilAdocao.getPorteAnimal() != null){
//               int positionPorteAnimal = porteAnimalAdapter.getPosition(perfilAdocao.getPorteAnimal());
//               spPorte.setSelection(positionPorteAnimal);
//           }
//           if(perfilAdocao.getTipoAnimal() != null){
//               int positionTipoAnimal = tipoAnimalAdapter.getPosition(perfilAdocao.getTipoAnimal());
//               spTipo.setSelection(positionTipoAnimal);
//           }
//       }


    }

    private void setSpinnerFaseVida(Spinner sp,int id,String desc){
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            try {
                EnumFaseVida enume = (EnumFaseVida) adapter.getItem(i);
                if(enume.getId()== id && desc.equals(enume.toString())){
                    sp.setSelection(i);
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

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {
                MessageUtil.messageWarning(getContext(),map.getMessage());
                CriationUtil.closeProgressBar(progressDialog);
            }
        }).execute("url do REST",perfilAdocao);
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
}
