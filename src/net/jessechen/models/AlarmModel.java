package net.jessechen.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmModel implements Parcelable {
	boolean enabled;
	int hour, minute; // 24-hr military style. 0 <= hour < 24, 0 <= minute < 60
	Set<Integer> repeat = new HashSet<Integer>(); // a set of {1,2,...,7}
	String ringtone;
	boolean vibrate;
	String label;

	public AlarmModel() {

	}

	public AlarmModel(boolean enabled, int hour, int minute,
			Set<Integer> repeat, String ringtone, boolean vibrate, String label) {
		super();
		this.enabled = enabled;
		this.hour = hour;
		this.minute = minute;
		if (repeat != null)
			this.repeat = repeat;
		this.ringtone = ringtone;
		this.vibrate = vibrate;
		this.label = label;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public Set<Integer> getRepeat() {
		return repeat;
	}

	public void setRepeat(Set<Integer> repeat) {
		if (repeat != null)
			this.repeat = repeat;
	}

	public String getRingtone() {
		return ringtone;
	}

	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}

	public boolean isVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTimeText() {
		return getTimeText(hour, minute);
	}

	public static String getTimeText(int hour, int minute) {
		if (hour < 0 || hour >= 24 || minute < 0 || minute >= 60)
			return "Bad Input";
		return String.format("%d:%02d %s", hour > 12 ? hour - 12
				: (hour == 0 ? 12 : hour), minute, hour >= 12 ? "PM" : "AM");
	}

	public String getDaysText() {
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(enabled ? 1 : 0);
		dest.writeInt(hour);
		dest.writeInt(minute);
		int[] repeatArray = new int[repeat.size()];
		for (int i = 0; i < repeat.size(); i++) {
			repeatArray[i] = repeat.toArray(new Integer[0])[i];
		}
		dest.writeIntArray(repeatArray);
		dest.writeString(ringtone);
		dest.writeInt(vibrate ? 1 : 0);
		dest.writeString(label);
	}

	public static final Parcelable.Creator<AlarmModel> CREATOR = new Parcelable.Creator<AlarmModel>() {
		public AlarmModel createFromParcel(Parcel in) {
			return new AlarmModel(in);
		}

		public AlarmModel[] newArray(int size) {
			return new AlarmModel[size];
		}
	};

	private AlarmModel(Parcel in) {
		enabled = in.readInt() == 1;
		hour = in.readInt();
		minute = in.readInt();
		int[] repeatArray = in.createIntArray();
		repeat = new HashSet<Integer>();
		for (int i = 0; i < repeatArray.length; i++) {
			repeat.add(repeatArray[i]);
		}
		ringtone = in.readString();
		vibrate = in.readInt() == 1;
		label = in.readString();
	}
}
