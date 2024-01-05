package lect.chat.client.event;
import java.io.*;
public interface ChatSocketListener {
	public void socketClosed();
	public void socketConnected(java.net.Socket s) throws IOException ;
}
