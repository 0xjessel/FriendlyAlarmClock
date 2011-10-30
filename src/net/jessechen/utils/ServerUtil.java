package net.jessechen.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;

import net.jessechen.fblisteners.AddToTimelineListener;
import net.jessechen.models.AlarmModel;
import net.jessechen.models.CommentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class ServerUtil {

	public static final String FB_GRAPH_URL = "https://graph.facebook.com";
	public static final String BASE_URL = "http://www.socialalarmclock.jessechen.net";
	public static final String POST_ALARM_URL = BASE_URL
			+ "/opengraph/alarm.php";
	public static final String NAMESPACE = "friendlyalarmclock";
	public static final String SET = "set";

	public static void addToTimeline(Context c,
			AsyncFacebookRunner mAsyncFacebookRunner, AlarmModel am) {
		ProgressDialog dialog = ProgressDialog.show(c, "",
				"adding to timeline..", true, true);

		String alarmURL = ServerUtil.POST_ALARM_URL;
		Bundle alarmParams = new Bundle();
		alarmParams.putString("title", am.getLabel());
		alarmParams.putString("time", am.getTimeText());
		alarmURL = alarmURL + "?" + Util.encodeUrl(alarmParams);
		alarmParams.putString("alarm", alarmURL);

		mAsyncFacebookRunner.request("me/" + ServerUtil.NAMESPACE + ":"
				+ ServerUtil.SET, alarmParams, "POST",
				new AddToTimelineListener(am, c, dialog), null);
	}

	public static LinkedList<CommentModel> readFromPost(
			AsyncFacebookRunner mAsyncFacebookRunner, String pid) {
		final LinkedList<CommentModel> comments = new LinkedList<CommentModel>();
		mAsyncFacebookRunner.request(pid + "/comments", new RequestListener() {

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
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
