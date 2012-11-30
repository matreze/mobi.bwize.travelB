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
 * File Name: TipDisplay.java
 * Description:
 * 
 * Displays the tip with message sent from the tip
 * notification/alarm
 * 
 */

package mobi.bwize.travelB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TipDisplay extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip);
		
		// Get intent and set textView
		Intent intent=getIntent();
		
		TextView title=(TextView) findViewById(R.id.tipTitle);
		TextView tip=(TextView) findViewById(R.id.tipBody);
		
		title.setText(intent.getStringExtra("title"));
		tip.setText(intent.getStringExtra("tip"));
	}
}
