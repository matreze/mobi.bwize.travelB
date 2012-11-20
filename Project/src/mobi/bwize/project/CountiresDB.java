package mobi.bwize.project;

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
import android.widget.Toast;

public class CountiresDB extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "Countries.db";
	public final static String DATABASE_PATH = "/data/data/mobi.bwize.project/databases/";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase Countires;
	private final Context dbContext;

	public CountiresDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.dbContext = context;
		// DATABASE_NAME = DATABASE_NAME;
		// checking database and open it if exists
		if (checkDataBase()) {
			openDataBase();
		} else {
			try {
				this.getReadableDatabase();
				copyDataBase();
				this.close();
				openDataBase();

			} catch (IOException e) {
				throw new Error("Error copying database");
			}
			// Toast.makeText(context, "Initial database is created",
			// Toast.LENGTH_LONG).show();
		}
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = dbContext.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String dbPath = DATABASE_PATH + DATABASE_NAME;
		Countires = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}