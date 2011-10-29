package net.jessechen.alarmclock;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.jessechen.fblisteners.AppRequestsListener;
import net.jessechen.fblisteners.LogoutListener;
import net.jessechen.secret.Secret;
import net.jessechen.socialalarmclock.R;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.markupartist.android.widget.ActionBar;
import com.viewpagerindicator.TitlePageIndicator;

public class AlarmClockActivity extends FragmentActivity {

	private Context ctx;
	private SharedPreferences mPrefs;
	private final String FILENAME = "profile_pic";
	private Facebook facebook = new Facebook(Secret.getAppId());
	private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);

	private ViewPager mViewPager;
	private MyFragmentPagerAdapter mAdapter;
	private TitlePageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		InputStream is = getProfilePicture();
		if (is != null) {
			actionBar.setHomeLogo(is);
		}

		ctx = this;

		mAdapter = new MyFragmentPagerAdapter(ctx, facebook, mAsyncRunner,
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mAdapter);
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mViewPager);

		mViewPager.setCurrentItem(1);

		/*
		 * Get existing access_token if any
		 */
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		/*
		 * Only call authorize if the access_token has expired.
		 */
		if (!facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "publish_actions" },
					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							getUserInfo(editor);
							editor.commit();

							downloadProfilePic();

							sendAppRequests();
						}

						@Override
						public void onFacebookError(FacebookError error) {
						}

						@Override
						public void onError(DialogError e) {
						}

						@Override
						public void onCancel() {
						}
					});
		}
	}

	private InputStream getProfilePicture() {
		try {
			return openFileInput(FILENAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	private void getUserInfo(Editor editor) {
		try {
			String jsonUser = facebook.request("me");
			JSONObject obj = new JSONObject(jsonUser);
			editor.putString("uid", obj.optString("id", "-1")); // get userid
			editor.putString("name", obj.optString("name", "John Doe")); // get
																			// name
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void downloadProfilePic() {
		final String BASE_URL = "https://graph.facebook.com/";
		String uid = mPrefs.getString("uid", "-1");
		InputStream is = null;

		try {
			if (!uid.equals("-1")) {
				is = new URL(BASE_URL + uid + "/picture?type=large")
						.openStream();
				byte[] pic = readBytes(is);
				FileOutputStream fos = openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				fos.write(pic);
				fos.close();
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] readBytes(InputStream inputStream) throws IOException {
		// this dynamically extends to take the bytes you read
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		// we need to know how may bytes were read to write them to the
		// byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	private void sendAppRequests() {
		Bundle params = new Bundle();
		params.putString("message", "check me out");
		params.putString("title", "Send an app request");
		facebook.dialog(ctx, "apprequests", params, new AppRequestsListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, getString(R.string.signout)).setIcon(
				android.R.drawable.ic_notification_clear_all);
		menu.add(0, 1, 0, getString(R.string.settings)).setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(0, 2, 0, getString(R.string.about)).setIcon(
				android.R.drawable.ic_menu_info_details);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0: // logout
			mAsyncRunner.logout(ctx, new LogoutListener());
			Toast.makeText(ctx, "access_token revoked", Toast.LENGTH_SHORT)
					.show();
			return true;
		case 1: // settings
			Toast.makeText(ctx, "Settings", Toast.LENGTH_SHORT).show();
			return true;
		case 2: // about
			Toast.makeText(ctx, "Mobile Hack", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}