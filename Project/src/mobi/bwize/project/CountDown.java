package mobi.bwize.project;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;


// TODO - consider making this a fragmentactivity
//TODO - Layout
public class CountDown extends Activity {
    CountDownTimer mCountDownTimer;
    long mMilliseconds = 0; // 60 seconds * 1000 milliseconds
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd 'Days'   HH:mm:ss 'To go'");
    TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        int dayValue = 20;
        int monthValue = 11;
        int hourValue = 15;
        int minuteValue = 30;
        
        //TODO - Get Time from DB
        
        Calendar countdownTimeEnd = Calendar.getInstance();
        countdownTimeEnd.setTimeZone(TimeZone.getTimeZone("GMT"));
        countdownTimeEnd.set(Calendar.DAY_OF_MONTH, dayValue);
        countdownTimeEnd.set(Calendar.MONTH, monthValue);
        countdownTimeEnd.set(Calendar.HOUR_OF_DAY, hourValue);
        countdownTimeEnd.set(Calendar.MINUTE, minuteValue);
        
        long currentTime=System.currentTimeMillis();
        mMilliseconds=countdownTimeEnd.getTimeInMillis()-currentTime;

        Log.d("millis",Integer.toString((int)mMilliseconds));
        
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        mTextView = (TextView) findViewById(R.id.textView1);

        mCountDownTimer = new CountDownTimer(mMilliseconds, 1000) {
            @Override
            public void onFinish() {
                mTextView.setText(mSimpleDateFormat.format(0));
                //mTextView.setText("Times Up!");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                mTextView.setText(mSimpleDateFormat.format(millisUntilFinished));
            }
        }.start();
    }
}