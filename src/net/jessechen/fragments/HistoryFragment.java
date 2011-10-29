package net.jessechen.fragments;

import net.jessechen.socialalarmclock.R;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.android.Facebook;

public class HistoryFragment extends ListFragment {
	private Facebook facebook;

	public HistoryFragment() {
	}

	public HistoryFragment(Facebook fb) {
		facebook = fb;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.history_frag, container, false);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText("Inbox");
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(30);
		return view;
	}
}
