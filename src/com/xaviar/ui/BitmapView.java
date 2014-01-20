package com.xaviar.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.xaviar.collect.bin_zip_pictures.FileData;
import com.xaviar.utils.BinUtil;

public class BitmapView  extends View {

	private static final String TAG = "BitmapView";
	private FileData fileData = null;
	
	Context ctx;
	
    public BitmapView(Context context,FileData fileData) {
        super(context);
        this.ctx = context;
        this.fileData =fileData;
    }

    @Override
    public void onDraw(Canvas canvas) {
    	
    	
    	String str = fileData.getData();
    	Bitmap bmpReconstructed = BinUtil.decodeBitmap(str);
    	Log.d(TAG,"Success to request server state!!");
    	drawBitmap(bmpReconstructed,canvas);
    }

	private void drawBitmap(Bitmap bmp, Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        canvas.drawBitmap(bmp, 10, 10, null);
        Log.d(TAG,"Success drawBitmap(..)");	
		
	}
}
