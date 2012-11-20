package mobi.bwize.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class CurrentWeather extends Activity {

	static final String URL_START = "http://api.wunderground.com/api/de3bca782c02b1a2/conditions/q/";
	static final String F_SLASH="/";
	static final String URL_END=".json";
	// XML node keys
	static final String KEY_RESPONSE = "response"; // parent node
	static final String KEY_CURRENT = "current_observation";
	static final String KEY_IMAGE = "image";
	static final String KEY_IMAGE_URL = "url";
	static final String KEY_WEATHER_DESC = "weather";
	static final String KEY_TEMP_C = "temp_c";
	static final String KEY_WIND_TEXT = "wind_string";
	static final String KEY_FEELSLIKE = "feelslike_c";

	// contacts JSONArray
	JSONArray array = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//TODO get db info
		String Country="South_Africa";
		String City="Margate";

		// Creating JSON Parser instance
		JSONParse jParser = new JSONParse();

		Log.d("",URL_START+Country+F_SLASH+City+URL_END);
		
		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(URL_START+Country+F_SLASH+City+URL_END);

		try {
					
			JSONObject info = json.getJSONObject(KEY_CURRENT);

			TextView viewToSet = (TextView) findViewById(R.id.weather_desc_view);
			String weather_xml = info.getString(KEY_WEATHER_DESC);
			
			Log.d("weather",weather_xml);
			viewToSet.setText(weather_xml);

			viewToSet = (TextView) findViewById(R.id.temp_view);
			String temp_c_xml = info.getString(KEY_TEMP_C);
			Log.d("weather",temp_c_xml);
			viewToSet.setText(temp_c_xml);

			viewToSet = (TextView) findViewById(R.id.wind_string);
			String wind_string_xml = info.getString(KEY_WIND_TEXT);
			Log.d("weather",wind_string_xml);
			viewToSet.setText(wind_string_xml);

			viewToSet = (TextView) findViewById(R.id.feelsLike_view);
			String feeslike_xml = info.getString(KEY_FEELSLIKE);
			Log.d("weather",feeslike_xml);
			viewToSet.setText(feeslike_xml);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
