package lect.chat.client;
import lect.chat.client.event.*;
import lect.chat.protocol.ChatCommandUtil;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
@SuppressWarnings("serial")
public class ChatPanel extends JPanel implements MessageReceiver, ActionListener, ChatSocketListener {
	JTextField chatTextField;
	ChatTextPane chatDispArea;
	UserList userList;
	ConnectButton connectDisconnect;
	JButton whisper;

	// ui 추가 변수
	JButton save; // 저장 버튼
	JButton init; // 초기화 버튼
	JButton statusBtn; // 상태 변경 버튼
	JLabel statusField; // 상태 표시 라벨

	// 
	
	PrintWriter writer;
	ChatConnector connector;
	StringBuilder msgBuilder = new StringBuilder();
	private JLabel titleLabel_1;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	
	public ChatPanel(ChatConnector c) {
		initUI();
		connector = c;
		chatTextField.addActionListener(this);
		connectDisconnect.addActionListener(this);
		whisper.addActionListener(this);
		// UI 생성
	}
	
	public void clearText() {
		chatDispArea.setText("");
	}
	
	private void initUI() {
		chatTextField = new JTextField();
		chatTextField.setBounds(2, 267, 295, 21);

		chatDispArea = new ChatTextPane();
		userList = new UserList();
		connectDisconnect = new ConnectButton();
		connectDisconnect.setBounds(305, 266, 90, 23);
		whisper = new JButton("   ✉   ");
		whisper.setBounds(397, 266, 90, 23);
		
		// ui 변수 선언
		statusField = new JLabel(" 버튼을 통해 현재 상태를 알려주세요");
		statusField.setBounds(2, 294, 284, 15);
		init = new JButton("   🔄   ");
		init.setBounds(306, 290, 90, 23);
		save = new JButton("   📂   ");
		save.setBounds(397, 290, 90, 23);
		statusBtn = new JButton("비움");
		statusBtn.setBounds(235, 290, 60, 23);
		//
		
		chatTextField.setEnabled(false);
		chatDispArea.setEditable(false);
		whisper.setEnabled(false);
		save.setEnabled(false);
		init.setEnabled(false);
		statusField.setEnabled(false);
		statusBtn.setEnabled(false);
		setLayout(null);
		JLabel titleLabel = new JLabel("Message Received", JLabel.CENTER);
		titleLabel.setBounds(77, 2, 142, 15);
		add(titleLabel);
		titleLabel_1 = new JLabel("List of Users", JLabel.CENTER);
		titleLabel_1.setBounds(320, 2, 84, 15);
		add(titleLabel_1);
		JScrollPane scrollPane = new JScrollPane(chatDispArea);
		scrollPane.setBounds(2, 20, 300, 245);
		add(scrollPane);
		scrollPane_1 = new JScrollPane(userList);
		scrollPane_1.setBounds(306, 20, 120, 245);
		add(scrollPane_1);
		
		add(chatTextField);
		add(connectDisconnect);
		add(whisper);
		add(statusField);
		add(init);
		add(save);
		add(statusBtn);
		
	}
	
	@Override
	public void messageArrived(String msg) {
		// 메세지 출력
		char command = ChatCommandUtil.getCommandType(msg);
		System.out.println(msg);
		msg = msg.replaceFirst("\\[{1}[a-z]\\]{1}", "");
		// 메세지의 첫글자 추출해 switch문에서 해당하는 명령 실행
		// 채팅 메세지 스타일 적용하게됨, 귓속말은 핑크, 퇴장알림은 파랑 등등 chatTextPane에 구현되어있음
		switch(command) {
			case ChatCommandUtil.NORMAL:
			case ChatCommandUtil.ENTER_ROOM:
			case ChatCommandUtil.WHISPER:
			case ChatCommandUtil.EXIT_ROOM:
				chatDispArea.append(msg + "\n", command);
				break;
			case ChatCommandUtil.USER_LIST:
				displayUserList(msg);
				break;
			default:
				break;
				}
	}

