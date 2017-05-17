package br.com.petshow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.petshow.R;
import br.com.petshow.model.Animal;
import br.com.petshow.util.ImagemUtil;

/**
 * Created by bruno on 26/03/2017.
 */

public class AnimalArrayAdapterListaPets extends ArrayAdapter<Animal> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public AnimalArrayAdapterListaPets(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    private static class ViewHolder {
        ImageView itemAnimal_imgFoto;
        TextView itemAnimal_txtNome;
        TextView itemAnimal_txtRaca;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);

            viewHolder.itemAnimal_imgFoto = (ImageView) view.findViewById(R.id.itemAnimal_imgPerfil);
            viewHolder.itemAnimal_txtNome = (TextView) view.findViewById(R.id.itemAnimal_txtNome);
            viewHolder.itemAnimal_txtRaca = (TextView) view.findViewById(R.id.itemAnimal_txtRaca);
            view.setTag(viewHolder);

        } else {
            viewHolder = (AnimalArrayAdapterListaPets.ViewHolder) convertView.getTag();
            view = convertView;
        }

        Animal animal = getItem(position);

        viewHolder.itemAnimal_txtNome.setText(animal.getNome());
        viewHolder.itemAnimal_txtRaca.setText(animal.getRaca());
        if(animal.getFotoPerfil() != null && !animal.getFotoPerfil().trim().equals("")) {
            viewHolder.itemAnimal_imgFoto.setImageBitmap(ImagemUtil.transformBase64Bitmap(animal.getFotoPerfil()));
        }else{
            viewHolder.itemAnimal_imgFoto.setImageResource(R.drawable.perfil_no_photo);
        }

        return view;
    }


}