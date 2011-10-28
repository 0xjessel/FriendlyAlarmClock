package net.jessechen.fblisteners;

import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class AppRequestsListener implements DialogListener {

	@Override
	public void onComplete(Bundle values) {
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
