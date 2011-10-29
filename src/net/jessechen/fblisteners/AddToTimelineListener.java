package net.jessechen.fblisteners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import net.jessechen.socialalarmclock.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

public class AddToTimelineListener implements RequestListener {
	private ProgressDialog dialog;
	private Handler mHandler;
	private Context ctx;

	public AddToTimelineListener(Context c, ProgressDialog dialog) {
		this.ctx = c;
		this.dialog = dialog;
		mHandler = new Handler();
	}

	@Override
	public void onComplete(String response, Object state) {
		dialog.dismiss();
		try {
			JSONObject json = new JSONObject(response);
			showAlertDialog("added to timeline!", json.toString(2));
		} catch (JSONException e) {
			Log.e("SAC", "Error: " + e.toString());
		}
	}

	@Override
	public void onIOException(IOException e, Object state) {
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
	}

	/*
	 * Show Alert Dialog
	 */
	public void showAlertDialog(final String title, final String message) {
		mHandler.post(new Runnable() {
			public void run() {
				new AlertDialog.Builder(ctx)
						.setTitle(title)
						.setMessage(message)
						.setPositiveButton(ctx.getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										return;
									}
								}).show();
			}
		});
	}
}