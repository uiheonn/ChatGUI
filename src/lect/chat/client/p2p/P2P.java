package lect.chat.client.p2p;
import java.io.*;
import java.net.*;
public class P2P implements Runnable {
	ServerSocket ss;
	public static int PORT = 1224;
	private static P2P instance;
	
	private P2P () {}
	public static P2P getInstance() {
		if(instance == null) instance = new P2P();
		return instance;
		// 싱글톤 패턴
	}
	public void startService() throws IOException {
		if(ss != null && ss.isBound()) return;
		ss = new ServerSocket(PORT);
		new Thread(this).start();
		// 소켓없으면 생성, 스레드 생성해 호출
	}
	public void stopService() {
		try {
			ss.close();
			ss = null;
		} catch(IOException e) {
			// 소켓 닫기
		}
	}
	public void run () {
		Socket s;
		try {
			while(true) {
				s = ss.accept();
				new Thread(new FileReceiver(s)).start();
			}
			// 소켓 연결 받아드려 스레드 생성해 파일 보내기 시작
		} catch(IOException e) {
			
		} finally {
			try { if(ss != null) ss.close(); } catch(Exception e) {}
		}
	}
}
