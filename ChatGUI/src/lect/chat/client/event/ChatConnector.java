package lect.chat.client.event;
import java.net.*;
public interface ChatConnector {
	public boolean connect();
	public void disConnect();
	public Socket getSocket();
	public boolean socketAvailable();
	public void invalidateSocket();
	public String getName();
	public String getId();
}
