package net.jessechen.fragments;

import net.jessechen.socialalarmclock.R;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.android.Facebook;

public class HistoryFragment extends Fragment {
	private Context ctx;
	private Facebook facebook;
	
	public HistoryFragment(Context c, Facebook fb) {
		ctx = c;
		facebook = fb;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.inboxfrag, container, false);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText("Inbox");
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(30);
		return view;
	}
}
