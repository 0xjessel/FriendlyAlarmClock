package net.jessechen.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import net.jessechen.models.MessageModel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerUtil {

	public static final String BASE_URL = "http://www.socialalarmclock.jessechen.net";
	public static final String POST_ALARM_URL = BASE_URL + "/opengraph/alarm.php";
	public static final String NAMESPACE = "socialalarmclock";
	public static final String SET = "set";

	public static boolean post(int from, int to, String msg) {
		String result = "";
		// the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("from", Integer
				.toString(from)));
		nameValuePairs.add(new BasicNameValuePair("to", Integer.toString(to)));
		nameValuePairs.add(new BasicNameValuePair("msg", msg));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(POST_ALARM_URL);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("SAC", "Error posting message: " + e.toString());
		}
		if (result.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	public static LinkedList<MessageModel> read(int uid) {
		String result = "";
		// the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("uid", Integer.toString(uid)));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(POST_ALARM_URL);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("SAC", "Error posting message: " + e.toString());
		}

		// parse json data
		LinkedList<MessageModel> messages = new LinkedList<MessageModel>();
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				messages.add(new MessageModel(
						json_data.getInt("id"), 
						json_data.getString("cur_timestamp"), 
						json_data.getInt("fromMsg"),
						json_data.getInt("toMsg"), 
						json_data.getString("msg")));
			}
		} catch (JSONException e) {
			Log.e("SAC", "Error parsing data: " + e.toString());
		}
		return messages;
	}
}
