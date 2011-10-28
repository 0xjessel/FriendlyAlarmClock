package net.jessechen.messages;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

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

public class MessagesUtil {

	private static final String BASE_URL = "http://www.socialalarmclock.jessechen.net";
	private static final String POST_MSG_URL = BASE_URL + "/postMessage.php";
	private static final String READ_MSG_URL = BASE_URL + "/readMessages.php";

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
			HttpPost httppost = new HttpPost(POST_MSG_URL);
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

	public static LinkedList<Message> read(int uid) {
		String result = "";
		// the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("uid", Integer.toString(uid)));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(READ_MSG_URL);
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
		LinkedList<Message> messages = new LinkedList<Message>();
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				messages.add(new Message(
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
