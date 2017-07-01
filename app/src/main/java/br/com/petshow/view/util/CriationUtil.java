package br.com.petshow.view.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

import java.util.List;

import br.com.petshow.R;
import br.com.petshow.activity.PetActivity;
import br.com.petshow.enums.EnumTipoAnimal;


public class CriationUtil {


    public static <T> ArrayAdapter<T> createArrayAdapter(Context ctx, Spinner spinner, List<T> data){

        return createArrayAdapter( ctx,  spinner,  data,android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);
    }

    public static <T> ArrayAdapter<T> createArrayAdapter(Context ctx, Spinner spinner, List<T> data,int layoutAdapter, int layoutDropDown){


        ArrayAdapter<T> arrayAdapter = new ArrayAdapter<T> (ctx,layoutAdapter,data);
        arrayAdapter.setDropDownViewResource(layoutDropDown);
        spinner.setAdapter(arrayAdapter);
        return arrayAdapter;
    }

    public static void  openProgressBar(ProgressDialog progressDialog){

        openProgressBar(progressDialog,progressDialog.getContext().getString(R.string.msgPadraoProgressDialog));
    }

    public static void openProgressBar(ProgressDialog progressDialog,String message){

        progressDialog.setMessage(message);

        progressDialog.show();



    }

    public static void closeProgressBar(ProgressDialog progressDialog){

        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
    public static void openAlertDialog(Context ctx,int idMessage, int title){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(idMessage).setTitle(title);
        builder.setPositiveButton(R.string.lblOK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
