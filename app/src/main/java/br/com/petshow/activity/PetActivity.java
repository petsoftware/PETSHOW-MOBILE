package br.com.petshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by bruno on 21/03/2017.
 */

public class PetActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    public void goToPrincipal(View view){
        Intent it = new Intent(view.getContext(),PrincipalActivity.class);
        view.getContext().startActivity(it);
    }
    public void goToListPets(View view){
        Intent it = new Intent(view.getContext(),ListaPetsActivity.class);
        view.getContext().startActivity(it);
    }
    public void goToListMessages(View view){
        Intent it = new Intent(view.getContext(),ListaPetsActivity.class);
        view.getContext().startActivity(it);
    }
    public void goToListNotification(View view){
        Intent it = new Intent(view.getContext(),NotificationActivity.class);
        view.getContext().startActivity(it);
    }

}
