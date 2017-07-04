package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.petshow.R;
import br.com.petshow.enums.EnumFaseVida;
import br.com.petshow.enums.EnumFrequenciaTratamento;
import br.com.petshow.enums.EnumPorteAnimal;
import br.com.petshow.enums.EnumTipoAnimal;
import br.com.petshow.model.PerfilAdocao;
import br.com.petshow.model.Tratamento;
import br.com.petshow.util.DateUtilsAndroid;
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

    Spinner spTipo;
    Spinner spFase;
    Spinner spPorte;

    PerfilAdocao perfilAdocao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_adocao);

        startComponents();
        startVariables();
        if(perfilAdocao!=null  && perfilAdocao.getId()>0) {
            loadPerfil();
            if(perfilAdocao==null){
                loadSpinnersValuesDefault();
            }
        }else{
          //Preencher
            //loadSpinners(null);
        }
    }
    public void startComponents() {

        MenuUtil.loadToolBar(this, R.id.toolbar_activity_perfil_adocao);
        progressDialog = new ProgressDialog(this);

        spTipo  =(Spinner) findViewById(R.id.perfilAdocao_spTipo);
        spFase  =(Spinner) findViewById(R.id.perfilAdocao_spFase);
        spPorte =(Spinner) findViewById(R.id.perfilAdocao_spPorte);

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
        perfilAdocao= perfilAdocao==null?new PerfilAdocao():perfilAdocao;

    }

    //Utilizaremos este metodo para buscar e preencher spnners de acordo com o banco de dados

    private void loadSpinnerTipo(String tipo){
        //No caso nos chamamos o RequestListObject pois queremos chamar um REST q vai retornar uma lista
        //porem existem outros que devemos chamar de acordo com o q gostariamos de fazer
        //ver mais detalhes no pacote web.util
        new RequestListObjects(new CallBack(this) {
            @Override
            public void successWithReturn(String json) {
                List<String> lista=null;
                try {
                    //Pegando a resposta do REST que veio como parametro
                    lista= JsonUtil.jsonToList(new String(), json,String.class);
                    //Adicionado o "Selecione" como default para ser o primeiro
                    lista.add(0, getContext().getString(R.string.selAnimal));
                    //Prenche o spinner de acordo com os parametros.
                    CriationUtil.createArrayAdapter(this.getContext(),spTipo, lista);
                    // Aqui iremos preencher o spinner
                    if(tipo !=null){
                        for(int i=0;lista.size() >i;++i){

                            if(lista.get(i).equals(tipo)){
                                spTipo.setSelection(i);
                                //Caso encontre a apçãol desejada , iremos parar
                                break;
                            }
                        }

                    }



                } catch (Exception e) {
                    //Em caso de exceção sempre chamar esse metodo.
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {

            }
        }).execute("animal/consulta/tipos");
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

    private void loadPerfil(){
        //TODO Carregar os valores detro dos componentes
        loadSpinnerTipo(perfilAdocao.getTipoAnimal().toString());
    }

    private void onClickSalvar(){
        //TODO Por metodo para savar, aproveitar e por as validações.
        salvar();

    }



    private void excluir(){

    }

    private void loadSpinnersValuesDefault(){
    //spFaixaIdade.setAdapter(new ArrayAdapter<EnumFaseVida>(this, android.R.layout.simple_list_item_1, EnumFaseVida.values()));

        spTipo.setAdapter(new ArrayAdapter<EnumTipoAnimal>(this,R.layout.support_simple_spinner_dropdown_item,EnumTipoAnimal.values()));
        spFase.setAdapter(new ArrayAdapter<EnumFaseVida>(this,R.layout.support_simple_spinner_dropdown_item, EnumFaseVida.values()));
        spPorte.setAdapter(new ArrayAdapter<EnumPorteAnimal>(this,R.layout.support_simple_spinner_dropdown_item, EnumPorteAnimal.values()));
    }
}
