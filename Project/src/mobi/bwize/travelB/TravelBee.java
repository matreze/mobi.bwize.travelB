/* Group 2 
 * travelB - A user friendly travel planner
 * 
 * By:
 * Amabille Leal
 * Dominika Nowak
 * Alison Price
 * Michael Reid
 * 
 * Date: 30th November 2012
 * 
 * v 1.0
 * 
 * File Name: TravelBee.java
 * Description:
 * 
 * View that performs the animation activity.
 * 
 */

package mobi.bwize.travelB;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;


public class TravelBee extends View {
	Bitmap myBee;
	float posy;
	float posx;
	Typeface font;
	Paint textBee;
	

	public TravelBee(Context context) {
		super(context);
	
		myBee=BitmapFactory.decodeResource(getResources(), R.drawable.mini_bee);
		posy=0;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		posy=height/3;
		posx=width/3;
		font=Typeface.createFromAsset(context.getAssets(),"fonts/aero.ttf");
		textBee=new Paint();
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
				
		//setting the logo of TravelB visible on the screen
		canvas.drawColor(Color.WHITE);
		
		
		textBee.setARGB(150,255,255,0);
		textBee.setTextAlign(Align.CENTER);
		textBee.setTextSize(100);
		textBee.setTypeface(font);
		canvas.drawText("TravelB", canvas.getWidth()/2, 200, textBee);
		
		canvas.drawBitmap(myBee,posx, posy, null);
	//moving the image across the screen,starting from the middle
	if(posy >0 )
		posy-=3;
		posx+=3;
	invalidate();
	
	}
}

