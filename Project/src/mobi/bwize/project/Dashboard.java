package mobi.bwize.project;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends Activity {

	ImageButton weatherButton, wikiButton, currencyButton, backButton;
	TextView txtDest;
	
/*	String[] columns = new String[] { KEY_TRIP_ID, KEY_DESTINATION_COUNTRY,
			KEY_DESTINATION_CITY, KEY_DEPARTURE_FLIGHT_CODE,
			KEY_DEPARTURE_DATE, KEY_DEPARTUE_TIME,
			KEY_DEPARTURE_FLIGHT_CONNECTION, KEY_VACCINATION, KEY_VISA,
			KEY_RETURN_COUNTRY, KEY_RETURN_CITY, KEY_RETURN_FLIGHT_CODE,
			KEY_RETURN_DATE, KEY_RETURN_TIME, KEY_RETURN_FLIGHT_CONNECTION,
			NOTIFICATION_STATUS, KEY_COMMENTS };*/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		// open the intent
		Intent intent = getIntent();

		// check the string from the previous activity
		String message = intent.getStringExtra("ID");

		Trip_Database db = new Trip_Database(this);
		
		try {
			db.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Get the destination name from the last activity to populate the
		String details[]=db.getTripDetails(Integer.valueOf(message));
		
		
		
		// textView
		txtDest = (TextView) findViewById(R.id.text_destination_name_xml);

	/*	String destData;

		Intent myIntent = getIntent();
		destData = myIntent.getStringExtra("DESTINATION");*/

		txtDest.setText(details[2]);

		addListenerOnButton();
	}

	// To look after all the buttons
	public void addListenerOnButton() {

		weatherButton = (ImageButton) findViewById(R.id.imageButton_weather);

		weatherButton.setOnClickListener(new OnClickListener() {

			// @Override
			public void onClick(View arg0) {

				Toast.makeText(Dashboard.this, "Will go to weather Activity",
						Toast.LENGTH_SHORT).show();

			}

		});

		wikiButton = (ImageButton) findViewById(R.id.imageButton_wiki);

		wikiButton.setOnClickListener(new OnClickListener() {

			// @Override
			public void onClick(View arg0) {

				Uri uri = Uri.parse("http://www.wikipedia.com");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);

			}

		});

		currencyButton = (ImageButton) findViewById(R.id.imageButton_currency);

		currencyButton.setOnClickListener(new OnClickListener() {

			// @Override
			public void onClick(View arg0) {

				Uri uri = Uri.parse("http://www.xe.com");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);

			}

		});

		backButton = (ImageButton) this.findViewById(R.id.imageButton_back);
		backButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
