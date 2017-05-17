package br.com.petshow.view.util;


import android.graphics.Color;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import br.com.petshow.R;
import br.com.petshow.activity.PetActivity;

/**
 * Created by bruno on 21/03/2017.
 */

public class MenuUtil {
    public static void loadMenuGlobal(PetActivity petActivity, Menu menu){
        MenuInflater inflater = petActivity.getMenuInflater();
        inflater.inflate(R.menu.menu_global,menu);

    }
    public static void loadToolBar(PetActivity petActivity, int idToolBar) {
        Toolbar toolbar = (Toolbar) petActivity.findViewById(idToolBar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setTitleTextColor(Color.WHITE);

        petActivity.setSupportActionBar(toolbar);
        petActivity.getSupportActionBar().setTitle(petActivity.getTitle());
        petActivity.getSupportActionBar().setHomeButtonEnabled(true);
        petActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
