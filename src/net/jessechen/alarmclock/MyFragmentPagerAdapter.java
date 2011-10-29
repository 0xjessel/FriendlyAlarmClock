package net.jessechen.alarmclock;

import net.jessechen.fragments.AlarmsFragment;
import net.jessechen.fragments.FriendsFragment;
import net.jessechen.fragments.HistoryFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.viewpagerindicator.TitleProvider;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter implements
		TitleProvider {

	private static Facebook facebook;
	private static AsyncFacebookRunner mAsyncRunner;
	private static Context ctx;
	private static String[] titles = new String[] { "Inbox", "Alarms", "Friends" };

	public MyFragmentPagerAdapter(Context c, Facebook fb, AsyncFacebookRunner afr, FragmentManager fm) {
		super(fm);
		ctx = c;
		facebook = fb;
		mAsyncRunner = afr;
	}

	@Override
	public String getTitle(int position) {
		return titles[position];
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new HistoryFragment(facebook);
		case 1:
			return new AlarmsFragment(facebook, mAsyncRunner);
		case 2:
			return new FriendsFragment(facebook);
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return titles.length;
	}

}
