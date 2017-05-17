package br.com.petshow.util;



import java.util.Arrays;
import java.util.List;

/**
 * Created by bruno on 11/03/2017.
 */

public class AtributosUtil {
    /*id Intents*/
    public static final int INTENT_OPEN_GALLERY = 10001;
    public static final int INTENT_OPEN_CAMERA  = 10002;
    public static final int INTENT_NOVO_PET  = 10003;
    public static final int INTENT_RETURN_GOOGLE_API_ERROR_DIALOG  = 10004;
    public static final int INTENT_PERFIL  = 10005;
    public static final int INTENT_NOVO_ADOCAO  = 10006;
    public static final int INTENT_NOVO_PERDIDO  = 10007;

    // Acess
    public static final List<String> FACEBOOK_ACESS = Arrays.asList("email","public_profile","user_friends");

    // acesso
    //public static final String URL_BASE= "http://10.0.2.2:8080/Petshow-REST/rest/";

    //transferencia de parametros entre telas
    public static final String PAR_ANIMAL="PARAMETRO_ANIMAL";
    public static final String PAR_USUARIO="PARAMETRO_USUARIO";
    public static final String PAR_USUARIO_ID="PARAMETRO_USUARIO_ID";
    public static final String PAR_IS_LOGIN="ISLOGIN";
    public static final String  PAR_USUARIO_WITH_ANIMALS="USUARIO_WITH_ANIMALS";
    public static final String PAR_ADOCAO="PARAMETRO_ADOCAO";
    public static final String PAR_PERDIDO="PARAMETRO_PERDIDO";

    public static final String URL_BASE= "http://192.168.1.5:8080/Petshow-REST/rest/";

}
