package net.jessechen.alarmclock;

import net.jessechen.fblisteners.AppRequestsListener;
import net.jessechen.fblisteners.LogoutListener;
import net.jessechen.secret.Secret;
import net.jessechen.socialalarmclock.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.viewpagerindicator.TitlePageIndicator;

public class AlarmClockActivity extends FragmentActivity {

	private Context ctx;
	private SharedPreferences mPrefs;
	private Facebook facebook = new Facebook(Secret.getAppId());
	private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);

	private ViewPager mViewPager;
	private MyFragmentPagerAdapter mAdapter;
	private TitlePageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ctx = this;

		mAdapter = new MyFragmentPagerAdapter(ctx, facebook,
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
			facebook.authorize(this, new String[] {}, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	private void downloadProfilePic() {
		String FILENAME = "profile_pic";
		
	}
	
	private void sendAppRequests() {
		Bundle params = new Bundle();
		params.putString("message", "check me out");
		params.putString("title", "Send an app request");
		facebook.dialog(ctx, "apprequests", params,
				new AppRequestsListener());					
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