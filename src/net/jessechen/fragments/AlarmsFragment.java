package net.jessechen.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.jessechen.alarmclock.AlarmReceiver;
import net.jessechen.alarmclock.EditAlarmActivity;
import net.jessechen.models.AlarmModel;
import net.jessechen.friendlyalarmclock.R;
import net.jessechen.utils.AlarmAdapter;
import net.jessechen.utils.ServerUtil;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class AlarmsFragment extends ListFragment {
	public static final String TAG = "AlarmsFragment";
	private static final int EDIT_ALARM_REQUEST = 0;
	private Facebook facebook;
	private static AsyncFacebookRunner mAsyncFacebookRunner;
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

				ServerUtil.addToTimeline(getActivity(), mAsyncFacebookRunner,
						alarm);
				addToAlarmManager(alarm);
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private void addToAlarmManager(AlarmModel alarm) {
		Calendar cur = Calendar.getInstance();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, alarm.getHour());
		cal.set(Calendar.MINUTE, alarm.getMinute());

		if (cal.before(cur)) {
			cal.roll(Calendar.DAY_OF_YEAR, true);
		}

		Intent intent = new Intent(getActivity(), AlarmReceiver.class);
		intent.putExtra("assoc_pid", String.valueOf(alarm.getPid()));

		PendingIntent sender = PendingIntent.getBroadcast(getActivity(),
				AlarmReceiver.REQUEST_CODE, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) getActivity().getSystemService(
				Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);

		long diff = cal.getTimeInMillis() - cur.getTimeInMillis();
		int hours = (int) ((diff / (1000 * 60 * 60)) % 24);
		int minutes = (int) ((diff / (1000 * 60)) % 60);
		
		Toast.makeText(getActivity(),
				String.format("This alarm is set for %d hours and %d minutes from now.", hours, minutes),
				Toast.LENGTH_LONG).show();
	}
}
