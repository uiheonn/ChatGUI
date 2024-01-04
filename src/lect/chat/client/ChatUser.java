package lect.chat.client;

public class ChatUser {
	String name;
	String id;
	String host;
	int status;
	public ChatUser(String name, String id, String host) {
		this.name = name;
		this.id = id;
		this.host = host;
		this.status = 0;
	}
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public String toString () {
		return name;
	}
	public String getHost() {
		return host;
	}
	public void setStatus(int s) {
		status = s;
	}
	public int getStatus() {
		return status;
	}
}
