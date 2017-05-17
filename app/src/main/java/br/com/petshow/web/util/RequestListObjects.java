package br.com.petshow.web.util;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;



import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import br.com.petshow.enums.EnumErrosSistema;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;

/**
 * Created by bruno on 14/03/2017.
 */

public class RequestListObjects extends AsyncTask<Object, Void, Object> {

    private CallBack callBack;

    public RequestListObjects(CallBack callBack) {
        super();
        this.callBack = callBack;
    }

    @Override
    protected Object doInBackground(Object... params) {
        String response = null;
        try {
            final String url = (String) params[0];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new PetResponseErrorHandler());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            response= restTemplate.getForObject(AtributosUtil.URL_BASE+url, String.class);
            return response;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            MapErroRetornoRest error = new MapErroRetornoRest(EnumErrosSistema.ERRO_INESPERADO, "Failed: HTTP error code:" + e.getMessage());
            String erroJson = JsonUtil.getJSON(error);

            return erroJson;

        }


    }

    @Override
    protected void onPostExecute(Object response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MapErroRetornoRest errorPredicted = mapper.readValue(response.toString(), MapErroRetornoRest.class);
            callBack.predictedError(errorPredicted);
        } catch (IOException e) {
            callBack.successWithReturn(response.toString());
        }


    }
}
