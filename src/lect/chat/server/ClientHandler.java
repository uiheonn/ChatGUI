
package lect.chat.server;
import java.net.*;
import java.io.*;

import lect.chat.protocol.ChatCommandUtil;
public class ClientHandler implements Runnable, MessageHandler {
	private Socket socket;
	BufferedReader br = null;
	PrintWriter pw = null;
	private String chatName, id, host;
	public ClientHandler(Socket s) throws IOException {
		// 소켓받아서 초기화
		socket = s;
		host = socket.getInetAddress().getHostAddress();
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
		// 그룹매니저에 사용자 추가
		GroupManager.addMessageHandler(this);
	}
	
	@Override
	public String getId() {
		return id;//socket.getRemoteSocketAddress().toString();
	}
	
	@Override
	public String getName() {
		return chatName;
	}
	
	@Override
	public String getFrom() {
		return host;
	}
	
	// 그룹매니저에서 아래 메소드들 호출해 사용함
	@Override
	public void sendMessage(String msg) {
		pw.println(msg);
	}
	
	@Override
	public String getMessage() throws IOException {
		return br.readLine();
	}
	
	@Override
	public void close() {
		try { socket.close(); } catch(IOException e) { e.printStackTrace();}
	}
	
	@Override
	public void run() {
		String msg;
		try {
			// 메세지 받으면 처리 맨 아래 메소드
			while(true) {
				msg = getMessage();
				if(msg == null) {
					break;
				}
				processMessageByCommandType(msg); // 명령어 타입에 따라 메시지를 처리
				System.out.println("lineRead: " + msg);
				//GroupManager.broadcastMessage(msg);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			// 클라이언트 종료하면 그룹에서 제거하고 소켓닫기
			GroupManager.removeMessageHandler(this);
			close();
		}
		System.out.println("Terminating ClientHandler");
	}
	
	public void processMessageByCommandType(String msg) {
		char command = ChatCommandUtil.getCommandType(msg);
		msg = msg.replaceFirst("\\[{1}[a-z]\\]{1}", "");
		// 메세지 형식에 따라 출력
		switch(command) {
		case ChatCommandUtil.NORMAL:
			GroupManager.broadcastMessageAllClient(String.format("%s: %s", chatName, msg));
			break;
		case ChatCommandUtil.INIT_ALIAS:
		// 모든 클라이언트에게 처음 연결된 사용자 입장 알림
			String nameWithId [] = msg.split("\\|");
			chatName = nameWithId[0];
			id = nameWithId[1];
			GroupManager.broadcastNewChatterAllClient(this);
			break;
		case ChatCommandUtil.WHISPER:
		// 귓속말이면 해당 클라이언트와 전송한 클라이언트에게만 메세지 전송
			String toId = msg.substring(0, msg.indexOf('|'));
			String msgToWhisper = msg.substring(msg.indexOf('|') + 1);
			GroupManager.sendWhisper(this, toId, String.format("%s: %s", chatName, msgToWhisper));
			break;
		case ChatCommandUtil.CHANGE_STATUS:
		    GroupManager.broadcastMessageAllClientStatus(chatName);
		case ChatCommandUtil.MSG:
			GroupManager.broadcastMessageAllClientStatus(msg);
            break;
		default:
			System.out.printf("ChatCommand %c \n", command);
			break;	
		}
	}
}
