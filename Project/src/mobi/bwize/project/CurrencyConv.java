package mobi.bwize.project;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CurrencyConv extends Activity {

	static final String URL_START = "http://openexchangerates.org/api/latest.json?app_id=d8f43cbacdc24d3c8e076157889234d3";

	static final String KEY_RESPONSE = "rates"; // parent node

	private JSONObject json;
	private String inputCurrency;
	private String outputCurrency;
	private Double amount_to_convert;
	private EditText editText;
	private JSONObject info;
	private Boolean isEmpty = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currency_conv);

		JSONParse jParser = new JSONParse();
		inputCurrency = "EUR";
		outputCurrency = "ZAR";

		editText = (EditText) findViewById(R.id.curencyInput_xml);

		SharedPreferences.Editor editor = getPreferences(0).edit();

		if (InternetStatus.getInstance(this).isOnline(this)) {

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(URL_START);
			if (json.length() > 0) {
				try {
					info = json.getJSONObject(KEY_RESPONSE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				editor.putString("json", info.toString());
				editor.commit();
			}
		} else {
			try {
				SharedPreferences prefs = getPreferences(0);
				String savedJson = prefs.getString("json", null);
				if (savedJson != null) {
					info = new JSONObject(savedJson);
				} else {
					isEmpty = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setCurrency(View view) {

		if (editText.getText().length() > 0) {
			amount_to_convert = Double.parseDouble(editText.getText()
					.toString());
			TextView viewToSet = (TextView) findViewById(R.id.currency_display_xml);
			try {
				if (isEmpty == false) {
					Double currency_for_xml;
					if (inputCurrency.equals("USD")) {
						currency_for_xml = amount_to_convert
								* info.getDouble(outputCurrency);
					} else {
						Double usd = amount_to_convert
								/ info.getDouble(inputCurrency);
						currency_for_xml = usd * info.getDouble(outputCurrency);
					}
					viewToSet.setText(outputCurrency + ": "
							+ Double.toString(currency_for_xml));
				} else {
					viewToSet.setText("No Currency Values Available - Please connect to the internet");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	
}
