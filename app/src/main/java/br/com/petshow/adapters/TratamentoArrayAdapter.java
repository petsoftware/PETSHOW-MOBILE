package br.com.petshow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.petshow.R;
import br.com.petshow.model.Tratamento;
import br.com.petshow.util.DateUtilsAndroid;

/**
 * Created by bruno on 26/03/2017.
 */

public class TratamentoArrayAdapter extends ArrayAdapter<Tratamento> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public TratamentoArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    private static class ViewHolder {

        TextView itemTratamento_txtNome;
        TextView itemTratamento_txtFrequencia;
        TextView itemTratamento_txtData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);

            viewHolder.itemTratamento_txtNome = (TextView) view.findViewById(R.id.itemTratamento_txtVNome);
            viewHolder.itemTratamento_txtFrequencia = (TextView) view.findViewById(R.id.itemTratamento_txtVFrequencia);
            viewHolder.itemTratamento_txtData = (TextView) view.findViewById(R.id.itemTratamento_txtVData);
            view.setTag(viewHolder);

        } else {
            viewHolder = (TratamentoArrayAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }

        Tratamento tratamento = getItem(position);
        viewHolder.itemTratamento_txtNome.setText(tratamento.getNm_tratamento());
        viewHolder.itemTratamento_txtFrequencia.setText(context.getResources().getString(R.string.lblFrequencia)+":"+tratamento.getFrequencia().toString());
        viewHolder.itemTratamento_txtData.setText(context.getResources().getString(R.string.lblData)+":"+ DateUtilsAndroid.dateTo_ddMMYYYY(tratamento.getDataInicio())+" a "+ DateUtilsAndroid.dateTo_ddMMYYYY(tratamento.getDataTermino()));
        return view;
    }


}