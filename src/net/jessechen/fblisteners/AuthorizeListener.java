package net.jessechen.fblisteners;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class AuthorizeListener implements DialogListener {
	Context ctx;
	SharedPreferences mPrefs;
	Facebook facebook;

	public AuthorizeListener(Context c, SharedPreferences sp, Facebook fb) {
		ctx = c;
		this.mPrefs = sp;
		facebook = fb;
	}

	@Override
	public void onComplete(Bundle values) {
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString("access_token", facebook.getAccessToken());
		editor.putLong("access_expires", facebook.getAccessExpires());
		editor.commit();

		// Send app requests
		facebook.dialog(ctx, "apprequests",
				new AppRequestsListener());
	}

	@Override
	public void onFacebookError(FacebookError e) {
	}

	@Override
	public void onError(DialogError e) {
	}

	@Override
	public void onCancel() {
	}

}
