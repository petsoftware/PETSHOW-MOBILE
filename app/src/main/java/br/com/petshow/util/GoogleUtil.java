package br.com.petshow.util;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import br.com.petshow.activity.PetActivity;



public class GoogleUtil {


    public static boolean isApiEnable(PetActivity petActivity){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int retorno =googleApiAvailability.isGooglePlayServicesAvailable (petActivity);

        if(retorno == ConnectionResult.SUCCESS){
            return true;
        }else if(retorno ==ConnectionResult.SERVICE_MISSING ||retorno ==ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED || retorno ==ConnectionResult.SERVICE_DISABLED ){
            googleApiAvailability.getErrorDialog(petActivity,retorno,AtributosUtil.INTENT_RETURN_GOOGLE_API_ERROR_DIALOG);
            return false;
        }else{
            return false;
        }

    }
}
