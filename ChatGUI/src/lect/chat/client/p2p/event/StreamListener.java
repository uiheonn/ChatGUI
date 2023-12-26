package lect.chat.client.p2p.event;

public interface StreamListener {
	public void streamClosed(Object streamSource);
	public void dataAvailable(byte [] data, int len);
}
