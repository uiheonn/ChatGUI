package lect.chat.server;
import java.io.IOException;
import java.net.*;
public class ChatServer implements Runnable {
	ServerSocket ss;
	public ChatServer() throws IOException {
		// 소켓 초기화하고 포트 설정
		ss = new ServerSocket(1223);
		System.out.printf("ChatServer[%s] is listening on port 1223\n", InetAddress.getLocalHost().getHostAddress());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			// 종료할때 정리를 위해 셧다운 후크 등록 -> 정상 비정상 종료시 호출되서 실행됨
			public void run() {
				try {
					cleanup();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void run() {
		Socket s = null;
		try {			
			while(true) {
				// 클라이언트 접속 대기 
				s = ss.accept();
				System.out.format("Client[%s] accepted\n", s.getInetAddress().getHostName());
				// 접속하면 클라이언트 핸들러 생성, 스레드 생성 및 실행
				new Thread(new ClientHandler(s)).start();
			}
		} catch(IOException e) {
			System.out.println("Terminating ChatServer: " + e.getMessage());
		}
		System.out.println("ChatServer shut down");
	}
	public void cleanup() throws IOException {
		// 서버 정리
		ss.close();
		GroupManager.closeAllMessageHandlers();
	}
	public static void main(String [] args) {
		// 스레드로 서버 생성해 실행
		try {
			Runnable r = new ChatServer();
			new Thread(r).start();
		} catch(IOException e) {
			System.out.println("Failed to start server: " + e.getMessage());
		}
	}
}
