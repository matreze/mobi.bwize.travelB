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
 * File Name: CurrencyConv.java
 * Description:
 * 
 * Fragment that grabs currency data based on the departure and 
 * destination countries.
 * 
 * Provides the ability to convert a set amount of money from 
 * the 'home' country to the currency of the destination.
 * 
 */

package mobi.bwize.travelB;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CurrencyConv extends Activity {

	// URL for the currency API
	static final String URL_START = "http://openexchangerates.org/api/latest.json?app_id=d8f43cbacdc24d3c8e076157889234d3";

	// JSON tag corresponding to the required json object
	static final String KEY_RESPONSE = "rates"; // parent node

	// Variables to hold various pieces of information
	private String inputCurrency;
	private String outputCurrency;
	private Double amount_to_convert;
	private EditText editText;
	private JSONObject info;
	private Boolean isEmpty = false;

	private Cursor cursor;
	private SQLiteDatabase countries;
	ImageButton backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currency_conv);

		// Open new json parsed
		JSONParse jParser = new JSONParse();

		// Get the country info from the calling activity- the dashboard
		Intent intent = getIntent();
		outputCurrency = getCurrency(intent.getStringExtra("countryDest"));
		inputCurrency = getCurrency(intent.getStringExtra("countryDep"));
		editText = (EditText) findViewById(R.id.curencyInput_xml);

		// The sharedPreferences are used to store a copy of the 
		// most recent values retrieved. If no internet connection
		// is available, these values are used
		SharedPreferences.Editor editor = getPreferences(0).edit();

		// Create the button and set the onclicklistner
		backButton = (ImageButton) this.findViewById(R.id.imageButton_back);
		backButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				finish();
			}
		});

		// Check the status of the internet
		if (InternetStatus.getInstance(this).isOnline(this)) {
			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(URL_START);
			if (json.length() > 0) {
				try {
					info = json.getJSONObject(KEY_RESPONSE);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Store a copy of the json object in the shared prefs
				editor.putString("json", info.toString());
				editor.commit();
			}
		} else {
			// if there is no internet, try using values from shared prefs
			try {
				SharedPreferences prefs = getPreferences(0);
				String savedJson = prefs.getString("json", null);
				if (savedJson != null) {
					info = new JSONObject(savedJson);
				} else {
					// sets a flag if there are no values available at all
					isEmpty = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// function called by button to actually convert the money
	public void setCurrency(View view) {

		// Check that a value has been entered
		if (editText.getText().length() > 0) {
			// parse the string into a double
			amount_to_convert = Double.parseDouble(editText.getText()
					.toString());
			TextView viewToSet = (TextView) findViewById(R.id.currency_display_xml);
			try {
				// check flag to see if values are available
				if (isEmpty == false) {
					Double currency_for_xml;
					// Base currency in retrieved values is USD, so calculations must
					// be adjusted based on the desired currencies
					// If the from currency is USD, just multiply by the value for 
					// desired currency
					if (inputCurrency.equals("USD")) {
						currency_for_xml = amount_to_convert
								* info.getDouble(outputCurrency);
					} else {
						// If the from currency is not USD, first convert the value to USD
						// before converting to desired currency
						Double usd = amount_to_convert
								/ info.getDouble(inputCurrency);
						currency_for_xml = usd * info.getDouble(outputCurrency);
					}
					// Set the tesxtview with the currency 
					viewToSet.setText(outputCurrency + ": "
							+ Double.toString(currency_for_xml));
				} else {
					// If no values are available, set textView to following:
					viewToSet
							.setText("No Currency Values Available - Please connect to the internet");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// Function to get the currency code for a country from the countries database
	String getCurrency(String country) {
		
		// Open db
		CountriesDB helper = new CountriesDB(this);
		countries = helper.getWritableDatabase();
		// Get cursor, where county equals desired country
		cursor = countries.query("tblCountries", new String[] {
				"currency_code", BaseColumns._ID }, "country_name='" + country
				+ "'", null, null, null, null);

		cursor.moveToFirst();
		// Extract currency code for this country
		String currencyCode = cursor.getString(cursor
				.getColumnIndex("currency_code"));
		helper.close();
		return currencyCode;
	}
}
