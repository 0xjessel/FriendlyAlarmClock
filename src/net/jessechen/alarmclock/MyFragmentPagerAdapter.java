package net.jessechen.alarmclock;

import net.jessechen.fragments.AlarmsFragment;
import net.jessechen.fragments.InboxFragment;
import net.jessechen.fragments.PageFragment;
import net.jessechen.fragments.PostFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

import com.facebook.android.Facebook;
import com.viewpagerindicator.TitleProvider;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter implements
		TitleProvider {

	private static Facebook facebook;
	private static int NUM_VIEWS = 3;
	private static Context ctx;
	private static String[] titles = new String[] { "Inbox", "Alarms", "Post" };

	public MyFragmentPagerAdapter(Context c, Facebook fb, FragmentManager fm) {
		super(fm);
		ctx = c;
		facebook = fb;
	}

	@Override
	public String getTitle(int position) {
		return titles[position];
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
			case 0:
				return new InboxFragment(ctx, facebook);
			case 1:
				return new AlarmsFragment(ctx, facebook);
			case 2:
				return new PostFragment(ctx, facebook);
			default:
				return new PageFragment("failed");
		}
	}

	@Override
	public int getCount() {
		return NUM_VIEWS;
	}

}
