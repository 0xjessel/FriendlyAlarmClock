package net.jessechen.models;

public class MessageModel {
	private int id;
	private String timestamp;
	private int fromMsg;
	private int toMsg;
	private String msg;
	
	public MessageModel(int id, String timestamp, int fromMsg, int toMsg, String msg) {
		this.setId(id);
		this.setTimestamp(timestamp);
		this.setFromMsg(fromMsg);
		this.setToMsg(toMsg);
		this.setMsg(msg);
	}

	public void setFromMsg(int fromMsg) {
		this.fromMsg = fromMsg;
	}

	public int getFromMsg() {
		return fromMsg;
	}

	public void setToMsg(int toMsg) {
		this.toMsg = toMsg;
	}

	public int getToMsg() {
		return toMsg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
