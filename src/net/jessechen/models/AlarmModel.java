package net.jessechen.models;

import java.io.File;
import java.util.Set;

public class AlarmModel {
	boolean enabled;
	int hour, minute; // 24-hr military style. 0 <= hour < 24, 0 <= minute < 60
	Set<Integer> repeat; // null or a set of {1,2,...,7}
	File ringtone;
	boolean vibrate;
	String label;

	public AlarmModel() {

	}

	public AlarmModel(boolean enabled, int hour, int minute,
			Set<Integer> repeat, File ringtone, boolean vibrate, String label) {
		super();
		this.enabled = enabled;
		this.hour = hour;
		this.minute = minute;
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
		this.repeat = repeat;
	}

	public File getRingtone() {
		return ringtone;
	}

	public void setRingtone(File ringtone) {
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
}
