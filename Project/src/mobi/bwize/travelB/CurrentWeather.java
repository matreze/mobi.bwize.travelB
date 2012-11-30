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
 * File Name: CurrentWeather.java
 * Description:
 * 
 * Fragment to display the current weather for the destination in the dashboard.
 * The weather info is retrieved from the weather.com api.
 * 
 */

package mobi.bwize.travelB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CurrentWeather extends Fragment {

	// URL fragments to be used to construct the final url with country and city
	static final String URL_START = "http://api.wunderground.com/api/de3bca782c02b1a2/conditions/q/";
	static final String F_SLASH = "/";
	static final String URL_END = ".json";
	// JSON node keys
	static final String KEY_RESPONSE = "response"; // parent node
	static final String KEY_CURRENT = "current_observation";
	static final String KEY_IMAGE = "image";
	static final String KEY_IMAGE_URL = "url";
	static final String KEY_WEATHER_DESC = "weather";
	static final String KEY_TEMP_C = "temp_c";
	static final String KEY_WIND_TEXT = "wind_string";
	static final String KEY_FEELSLIKE = "feelslike_c";

	ViewGroup root;

	// JSONArray for current weather
	JSONArray array = null;
	JSONObject json;
	private Boolean isEmpty = false;

	// Set the view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = (ViewGroup) inflater.inflate(R.layout.activity_main, null);
		return root;

	}

	// Called by fragment manager with country and city
	void setCurrentWeather(String country, String city) {

		// Used to save previous values in case there is no internet connection
		SharedPreferences.Editor editor = getActivity().getPreferences(0)
				.edit();

		// Creating JSON Parser instance
		JSONParse jParser = new JSONParse();

		// Check if there is an internet connection
		if (InternetStatus.getInstance(getActivity()).isOnline(getActivity())) {
			// getting JSON string from URL and saving a copy to shared prefs
			json = jParser.getJSONFromUrl(URL_START + country + F_SLASH + city
					+ URL_END);
			editor.putString("jsonWeatherCurrent", json.toString());
			editor.commit();
		} else {
			// Otherwise get vales from shared prefs
			try {
				SharedPreferences prefs = getActivity().getPreferences(0);
				String savedJson = prefs.getString("jsonWeather", null);
				if (savedJson != null) {
					json = new JSONObject(savedJson);
				} else {
					isEmpty = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {
			// Try to parse json and set relevant fields
			if (isEmpty == false) {
				JSONObject info = json.getJSONObject(KEY_CURRENT);
				TextView viewToSet = (TextView) root
						.findViewById(R.id.weather_desc_view);
				String weather_xml = info.getString(KEY_WEATHER_DESC);
				viewToSet.setText(weather_xml);
				
				viewToSet = (TextView) root.findViewById(R.id.temp_view);
				String temp_c_xml = info.getString(KEY_TEMP_C);
				viewToSet.setText(temp_c_xml);
				
				viewToSet = (TextView) root.findViewById(R.id.wind_string);
				String wind_string_xml = info.getString(KEY_WIND_TEXT);
				viewToSet.setText(wind_string_xml);
				
				viewToSet = (TextView) root.findViewById(R.id.feelsLike_view);
				String feeslike_xml = info.getString(KEY_FEELSLIKE);
				viewToSet.setText(feeslike_xml);
			}else{
				// If no values are available, notify the user
				TextView viewToSet = (TextView) root
						.findViewById(R.id.weather_desc_view);
				String weather_xml = "No Internet Connection";
				viewToSet.setText(weather_xml);

				viewToSet = (TextView) root.findViewById(R.id.temp_view);
				String temp_c_xml = "Info not currently available";
				viewToSet.setText(temp_c_xml);

				viewToSet = (TextView) root.findViewById(R.id.wind_string);
				String wind_string_xml ="";
				viewToSet.setText(wind_string_xml);

				viewToSet = (TextView) root.findViewById(R.id.feelsLike_view);
				String feeslike_xml = "";
				viewToSet.setText(feeslike_xml);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}