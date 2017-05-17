package br.com.petshow.adapters;

import br.com.petshow.model.Cidade;

/**
 * Created by bruno on 16/03/2017.
 */

public class CidadeAdapterToArray extends Cidade {
    @Override
    public String toString() {
        return this.getNome();
    }
}
