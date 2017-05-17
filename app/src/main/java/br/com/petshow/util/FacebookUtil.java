package br.com.petshow.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.IOException;

import br.com.petshow.activity.LoginActivity;
import br.com.petshow.activity.MainActivity;
import br.com.petshow.activity.PetActivity;
import br.com.petshow.exceptions.FacebookPermissionException;
import br.com.petshow.model.Usuario;

import br.com.petshow.web.util.CallBack;
import br.com.petshow.web.util.CallBackBitMap;
import br.com.petshow.web.util.LoadImageTask;
import br.com.petshow.web.util.RequestGetEntity;
import br.com.petshow.web.util.RequestPostEntity;

/**
 * Created by bruno on 09/03/2017.
 */

public class FacebookUtil {

    public static Usuario usuarioLogado;


    public static void loadProfileFacebook(PetActivity petActivity){
        if (Profile.getCurrentProfile() == null) {
            ProfileTracker profileTracker = new ProfileTracker() {
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    stopTracking();
                    Profile.setCurrentProfile(currentProfile);
                    Profile profile = Profile.getCurrentProfile();

                    new RequestGetEntity(new CallBack(petActivity) {

                        public void successWithReturn(String json)  {
                            if(json ==null){
                                salvarUsuario( null,profile.getName(),"semEmail@hotmail.com",Long.parseLong(profile.getId()),profile.getProfilePictureUri(200, 200).toString(),petActivity);
                            }else {

                                try {

                                    usuarioLogado = JsonUtil.transformObject(json, Usuario.class);
                                    salvarUsuario( usuarioLogado,profile.getName(),"semEmail@hotmail.com",Long.parseLong(profile.getId()),profile.getProfilePictureUri(200, 200).toString(),petActivity);
                                } catch (Throwable e) {
                                    Log.e("MainActivity", e.getMessage(), e);
                                }
                            }

                        }
                        public void successNoReturn() {}
                        public void predictedError(MapErroRetornoRest map) {
                            String abc="";

                        }

                    }).execute("usuario/facebook/"+profile.getId());

                    // atualizar foto,nome, setar o id,  criar registro senao existir e setar o id do facebook

                }
            };
            profileTracker.startTracking();

        }else if(usuarioLogado==null){
            Profile profile =Profile.getCurrentProfile();
            new RequestGetEntity(new CallBack(null) {

                public void successWithReturn(String json)  {
                    if(json ==null){
                        salvarUsuario( null,profile.getName(),"semEmail@hotmail.com",Long.parseLong(profile.getId()),profile.getProfilePictureUri(200, 200).toString(),petActivity);
                    }else {

                        try {

                            usuarioLogado = JsonUtil.transformObject(json, Usuario.class);
                            salvarUsuario( usuarioLogado,profile.getName(),"semEmail@hotmail.com",Long.parseLong(profile.getId()),profile.getProfilePictureUri(200, 200).toString(),petActivity);
                        } catch (Throwable e) {
                            Log.e("MainActivity", e.getMessage(), e);
                        }
                    }

                }
                public void successNoReturn() {}
                public void predictedError(MapErroRetornoRest map) {
                    String abc="";

                }

            }).execute("usuario/facebook/"+profile.getId());
        }


    }

    private static void salvarUsuario(Usuario usuario,String nome,String email,long idFacebook,String urlPhoto,PetActivity petActivity){
        // futuro : ver obrigação da foto de perfil
        boolean isUsuarioNull=usuario==null;

        if(isUsuarioNull) {
            usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setIdFacebook(idFacebook);
            usuario.setFlTpEstabelecimento("F");
            usuario.setEmail(email);
            usuario.setFlPreCadastro(false);
            usuario.setEmail("semEmail@hotmail.com");
            usuario.setPassword("semPassword4848648");
            usuario.setNmLogin("semNmLogin4848648");
            usuario.setURLFacebook(Profile.getCurrentProfile().getLinkUri().toString());

        }else{
            usuario.setNome(nome);
            usuario.setEmail(email);
           // usuario.setURLFacebook(Profile.getCurrentProfile().getLinkUri().toString());
        }

        new LoadImageTask(new CallBackBitMap(usuario) {

            Usuario usuarioInterno = (Usuario) getAuxObj();

            @Override
            public void successWithReturn(Bitmap ob) {

                usuarioInterno.setFoto(ImagemUtil.transformBase64AsString(ob));
                new RequestPostEntity(new CallBack(petActivity){

                    @Override
                    public void successWithReturn(String json) {
                        try {
                            if(usuarioLogado==null) {
                                usuarioLogado = JsonUtil.transformObject(json, Usuario.class);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(getContext() instanceof LoginActivity)
                            ((LoginActivity) getContext()).goToPrincipal();
                        else if (getContext() instanceof MainActivity)
                            ((MainActivity) getContext()).goToPrincipalScreen();
                    }

                    @Override
                    public void successNoReturn() {

                    }

                    @Override
                    public void predictedError(MapErroRetornoRest map) {

                    }
                }).execute("usuario/salvar",usuarioInterno);



            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {

            }
        }).execute(urlPhoto);



    }

    public static void postPhoto(Bitmap bitmap,String mensagem ) throws FacebookPermissionException{
        //Bitmap bitmap = BitmapFactory.decodeResource(petActivity.getResources(), R.drawable.perfil_no_photo);

        SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).setCaption(mensagem).build();

        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();


        ShareApi.share(content,null);
    }


/*      codigo para postar
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.perfil_no_photo);
        GraphRequest.newUploadPhotoRequest(AccessToken.getCurrentAccessToken(), "me/photos", bitmap, null, null, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Bundle params = new Bundle();
                params.putString("message", "This is a test message");
                params.putString("object_attachment","137531846778150");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/feed",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                            }
                        }
                ).executeAsync();
            }
        }).executeAsync();
*/

        /*Bundle params = new Bundle();
        params.putString("message", "This is a test message");

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            params.putString("object_attachment","137523180112350");
        }catch (Exception e){
            String aaa="";
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                    }
                }
        ).executeAsync();*/

    private static void salvarUsuario222(Usuario usuario,String nome,String email,long idFacebook,String urlPhoto,PetActivity petActivity){
        // futuro : ver obrigação da foto de perfil
        boolean isUsuarioNull=usuario==null;
        if(isUsuarioNull) {
            usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setIdFacebook(idFacebook);
            usuario.setFlTpEstabelecimento("F");
            usuario.setEmail(email);
            usuario.setFlPreCadastro(false);
            usuario.setEmail("semEmail@hotmail.com");
            usuario.setPassword("semPassword4848648");
            usuario.setNmLogin("semNmLogin4848648");
            usuario.setFoto("548484");
        }else{
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setFoto("548484");
        }

        new RequestPostEntity(new CallBack(petActivity){

            @Override
            public void successWithReturn(String json) {
                try {
                    if(usuarioLogado==null) {
                        usuarioLogado = JsonUtil.transformObject(json, Usuario.class);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(getContext() instanceof LoginActivity)
                    ((LoginActivity) getContext()).goToPrincipal();
                else if (getContext() instanceof MainActivity)
                    ((MainActivity) getContext()).goToPrincipalScreen();
            }

            @Override
            public void successNoReturn() {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {

            }
        }).execute("usuario/salvar",usuario);


    }

}
