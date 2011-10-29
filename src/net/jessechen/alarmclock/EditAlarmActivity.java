package net.jessechen.alarmclock;

import java.util.Date;
import java.util.HashSet;

import net.jessechen.models.AlarmModel;
import net.jessechen.socialalarmclock.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.FrameLayout.LayoutParams;

public class EditAlarmActivity extends Activity implements OnTimeSetListener {
	private static final String TAG = "EditAlarmActivity";
	AlarmModel mAlarm;

	CheckBox enabled;
	TextView time;
	TextView label;

	int hour, minute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm_edit);

		mAlarm = getIntent().getParcelableExtra("alarm");

		boolean newAlarm = mAlarm == null;
		if (newAlarm) {
			Date now = new Date();
			mAlarm = new AlarmModel(true, now.getHours(), now.getMinutes(),
					new HashSet<Integer>(), "", true, "");
		}

		hour = mAlarm.getHour();
		minute = mAlarm.getMinute();

		enabled = (CheckBox) findViewById(R.id.alarm_enabled);
		time = (TextView) findViewById(R.id.alarm_time);
		label = (TextView) findViewById(R.id.alarm_label);

		enabled.setChecked(mAlarm.isEnabled());
		time.setText(mAlarm.getTimeText());
		label.setText(mAlarm.getLabel());

		if (newAlarm)
			startTimePickerDialog();
	}

	public void enabledClicked(View v) {
		enabled.setChecked(!enabled.isChecked());
	}

	public void timeClicked(View v) {
		startTimePickerDialog();
	}

	public void labelClicked(View v) {
		EditText labelEditText = new EditText(this);
		labelEditText.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		Dialog dialog = new Dialog(this);
		dialog.setContentView(labelEditText);
		dialog.setTitle(getResources().getString(R.string.alarm_label));
		dialog.show();
	}

	private void startTimePickerDialog() {
		TimePickerDialog dialog = new TimePickerDialog(this, this, hour,
				minute, false);
		dialog.show();
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int min) {
		Log.d(TAG, String.format("onTimeSet(view, %d, %d)", hourOfDay, min));
		hour = hourOfDay;
		minute = min;
		time.setText(AlarmModel.getTimeText(hourOfDay, min));
	}

	public void saveClicked(View v) {
		mAlarm.setEnabled(enabled.isEnabled());
		mAlarm.setHour(hour);
		mAlarm.setMinute(minute);
		mAlarm.setLabel(label.getText().toString());

		Intent intent = new Intent();
		intent.putExtra("alarm", mAlarm);
		setResult(RESULT_OK, intent);
	}

	public void cancelClicked(View v) {
		Intent intent = new Intent();
		intent.putExtra("alarm", mAlarm);
		setResult(RESULT_CANCELED, intent);
	}
}
