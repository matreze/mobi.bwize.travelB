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
 * File Name: CountriesDB.java
 * Description:
 * 
 * Helper file for the countries database.
 * 
 * Due to the number of cities and static nature of this database
 * it was decided to statically import a finished database
 * the first time it is needed. This significantly reduces load
 * time for the Spinners in the AddTrip form.
 * 
 */

package mobi.bwize.travelB;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// This class creates a new database helper
public class CountriesDB extends SQLiteOpenHelper {

	// Static database names and path variables
	private static String DATABASE_NAME = "Countries.db";
	public final static String DATABASE_PATH = "/data/data/mobi.bwize.travelB/databases/";
	private static final int DATABASE_VERSION = 1;

	@SuppressWarnings("unused")
	private SQLiteDatabase Countries;
	private final Context dbContext;

	public CountriesDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.dbContext = context;
		// checking database and open it if exists
		if (checkDataBase()) {
			openDataBase();
		} else {
			// If the database does not exist, then copy it from the assets
			// folder and then open it.
			try {
				this.getReadableDatabase();
				copyDataBase();
				this.close();
				openDataBase();

			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	// Function to copy database - done by opening 
	// the original db as a strem and saving it to the new location
	private void copyDataBase() throws IOException {
		// Open streams
		InputStream myInput = dbContext.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		// Copy entire file
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	// Helper function to open the database
	public void openDataBase() throws SQLException {
		String dbPath = DATABASE_PATH + DATABASE_NAME;
		Countries = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	// Function to check if the database exists
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		boolean exist = false;
		try {
			String dbPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			Log.v("db log", "database does't exist");
		}

		if (checkDB != null) {
			exist = true;
			checkDB.close();
		}
		return exist;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}