package net.jessechen.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.jessechen.alarmclock.EditAlarmActivity;
import net.jessechen.fblisteners.AddToTimelineListener;
import net.jessechen.models.AlarmModel;
import net.jessechen.models.CommentModel;
import net.jessechen.socialalarmclock.R;
import net.jessechen.utils.AlarmAdapter;
import net.jessechen.utils.ServerUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class AlarmsFragment extends ListFragment {
	public static final String TAG = "AlarmsFragment";
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncFacebookRunner;
	ArrayList<AlarmModel> mAlarms;

	public AlarmsFragment() {
	}

	public AlarmsFragment(Facebook fb, AsyncFacebookRunner mAsyncRunner) {
		facebook = fb;
		mAsyncFacebookRunner = mAsyncRunner;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAlarms = retrieveAlarms();

		setListAdapter(new AlarmAdapter(getActivity(), R.layout.alarm_item,
				mAlarms));
	}

	private ArrayList<AlarmModel> retrieveAlarms() {
		ArrayList<AlarmModel> alarms = new ArrayList<AlarmModel>();
		Set<Integer> repeat = new HashSet<Integer>();
		repeat.add(2);
		repeat.add(5);
		alarms.add(new AlarmModel(false, 1, 10, repeat, null, true, "test 1:10"));
		alarms.add(new AlarmModel(true, 13, 30, null, null, true, "test 13:30"));
		return alarms;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.alarm_frag, container, false);

		LinearLayout alarmAddLayout = (LinearLayout) view
				.findViewById(R.id.alarm_add_layout);
		alarmAddLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editAlarm(-1);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				editAlarm(position);
			}
		});
	}

	private void editAlarm(int position) {
		Log.d(TAG, String.format("editAlarm(%d)", position));
		AlarmModel alarm = null;
		if (position >= 0)
			alarm = mAlarms.get(position);
		Intent intent = new Intent(getActivity(), EditAlarmActivity.class);
		intent.putExtra("alarm", alarm);
		startActivityForResult(intent, 0);
	}

	private void addToTimeline(AlarmModel am) {
		ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
				"adding to timeline..", true, true);

		String alarmURL = ServerUtil.POST_ALARM_URL;
		Bundle alarmParams = new Bundle();
		alarmParams.putString("title", am.getLabel());
		alarmParams.putString("time", am.getTimeText());
		alarmURL = alarmURL + "?" + Util.encodeUrl(alarmParams);
		alarmParams.putString("alarm", alarmURL);

		mAsyncFacebookRunner.request("me/" + ServerUtil.NAMESPACE + ":"
				+ ServerUtil.SET, alarmParams, "POST",
				new AddToTimelineListener(am, getActivity(), dialog), null);
	}

	private LinkedList<CommentModel> readFromPost(String pid) {
		final LinkedList<CommentModel> comments = new LinkedList<CommentModel>();
		mAsyncFacebookRunner.request(pid + "/comments", new RequestListener() {
			
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
			}
			
			@Override
			public void onIOException(IOException e, Object state) {
			}
			
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
			}
			
			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
			
			@Override
			public void onComplete(String response, Object state) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONArray commentsArray = obj.getJSONArray("data");
					for (int i = 0; i < commentsArray.length(); i++) {
						JSONObject comment = commentsArray.getJSONObject(i);
						CommentModel c = new CommentModel();
						c.setCommentID(comment.getString("id"));
						JSONObject from = comment.getJSONObject("from");
						c.setFrom(from.getString("name"));
						c.setMsg(comment.getString("message"));
						
						comments.add(c);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}					
		});
		return comments;
	}
}
