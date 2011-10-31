package net.jessechen.alarmclock;

import java.util.Date;
import java.util.HashSet;

import net.jessechen.models.AlarmModel;
import net.jessechen.socialalarmclock.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditAlarmActivity extends Activity implements OnTimeSetListener {
	private static final String TAG = "EditAlarmActivity";
	AlarmModel mAlarm;
	int mPosition;

	CheckBox enabled;
	TextView time;
	TextView label;

	int hour, minute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm_edit);

		Bundle bundle = getIntent().getExtras();

		mAlarm = bundle.getParcelable("alarm");
		mPosition = bundle.getInt("position");

		boolean newAlarm = mAlarm == null;
		if (newAlarm) {
			Date now = new Date();
			mAlarm = new AlarmModel(true, now.getHours(), now.getMinutes(),
					new HashSet<Integer>(), "", true, getString(R.string.alarm_label_default));
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
		final EditText labelEditText = new EditText(this);

		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		float dp = 250f;
		int pixels = (int) (metrics.density * dp + 0.5f);

		labelEditText.setWidth(pixels);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(labelEditText);

		OnClickListener dialogClickListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					String text = labelEditText.getText().toString();
					label.setText(text);
					break;
				}
			}
		};

		builder.setPositiveButton(R.string.done, dialogClickListener);
		builder.setNegativeButton(R.string.revert, dialogClickListener);

		builder.create().show();
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
		intent.putExtra("position", mPosition);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void cancelClicked(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
}
