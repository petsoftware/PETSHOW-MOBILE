package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.petshow.R;
import br.com.petshow.adapters.TratamentoArrayAdapter;
import br.com.petshow.adapters.VacinaArrayAdapter;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Tratamento;
import br.com.petshow.model.Vacina;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestGetHashMap;
import br.com.petshow.web.util.RequestListObjects;

public class ListaTratamentoActivity extends PetActivity {

    private TratamentoArrayAdapter adpTratamento;
    private ListView lstTratamento;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tratamento);
        loadVariablesExternal();
        startComponents();
        startVariables();
        loadListTratamento();
    }

    public void startVariables(){


    }

    public void startComponents() {

        MenuUtil.loadToolBar(this,R.id.toolbar_activity_lista_tratamento);
        progressDialog = new ProgressDialog(this);
        lstTratamento=(ListView) findViewById(R.id.tratamento_listTratamento);

        lstTratamento.setItemsCanFocus(false);
        lstTratamento.setOnItemClickListener( new ListaTratamentoActivity.TratamentoListeners());

    }

    public void loadListTratamento() {
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json) {
                List<Tratamento> lista = null;
                try {
                    //new AnimalArrayAdapterListaPets(getContext(),R.layout.item_animal)
                    lista = JsonUtil.jsonToList(new Tratamento(), json, Tratamento.class);


                    adpTratamento = new TratamentoArrayAdapter(getContext(), R.layout.item_tratamento);

                    for (Tratamento v : lista) {
                        adpTratamento.add(v);
                    }

                    lstTratamento.setAdapter(adpTratamento);


                    CriationUtil.closeProgressBar(progressDialog);


                } catch (Exception e) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageErro(getContext(), e.getMessage());
                    e.printStackTrace();
                }
            }

            public void successNoReturn() {
            }

            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(), map.getMessage());

            }


        }).execute("animal/tratamento/animal/" + animal.getId());
    }
    public class TratamentoListeners implements AdapterView.OnItemClickListener{

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Tratamento tratamento =  adpTratamento.getItem(position);
            Intent it = new Intent(view.getContext(),EdtTratamentoActivity.class);
            it.putExtra(AtributosUtil.PAR_TRATAMENTO,tratamento);//usuario.get("uID").toString());
            startActivityForResult(it, AtributosUtil.INTENT_NOVO_TRATAMENTO);
        }


    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        loadListTratamento();
    }

    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)) {
            animal = (Animal) bundle.getSerializable(AtributosUtil.PAR_ANIMAL);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);
        MenuItem itemMenu= menu.add(0,1,1,getString(R.string.lblAdicionar));
        itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);



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

            Intent it = new Intent(this,EdtTratamentoActivity.class);
            it.putExtra(AtributosUtil.PAR_ANIMAL,animal);
            startActivityForResult(it, AtributosUtil.INTENT_NOVO_TRATAMENTO);

            return true;
        }





        return super.onOptionsItemSelected(item);
    }

}
