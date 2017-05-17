package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.Profile;

import java.util.List;

import br.com.petshow.R;
import br.com.petshow.adapters.AdocaoArrayAdapterListaPets;
import br.com.petshow.adapters.AnimalArrayAdapterListaPets;
import br.com.petshow.adapters.PerdidoArrayAdapterListaPets;
import br.com.petshow.model.Adocao;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Perdido;
import br.com.petshow.model.Usuario;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.view.util.NavegationUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestListObjects;

public class ListaPetsActivity extends PetActivity{


    private AnimalArrayAdapterListaPets adpAnimal;
    private ListView lstAnimal;
    private PerdidoArrayAdapterListaPets adpPerdido;
    private ListView lstPerdido;
    private AdocaoArrayAdapterListaPets adpAdocao;
    private ListView lstAdocao;


    ProgressDialog progressDialog;
    Usuario usuarioLogado;
    Profile profile;


    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pets);

        startComponents();
        startVariables();
        loadListAnimals();
    }
    public void startVariables() {
        usuarioLogado = FacebookUtil.usuarioLogado;
        profile = Profile.getCurrentProfile();



    }

    public void loadListAnimals(){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<Animal> lista=null;
                try {
                    //new AnimalArrayAdapterListaPets(getContext(),R.layout.item_animal)
                    lista= JsonUtil.jsonToList(new Animal(), json,Animal.class);


                    //adpAnimal = new ArrayAdapter<Animal> (getContext(),R.layout.item_animal,lista);
                    adpAnimal = new AnimalArrayAdapterListaPets (getContext(),R.layout.item_animal);

                    for(Animal a:lista){
                        adpAnimal.add(a);
                    }

                    lstAnimal.setAdapter(adpAnimal);




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

        }).execute("animal/consulta/usuario/"+usuarioLogado.getId());
    }


    public void loadListPerdido(){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<Perdido> lista=null;
                try {
                    //new AnimalArrayAdapterListaPets(getContext(),R.layout.item_animal)
                    lista= JsonUtil.jsonToList(new Perdido(), json,Perdido.class);


                    //adpAnimal = new ArrayAdapter<Animal> (getContext(),R.layout.item_animal,lista);
                    adpPerdido = new PerdidoArrayAdapterListaPets (getContext(),R.layout.item_animal);

                    for(Perdido a:lista){
                        adpPerdido.add(a);
                    }

                    lstPerdido.setAdapter(adpPerdido);




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

        }).execute("animal/consulta/perdido/usuario/"+usuarioLogado.getId());
    }

    public void loadListAdocao(){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<Adocao> lista=null;
                try {

                    lista= JsonUtil.jsonToList(new Adocao(), json,Adocao.class);


                    //adpAnimal = new ArrayAdapter<Animal> (getContext(),R.layout.item_animal,lista);
                    adpAdocao = new AdocaoArrayAdapterListaPets (getContext(),R.layout.item_animal);

                    for(Adocao a:lista){
                        adpAdocao.add(a);
                    }

                    lstAdocao.setAdapter(adpAdocao);




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

        }).execute("adocao/consulta/usuario/"+usuarioLogado.getId());
    }

    public void startComponents() {

        MenuUtil.loadToolBar(this,R.id.toolbar_activity_lista_pets);
        progressDialog = new ProgressDialog(this);

        lstAnimal=(ListView) findViewById(R.id.listaPets_listAnimals);
        lstAnimal.setItemsCanFocus(false);
        lstAnimal.setOnItemClickListener( new ListaPetListeners());

        lstPerdido=(ListView) findViewById(R.id.listaPets_listPerdido);
        lstPerdido.setItemsCanFocus(false);
        lstPerdido.setOnItemClickListener( new ListaPerdidoListeners());

        lstAdocao=(ListView) findViewById(R.id.listaPets_listAdocao);
        lstAdocao.setItemsCanFocus(false);
        lstAdocao.setOnItemClickListener( new ListaAdocaoListeners());



        tabLayout =(TabLayout) findViewById(R.id.listaPets_layoutTab);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                alterarTab(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
             }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.loadMenuGlobal(this, menu);

        MenuItem itemMenuAdicionar= menu.add(0,1,1,getString(R.string.lblAdicionar)); //   (int groupId,        int itemId,        int order,        int titleRes)
        itemMenuAdicionar.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavegationUtil.goToMenu(this);
            return true;
        }


        if (id == 1) {
            showTipos();
            return true;
        }

        return true;
    }

    public void showTipos (){

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.title_modal_tipo_cadastro_animal);

                ad.setSingleChoiceItems(R.array.list_type_cadastro_pet,-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            dialog.dismiss();;
                            Intent it = new Intent(ListaPetsActivity.this,NovoPetActivity.class);
                            startActivityForResult(it, AtributosUtil.INTENT_NOVO_PET);
                        }
                        if(which==1){
                            dialog.dismiss();;
                            Intent it = new Intent(ListaPetsActivity.this,NovoAdocaoActivity.class);
                            startActivityForResult(it, AtributosUtil.INTENT_NOVO_ADOCAO);

                        }
                        if(which==2){
                            dialog.dismiss();;
                            Intent it = new Intent(ListaPetsActivity.this,NovoPerdidoActivity.class);
                            startActivityForResult(it, AtributosUtil.INTENT_NOVO_PERDIDO);

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

    public class ListaPetListeners implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Animal Animal = adpAnimal.getItem(position);
            Intent it = new Intent(view.getContext(),NovoPetActivity.class);
            it.putExtra(AtributosUtil.PAR_ANIMAL,Animal);
            startActivityForResult(it,AtributosUtil.INTENT_NOVO_PET);
        }
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        loadListAnimals();
    }

    public class ListaAdocaoListeners implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Adocao adocao = adpAdocao.getItem(position);

            Intent it = new Intent(view.getContext(),NovoAdocaoActivity.class);
            it.putExtra(AtributosUtil.PAR_ADOCAO,adocao);
            startActivityForResult(it,AtributosUtil.INTENT_NOVO_ADOCAO);
        }
    }

    public class ListaPerdidoListeners implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Perdido perdido = adpPerdido.getItem(position);

            Intent it = new Intent(view.getContext(),NovoPerdidoActivity.class);
            it.putExtra(AtributosUtil.PAR_PERDIDO,perdido);
            startActivityForResult(it,AtributosUtil.INTENT_NOVO_PERDIDO);
        }
    }

    public void alterarTab(int index){
        if(index==0){
            lstAnimal.setVisibility(View.VISIBLE);
            lstPerdido.setVisibility(View.GONE);
            lstAdocao.setVisibility(View.GONE);
            loadListAnimals();

        }else if(index==1){
            lstAnimal.setVisibility(View.GONE);
            lstPerdido.setVisibility(View.GONE);
            lstAdocao.setVisibility(View.VISIBLE);
            loadListAdocao();
        }else if(index==2){
            lstAnimal.setVisibility(View.GONE);
            lstPerdido.setVisibility(View.VISIBLE);
            lstAdocao.setVisibility(View.GONE);
            loadListPerdido();
        }
    }

}
