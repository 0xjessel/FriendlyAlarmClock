package net.jessechen.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jessechen.models.AlarmModel;
import net.jessechen.socialalarmclock.R;
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
			mHolder.timeDisplay.setText(getTimeText(alarm.getHour(),
					alarm.getMinute()));
			mHolder.daysOfWeek.setText(getDaysText(alarm.getRepeat()));
			mHolder.label.setText(alarm.getLabel());
		}
		return convertView;
	}

	private static String getTimeText(int hour, int minute) {
		if (hour < 0 || hour >= 24 || minute < 0 || minute >= 60)
			return "Bad Input";
		return String.format("%d:%d %s", hour > 12 ? hour - 12 : hour, minute,
				hour > 12 ? "PM" : "AM");
	}

	private static String getDaysText(Set<Integer> repeat) {
		if (repeat == null)
			return "";
		Integer[] days = new Integer[repeat.size()];
		days = repeat.toArray(days);
		Arrays.sort(days);

		String ret = "";
		for (int day : days) {
			if (!ret.equals(""))
				ret += ", ";
			switch (day) {
			case 1:
				ret += "Mo";
				break;
			case 2:
				ret += "Tu";
				break;
			case 3:
				ret += "We";
				break;
			case 4:
				ret += "Th";
				break;
			case 5:
				ret += "Fr";
				break;
			case 6:
				ret += "Sa";
				break;
			case 7:
				ret += "Su";
				break;
			default:
				ret += "?";
				break;
			}
		}
		return ret;
	}

	static class ViewHolder {
		CheckBox enabled;
		TextView timeDisplay;
		TextView daysOfWeek;
		TextView label;
	}
}
