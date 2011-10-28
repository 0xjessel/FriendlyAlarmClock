package net.jessechen.fragments;

import net.jessechen.socialalarmclock.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {
	private String mTitle;

	public PageFragment(String title) {
		mTitle = title;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.pagefrag, container, false);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTitle);
		return view;
	}
}
