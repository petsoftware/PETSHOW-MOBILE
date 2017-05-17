package br.com.petshow.view.util;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.petshow.R;

/**
 * Created by bruno on 18/03/2017.
 */

public class MessageUtil {

    public static void messageSucess(Context context,String msg){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view =inflater.inflate(R.layout.toast_sucess, null);

        TextView txtMsg=(TextView) view.findViewById(R.id.toast_sucess_txtMsg);
        txtMsg.setText(msg);

        Toast toast=new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();


    }

    public static void messageWarning(Context context,String msg){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view =inflater.inflate(R.layout.toast_warning, null);

        TextView txtMsg=(TextView) view.findViewById(R.id.toast_warning_txtMsg);
        txtMsg.setText(msg);

        Toast toast=new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void messageErro(Context context,String msg){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view =inflater.inflate(R.layout.toast_error, null);

        TextView txtMsg=(TextView) view.findViewById(R.id.toast_error_txtMsg);
        txtMsg.setText(msg);

        Toast toast=new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
