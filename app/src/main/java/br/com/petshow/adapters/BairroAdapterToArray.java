package br.com.petshow.adapters;

import br.com.petshow.model.Bairro;

/**
 * Created by bruno on 16/03/2017.
 */

public class BairroAdapterToArray extends Bairro {
    @Override
    public String toString() {
        return this.getNome();
    }
}
