package br.com.petshow.web.util;

import android.content.Context;

import br.com.petshow.util.MapErroRetornoRest;

/**
 * Created by bruno on 04/03/2017.
 */

public abstract class CallBack {

    private  Context context ;
    public CallBack (Context ctx){
        this.context=ctx;
    }
    public abstract void successWithReturn (String json);// para chamadas com retorno
    public abstract void successNoReturn ();// para chamadas sem retorno
    public abstract void predictedError (MapErroRetornoRest map);
    public void before(){

    }

    public Context getContext() {
        return context;
    }
}
