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
 * File Name: TripList.java
 * Description:
 * 
 * first activity to fire in app.
 * Displays the list of trips in a fragment.
 * Holds button to add a new trip
 * 
 */

package mobi.bwize.travelB;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class TripList extends FragmentActivity implements
TripListFragment.OnListSelectedListener {

	Trip_Database helper;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	
		setContentView(R.layout.list_container);

	}

	@Override
		protected void onResume() {
		super.onResume();
		// Refresh the list cursor every time the view is resumed
		TripListFragment.refresh();
	}

	// Method to start new trip
	public void addModule(View view) {
	// Declare new intent
		Intent intent = new Intent(this, AddTrip.class);
		// Start the activity
		startActivity(intent);
	}

	// method for action to take when an item is selected
	@Override
	public void onListSelected(long id) {
			Intent showContent = new Intent(getApplicationContext(),
					DashBoardActivity.class);
			// send the list item data
			showContent.putExtra("ID",String.valueOf(id));
			startActivity(showContent);
	}
}