	@Override
	public void socketClosed() {
		//  소켓 닫히면 호출되어 UI이 비활성하고 연결 버튼으로 변경됨
		chatTextField.setEnabled(false);
		chatDispArea.setEnabled(false);
		whisper.setEnabled(false);
		userList.setEnabled(false);
		save.setEnabled(false);
		init.setEnabled(false);
		statusField.setEnabled(false);
		statusBtn.setEnabled(false);
		connectDisconnect.changeButtonStatus(ConnectButton.CMD_CONNECT);
	}

	@Override
	public void socketConnected(Socket s) throws IOException {
		// 소켓 연결되면 출력스트림 생성 및 UI 활성화, 사용자 정보 초기화해 서버 전송
		writer = new PrintWriter(s.getOutputStream(), true);
		writer.println(createMessage(ChatCommandUtil.INIT_ALIAS, String.format("%s|%s", connector.getName(), connector.getId()) ));
		chatTextField.setEnabled(true);
		chatDispArea.setEnabled(true);
		whisper.setEnabled(true);
		userList.setEnabled(true);
		statusField.setEnabled(true);
		save.setEnabled(true);
		init.setEnabled(true);
		statusBtn.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 메세지 입력, 연결 및 해제, 귓속말등 버튼 이벤트 클릭시 호출됨
		Object sourceObj = e.getSource();
		if(sourceObj == chatTextField) { // 엔터키 인식해 실행됨
			String msgToSend = chatTextField.getText();
			if(msgToSend.trim().equals("")) return;
			if(connector.socketAvailable()) {
				sendMessage(ChatCommandUtil.NORMAL, msgToSend);
			}
			chatTextField.setText(""); // 입력창 비우기
			// 일반 메세지 출력
		} else if(sourceObj == connectDisconnect) { // 연결 상태면 해제, 해제 상태면 연결 실행
			if(e.getActionCommand().equals(ConnectButton.CMD_CONNECT)) {
				if(connector.connect()) {
					connectDisconnect.changeButtonStatus(ConnectButton.CMD_DISCONNECT);
				}
			} else {//when clicked Disconnect button
				connector.disConnect();
				connectDisconnect.changeButtonStatus(ConnectButton.CMD_CONNECT);
			}
			// 연결 해제 버튼 클릭시 호출
		} else if (sourceObj == whisper) {//whisper button
			ChatUser userToWhisper = (ChatUser)userList.getSelectedValue();
			if(userToWhisper == null) {
				JOptionPane.showMessageDialog(this, "User to whisper to must be selected", "Whisper", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String msgToSend = chatTextField.getText();
			if(msgToSend.trim().equals("")) return;
			sendMessage(ChatCommandUtil.WHISPER, String.format("%s|%s", userToWhisper.getId(), msgToSend));
			chatTextField.setText("");
			// 귓속말 버튼 클릭시 호출
		} else  if (sourceObj == init){
			// 초기화
			String msgToSend = "reset";
			sendMessage(ChatCommandUtil.INITIALIZE, msgToSend);
			clearText();
		} else  if (sourceObj == save){
			// 파일 저장
			/*
			 * 
			 * 
			 * 구현 해야 할 곳
			 * 
			 * 
			 * 
			 */
		}
	}
	
	private void displayUserList(String users) {
		// 서버에서 사용자 목록 받아서 목록 업데이트 GroupManager에서 호출됨

		//format should be like 'name1,id1,host1|name2,id2,host2|...'
		//System.out.println(users);
		String [] strUsers = users.split("\\|");
		String [] nameWithIdHost;
		ArrayList<ChatUser> list = new ArrayList<ChatUser>();
		for(String strUser : strUsers) {
			nameWithIdHost = strUser.split(",");
			if(connector.getId().equals(nameWithIdHost[1])) continue;
			list.add(new ChatUser(nameWithIdHost[0], nameWithIdHost[1], nameWithIdHost[2], Integer.parseInt(nameWithIdHost[3])));
		}
		userList.addNewChatUsers(list);
	}
	
	private void sendMessage(char command, String msg) {
		writer.println(createMessage(command, msg));
	}
	
	private String createMessage(char command, String msg) {
		msgBuilder.delete(0, msgBuilder.length());
		msgBuilder.append("[");
		msgBuilder.append(command);
		msgBuilder.append("]");
		msgBuilder.append(msg);
		return msgBuilder.toString();
		
	}
}
