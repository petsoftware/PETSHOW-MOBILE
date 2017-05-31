package br.com.petshow.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by bruno on 19/03/2017.
 */

public class ValidationAndroidUtil {

    public static void setFocus(Context context,Object component){
        setFocus(context, component,true);
    }

    public static void setFocus(Context context,Object component,boolean openKeyboard){

        if(component instanceof EditText){
            EditText editText=(EditText) component;
            editText.requestFocus();
            if(openKeyboard){
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
        if(component instanceof Spinner){
            Spinner spinner =(Spinner)component;
            spinner.requestFocus();
        }

    }
}
