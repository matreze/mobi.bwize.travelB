package mobi.bwize.project;

import java.sql.SQLException;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TripListFragment extends ListFragment {
    private OnListSelectedListener listSelectedListener;


	// fields of the database that will be required

	private static final int xml_fields[] = {R.id.text_dep_date_xml, R.id.text_destination_xml,R.id.text_destination_country_xml,  };
	
	static CursorAdapter dataSource;
	
    // data to send to click handler
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        listSelectedListener.onListSelected(id);
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Trip_Database db = new Trip_Database(getActivity());
		
        try {
			db.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// set the fields in the row template with data from the database, creating a new list item for each row.
		//ArrayAdapter<String> arrayAdapter =      
		//         new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, db.getAllTrips());
		
        dataSource = new SimpleCursorAdapter(getActivity(), R.layout.list_trips, db.get_DashboardInfoTrips(), db.getColumnsTripList(), xml_fields);

		
        // setting the array adaptor and creating the list
        setListAdapter(dataSource);
        //db.close();
        
        
    }
    // What to do when an item is selected and use the id from that item
    public interface OnListSelectedListener {
        public void onListSelected(long id); 
    }

    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	listSelectedListener = (OnListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }
    
    // method that can be called to refresh the cursor.
    public static void refresh(){
    	dataSource.getCursor().requery();
    	
    }
}