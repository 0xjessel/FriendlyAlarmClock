package net.jessechen.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.jessechen.alarmclock.AlarmReceiver;
import net.jessechen.alarmclock.EditAlarmActivity;
import net.jessechen.fblisteners.AddToTimelineListener;
import net.jessechen.models.AlarmModel;
import net.jessechen.models.CommentModel;
import net.jessechen.socialalarmclock.R;
import net.jessechen.utils.AlarmAdapter;
import net.jessechen.utils.ServerUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class AlarmsFragment extends ListFragment {
	public static final String TAG = "AlarmsFragment";
	private static final int EDIT_ALARM_REQUEST = 0;
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncFacebookRunner;
	ArrayList<AlarmModel> mAlarms;

	public AlarmsFragment() {
	}

	public AlarmsFragment(Facebook fb, AsyncFacebookRunner mAsyncRunner) {
		facebook = fb;
		mAsyncFacebookRunner = mAsyncRunner;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAlarms = retrieveAlarms();

		setListAdapter(new AlarmAdapter(getActivity(), R.layout.alarm_item,
				mAlarms));
	}

	private ArrayList<AlarmModel> retrieveAlarms() {
		ArrayList<AlarmModel> alarms = new ArrayList<AlarmModel>();
		Set<Integer> repeat = new HashSet<Integer>();
		repeat.add(2);
		repeat.add(5);
		alarms.add(new AlarmModel(false, 1, 10, repeat, null, true, "test 1:10"));
		alarms.add(new AlarmModel(true, 13, 30, null, null, true, "test 13:30"));
		return alarms;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.alarm_frag, container, false);

		LinearLayout alarmAddLayout = (LinearLayout) view
				.findViewById(R.id.alarm_add_layout);
		alarmAddLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editAlarm(-1);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				editAlarm(position);
			}
		});
	}

	private void editAlarm(int position) {
		Log.d(TAG, String.format("editAlarm(%d)", position));
		AlarmModel alarm = null;
		if (position >= 0)
			alarm = mAlarms.get(position);
		Intent intent = new Intent(getActivity(), EditAlarmActivity.class);
		intent.putExtra("alarm", alarm);
		intent.putExtra("position", position);
		startActivityForResult(intent, EDIT_ALARM_REQUEST);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_ALARM_REQUEST:
			if (data != null) {
				Bundle bundle = data.getExtras();
				AlarmModel alarm = bundle.getParcelable("alarm");
				int position = bundle.getInt("position");
				if (position >= 0)
					mAlarms.remove(position);
				mAlarms.add(alarm);
				((ArrayAdapter<?>) getListAdapter()).notifyDataSetChanged();

				addToTimeline(alarm);
				addToAlarmManager(alarm);
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private void addToAlarmManager(AlarmModel alarm) {
		// Date now = new Date();
		// Date alarmDate = new Date(now.getYear(), now.getMonth(),
		// now.getDay(),
		// alarm.getHour(), alarm.getMinute());
		Calendar cal = Calendar.getInstance();
//		cal.set(cal.HOUR_OF_DAY, alarm.getHour());
//		cal.set(cal.MINUTE, alarm.getMinute());
		// cal.set(now.getYear(), now.getMonth(), now.getDay(), alarm.getHour(),
		// alarm.getMinute());

		// if (now.after(alarmDate)) {
		// // get a Calendar object with current time
		// // add 5 minutes to the calendar object
		// cal.roll(Calendar.DAY_OF_YEAR, true);
		// }

		Intent intent = new Intent(getActivity(), AlarmReceiver.class);
		intent.putExtra("alarm_message", String.valueOf(alarm.getPid()));
//		intent.setAction("alarm_message");
		// In reality, you would want to have a static variable for the request
		// code instead of 192837
		PendingIntent sender = PendingIntent.getBroadcast(getActivity(),
				AlarmReceiver.REQUEST_CODE, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) getActivity().getSystemService(
				Context.ALARM_SERVICE);
		// am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + 3000, sender);
	}

	private void addToTimeline(AlarmModel am) {
		ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
				"adding to timeline..", true, true);

		String alarmURL = ServerUtil.POST_ALARM_URL;
		Bundle alarmParams = new Bundle();
		alarmParams.putString("title", am.getLabel());
		alarmParams.putString("time", am.getTimeText());
		alarmURL = alarmURL + "?" + Util.encodeUrl(alarmParams);
		alarmParams.putString("alarm", alarmURL);

		mAsyncFacebookRunner.request("me/" + ServerUtil.NAMESPACE + ":"
				+ ServerUtil.SET, alarmParams, "POST",
				new AddToTimelineListener(am, getActivity(), dialog), null);
	}

	private LinkedList<CommentModel> readFromPost(AlarmModel am) {
		String pid = Long.toString(am.getPid());
		final LinkedList<CommentModel> comments = new LinkedList<CommentModel>();
		mAsyncFacebookRunner.request(pid + "/comments", new RequestListener() {

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}

			@Override
			public void onComplete(String response, Object state) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONArray commentsArray = obj.getJSONArray("data");
					for (int i = 0; i < commentsArray.length(); i++) {
						JSONObject comment = commentsArray.getJSONObject(i);
						CommentModel c = new CommentModel();
						c.setCommentID(comment.getString("id"));
						JSONObject from = comment.getJSONObject("from");
						c.setFrom(from.getString("name"));
						c.setMsg(comment.getString("message"));

						comments.add(c);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return comments;
	}
}
