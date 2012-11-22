package mobi.bwize.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ToDoDBHelper extends SQLiteOpenHelper {

	private SQLiteDatabase database;
	private Context context2;

	public ToDoDBHelper(Context context) {
		super(context, "ToDoList", null, 1);
		context2=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// Executes SQL statement to create the appropriate table if it does not
		// exist
		db.execSQL("CREATE TABLE IF NOT EXISTS tblToDo ("
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,trip VARCHAR, task VARCHAR, "
				+ "date INTEGER)");

		
		final int time = (int) System.currentTimeMillis();
		//db.execSQL("INSERT INTO tblToDo (trip,task,date) VALUES ('one', 'Sample to do',"+time+")");
		//db.execSQL("INSERT INTO tblToDo (trip,task,date) VALUES ('two', 'From trip 2',"+time+")");
		//db.execSQL("INSERT INTO tblToDo (trip,task,date) VALUES ('one', 'Sample to do',"+time+")");
		
		

	}

	public Cursor getToDo(String tripID){
		Cursor data;
		
		ToDoDBHelper helper = new ToDoDBHelper(context2);
		database = helper.getWritableDatabase();
		
		data=database.query("tblToDo", new String[]{"task", BaseColumns._ID}, "trip='"+tripID+"'", null, null, null,
				"date DESC");
		
		return data;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// would be usd for future database upgrades.
	}

	
}
