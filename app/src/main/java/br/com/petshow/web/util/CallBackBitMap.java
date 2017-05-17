package br.com.petshow.web.util;

import android.graphics.Bitmap;

import br.com.petshow.activity.PetActivity;
import br.com.petshow.util.MapErroRetornoRest;

/**
 * Created by bruno on 10/03/2017.
 */

public abstract class CallBackBitMap {

    private Object auxObj;

    public CallBackBitMap(){

    }
    public CallBackBitMap(Object auxObj){
        this.auxObj=auxObj;
    }



    public abstract void successWithReturn (Bitmap ob);// para chamadas com retorno
    public abstract void successNoReturn ();// para chamadas sem retorno
    public abstract void predictedError (MapErroRetornoRest map);
    public void before(){

    }


    public Object getAuxObj() {
        return auxObj;
    }
}
