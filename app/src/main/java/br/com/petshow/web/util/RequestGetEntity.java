package br.com.petshow.web.util;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import br.com.petshow.enums.EnumErrosSistema;
import br.com.petshow.util.AtributosUtil;
import br.com.petshow.util.JsonUtil;
import br.com.petshow.util.MapErroRetornoRest;
/*
 new RequestGetEntity(new CallBack() {
            @Override
            public void success(String ob) {

            }

            @Override
            public void predictedError(MapErroRetornoRest map) {

            }


            public void run(Object greeting) {
                // TextView greetingIdText = (TextView) findViewById(R.id.id_value);
             //   EditText greetingContentText = (EditText) findViewById(R.id.txtRetorno);
                //greetingIdText.setText(greeting.getId());
            //    greetingContentText.setText(((GreetingFilho)greeting).getContent());
            }
        }).execute("http://rest-service.guides.spring.io/greeting",new GreetingFilho());

 */
public class RequestGetEntity extends AsyncTask<Object, Void, Object> {

    private CallBack callBack;

    public RequestGetEntity(CallBack callBack){
        super();
        this.callBack=callBack;
    }

    @Override
    protected Object doInBackground(Object... params) {
        String response =null;
        try {
                final String url = (String) params[0];

            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter() );
            restTemplate.setErrorHandler(new PetResponseErrorHandler());
            response= restTemplate.getForObject(AtributosUtil.URL_BASE+url, String.class);

            return response;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);

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
            }else {
                ObjectMapper mapper = new ObjectMapper();
                MapErroRetornoRest errorPredicted = mapper.readValue(response.toString(), MapErroRetornoRest.class);
                callBack.predictedError(errorPredicted);
            }
        } catch (IOException e) {
           // Log.e("MainActivity", e.getMessage(), e);
            callBack.successWithReturn(response.toString());
        }



    }

}