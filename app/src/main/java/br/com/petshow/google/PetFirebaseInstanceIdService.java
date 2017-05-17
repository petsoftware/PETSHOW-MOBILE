package br.com.petshow.google;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.petshow.model.SmartphoneREG;
import br.com.petshow.util.FacebookUtil;
import br.com.petshow.util.MapErroRetornoRest;
import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.RequestPostEntity;

import static android.content.ContentValues.TAG;


public class PetFirebaseInstanceIdService extends FirebaseInstanceIdService  {
    private  static String token;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        token=refreshedToken;
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //if(FacebookUtil.usuarioLogado !=null) {
        //    sendRegistrationToServer(refreshedToken);
        //}
    }
    public static void sendRegistrationToServer() {
        // deletar TOKEN se tiver um facebook com token diferente
        //if(FacebookUtil.usuarioLogado!=null && token!=null) {
            SmartphoneREG smartphoneREG = new SmartphoneREG();
            smartphoneREG.setUsuario(FacebookUtil.usuarioLogado);
            smartphoneREG.setIdSmartPhoneFCM(FirebaseInstanceId.getInstance().getToken());

            new RequestPostEntity(new CallBack(null) {
                @Override
                public void successWithReturn(String json) {

                }

                @Override
                public void successNoReturn() {

                }

                @Override
                public void predictedError(MapErroRetornoRest map) {

                }
            }).execute("fcm/salvar/smart", smartphoneREG);
        //}
    }

    public static String getToken() {
        return token;
    }
}
