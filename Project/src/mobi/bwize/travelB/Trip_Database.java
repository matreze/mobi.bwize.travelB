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
 * File Name: Trip_Database.java
 * Description:
 * 
 * Helper to handle all main database interactions.
 * 
 */

package mobi.bwize.travelB;

import java.sql.SQLException;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Trip_Database {

	public static final String DATABASE_NAME = "MY_Trips_App";
	public static final String DATABASE_TABLE = "My_Trips";
	public static final int DATABASE_VERSION = 3;

	public static final String KEY_TRIP_ID = "_id";
	public static final String KEY_DESTINATION_COUNTRY = "destination_country";
	public static final String KEY_DESTINATION_CITY = "destination_city";
	public static final String KEY_DEPARTURE_FLIGHT_CODE = "departure_code";
	public static final String KEY_DEPARTURE_DATE = "departure_date";
	public static final String KEY_DEPARTUE_TIME = "departure_time";
	public static final String KEY_DEPARTURE_FLIGHT_CONNECTION = "departure_connection";

	public static final String KEY_VACCINATION = "vaccination";
	public static final String KEY_VISA = "visa";

	public static final String KEY_RETURN_COUNTRY = "return_country";
	public static final String KEY_RETURN_CITY = "return_city";
	public static final String KEY_RETURN_FLIGHT_CODE = "return_code";
	public static final String KEY_RETURN_DATE = "return_date";
	public static final String KEY_RETURN_TIME = "return_time";
	public static final String KEY_RETURN_FLIGHT_CONNECTION = "return_connection";

	public static final String NOTIFICATION_STATUS = "notification";
	public static final String KEY_COMMENTS = "comments";

	private DbHelper myDbHelper;
	private final Context context;
	private SQLiteDatabase tripsDatabase;
	private Cursor c;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/*
		 * function onCreate creates SQL table with required columns when
		 * entering the details of the trip user must enter destination country
		 * and city declared as not null fields in the table, this should be
		 * indicated in the form that this fields are obligatory as a hint
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " ("
					+ KEY_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_DESTINATION_COUNTRY + " TEXT NOT NULL, "
					+ KEY_DESTINATION_CITY + " TEXT NOT NULL, "
					+ KEY_DEPARTURE_FLIGHT_CODE + " TEXT, "
					+ KEY_DEPARTURE_DATE + " TEXT, " + KEY_DEPARTUE_TIME
					+ " TEXT, " + KEY_DEPARTURE_FLIGHT_CONNECTION + " TEXT, "
					+ KEY_VISA + " TEXT, " + KEY_VACCINATION + " TEXT, "
					+ KEY_RETURN_COUNTRY + " TEXT, " + KEY_RETURN_CITY
					+ " TEXT, " + KEY_RETURN_FLIGHT_CODE + " TEXT, "
					+ KEY_RETURN_DATE + " TEXT, " + KEY_RETURN_TIME + " TEXT, "
					+ KEY_RETURN_FLIGHT_CONNECTION + " TEXT, "
					+ NOTIFICATION_STATUS + " TEXT, " + KEY_COMMENTS
					+ " TEXT);");
		}

		/*
		 * function onUpgrade
		 */

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	/*
	 * constructor for class Trip_Database
	 */
	public Trip_Database(Context c) {
		context = c;
	}

	/*
	 * function Trip_Database open for opening writable database
	 */
	public Trip_Database open() throws SQLException {
		myDbHelper = new DbHelper(context);
		tripsDatabase = myDbHelper.getWritableDatabase();
		return this;
	}

	/*
	 * function Trip_Database close for closing database
	 */
	public void close() {
		myDbHelper.close();
		// c.close();
	}

	/*
	 * function Trip_Database create_Entry for creating new row an inserting
	 * into table
	 */
	public long create_Entry(String destination_country,
			String destination_city, String departure_flight_code,
			String departure_date, String departure_time,
			String departure_connection, String vaccination, String visa,
			String return_country, String return_city,
			String return_flight_code, String return_date, String return_time,
			String return_connection, String comments,
			String notification_status) {

		ContentValues cv = new ContentValues();
		cv.put(KEY_DESTINATION_COUNTRY, destination_country);
		cv.put(KEY_DESTINATION_CITY, destination_city);
		cv.put(KEY_DEPARTURE_FLIGHT_CODE, departure_flight_code);
		cv.put(KEY_DEPARTURE_DATE, departure_date);
		cv.put(KEY_DEPARTUE_TIME, departure_time);
		cv.put(KEY_DEPARTURE_FLIGHT_CONNECTION, departure_connection);
		cv.put(KEY_VACCINATION, vaccination);
		cv.put(KEY_VISA, visa);
		cv.put(KEY_RETURN_CITY, return_city);
		cv.put(KEY_RETURN_COUNTRY, return_country);
		cv.put(KEY_RETURN_FLIGHT_CODE, return_flight_code);
		cv.put(KEY_RETURN_DATE, return_date);
		cv.put(KEY_RETURN_TIME, return_time);
		cv.put(KEY_RETURN_FLIGHT_CONNECTION, return_connection);

		cv.put(KEY_COMMENTS, comments);
		cv.put(NOTIFICATION_STATUS, notification_status);

		return tripsDatabase.insert(DATABASE_TABLE, null, cv);

	}

	/*
	 * function Trip_Database getAllTrips for acquiring all rows(entries) from
	 * database table with specified columns
	 */
	public Cursor getAllTrips() {

		String[] columns = new String[] { KEY_TRIP_ID, KEY_DESTINATION_COUNTRY,
				KEY_DESTINATION_CITY, KEY_DEPARTURE_FLIGHT_CODE,
				KEY_DEPARTURE_DATE, KEY_DEPARTUE_TIME,
				KEY_DEPARTURE_FLIGHT_CONNECTION, KEY_VACCINATION, KEY_VISA,
				KEY_RETURN_COUNTRY, KEY_RETURN_CITY, KEY_RETURN_FLIGHT_CODE,
				KEY_RETURN_DATE, KEY_RETURN_TIME, KEY_RETURN_FLIGHT_CONNECTION,
				NOTIFICATION_STATUS, KEY_COMMENTS };

		Cursor c = tripsDatabase.query(DATABASE_TABLE, columns, null, null,
				null, null, null);

	
		return c;
	}
	/*
	 * function Trip_Database get_DashboardInfoTrips for acquiring all rows(entries) from
	 * database for the dashboard
	 */
	public Cursor get_DashboardInfoTrips() {

		String[] columns = new String[] { KEY_DEPARTURE_DATE,
				KEY_DESTINATION_CITY, KEY_DESTINATION_COUNTRY, KEY_TRIP_ID };

		c = tripsDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);

		return c;
	}
	/*
	 * function Trip_Database checkIfExists to check if an id exists
	 */
	public Cursor checkIfExists(int id) {
		String[] columns = new String[] { KEY_DEPARTURE_DATE,
				KEY_DESTINATION_CITY, KEY_DESTINATION_COUNTRY, KEY_TRIP_ID };
		c = tripsDatabase.query(DATABASE_TABLE, columns,
				KEY_TRIP_ID + "=" + id, null, null, null, null);
		return c;
	}

	/*
	 * function Trip_Database getTripDetails retrieving all columns for the
	 * specified row id
	 */
	public String[] getTripDetails(int trip_id) {

		String[] columns = new String[] { KEY_TRIP_ID, KEY_DESTINATION_COUNTRY,
				KEY_DESTINATION_CITY, KEY_DEPARTURE_FLIGHT_CODE,
				KEY_DEPARTURE_DATE, KEY_DEPARTUE_TIME,
				KEY_DEPARTURE_FLIGHT_CONNECTION, KEY_VACCINATION, KEY_VISA,
				KEY_RETURN_COUNTRY, KEY_RETURN_CITY, KEY_RETURN_FLIGHT_CODE,
				KEY_RETURN_DATE, KEY_RETURN_TIME, KEY_RETURN_FLIGHT_CONNECTION,
				NOTIFICATION_STATUS, KEY_COMMENTS };
		String[] trip_details = new String[columns.length];
		Cursor c = tripsDatabase.query(DATABASE_TABLE, columns, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);

		if (c != null) {
			c.moveToFirst();
			for (int i = 0; i < columns.length; i++) {
				trip_details[i] = c.getString(i);
			}
		}
		c.close();
		return trip_details;
	}

	/*
	 * function Trip_Database deleteEntry for removing specified row from the
	 * database table
	 */
	public void deleteEntry(int trip_id) throws SQLException {
		tripsDatabase.delete(DATABASE_TABLE, KEY_TRIP_ID + "=" + trip_id, null);

	}

	// function updateTrip_byid updates specified row selected by id

	public void updateTrip_byid(int trip_id, String destination_country,
			String destination_city, String departure_flight_code,
			String departure_date, String departure_time,
			String departure_connection, String vaccination, String visa,
			String return_country, String return_city,
			String return_flight_code, String return_date, String return_time,
			String return_connection, String comments,
			String notification_status) {

		ContentValues values = new ContentValues();
		values.put(KEY_DESTINATION_COUNTRY, destination_country);
		values.put(KEY_DESTINATION_CITY, destination_city);
		values.put(KEY_DEPARTURE_FLIGHT_CODE, departure_flight_code);
		values.put(KEY_DEPARTURE_DATE, departure_date);
		values.put(KEY_DEPARTUE_TIME, departure_time);
		values.put(KEY_DEPARTURE_FLIGHT_CONNECTION, departure_connection);
		values.put(KEY_VACCINATION, vaccination);
		values.put(KEY_VISA, visa);
		values.put(KEY_RETURN_COUNTRY, return_country);
		values.put(KEY_RETURN_CITY, return_city);
		values.put(KEY_RETURN_FLIGHT_CODE, return_flight_code);
		values.put(KEY_RETURN_DATE, return_date);
		values.put(KEY_RETURN_TIME, return_time);
		values.put(KEY_RETURN_FLIGHT_CONNECTION, return_connection);
		values.put(NOTIFICATION_STATUS, comments);
		values.put(KEY_COMMENTS, notification_status);

		tripsDatabase.update(DATABASE_TABLE, values, KEY_TRIP_ID + "="
				+ trip_id, null);

	}

	// function fet_Destination return the country and the city of the
	// destination flight in the form of String array

	public String[] get_Destination(int trip_id) {

		String[] columns = new String[] { KEY_DESTINATION_COUNTRY,
				KEY_DESTINATION_CITY };

		String[] destination_info = new String[columns.length];
		Cursor c = tripsDatabase.query(DATABASE_TABLE, columns, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int idestination_country = c.getColumnIndex(KEY_DESTINATION_COUNTRY);
		int idestination_city = c.getColumnIndex(KEY_DESTINATION_CITY);

		if (c != null) {
			c.moveToFirst();
			destination_info[Arrays.asList(columns).indexOf(
					KEY_DESTINATION_COUNTRY)] = c
					.getString(idestination_country);
			destination_info[Arrays.asList(columns).indexOf(
					KEY_DESTINATION_CITY)] = c.getString(idestination_city);
		}
		c.close();
		return destination_info;
	}

	// function get_departureDate return date of the departure flight
	public String get_DepartureDate(int trip_id) {

		String[] column = { KEY_DEPARTURE_DATE };
		String departure_date_info = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ideparture_date = c.getColumnIndex(KEY_DEPARTURE_DATE);
		if (c != null) {
			c.moveToFirst();
			departure_date_info = c.getString(ideparture_date);

		}
		c.close();
		return departure_date_info;

	}

	// function get_DepartureTime returns the time of the departure flight
	public String get_DepartureTime(int trip_id) {

		String[] column = { KEY_DEPARTUE_TIME };
		String departure_time_info = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ideparture_time = c.getColumnIndex(KEY_DEPARTUE_TIME);
		if (c != null) {
			c.moveToFirst();
			departure_time_info = c.getString(ideparture_time);

		}
		c.close();
		return departure_time_info;

	}

	// get_DepartureFlightCode return the code of departure flight
	public String get_DepartureFlightCode(int trip_id) {

		String[] column = { KEY_DEPARTURE_FLIGHT_CODE };
		String departure_flight_code = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ideparture_code = c.getColumnIndex(KEY_DEPARTURE_FLIGHT_CODE);
		if (c != null) {
			c.moveToFirst();
			departure_flight_code = c.getString(ideparture_code);

		}
		c.close();
		return departure_flight_code;

	}

	// get_DepartureFlightConnection returns information about existence of
	// connection flight
	public String get_DepartureFlightConnection(int trip_id) {

		String[] column = { KEY_DEPARTURE_FLIGHT_CONNECTION };
		String departure_flight_connection = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ideparture_code = c.getColumnIndex(KEY_DEPARTURE_FLIGHT_CONNECTION);
		if (c != null) {
			c.moveToFirst();
			departure_flight_connection = c.getString(ideparture_code);

		}
		c.close();
		return departure_flight_connection;

	}

	// get_Vaccination returns information about vaccination necessity
	public String get_Vaccination(int trip_id) {

		String[] column = { KEY_VACCINATION };
		String vaccination = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ideparture_code = c.getColumnIndex(KEY_VACCINATION);
		if (c != null) {
			c.moveToFirst();
			vaccination = c.getString(ideparture_code);

		}
		c.close();
		return vaccination;

	}

	// get_Visa returns information about the visa
	public String get_Visa(int trip_id) {

		String[] column = { KEY_VISA };
		String visa = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ideparture_code = c.getColumnIndex(KEY_VISA);
		if (c != null) {
			c.moveToFirst();
			visa = c.getString(ideparture_code);

		}
		c.close();
		return visa;

	}

	// get_ReturnInfo return information about country and city of return flight
	public String[] get_ReturnInfo(int trip_id) {

		String[] columns = new String[] { KEY_RETURN_COUNTRY, KEY_RETURN_CITY };

		String[] return_info = new String[columns.length];
		Cursor c = tripsDatabase.query(DATABASE_TABLE, columns, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ireturn_country = c.getColumnIndex(KEY_RETURN_COUNTRY);
		int ireturn_city = c.getColumnIndex(KEY_RETURN_CITY);

		if (c != null) {
			c.moveToFirst();
			return_info[Arrays.asList(columns).indexOf(KEY_RETURN_COUNTRY)] = c
					.getString(ireturn_country);
			return_info[Arrays.asList(columns).indexOf(KEY_RETURN_CITY)] = c
					.getString(ireturn_city);
		}
		c.close();
		return return_info;
	}

	// get_ReturnDate returns information about date of return flight

	public String get_ReturnDate(int trip_id) {

		String[] column = { KEY_RETURN_DATE };
		String return_date = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ireturn_date = c.getColumnIndex(KEY_RETURN_DATE);
		if (c != null) {
			c.moveToFirst();
			return_date = c.getString(ireturn_date);

		}
		c.close();
		return return_date;

	}

	// get_ReturnTime returns information about time of return flight
	public String get_ReturnTime(int trip_id) {

		String[] column = { KEY_RETURN_TIME };
		String return_time = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ireturn_time = c.getColumnIndex(KEY_RETURN_TIME);
		if (c != null) {
			c.moveToFirst();
			return_time = c.getString(ireturn_time);

		}
		c.close();
		return return_time;

	}

	// get_ReturnFlightCode return return flight code
	public String get_ReturnFlightCode(int trip_id) {

		String[] column = { KEY_RETURN_FLIGHT_CODE };
		String return_flight_code = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ireturn_code = c.getColumnIndex(KEY_RETURN_FLIGHT_CODE);
		if (c != null) {
			c.moveToFirst();
			return_flight_code = c.getString(ireturn_code);

		}
		c.close();
		return return_flight_code;

	}

	// get_ReturnFlightConnection returns the information about return flight
	// connection
	public String get_ReturnFlightConnection(int trip_id) {

		String[] column = { KEY_RETURN_FLIGHT_CONNECTION };
		String return_flight_connection = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int ireturn_connection = c.getColumnIndex(KEY_RETURN_FLIGHT_CONNECTION);
		if (c != null) {
			c.moveToFirst();
			return_flight_connection = c.getString(ireturn_connection);

		}
		c.close();
		return return_flight_connection;

	}

	// get_Comments return the comments
	public String get_Comments(int trip_id) {

		String[] column = { KEY_COMMENTS };
		String comments = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int icomments = c.getColumnIndex(KEY_COMMENTS);
		if (c != null) {
			c.moveToFirst();
			comments = c.getString(icomments);

		}
		c.close();
		return comments;

	}

	// get_Notification return the information about Notification
	public String get_Notification(int trip_id) {

		String[] column = { NOTIFICATION_STATUS };
		String notification = "";
		Cursor c = tripsDatabase.query(DATABASE_TABLE, column, KEY_TRIP_ID
				+ "=" + trip_id, null, null, null, null);
		int inotification = c.getColumnIndex(NOTIFICATION_STATUS);
		if (c != null) {
			c.moveToFirst();
			notification = c.getString(inotification);

		}
		c.close();
		return notification;

	}
	// returns the columns used for the db
	public String[] getColumns() {
		String[] columns = new String[] { KEY_TRIP_ID, KEY_DESTINATION_COUNTRY,
				KEY_DESTINATION_CITY, KEY_DEPARTURE_FLIGHT_CODE,
				KEY_DEPARTURE_DATE, KEY_DEPARTUE_TIME,
				KEY_DEPARTURE_FLIGHT_CONNECTION, KEY_VACCINATION, KEY_VISA,
				KEY_RETURN_COUNTRY, KEY_RETURN_CITY, KEY_RETURN_FLIGHT_CODE,
				KEY_RETURN_DATE, KEY_RETURN_TIME, KEY_RETURN_FLIGHT_CONNECTION,
				NOTIFICATION_STATUS, KEY_COMMENTS };
		return columns;

	}
	// returns the columns used in the dashboard
	public String[] getColumnsTripList() {
		String[] columns = new String[] { KEY_DEPARTURE_DATE,
				KEY_DESTINATION_CITY, KEY_DESTINATION_COUNTRY, KEY_TRIP_ID };
		return columns;

	}

}
