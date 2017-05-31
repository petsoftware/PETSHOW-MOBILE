package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.petshow.R;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestGetHashMap;

public class ListaVermifugoActivity extends PetActivity {

    //private VermifugoArrayAdapter adpVermifugo;
    private ListView lstVermifugo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vermifugo);

        startComponents();
        startVariables();
        loadListVermifugo();
    }

    public void startVariables(){


    }

    public void startComponents() {

        MenuUtil.loadToolBar(this,R.id.toolbar_activity_amigo);
        progressDialog = new ProgressDialog(this);
        lstVermifugo=(ListView) findViewById(R.id.vacina_listVacina);

        lstVermifugo.setItemsCanFocus(false);
        lstVermifugo.setOnItemClickListener( new ListaVermifugoActivity.VermifugoListeners());

    }

    public void loadListVermifugo(){
//        CriationUtil.openProgressBar(progressDialog);
//        new RequestGetHashMap(new CallBack(this) {
//
//            public void successWithReturn(String json)  {
//                HashMap<String,Object> resposta;
//                try {
//
//                    resposta= JsonUtil.transformObject(json,HashMap.class);
//                    ArrayList<HashMap<String,Object>> retornoListaAmigo = (ArrayList<HashMap<String,Object>>) resposta.get("listaAmigo");
//
//                    adpVermifugo = new VermifugoArrayAdapter (getContext(),R.layout.item_amigo);
//
//                    for(HashMap<String,Object> o:retornoListaAmigo){
//                        adpVermifugo.add(o);
//                    }
//                    //adpAmigos.add(retornoListaAmigo);
//
//
//                    lstVermifugo.setAdapter(adpVermifugo);
//                    CriationUtil.closeProgressBar(progressDialog);
//
//
//                } catch (Exception e) {
//                    CriationUtil.closeProgressBar(progressDialog);
//                    MessageUtil.messageErro(getContext(),e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//            public void successNoReturn() {}
//            public void predictedError(MapErroRetornoRest map) {
//                CriationUtil.closeProgressBar(progressDialog);
//                MessageUtil.messageWarning(getContext(),map.getMessage());
//
//            }
//
//        }).execute("usuario/amigos/"+usuarioLogado.getId());
    }

    public class VermifugoListeners implements AdapterView.OnItemClickListener{

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            HashMap<String,Object> vermifugo = (HashMap<String,Object>) adpVermifugo.getItem(position);
//            Intent it = new Intent(view.getContext(),EdtVermifugoActivity.class);
//            it.putExtra(AtributosUtil.PAR_VERMIFUGO,vermifugo);//usuario.get("uID").toString());
//            startActivityForResult(it, AtributosUtil.INTENT_NOVO_VERMIFUGO);
        }


    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        loadListVermifugo();
    }

}
