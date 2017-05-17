package br.com.petshow.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;



public class ImagemUtil {

	public static String transformBase64AsString(Bitmap imagem){

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imagem.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		String base64= Base64.encodeToString(byteArray,Base64.DEFAULT);

		return base64;
	}
	
	public static String transformBase64AsString(byte[] imageAsByte){
		String base64AsString = new String(Base64.encode(imageAsByte,Base64.DEFAULT) );
		return base64AsString;
	}

	public static Bitmap transformBase64Bitmap(String imageAsByte){
		byte[] decodedBytes = Base64.decode(imageAsByte, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
}
