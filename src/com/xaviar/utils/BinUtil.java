package com.xaviar.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import java.nio.*;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.xaviar.collect.bin_zip_pictures.FileData;
import com.xaviar.ui.R;

public class BinUtil {

	
	
   public static String encodeBitmap(Bitmap bitmap,Bitmap.CompressFormat compressFormat)
   {
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	   bitmap.compress(compressFormat, 100, baos);  
       byte[] bytes = baos.toByteArray();
       byte [] encodedImage = Base64.encode(bytes, Base64.DEFAULT);
       return new String(encodedImage);
   }
   
   public static Bitmap decodeBitmap(String encodedImage)
   {
	   byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
	   Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
	   return decodedByte;
   }
	
	
	public static String FileToEncodeString(String pathToFile) throws IOException {    
		byte[] bytes = readFile(pathToFile);
		String encodedStr = EncodeUtil.encode(bytes);
		return encodedStr;
  }	
	
	
	public static Bitmap loadBinImageFromUrl(String url) {
		try {
			URLConnection connection = (new URL(url)).openConnection();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			byte[] imageData = baf.toByteArray();
			return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
		} catch (Exception exc) {
			return null;
		}
	}
	
	
	
	public static Bitmap getImageFromResourcde(Context ctx,int drawableResource)
	{
		// e.g. R.drawable.watermelon
		try{
			Bitmap image = BitmapFactory.decodeResource(ctx.getResources(),drawableResource);
			return image;							
			}
		catch(Exception e) {
			return null;
		}				
	}
	

	
	@Deprecated
	public static String getStrFromImg(Bitmap bitmap)
	{
		try{
			InputStream is = bitmapToInputStream(bitmap); 
			ByteArrayOutputStream  os = new ByteArrayOutputStream();
			FileUtil.streamCopy(is,os);
			byte [] imgBytes = os.toByteArray();
			return new String(imgBytes);								
			}
		catch(Exception e) {			return null;
		}				
	}
	
	
	public static String getStr(InputStream is)
	{
		try{
			ByteArrayOutputStream  os = new ByteArrayOutputStream();
			FileUtil.streamCopy(is,os);
			byte [] imgBytes = os.toByteArray();
			return new String(imgBytes);								
			}
		catch(Exception e) {			return null;
		}				
	}
	
	
	public static void getImgFromStr(Context ctx, String str) {
		try {
			byte[] decodedString = EncodeUtil.decode(str);	
			Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			Canvas canvas = new Canvas();
			Paint p=new Paint();	     
	        p.setColor(Color.RED);
	        canvas.drawBitmap(b, 0, 0, p);

			
			return;
		} catch (Exception e) {
			return;
		}
	}
	

	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}

	public static InputStream bitmapToInputStream(Bitmap bitmap) {
	    int size = bitmap.getHeight() * bitmap.getRowBytes();
	    ByteBuffer buffer = ByteBuffer.allocate(size);
	    bitmap.copyPixelsToBuffer(buffer);
	    return new ByteArrayInputStream(buffer.array());
	}

	public static FileData getFileDataOK(Context ctx) {
		Bitmap bmpOrigin1 = BinUtil.getImageFromResourcde(ctx, R.drawable.server_multiple);
    	String strdata1 = BinUtil.encodeBitmap(bmpOrigin1,Bitmap.CompressFormat.PNG);
		String type1 = "png";
		String name1 = "server_multiple.png";
		FileData fileData1 = new FileData(name1,type1,strdata1);
		return fileData1;
	}
	
	public static FileData getFileDataError(Context ctx) {
		Bitmap bmpOrigin1 = BinUtil.getImageFromResourcde(ctx, R.drawable.error1);
    	String strdata1 = BinUtil.encodeBitmap(bmpOrigin1,Bitmap.CompressFormat.PNG);
		String type1 = "png";
		String name1 = "error1.png";
		FileData fileData1 = new FileData(name1,type1,strdata1);
		return fileData1;
	}
	
	
	public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

}
  

	
	


