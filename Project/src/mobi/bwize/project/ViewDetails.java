package mobi.bwize.project;

import java.util.Calendar;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewDetails extends Activity{
	
	EditText myEdit, myEdit2, myEditDepFlightNo, myEditReturnFlightNo,
	myEditExtra;
	Button myButton, myButton2, delButton, mPickTime, myInsertTrip;
	RadioGroup myEditDepFlightCon, myEditReturnFlightCon, myEditNotification;
	Calendar depDate, retDate;
	ImageButton backButton;
	TextView txtDest, txtDepTime, txtReturnTime;
	Spinner DestCountry, DestCity, DepCountry, DepCity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_trip);
		
		//Get the destination name from the last activity to populate the textView
		txtDest = (TextView) findViewById(R.id.text_trip_name_xml);
		myEdit = (EditText) findViewById(R.id.departure_date_xml);
		myEditDepFlightNo= (EditText) findViewById(R.id.departure_flight_xml);
		txtDepTime = (TextView) findViewById(R.id.dep_time_xml);
		DestCountry= (Spinner) findViewById(R.id.destination_country_xml);
		DestCity= (Spinner) findViewById(R.id.destination_city_xml);
		DepCountry= (Spinner) findViewById(R.id.departure_country_xml);
		DepCity= (Spinner) findViewById(R.id.departure_city_xml);
		myEdit2 = (EditText) findViewById(R.id.return_date_xml);
		myEditReturnFlightNo= (EditText) findViewById(R.id.return_flight_xml);
		txtReturnTime = (TextView) findViewById(R.id.ret_time_xml);
		myEditExtra= (EditText) findViewById(R.id.comments_xml);
				
		String destData, destDepDate, depFlight, depTime, destCountry, destCity, depCountry, depCity, returnDate, returnFlight, 
		returnTime, comments;
				
		Intent myIntent = getIntent();
		destData = myIntent.getStringExtra("City");
		destDepDate = myIntent.getStringExtra("DepDate");
		depFlight = myIntent.getStringExtra("DepFlightNo");
		depTime = myIntent.getStringExtra("DepTime");
		destCountry = myIntent.getStringExtra("CountryDest");
		destCity = myIntent.getStringExtra("CityDest");
		depCountry = myIntent.getStringExtra("CountryDep");
		depCity = myIntent.getStringExtra("CityDep");
		returnDate = myIntent.getStringExtra("ReturnDate");
		returnFlight = myIntent.getStringExtra("ReturnFlightNo");
		returnTime = myIntent.getStringExtra("ReturnTime");
		comments = myIntent.getStringExtra("Comments");
				
		txtDest.setText(destData);	
		myEdit.setText(destDepDate);
		myEditDepFlightNo.setText(depFlight);
		txtDepTime.setText(depTime);
		DestCountry.setTag(destCountry);
		DestCity.setTag(destCity);
		DepCountry.setTag(depCountry);
		DepCity.setTag(depCity);
		myEdit2.setText(returnDate);
		myEditReturnFlightNo.setText(returnFlight);
		txtReturnTime.setText(returnTime);
		myEditExtra.setText(comments);
	
	
	backButton = (ImageButton)this.findViewById(R.id.imageButton_back);
	myButton = (Button)this.findViewById(R.id.button_updatetrip_xml);
	myButton2 = (Button)this.findViewById(R.id.button_delete_xml);
	
	backButton.setOnClickListener(new OnClickListener() {
	  //@Override
	  public void onClick(View v) {
	    finish();
	  }
	});
	
	}
	
	
	//@Override
	public void UpdateTrip () {
		
		
		
	}
	
	
	//@Override
	public void DeleteTrip () {
		
		
		
	}

}
