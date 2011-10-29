package net.jessechen.models;

public class CommentModel {
	private String commentID;
	private String from;
	private String msg;

	public CommentModel() {
	}

	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}

	public String getCommentID() {
		return commentID;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom() {
		return from;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
