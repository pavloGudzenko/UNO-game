package com.example.uno;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class TitleView extends View {
	     private Bitmap titleGraphic;
	     private Bitmap startButtonUp;
	     private Bitmap startButtonDown;
	     private Bitmap rulesButtonUp;
	     private Bitmap rulesButtonDown;

	     private int screenW;
	     private int screenH;
	     private boolean playButtonPressed;
	     private boolean rulesButtonPressed;
	     private Context myContext;
	     
	
	public TitleView(Context context) {
		super(context);
		  myContext = context;
		  titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.load_view);
		  startButtonUp =  BitmapFactory.decodeResource(getResources(), R.drawable.start_button_up_new);
		  startButtonDown =  BitmapFactory.decodeResource(getResources(), R.drawable.start_button_down_new);
		  rulesButtonUp =  BitmapFactory.decodeResource(getResources(), R.drawable.rules_button_up);
		  rulesButtonDown =  BitmapFactory.decodeResource(getResources(), R.drawable.rules_button_down);

		}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();
	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);
	    // recreate the new Bitmap
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	@Override
	public void onSizeChanged (int w, int h, int oldw,
			int oldh){ 
			super.onSizeChanged(w, h, oldw, oldh);
			screenW = w;
			screenH = h;
			}
	
		@Override
		protected void onDraw(Canvas canvas) {
			titleGraphic = getResizedBitmap(titleGraphic, screenH, screenW); 
			canvas.drawBitmap(titleGraphic,
					(screenW-titleGraphic.getWidth())/2, (screenH-titleGraphic.getHeight())/2, null);
			if (playButtonPressed == false){
			    canvas.drawBitmap(startButtonUp, (screenW-startButtonUp.getWidth())/2, (int)(screenH*0.25), null);
			} else {
				canvas.drawBitmap(startButtonDown, (screenW-startButtonUp.getWidth())/2, (int)(screenH*0.25), null);	
			}
			
			
			if (rulesButtonPressed == false){
			    canvas.drawBitmap(rulesButtonUp, (screenW-rulesButtonUp.getWidth())/2, (int)(screenH - screenH*0.25 - rulesButtonUp.getHeight()), null);
			} else {
				canvas.drawBitmap(rulesButtonDown, (screenW-rulesButtonUp.getWidth())/2, (int)(screenH - screenH*0.25 - rulesButtonUp.getHeight()), null);	
			}
		}
		
		public boolean onTouchEvent(MotionEvent event) { 
		int eventaction = event.getAction();
		int X = (int)event.getX();
		int Y = (int)event.getY();
		
		switch (eventaction ) {
		case MotionEvent.ACTION_DOWN:
			if (X > (screenW-startButtonUp.getWidth())/2 &&	
					X < ((screenW-startButtonUp.getWidth())/2 + startButtonUp.getWidth()) && 
					Y > (int)(screenH*0.25) && 	
					Y < (int)(screenH*0.25) + startButtonUp.getHeight())	{
					playButtonPressed = true;
					} else {
						
						if (X > (screenW-startButtonUp.getWidth())/2 &&	
								X < ((screenW-rulesButtonUp.getWidth())/2 + rulesButtonUp.getWidth()) && 
								Y > (int)(screenH - screenH*0.25 - rulesButtonUp.getHeight()) && 	
								Y < (int)(screenH - screenH*0.25 - rulesButtonUp.getHeight()) + rulesButtonUp.getHeight())	{
								rulesButtonPressed = true;
								}			
					}
		break;
		case MotionEvent.ACTION_MOVE:
		break;
		case MotionEvent.ACTION_UP:
			if (playButtonPressed) {
				Intent gameIntent = new Intent(myContext,
				GameActivity.class); 
				myContext.startActivity(gameIntent); 
				}	
			if (rulesButtonPressed) {
				}	
			playButtonPressed = false;
			rulesButtonPressed = false;
		break;
		
		}
		
		invalidate();
		return true;
		}
		
		
}
