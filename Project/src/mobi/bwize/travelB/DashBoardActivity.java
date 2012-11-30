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
 * File Name: DashBoardActivity.java
 * Description:
 * 
 * This is the main screen for each trip, providing various
 * pieces of information and links to others.
 * 
 * Based on fragments, so as to modularize it a bit more.
 * 
 */

package mobi.bwize.travelB;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoardActivity extends FragmentActivity {

	ImageButton backButton, viewButton;
	TextView txtDest;
	String details[];
	String destination[];
	String departure[];
	String cityParsed;
	String countryParsed;
	int id;
	String message;
	CountDown countDownFragment; 


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the layout
		setContentView(R.layout.activity_dashboard);
		// Fix the screen orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Open a new fragment manager
		FragmentManager fragmentManager = getSupportFragmentManager();

		// The the id of the trip from the calling activity
		Intent intent = getIntent();
		// check the string from the previous activity
		message = intent.getStringExtra("ID");
		id = Integer.valueOf(message);

		// Open new database connection
		Trip_Database db = new Trip_Database(this);
		try {
			db.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Get the destination name from the last activity to populate the dashboard
		details = db.getTripDetails(Integer.valueOf(message));
		destination = db.get_Destination(id);
		departure = db.get_ReturnInfo(id);

		// Parse the city and country values to replace spaces for urls
		cityParsed = destination[1].replaceAll("\\s", "%20");
		countryParsed = destination[0].replaceAll("\\s", "%20");

		// Set the weather fragment
		CurrentWeather currentWeatherDownFragment = (CurrentWeather) fragmentManager
				.findFragmentById(R.id.frag_2);
		currentWeatherDownFragment.setCurrentWeather(countryParsed, cityParsed);

		// Set the to do list fragment
		ToDO toDoFragment = (ToDO) fragmentManager
				.findFragmentById(R.id.frag_3);
		toDoFragment.setID(message);

		// Set and start the countdown fragment
		countDownFragment = (CountDown) fragmentManager
				.findFragmentById(R.id.frag_1);
		countDownFragment.countDown(db.get_DepartureDate(id),
				db.get_DepartureTime(id));

		// close the database
		db.close();

		// Move the scrollview to the top of the page
		ScrollView sview = (ScrollView) this.findViewById(R.id.scrollview);
		sview.smoothScrollTo(0, 0);

		// Set the onclicklistner for the back button omage
		backButton = (ImageButton) this.findViewById(R.id.imageButton_back);
		backButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// Use onResume to see if the current trip still exists
	// This is to finish the activity after a trip has been deleted in
	// the viewDetails activity
	@Override
	public void onResume() {
		super.onResume();
		
		// Open db
		Trip_Database db = new Trip_Database(this);
		try {
			db.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Check if trip still exists
		Cursor test = db.checkIfExists(id);
		if (test.getCount() != 0) {
			// If it exists, do nothing
			db.close();
		} else {
			// If it does not exist, finish the dashboard activity
			db.close();
			finish();
		}
	}

	// Function to open the viewdetails activity and send relevant details to it
	public void viewDetails(View view) {

		// Create new intent
		Intent ViewDetails = new Intent(this, ViewDetails.class);

		// Save relevant details to intent
		ViewDetails.putExtra("id", message);
		ViewDetails.putExtra("City", destination[1]);
		ViewDetails.putExtra("DepDate", details[4]);
		ViewDetails.putExtra("DepFlightNo", details[3]);
		ViewDetails.putExtra("DepTime", details[5]);
		ViewDetails.putExtra("DepConnection", details[6]);
		ViewDetails.putExtra("CountryDep", departure[0]);
		ViewDetails.putExtra("CityDep", departure[1]);
		ViewDetails.putExtra("CountryDest", destination[0]);
		ViewDetails.putExtra("CityDest", destination[1]);
		ViewDetails.putExtra("ReturnDate", details[12]);
		ViewDetails.putExtra("ReturnFlightNo", details[11]);
		ViewDetails.putExtra("ReturnTime", details[13]);
		ViewDetails.putExtra("ReturnConnection", details[14]);
		ViewDetails.putExtra("Notification", details[15]);
		ViewDetails.putExtra("Comments", details[16]);

		// Start the activity with intent
		startActivity(ViewDetails);

	}

	// Open the 10 day weather forecast for destination city
	public void weatherTenDay(View view) {
		Intent weatherTenDay = new Intent(this, WeatherTenDay.class);
		weatherTenDay.putExtra("Country", countryParsed);
		weatherTenDay.putExtra("City", cityParsed);
		startActivity(weatherTenDay);
	}

	// Open the currency converter with correct country vaules
	public void currency(View view) {
		Intent currency = new Intent(this, CurrencyConv.class);
		currency.putExtra("countryDest", destination[0]);
		currency.putExtra("countryDep", departure[0]);
		startActivity(currency);

	}

	// Start webview for the destination city
	@SuppressLint("SetJavaScriptEnabled")
	public void wiki(View view) {
		// Check if there is internet
		if (InternetStatus.getInstance(this).isOnline(this)) {
			// If there is, start webview for wiki
			WebView webView = (WebView) findViewById(R.id.wikiView_xml);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl("http://en.wikipedia.org/wiki/" + cityParsed);
		} else {
			// If there is no internet - notify user
			Toast.makeText(this,
					"No Intenet Connection Detected. Please Try Again Later",
					Toast.LENGTH_SHORT).show();
		}
	}

	// Dialog to add task to to-do list
	public void add(View view) {

		View entryView;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		entryView = getLayoutInflater().inflate(R.layout.new_task, null);
		alertDialogBuilder.setView(entryView);
		final EditText taskEditText = (EditText) entryView
				.findViewById(R.id.task_text_xml);
		// set the database helper
		final ToDoDBHelper helper2 = new ToDoDBHelper(this);
		// set title
		alertDialogBuilder.setTitle("New Task");
		// set dialog message
		alertDialogBuilder
				.setCancelable(true)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								SQLiteDatabase database;
								database = helper2.getWritableDatabase();
								// Insert task into db
								database.execSQL("INSERT INTO tblToDo (trip,task,date) VALUES ('"
										+ message
										+ "', '"
										+ taskEditText.getText().toString()
										+ "',"
										+ (int) System.currentTimeMillis()
										+ ")");
								helper2.close();
								// Refresh list for to-do list
								ToDO.refresh();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
}