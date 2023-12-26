package lect.chat.server;
import java.io.*;
public interface MessageHandler {
	public String getId();
	public String getName();
	public String getFrom();
	public void sendMessage(String msg);
	public String getMessage() throws IOException;
	public void close();
}
