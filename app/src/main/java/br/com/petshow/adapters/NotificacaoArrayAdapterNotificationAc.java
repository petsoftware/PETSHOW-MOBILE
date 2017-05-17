package br.com.petshow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.petshow.R;
import br.com.petshow.model.Animal;
import br.com.petshow.model.Notificacao;
import br.com.petshow.util.ImagemUtil;

/**
 * Created by bruno on 26/03/2017.
 */

public class NotificacaoArrayAdapterNotificationAc extends ArrayAdapter<Notificacao> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public NotificacaoArrayAdapterNotificationAc(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    private static class ViewHolder {
        ImageView itemNotificacao_imgFoto;
        TextView itemNotificacao_txtMensagem;
        TextView itemNotificacao_txtData;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);

            viewHolder.itemNotificacao_imgFoto = (ImageView) view.findViewById(R.id.itemNotificacao_imgFoto);
            viewHolder.itemNotificacao_txtMensagem = (TextView) view.findViewById(R.id.itemNotificacao_txtMensagem);
            viewHolder.itemNotificacao_txtData = (TextView) view.findViewById(R.id.itemNotificacao_txtData);

            view.setTag(viewHolder);

        } else {
            viewHolder = (NotificacaoArrayAdapterNotificationAc.ViewHolder) convertView.getTag();
            view = convertView;
        }

        Notificacao notificacao = getItem(position);

        viewHolder.itemNotificacao_txtMensagem.setText(notificacao.getMsgNotificacao());


        if(notificacao.getUsuarioRemetente().getFoto() != null && !notificacao.getUsuarioRemetente().getFoto().trim().equals("")) {
            viewHolder.itemNotificacao_imgFoto.setImageBitmap(ImagemUtil.transformBase64Bitmap(notificacao.getUsuarioRemetente().getFoto()));
        }else{
            viewHolder.itemNotificacao_imgFoto.setImageResource(R.drawable.perfil_no_photo);
        }
        Date date = notificacao.getDtNotificacao();


        String dateFormatada = new SimpleDateFormat("dd/MM/yyyy 'as' kk:mm").format(date);

        viewHolder.itemNotificacao_txtData.setText(dateFormatada);
        return view;
    }


}