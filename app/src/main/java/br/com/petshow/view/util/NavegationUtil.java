package br.com.petshow.view.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

import br.com.petshow.activity.ListaPetsActivity;
import br.com.petshow.activity.MenuActivity;
import br.com.petshow.activity.PetActivity;
import br.com.petshow.util.AtributosUtil;

/**
 * Created by bruno on 21/03/2017.
 */

public class NavegationUtil {

    public static void goToMenu(AppCompatActivity apt){
        Intent it = new Intent(apt,MenuActivity.class);
        apt.startActivity(it);
    }

    public static void openGalleryPhoto(PetActivity petActivity){
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            File pictureDirect= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String picturePath =pictureDirect.getPath();
            Uri data = Uri.parse(picturePath);
            photoPicker.setDataAndType(data,"image/*");
            petActivity.startActivityForResult(photoPicker, AtributosUtil.INTENT_OPEN_GALLERY);

    }

    public static void openCamera(PetActivity petActivity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        petActivity.startActivityForResult(intent, AtributosUtil.INTENT_OPEN_CAMERA);
    }
}
