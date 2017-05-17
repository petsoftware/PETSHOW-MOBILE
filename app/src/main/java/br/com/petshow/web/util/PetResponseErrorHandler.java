package br.com.petshow.web.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Created by bruno on 25/03/2017.
 */

public class PetResponseErrorHandler extends DefaultResponseErrorHandler {

    public boolean hasError(ClientHttpResponse response) throws IOException {
        String aaa="";
        HttpStatus aaaaa=response.getStatusCode();

        if(aaaaa.value()==400){
            return false;
        }

        return hasError(response.getStatusCode());
    }
}
