package br.com.petshow.adapters;

import br.com.petshow.model.Estado;

/**
 * Created by bruno on 16/03/2017.
 */

public class EstadoAdapterToArray extends Estado {

    @Override
    public String toString(){
        return this.getNome();
    }
}
