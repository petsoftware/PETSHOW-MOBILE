package br.com.petshow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.petshow.R;
import br.com.petshow.model.Vacina;
import br.com.petshow.util.DateUtilsAndroid;

import static br.com.petshow.util.FacebookUtil.usuarioLogado;
/**
 * Created by bruno on 26/03/2017.
 */

public class VacinaArrayAdapter extends ArrayAdapter<Vacina> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public VacinaArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    private static class ViewHolder {

        TextView itemVacina_txtVTipo;
        TextView itemVacina_txtVData;
        TextView itemVacina_txtVCadastro;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);

            viewHolder.itemVacina_txtVTipo = (TextView) view.findViewById(R.id.itemVacina_txtVTipo);
            viewHolder.itemVacina_txtVData = (TextView) view.findViewById(R.id.itemVacina_txtVData);
            viewHolder.itemVacina_txtVCadastro = (TextView) view.findViewById(R.id.itemVacina_txtVCadastro);
            view.setTag(viewHolder);

        } else {
            viewHolder = (VacinaArrayAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }

        Vacina vacina = getItem(position);

        viewHolder.itemVacina_txtVTipo.setText(vacina.getTpVacina().getNome());
        viewHolder.itemVacina_txtVData.setText(DateUtilsAndroid.dateTo_ddMMYYYY(vacina.getData()));
        viewHolder.itemVacina_txtVCadastro.setText(context.getResources().getString(R.string.lblCadastradoPor)+" "+(vacina.getEstabelecimento().getId()==usuarioLogado.getId()?context.getResources().getString(R.string.lblvoce):vacina.getEstabelecimento().getNome()));

        return view;
    }


}