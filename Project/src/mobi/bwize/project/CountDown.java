package mobi.bwize.project;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class CountDown extends Fragment {
    CountDownTimer mCountDownTimer;
    long mMilliseconds = 0; // 60 seconds * 1000 milliseconds
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("'Only'  D 'Days, ' HH' hours, 'mm' min, ' ss' sec To Go'");
    TextView mTextView;
    ViewGroup root;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    
        
        root = (ViewGroup) inflater.inflate(R.layout.main, null);
        //return root;
        
       
		return root;
    }
    
    void countDown(String date,String time){
    	
    
    	String dateParsed=date.replaceAll("\\s","");
    	String timeParsed=time.replaceAll("\\s","");
    	
    	String[] tempDate;
    	String[] tempTime;
    	 
    	  /* delimiter */
    	  String delimiter = "-";
    	  /* given string will be split by the argument delimiter provided. */
    	  tempDate = dateParsed.split(delimiter);
    	  
    	  /* delimiter */
    	  delimiter = ":";
    	  /* given string will be split by the argument delimiter provided. */
    	  tempTime = timeParsed.split(delimiter);
    	

    	 int dayValue = Integer.parseInt(tempDate[1]);
         int monthValue = Integer.parseInt(tempDate[0]);
         int yearValue=Integer.parseInt(tempDate[2]);
         int hourValue = Integer.parseInt(tempTime[0]);
         int minuteValue = Integer.parseInt(tempTime[1]);
         
         //TODO - Get Time from DB
         
         Calendar countdownTimeEnd = Calendar.getInstance();
         countdownTimeEnd.setTimeZone(TimeZone.getTimeZone("GMT"));
         countdownTimeEnd.set(Calendar.DAY_OF_MONTH, dayValue);
         countdownTimeEnd.set(Calendar.MONTH, monthValue-1);
         countdownTimeEnd.set(Calendar.YEAR, yearValue);
         countdownTimeEnd.set(Calendar.HOUR_OF_DAY, hourValue);
         countdownTimeEnd.set(Calendar.MINUTE, minuteValue);
         
         long currentTime=System.currentTimeMillis();
         mMilliseconds=countdownTimeEnd.getTimeInMillis()-currentTime;


         
         mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
         mTextView = (TextView) root.findViewById(R.id.textView1);

         mCountDownTimer = new CountDownTimer(mMilliseconds, 1000) {
             @Override
             public void onFinish() {
                 mTextView.setText(mSimpleDateFormat.format(0));
                 mTextView.setText("Enjoy your Trip!");
             }

             @Override
             public void onTick(long millisUntilFinished) {
                 mTextView.setText(mSimpleDateFormat.format(millisUntilFinished));
             }
         }.start();
    	
    	
    }
 
}
