package lect.chat.client.p2p.event;

public interface FileProgressListener {
	public void progressWillStart();
	public void fileProgressed(String msg, int progress);
	public void progressFinished();
}
