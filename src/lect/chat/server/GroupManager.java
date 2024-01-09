package lect.chat.server;
import java.util.*;

import lect.chat.protocol.ChatCommandUtil;
public class GroupManager {
	private static Vector<MessageHandler> clientGroup = new Vector<MessageHandler>();
	private GroupManager() {} // 객체 생성 방지로 private 선언
	public static void addMessageHandler(MessageHandler handler) {
		// 새로운 사용자 그룹에 추가. ClientHandler에서 호출됨
		//broadcastMessage(handler.getId() + " has just entered chat room");
		clientGroup.add(handler);
		System.out.println("Active clients count: " + clientGroup.size());
	}
	public static void removeMessageHandler(MessageHandler handler) {
		// 사용자 제거
		clientGroup.remove(handler);
		System.out.println("Active clients count: " + clientGroup.size());
		// 퇴장 알림
		if(handler.getName() != null) { // 닉네임이 동일한 사용자가 접속한 경우 다른 사용자에게 메시지를 전달하지 않음
			for(MessageHandler mh: clientGroup) {
				mh.sendMessage(createMessage(ChatCommandUtil.EXIT_ROOM, handler.getName() + " has just left chat room"));
			}
		}
	}
	public static void broadcastMessageAllClient(String msg) {
		// 모든 사용자에게 메세지 전송
		for(MessageHandler handler: clientGroup) {
			handler.sendMessage(createMessage(ChatCommandUtil.NORMAL, msg));
		}
	}
	public static void closeAllMessageHandlers() {
		// 서버 정리 시 모든 사용자의 메세지 핸들러 종료
		for(MessageHandler handler: clientGroup) {
			handler.close();
		}
		clientGroup.clear();
	}
	public static void broadcastNewChatterAllClient(MessageHandler newHandler) {
		// 새로운 사용자 입장시 모든 사용자에게 알림
		StringBuilder users = new StringBuilder();
		MessageHandler handler;
		int size = clientGroup.size();
		for(int i = 0; i < size; i++) {
			handler = clientGroup.get(i);
			users.append(handler.getName()).append(",")
			.append(handler.getId()).append(",")
			.append(handler.getFrom());
			if(i < (size-1)) {
				users.append("|");
			}
		}
		for(MessageHandler mh: clientGroup) {
			if(mh != newHandler) {
				mh.sendMessage(createMessage(ChatCommandUtil.ENTER_ROOM, (newHandler.getName() + " has just entered chat room")));
			}
			mh.sendMessage(createMessage(ChatCommandUtil.USER_LIST, users.toString()));
		}
	}
	public static void sendWhisper(MessageHandler from, String to, String msg) {
		// 귓속말 전송, 보낸사람 받는 사람 둘에게만
		msg = createMessage(ChatCommandUtil.WHISPER, msg);
		for(MessageHandler handler: clientGroup) {
			if(handler.getId().equals(to)) { // 선택된 사용자 찾아서 해당 사용자에게만 메세지 전송
				handler.sendMessage(msg);
				break;
			}
		}
		// 보낸 사용자에게도 전송
		from.sendMessage(msg);
	}
	static String createMessage(char command, String msg) {
		// 메세지 형식에 따라 메세지 생성
		StringBuilder msgBuilder = new StringBuilder();
		msgBuilder.delete(0, msgBuilder.length());
		msgBuilder.append("[");
		msgBuilder.append(command);
		msgBuilder.append("]");
		msgBuilder.append(msg);
		return msgBuilder.toString();
		
	}
	public static void broadcastMessageAllClientStatus(String msg) {
		// 모든 사용자에게 상태 메세지 전송
		for(MessageHandler handler: clientGroup) {
			handler.sendMessage(createMessage(ChatCommandUtil.CHANGE_STATUS, msg));
		}
	}
	public static Vector<MessageHandler> getClientGroup() { // ClientHandler.java에서 사용자 목록을 확인하기 위해 사용
		return clientGroup;
	}
}
