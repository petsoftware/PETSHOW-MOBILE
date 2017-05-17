package br.com.petshow.adapters;

import br.com.petshow.model.Racas;

/**
 * Created by bruno on 23/03/2017.
 */

public class RacaAdapterToArray extends Racas {

    public String toString(){
        if(this.getDescricao()==null || this.getDescricao().trim().equals("")){
            return "Selecione a raça";
        }else{
            return this.getDescricao();
        }
    }
}
