package net.jessechen.utils;

import java.util.List;

import net.jessechen.models.AlarmModel;
import net.jessechen.friendlyalarmclock.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AlarmAdapter extends ArrayAdapter<AlarmModel> {
	ViewHolder mHolder;
	List<AlarmModel> mAlarms;

	public AlarmAdapter(Context context, int textViewResourceId,
			List<AlarmModel> objects) {
		super(context, textViewResourceId, objects);
		mAlarms = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.alarm_item, parent, false);

			mHolder = new ViewHolder();
			mHolder.enabled = (CheckBox) convertView.findViewById(R.id.enabled);
			mHolder.timeDisplay = (TextView) convertView
					.findViewById(R.id.timeDisplay);
			mHolder.daysOfWeek = (TextView) convertView
					.findViewById(R.id.daysOfWeek);
			mHolder.label = (TextView) convertView.findViewById(R.id.label);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		// Fill in the view

		AlarmModel alarm = mAlarms.get(position);
		if (alarm != null) {
			mHolder.enabled.setChecked(alarm.isEnabled());
			mHolder.timeDisplay.setText(alarm.getTimeText());
			mHolder.daysOfWeek.setText(alarm.getDaysText());
		}
		return convertView;
	}

	static class ViewHolder {
		CheckBox enabled;
		TextView timeDisplay;
		TextView daysOfWeek;
		TextView label;
	}
}
