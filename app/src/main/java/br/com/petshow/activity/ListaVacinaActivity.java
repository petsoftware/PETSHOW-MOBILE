

package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.petshow.R;
import br.com.petshow.adapters.VacinaArrayAdapter;
import br.com.petshow.model.Animal;
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

public class ListaVacinaActivity extends PetActivity {

    private VacinaArrayAdapter adpVacina;
    private ListView lstVacina;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vacina);
        loadVariablesExternal();
        startComponents();
        startVariables();
        loadListVacina();
    }

    public void startVariables(){


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

            Intent it = new Intent(this,EdtVacinaActivity.class);
            it.putExtra(AtributosUtil.PAR_ANIMAL   , animal);
            startActivityForResult(it, AtributosUtil.INTENT_NOVO_VACINA);

            return true;
        }





        return super.onOptionsItemSelected(item);
    }

    public void startComponents() {

        MenuUtil.loadToolBar(this,R.id.toolbar_activity_lista_vacina);
        progressDialog = new ProgressDialog(this);
        lstVacina=(ListView) findViewById(R.id.vacina_listVacina);

        lstVacina.setItemsCanFocus(false);
        lstVacina.setOnItemClickListener( new ListaVacinaActivity.VacinaListeners());

    }

    public void loadListVacina(){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<Vacina> lista=null;
                try {
                    //new AnimalArrayAdapterListaPets(getContext(),R.layout.item_animal)
                    lista= JsonUtil.jsonToList(new Vacina(), json,Vacina.class);


                    adpVacina = new VacinaArrayAdapter (getContext(),R.layout.item_vacina);

                    for(Vacina v:lista){
                        adpVacina.add(v);
                    }

                    lstVacina.setAdapter(adpVacina);




                    CriationUtil.closeProgressBar(progressDialog);


                } catch (Exception e) {
                    CriationUtil.closeProgressBar(progressDialog);
                    MessageUtil.messageErro(getContext(),e.getMessage());
                    e.printStackTrace();
                }
            }
            public void successNoReturn() {}
            public void predictedError(MapErroRetornoRest map) {
                CriationUtil.closeProgressBar(progressDialog);
                MessageUtil.messageWarning(getContext(),map.getMessage());

            }


        }).execute("animal/vacina/animal/"+animal.getId());
    }

    public class VacinaListeners implements AdapterView.OnItemClickListener{

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Vacina vacina =  adpVacina.getItem(position);
            Intent it = new Intent(view.getContext(),EdtVacinaActivity.class);
            it.putExtra(AtributosUtil.PAR_VACINA,vacina);
            startActivityForResult(it, AtributosUtil.INTENT_NOVO_VACINA);
        }


    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        loadListVacina();
    }

    public void loadVariablesExternal(){
        Bundle bundle= getIntent().getExtras();
        if(bundle != null && bundle.containsKey(AtributosUtil.PAR_ANIMAL)) {
            animal = (Animal) bundle.getSerializable(AtributosUtil.PAR_ANIMAL);
        }
    }
}
