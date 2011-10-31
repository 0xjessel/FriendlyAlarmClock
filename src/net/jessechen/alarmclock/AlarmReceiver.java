package net.jessechen.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	public static final int REQUEST_CODE = 1111;
	MediaPlayer mMediaPlayer;

	@Override
	public void onReceive(Context context, Intent intent) {

		Toast.makeText(context, "alarm!", Toast.LENGTH_LONG)
				.show();

		try {
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_ALARM);
			if (alert == null) {
				// alert is null, using backup
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				if (alert == null) { // I can't see this ever being null (as
										// always have a default notification)
										// but just incase
					// alert backup is null, using 2nd backup
					alert = RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
				}
			}
			
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.setLooping(true);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				this.wait(2000);
				mMediaPlayer.stop();
			}
		} catch (Exception ex) {

		}

		// try {
		// Bundle bundle = intent.getExtras();
		// String pid = bundle.getString("alarm_message");
		// LinkedList<CommentModel> comments = AlarmsFragment
		// .readFromPost(pid);
		//
		// if (comments.size() > 0) {
		// Toast.makeText(context, comments.get(0).getMsg(),
		// Toast.LENGTH_LONG).show();
		// }
		//
		// } catch (Exception e) {
		//
		// // AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// // builder.setMessage("don't forget class at 3!").setTitle("Alarm");
		// // AlertDialog alert = builder.create();
		// // alert.show();
		// e.printStackTrace();
		//
		// }
	}

}
