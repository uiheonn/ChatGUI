package lect.chat.client.p2p;
import java.io.*;
import java.net.*;
import java.util.List;

import javax.swing.SwingWorker;

import lect.chat.client.p2p.event.FileProgressListener;
public class FileSender extends SwingWorker<Void, FileProgress> {
	Socket socket;
	List<File> filesToSend;
	public static final int START = 0xffffffff;
	public static final int DELIMETER = 0xffffbbaa;
	public static final int END = 0xafafafaf;
	private FileProgressListener listener;
	public FileSender(String host, List<File> files) throws IOException {
		socket = new Socket(host, P2P.PORT);
		filesToSend = files;
	}
	public void setListener(FileProgressListener l) {
		listener = l;
	}
	@Override
	public Void doInBackground() throws Exception {
		try {
			send();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try  { socket.close(); } catch(Exception ex) {}
		}
		return null;
	}
	@Override 
	public void process(List<FileProgress> progs) {
		FileProgress prog = progs.get(progs.size() -1);
		listener.fileProgressed(prog.msg, prog.percent);
		// ChatUserList에서 상태바를 매개변수로 listener 전달
	}
	public void send() throws IOException {
		BufferedInputStream in = null;
		DataOutputStream out = null;
		long bytesSent  = 0;
		int bytesRead;
		long fileSize;
		String fileName;
		int fileCount = filesToSend.size();
		int nCurFile = 0;
		int percent;
		byte [] buffer = new byte[512];
		try {
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			listener.progressWillStart();
			for(File f: filesToSend) {
				// 전달된 파일 버퍼에 써서 내보내기
				nCurFile++;
				System.out.println("starting sending a file " + f.getName());
				in = new BufferedInputStream(new FileInputStream(f));
				fileName = f.getName();
				fileSize = f.length();
				out.writeInt(START);
				out.writeUTF(f.getName());
				out.writeInt(DELIMETER);
				out.writeLong(f.length());
				while((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
					bytesSent += bytesRead;
					percent = (int)((bytesSent * 100.)/fileSize);
					publish(new FileProgress(String.format("uploading %s %d %% (%d/%d)", fileName, percent, nCurFile, fileCount), percent));
				}
				out.writeInt(END);
				out.flush();
				in.close();
				System.out.println("finished sending all file(s)");
			}
		} catch(IOException e) {
			try { if(in != null) in.close(); } catch(IOException ex) {}
		} finally {
			try { socket.close(); } catch(IOException e) {}
			listener.progressFinished();
		}
	}
}
class FileProgress {
	String msg;
	int percent;
	public FileProgress(String m, int p) {
		msg = m;
		percent = p;
	}
}
