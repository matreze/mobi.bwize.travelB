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
 * File Name: ToDO.java
 * Description:
 * 
 * Implements a basic todo list where a task can be 
 * added with a dialog box. If a task is to be deleted, touch it 
 * and confirm deletion via the dialog box.
 * 
 * It is a fragment that appears in the dashboard.
 * 
 */

package mobi.bwize.travelB;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ToDO extends ListFragment {

	private SQLiteDatabase database;
	static CursorAdapter dataSource;
	private View entryView;
	private String trip;

	ViewGroup root;

	// fields of the database that will be required
	private static final String fields[] = { "task", BaseColumns._ID };

	
	// Set the view root to return to the dashbpard
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = (ViewGroup) inflater.inflate(R.layout.activity_to_do, null);
		return root;

	}

	// set the to-do list to all tasks forund for the current trip - by id
	public void setID(String id) {
		trip = id;
		ToDoDBHelper helper = new ToDoDBHelper(getActivity());
		database = helper.getWritableDatabase();

		// get all the data from the database for that trip
		Cursor data = helper.getToDo(trip);

		dataSource = new SimpleCursorAdapter(getActivity(), R.layout.row, data,
				fields, new int[] { R.id.todoText_xml });

		setListAdapter(dataSource);
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// instantiate new dialog builder for delete confirmation
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		// set the database helper
		final ToDoDBHelper helper2 = new ToDoDBHelper(getActivity());
		final long id2 = id;
		// set title
		alertDialogBuilder.setTitle("Delete ToDo");
		// set dialog message
		alertDialogBuilder
				.setMessage("Delete this task?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								database = helper2.getWritableDatabase();
								// Delete the selected task
								database.delete("tblToDo", BaseColumns._ID
										+ "=" + String.valueOf(id2), null);
								Log.d("", String.valueOf(id2));
								database.close();
								refresh();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

	public void add(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		entryView = getActivity().getLayoutInflater().inflate(
				R.layout.new_task, null);
		alertDialogBuilder.setView(entryView);
		final EditText taskEditText = (EditText) entryView
				.findViewById(R.id.task_text_xml);

		// set the database helper
		final ToDoDBHelper helper2 = new ToDoDBHelper(getActivity());
		// set title
		alertDialogBuilder.setTitle("New Task");

		// set dialog message
		alertDialogBuilder
				.setCancelable(true)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								database = helper2.getWritableDatabase();
								database.execSQL("INSERT INTO tblToDo (trip,task,date) VALUES ('"
										+ trip
										+ "', '"
										+ taskEditText.getText().toString()
										+ "',"
										+ (int) System.currentTimeMillis()
										+ ")");
								database.close();
								refresh();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	// function to refresh the cursor data
	public static void refresh() {
		dataSource.getCursor().requery();
	}
}
