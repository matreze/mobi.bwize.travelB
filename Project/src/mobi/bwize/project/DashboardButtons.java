package mobi.bwize.project;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DashboardButtons extends Fragment implements OnClickListener  {  
 
	
	TextView mTextWiki, mTextCurrency, mTenDayWeather;
	
		@Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	    
	       
	    ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_dashboard_links, null);
	    
	    mTextWiki = (TextView)root.findViewById(R.id.wiki_view);
	    mTextCurrency = (TextView)root.findViewById(R.id.currency_view);
	    mTenDayWeather = (TextView)root.findViewById(R.id.ten_day_view);
	    
	    mTextWiki.setOnClickListener(this);
	    mTextCurrency.setOnClickListener(this);
	    mTenDayWeather.setOnClickListener(this);
	
	    return root;
		
		}
		
	    public void onClick(View arg0) {
		

		    if(arg0 == mTenDayWeather){
		    	   
		    	 Intent myIntent = new Intent(arg0.getContext(),WeatherTenDay.class);        
		         startActivity(myIntent);
		        }
		    else if(arg0 == mTextWiki){
	    	   
	    	   Intent myIntent = new Intent(arg0.getContext(),CurrencyConv.class);        
	           startActivity(myIntent);
	        }
	       else if(arg0 == mTextCurrency){

	    	   Intent myIntent = new Intent(arg0.getContext(), CurrencyConv.class);        
	           startActivity(myIntent);
	        }	    
		
      
	    }
	
   
}