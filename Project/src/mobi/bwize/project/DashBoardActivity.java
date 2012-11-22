package mobi.bwize.project;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
	String trip;

	/*
	 * String[] columns = new String[] { KEY_TRIP_ID, 
	 * KEY_DESTINATION_COUNTRY,
	 * KEY_DESTINATION_CITY, 
	 * KEY_DEPARTURE_FLIGHT_CODE, 
	 * KEY_DEPARTURE_DATE,
	 * KEY_DEPARTUE_TIME, 
	 * KEY_DEPARTURE_FLIGHT_CONNECTION, 
	 * KEY_VACCINATION,
	 * KEY_VISA, 
	 * KEY_RETURN_COUNTRY, 
	 * KEY_RETURN_CITY, 
	 * KEY_RETURN_FLIGHT_CODE,
	 * KEY_RETURN_DATE, 
	 * KEY_RETURN_TIME, 
	 * KEY_RETURN_FLIGHT_CONNECTION,
	 * NOTIFICATION_STATUS, 
	 * KEY_COMMENTS };
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		FragmentManager fragmentManager = getSupportFragmentManager();

		Intent intent = getIntent();

		// check the string from the previous activity
		String message = intent.getStringExtra("ID");
		int id=Integer.valueOf(message);
		trip=message;

		Trip_Database db = new Trip_Database(this);

		try {
			db.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the destination name from the last activity to populate the

		// Get the destination name from the last activity to populate the
		details = db.getTripDetails(Integer.valueOf(message));
		destination = db.get_Destination(id);
		departure = db.get_ReturnInfo(id);

		cityParsed = destination[1].replaceAll("\\s", "%20");
		countryParsed = destination[0].replaceAll("\\s", "%20");

		
		CurrentWeather currentWeatherDownFragment = (CurrentWeather) fragmentManager
				.findFragmentById(R.id.frag_2);
		currentWeatherDownFragment.setCurrentWeather(countryParsed, cityParsed);
		
		ToDO toDoFragment = (ToDO) fragmentManager
				.findFragmentById(R.id.frag_3);
		toDoFragment.setID(message);
		
		CountDown countDownFragment = (CountDown) fragmentManager
				.findFragmentById(R.id.frag_1);
		countDownFragment.countDown(db.get_DepartureDate(id), db.get_DepartureTime(id));


		db.close();
		
		ScrollView sview=(ScrollView) this.findViewById(R.id.scrollview);
		sview.smoothScrollTo(0,0);
		/*
		 * String destData;
		 * 
		 * Intent myIntent = getIntent(); destData =
		 * myIntent.getStringExtra("DESTINATION");
		 */

		// txtDest.setText(details[2]);
		
		backButton = (ImageButton)this.findViewById(R.id.imageButton_back);
		backButton.setOnClickListener(new OnClickListener() {
		  //@Override
		  public void onClick(View v) {
		    finish();
		  }
		});

	}
	
	public void ViewDetails(View view) {

		Intent ViewDetails = new Intent(this, ViewDetails.class);

		//To do 
		ViewDetails.putExtra("City", destination[1]);
		ViewDetails.putExtra("DepDate", details[4]);
		ViewDetails.putExtra("DepFlightNo", details[3]);
		ViewDetails.putExtra("DepTime", details[5]);
		ViewDetails.putExtra("DepConnection", details[6]);
		ViewDetails.putExtra("CountryDep", details[9]);
		ViewDetails.putExtra("CityDep", details[10]);
		ViewDetails.putExtra("CountryDest", details[1]);
		ViewDetails.putExtra("CityDest", details[2]);
		ViewDetails.putExtra("ReturnDate", details[12]);
		ViewDetails.putExtra("ReturnFlightNo", details[11]);
		ViewDetails.putExtra("ReturnTime", details[13]);
		ViewDetails.putExtra("ReturnConnection", details[14]);
		ViewDetails.putExtra("Notification", details[15]);
		ViewDetails.putExtra("Comments", details[16]);

		startActivity(ViewDetails);

	}

	public void weatherTenDay(View view) {

		Intent weatherTenDay = new Intent(this, WeatherTenDay.class);

		weatherTenDay.putExtra("Country",countryParsed );
		weatherTenDay.putExtra("City", cityParsed);

		startActivity(weatherTenDay);

	}

	public void currency(View view) {

		Intent currency = new Intent(this, CurrencyConv.class);

		currency.putExtra("countryDest", destination[0]);
		currency.putExtra("countryDep", departure[0]);

		startActivity(currency);

	}

	@SuppressLint("SetJavaScriptEnabled")
	public void wiki(View view) {
		
		if (InternetStatus.getInstance(this).isOnline(this)) {

		WebView webView = (WebView) findViewById(R.id.wikiView_xml);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://en.wikipedia.org/wiki/" + cityParsed);
		}else{
			
			Toast.makeText(this,
					"No Intenet Connection Detected. Please Try Again Later", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	public void add(View view) {
		 
		View entryView;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
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
								database = helper2
										.getWritableDatabase();
								database.execSQL("INSERT INTO tblToDo (trip,task,date) VALUES ('"
										+ trip
										+ "', '"
										+ taskEditText.getText().toString()
										+ "',"
										+ (int) System.currentTimeMillis()
										+ ")");
								Log.d("set",taskEditText.getText().toString());
								helper2.close();
								ToDO.refresh();
							}
						})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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