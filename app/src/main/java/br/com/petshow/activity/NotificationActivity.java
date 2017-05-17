package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import java.util.List;
import br.com.petshow.R;
import br.com.petshow.adapters.NotificacaoArrayAdapterNotificationAc;
import br.com.petshow.model.Notificacao;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.view.util.CriationUtil;
import br.com.petshow.view.util.MenuUtil;
import br.com.petshow.view.util.MessageUtil;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestListObjects;
import br.com.petshow.web.util.RequestPostEntity;

import static br.com.petshow.util.FacebookUtil.usuarioLogado;

public class NotificationActivity extends PetActivity {

    private NotificacaoArrayAdapterNotificationAc adpNotification;
    private ListView lstNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        startComponents();
        startVariables();
        loadListNotification();

    }

    public void startVariables(){


    }

    public void startComponents() {

        MenuUtil.loadToolBar(this,R.id.toolbar_activity_notification);
        progressDialog = new ProgressDialog(this);
        lstNotification=(ListView) findViewById(R.id.notification_list);
        lstNotification.setItemsCanFocus(false);

    }

    public void loadListNotification(){
        CriationUtil.openProgressBar(progressDialog);
        new RequestListObjects(new CallBack(this) {

            public void successWithReturn(String json)  {
                List<Notificacao> lista=null;
                try {

                    lista= JsonUtil.jsonToList(new Notificacao(), json,Notificacao.class);



                    adpNotification = new NotificacaoArrayAdapterNotificationAc (getContext(),R.layout.item_notificacao);

                    for(Notificacao notificacao:lista){
                        if(notificacao.isFlLida()==false){
                            changeToRead( notificacao);
                        }

                        adpNotification.add(notificacao);
                    }

                    lstNotification.setAdapter(adpNotification);
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

        }).execute("notificacao/usuario/"+usuarioLogado.getId());
    }

    public void changeToRead(Notificacao notificacao){
        notificacao.setFlLida(true);
        new RequestPostEntity(new CallBack(null) {

            public void successWithReturn(String json)  { }
            public void successNoReturn() {}
            public void predictedError(MapErroRetornoRest map) {
                  MessageUtil.messageWarning(getContext(),map.getMessage());

            }

        }).execute("notificacao/salvar",notificacao);
    }

}
