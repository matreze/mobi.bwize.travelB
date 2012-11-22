package mobi.bwize.project;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoardActivity extends FragmentActivity {

	TextView txtDest;
	String details[];
	String cityParsed;
	String countryParsed;

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

		FragmentManager fragmentManager = getSupportFragmentManager();

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
		details = db.getTripDetails(Integer.valueOf(message));

		cityParsed = details[2].replaceAll("\\s", "%20");
		countryParsed = details[1].replaceAll("\\s", "%20");

		CountDown countDownFragment = (CountDown) fragmentManager
				.findFragmentById(R.id.frag_1);
		countDownFragment.countDown(details[4], details[5]);

		CurrentWeather currentWeatherDownFragment = (CurrentWeather) fragmentManager
				.findFragmentById(R.id.frag_2);
		currentWeatherDownFragment.setCurrentWeather(countryParsed, cityParsed);

		db.close();

		/*
		 * String destData;
		 * 
		 * Intent myIntent = getIntent(); destData =
		 * myIntent.getStringExtra("DESTINATION");
		 */

		// txtDest.setText(details[2]);

	}

	public void weatherTenDay(View view) {

		Intent weatherTenDay = new Intent(this, WeatherTenDay.class);

		weatherTenDay.putExtra("Country",countryParsed );
		weatherTenDay.putExtra("City", cityParsed);

		startActivity(weatherTenDay);

	}

	public void currency(View view) {

		Intent currency = new Intent(this, CurrencyConv.class);

		currency.putExtra("countryDest", details[1]);
		currency.putExtra("countryDep", details[9]);

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

}