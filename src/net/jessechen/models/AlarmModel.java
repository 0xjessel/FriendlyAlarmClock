package net.jessechen.models;

import java.io.File;
import java.util.Set;

public class AlarmModel {
	boolean enabled;
	int hour, minute; // 24-hr military style
	Set<Integer> repeat;
	File ringtone;
	boolean vibrate;
	String label;

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
