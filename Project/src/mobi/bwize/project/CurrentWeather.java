package mobi.bwize.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CurrentWeather extends Fragment {

	static final String URL_START = "http://api.wunderground.com/api/de3bca782c02b1a2/conditions/q/";
	static final String F_SLASH = "/";
	static final String URL_END = ".json";
	// XML node keys
	static final String KEY_RESPONSE = "response"; // parent node
	static final String KEY_CURRENT = "current_observation";
	static final String KEY_IMAGE = "image";
	static final String KEY_IMAGE_URL = "url";
	static final String KEY_WEATHER_DESC = "weather";
	static final String KEY_TEMP_C = "temp_c";
	static final String KEY_WIND_TEXT = "wind_string";
	static final String KEY_FEELSLIKE = "feelslike_c";

	ViewGroup root;

	// contacts JSONArray
	JSONArray array = null;
	JSONObject json;
	private Boolean isEmpty = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = (ViewGroup) inflater.inflate(R.layout.activity_main, null);

		/*
		 * public void onCreate(Bundle savedInstanceState) {
		 * super.onCreate(savedInstanceState);
		 * setContentView(R.layout.activity_main);
		 */

		return root;

	}

	void setCurrentWeather(String country, String city) {

		Log.d("country", country);
		Log.d("country", URL_START + country + F_SLASH + city + URL_END);

		SharedPreferences.Editor editor = getActivity().getPreferences(0)
				.edit();

		// Creating JSON Parser instance
		JSONParse jParser = new JSONParse();

		if (InternetStatus.getInstance(getActivity()).isOnline(getActivity())) {

			// getting JSON string from URL

			json = jParser.getJSONFromUrl(URL_START + country + F_SLASH + city
					+ URL_END);
			editor.putString("jsonWeatherCurrent", json.toString());
			editor.commit();

		} else {

			try {
				SharedPreferences prefs = getActivity().getPreferences(0);
				String savedJson = prefs.getString("jsonWeather", null);
				if (savedJson != null) {
					json = new JSONObject(savedJson);
				} else {
					isEmpty = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {

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
