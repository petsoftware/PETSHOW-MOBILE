package br.com.petshow.web.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import br.com.petshow.enums.EnumErrosSistema;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;

public class LoadImageTask extends AsyncTask<String, Void, Object> {

    public CallBackBitMap callBack;
    public LoadImageTask(CallBackBitMap callBack) {

        this.callBack= callBack;
    }



    @Override
    protected Object doInBackground(String... args) {

        try {

            return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

        } catch (Exception e) {

            MapErroRetornoRest error = new MapErroRetornoRest(EnumErrosSistema.ERRO_INESPERADO, "Failed: HTTP error code:"+e.getMessage());

            String erroJson = JsonUtil.getJSON(error);

            return erroJson;

        }
    }

    @Override
    protected void onPostExecute(Object response) {

        try{
            if(response == null) {
                callBack.successWithReturn(null);
            }
            ObjectMapper mapper = new ObjectMapper();
            MapErroRetornoRest errorPredicted = mapper.readValue(response.toString(), MapErroRetornoRest.class);
            callBack.predictedError(errorPredicted);
        } catch (IOException e) {
            callBack.successWithReturn((Bitmap) response);
        }
    }
}