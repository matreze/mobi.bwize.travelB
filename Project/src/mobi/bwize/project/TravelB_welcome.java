package mobi.bwize.project;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class TravelB_welcome extends Activity {
	
	TravelBee beeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//setting the layout of the welcome screen 
		beeView= new TravelBee(this);
		setContentView(beeView);
		//setting sound for the welcome screen using MediaPlayer
		MediaPlayer travelBSound=MediaPlayer.create(this,R.raw.bee01);
		final Intent openMainScreen=new Intent(this, TripList.class);
		travelBSound.start();
		Thread timer=new Thread(){
		public void run(){
			try{
				sleep(3500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}finally{
				//starting the main screen for the application 
				startActivity(openMainScreen);	
			}		
		}	
		};	
		timer.start();
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
