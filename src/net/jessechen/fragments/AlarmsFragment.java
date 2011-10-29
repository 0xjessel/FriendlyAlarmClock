package net.jessechen.fragments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.jessechen.models.AlarmModel;
import net.jessechen.socialalarmclock.R;
import net.jessechen.utils.AlarmAdapter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.android.Facebook;

public class AlarmsFragment extends ListFragment {
	public static final String TAG = "AlarmsFragment";
	private Facebook facebook;

	ArrayList<AlarmModel> mAlarms;

	public AlarmsFragment(Facebook fb) {
		facebook = fb;
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
		AlarmModel alarm = new AlarmModel();
		alarm.setEnabled(true);
		alarm.setHour(2);
		alarm.setMinute(40);
		alarm.setLabel("test");
		Set<Integer> repeat = new HashSet<Integer>();
		repeat.add(2);
		repeat.add(5);
		alarm.setRepeat(repeat);
		alarms.add(alarm);
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
				addAlarm();
			}
		});
		return view;
	}

	private void addAlarm() {
		Log.d(TAG, "addAlarm");
	}
}
