package mobi.bwize.project;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class WeatherTenDay extends ListActivity {

	static final String URL_START = "http://api.wunderground.com/api/de3bca782c02b1a2/forecast10day/q/";
	static final String F_SLASH = "/";
	static final String URL_END = ".json";
	// XML node keys
	static final String KEY_RESPONSE = "response"; // parent node
	static final String KEY_FORECAST = "forecast";
	static final String KEY_FORECAST_START = "txt_forecast";
	static final String KEY_FORECAST_DAY = "forecastday";
	static final String KEY_PERIOD = "period";
	static final String KEY_DAY = "title";
	static final String KEY_ICON = "icon";
	static final String KEY_FORECAST_TEXT_IMPERIAL = "fcttext";
	static final String KEY_FORECAST_TEXT_METRIC = "fcttext_metric";

	private Boolean isEmpty = false;
	ImageButton backButton;
	// contacts JSONArray
	JSONArray array = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_ten_day);

		ArrayList<HashMap<String, String>> weatherList = new ArrayList<HashMap<String, String>>();

		Intent intent=getIntent();
		
		String Country = intent.getStringExtra("Country");

		String City = intent.getStringExtra("City");

		SharedPreferences.Editor editor = getPreferences(0).edit();
		// Creating JSON Parser instance
		JSONParse jParser = new JSONParse();
		JSONObject json=null;
		if (InternetStatus.getInstance(this).isOnline(this)) {

			// getting JSON string from URL

				json = jParser.getJSONFromUrl(URL_START + Country + F_SLASH
						+ City + URL_END);
				editor.putString("jsonWeather", json.toString());
				editor.commit();
			
		} else {
			try {
				SharedPreferences prefs = getPreferences(0);
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
				JSONObject json_objects = json.getJSONObject(KEY_FORECAST);
				JSONObject json_forecast = json_objects
						.getJSONObject(KEY_FORECAST_START);
				array = json_forecast.getJSONArray(KEY_FORECAST_DAY);

				for (int i = 0; i < array.length(); i++) {
					JSONObject info = array.getJSONObject(i);

					String day = info.getString(KEY_DAY);
					String icon = info.getString(KEY_ICON);
					String forecast = info.getString(KEY_FORECAST_TEXT_METRIC);

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					int imageResource = getResources().getIdentifier(icon,
							"drawable", "mobi.bwize.project");
					Log.d("icon", icon);
					Log.d("day", day);
					Log.d("forecast", forecast);

					// adding each child node to HashMap key => value
					map.put(KEY_DAY, day);
					map.put(KEY_ICON, Integer.toString(imageResource));
					map.put(KEY_FORECAST_TEXT_METRIC, forecast);

					weatherList.add(map);
				}
			} else {
				HashMap<String, String> map = new HashMap<String, String>();

				int imageResource = getResources().getIdentifier(
						"ic_action_search", "drawable",
						"mobi.bwize.tendayweathertest");

				map.put(KEY_DAY, "No Data");
				map.put(KEY_ICON, Integer.toString(imageResource));
				map.put(KEY_FORECAST_TEXT_METRIC,
						"Please connect to the internet for the latest forecast");

				weatherList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ListAdapter adapter = new SimpleAdapter(this, weatherList,
				R.layout.list_item, new String[] { KEY_ICON, KEY_DAY,
						KEY_FORECAST_TEXT_METRIC }, new int[] { R.id.icon,
						R.id.title, R.id.fcttext_metric });

		setListAdapter(adapter);

		backButton = (ImageButton)this.findViewById(R.id.imageButton_back);
		backButton.setOnClickListener(new OnClickListener() {
		  //@Override
		  public void onClick(View v) {
		    finish();
		  }
		});
	}

	
}
