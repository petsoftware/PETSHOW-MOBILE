package br.com.petshow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.petshow.R;
import br.com.petshow.model.Notificacao;
import br.com.petshow.util.ImagemUtil;

/**
 * Created by bruno on 26/03/2017.
 */

public class AmigoArrayAdapter extends ArrayAdapter<Object> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public AmigoArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    private static class ViewHolder {
        ImageView itemAnimal_imgPerfil;

        ImageView itemAmigo_imgAnimal1;
        ImageView itemAmigo_imgAnimal2;
        ImageView itemAmigo_imgAnimal3;
        ImageView itemAmigo_imgAnimal4;

        ImageView itemAmigo_imgCaes;
        ImageView itemAmigo_imgGatos;
        ImageView itemAmigo_imgPassaros;
        ImageView itemAmigo_imgOutros;

        TextView itemAmigo_txtNome;
        TextView itemAmigo_txtqtdCaes;
        TextView itemAmigo_txtqtdGato;
        TextView itemAmigo_txtqtdPassaros;
        TextView itemAmigo_txtqtdOutros;

        LinearLayout layoutFtAnimal ;
        LinearLayout layoutFtGenerico ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);

            viewHolder.itemAnimal_imgPerfil = (ImageView) view.findViewById(R.id.itemAmigo_imgPerfil);

            viewHolder.itemAmigo_imgAnimal1 = (ImageView) view.findViewById(R.id.itemAmigo_imgAnimal1);
            viewHolder.itemAmigo_imgAnimal2 = (ImageView) view.findViewById(R.id.itemAmigo_imgAnimal2);
            viewHolder.itemAmigo_imgAnimal3 = (ImageView) view.findViewById(R.id.itemAmigo_imgAnimal3);
            viewHolder.itemAmigo_imgAnimal4 = (ImageView) view.findViewById(R.id.itemAmigo_imgAnimal4);

            viewHolder.itemAmigo_imgCaes = (ImageView) view.findViewById(R.id.itemAmigo_imgCaes);
            viewHolder.itemAmigo_imgGatos = (ImageView) view.findViewById(R.id.itemAmigo_imgGatos);
            viewHolder.itemAmigo_imgPassaros = (ImageView) view.findViewById(R.id.itemAmigo_imgPassaros);
            viewHolder.itemAmigo_imgOutros = (ImageView) view.findViewById(R.id.itemAmigo_imgOutros);

            viewHolder.itemAmigo_txtNome = (TextView) view.findViewById(R.id.itemAmigo_txtNome);
            viewHolder.itemAmigo_txtqtdCaes = (TextView) view.findViewById(R.id.itemAmigo_txtqtdCaes);
            viewHolder.itemAmigo_txtqtdGato = (TextView) view.findViewById(R.id.itemAmigo_txtqtdGato);
            viewHolder.itemAmigo_txtqtdPassaros = (TextView) view.findViewById(R.id.itemAmigo_txtqtdPassaros);
            viewHolder.itemAmigo_txtqtdOutros = (TextView) view.findViewById(R.id.itemAmigo_txtqtdOutros);

            viewHolder.layoutFtAnimal = (LinearLayout ) view.findViewById(R.id.itemAmigo_fotoAnimalLay);
            viewHolder.layoutFtGenerico = (LinearLayout ) view.findViewById(R.id.itemAmigo_imgAnimalLay);

            view.setTag(viewHolder);

        } else {
            viewHolder = (AmigoArrayAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }

        HashMap<String,Object> linha= (HashMap<String,Object>) getItem(position);

        if(linha.get("uFoto") != null && !linha.get("uFoto").toString().trim().equals("")) {
            viewHolder.itemAnimal_imgPerfil.setImageBitmap(ImagemUtil.transformBase64Bitmap(linha.get("uFoto").toString()));
        }else{
            viewHolder.itemAnimal_imgPerfil.setImageResource(R.drawable.perfil_no_photo);
        }
        viewHolder.itemAmigo_txtNome.setText(linha.get("uNome").toString());

        ArrayList<HashMap<String,String>> animais = (ArrayList<HashMap<String,String>>) linha.get("animais");

        if(animais.size()<=4){
            viewHolder.layoutFtAnimal.setVisibility(View.VISIBLE);
            viewHolder.layoutFtGenerico.setVisibility(View.GONE);
            if(animais.size()>0) {
                viewHolder.itemAmigo_imgAnimal1.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(0).get("aFoto").toString()));
                viewHolder.itemAmigo_imgAnimal1.setVisibility(View.VISIBLE);
            }
            if(animais.size()>1) {
                viewHolder.itemAmigo_imgAnimal2.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(1).get("aFoto").toString()));
                viewHolder.itemAmigo_imgAnimal2.setVisibility(View.VISIBLE);
            }
            if(animais.size()>2) {
                viewHolder.itemAmigo_imgAnimal3.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(2).get("aFoto").toString()));
                viewHolder.itemAmigo_imgAnimal3.setVisibility(View.VISIBLE);

            }
            if(animais.size()>3) {
                viewHolder.itemAmigo_imgAnimal4.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(3).get("aFoto").toString()));
                viewHolder.itemAmigo_imgAnimal4.setVisibility(View.VISIBLE);
            }
        }else{
            viewHolder.layoutFtAnimal.setVisibility(View.GONE);
            viewHolder.layoutFtGenerico.setVisibility(View.VISIBLE);

            int qtdCaes=0;
            int qtdGatos=0;
            int qtdPassaros=0;
            int qtdOutros=0;

            for(HashMap<String,String> tipo:animais){
                if(tipo.get("aTp").toString().equals("0")){
                    ++qtdCaes;
                }else if(tipo.get("aTp").toString().equals("1")){
                    ++qtdGatos;
                }else if(tipo.get("aTp").toString().equals("2")){
                    ++qtdPassaros;
                }else{
                    ++qtdOutros;
                }
            }


            if(qtdCaes>0){
                viewHolder.itemAmigo_imgCaes.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(0).get("aFoto").toString()));
                viewHolder.itemAmigo_txtqtdCaes.setText(qtdCaes+"");
            }else{
                viewHolder.itemAmigo_imgCaes.setVisibility(View.GONE);
                viewHolder.itemAmigo_txtqtdCaes.setVisibility(View.GONE);
            }
            if(qtdGatos>0){
                viewHolder.itemAmigo_imgGatos.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(0).get("aFoto").toString()));
                viewHolder.itemAmigo_txtqtdGato.setText(qtdGatos+"");
            }else{
                viewHolder.itemAmigo_imgGatos.setVisibility(View.GONE);
                viewHolder.itemAmigo_txtqtdGato.setVisibility(View.GONE);
            }
            if(qtdPassaros>0){
                viewHolder.itemAmigo_imgPassaros.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(0).get("aFoto").toString()));
                viewHolder.itemAmigo_txtqtdPassaros.setText(qtdPassaros+"");
            }else{
                viewHolder.itemAmigo_imgPassaros.setVisibility(View.GONE);
                viewHolder.itemAmigo_txtqtdPassaros.setVisibility(View.GONE);
            }
            if(qtdOutros>0){
                viewHolder.itemAmigo_imgOutros.setImageBitmap(ImagemUtil.transformBase64Bitmap(animais.get(0).get("aFoto").toString()));
                viewHolder.itemAmigo_txtqtdOutros.setText(qtdOutros+"");
            }else{
                viewHolder.itemAmigo_imgOutros.setVisibility(View.GONE);
                viewHolder.itemAmigo_txtqtdOutros.setVisibility(View.GONE);
            }








        }



        return view;
    }


}