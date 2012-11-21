package mobi.bwize.project;

import java.util.prefs.Preferences;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TripList extends FragmentActivity implements
TripListFragment.OnListSelectedListener {

	private long currentID;
	private SQLiteDatabase database;
	Trip_Database helper;
	private static final int DIALOG_ID = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String string;
		
		Trip_Database helper = new Trip_Database(this);
		

		
		//setTheme(themeResource);
		super.onCreate(savedInstanceState);
		// Set the orientation, based on screen size
		/*if (isScreenLarge()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}*/
		// load the layout that contains the fragment (one for phone or two for
		// tablet)
		setContentView(R.layout.list_container);

	}

	// function to check screen size and return a bool indicating screen size
	public boolean isScreenLarge() {
		final int screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
				|| screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}
	@Override
		protected void onResume() {
		super.onResume();
		
		// Refresh the list cursor every time the view is resumed
		TripListFragment.refresh();
	}

	public void refreshUI(){
	
		finish();
		startActivity(getIntent());
	
	}
	
	

	// Method to send message
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
			Log.d("id",String.valueOf(id));
			startActivity(showContent);
	}

	// Method to delete a module - only used in the fragment layout on a tablet
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	menu.add(0, DIALOG_ID, 1, R.string.prefs1);
	//	menu.add(0, 1000, 1, "Refresh");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId,MenuItem item) {
		if(item.getItemId()==1000){
		refreshUI();	
		return true;
		}
		Intent intent = new Intent(this, Preferences.class);
		startActivity(intent);
		return true;
	}



}