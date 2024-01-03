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
	JButton save;
	JButton init;
	PrintWriter writer;
	ChatConnector connector;
	StringBuilder msgBuilder = new StringBuilder();
	
	public ChatPanel(ChatConnector c) {
		super(new GridBagLayout());
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
		chatDispArea = new ChatTextPane();//new ChatTextArea();
		userList = new UserList();
		
		connectDisconnect = new ConnectButton();
		whisper = new JButton("✉");
		init = new JButton("❌");
		save = new JButton("📂");
		
		chatTextField.setEnabled(false);
		chatDispArea.setEditable(false);
		whisper.setEnabled(false);
		
		GridBagConstraints c = new GridBagConstraints();
		JLabel titleLabel = new JLabel("Message Received", JLabel.CENTER);
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(2,2,2,2);
		add(titleLabel, c);
		
		c = new GridBagConstraints();
		titleLabel = new JLabel("List of Users", JLabel.CENTER);
		c.gridy = 0;
		c.gridx = 2;
		c.gridwidth = 2;
		c.insets = new Insets(2,2,2,2);
		add(titleLabel, c);
		
		c = new GridBagConstraints();
		c.gridy = 1;
		c.gridx = 0;
		c.weighty = 1.0f;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.9;
		c.insets = new Insets(1, 2, 0, 2);
		JScrollPane scrollPane = new JScrollPane(chatDispArea);
		add(scrollPane, c);
		
		c = new GridBagConstraints();
		c.gridy = 1;
		c.gridx = 1;
		c.gridwidth = 4;
		c.weightx = 0.1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(1, 2, 0, 2);
		scrollPane = new JScrollPane(userList);
		add(scrollPane, c);
		
		c = new GridBagConstraints();
		c.gridy = 2;
		c.gridx = 0;
		c.insets = new Insets(0,0, 1, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(chatTextField, c);
		
		c = new GridBagConstraints();
		c.gridy = 2;
		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		add(connectDisconnect, c);
		
		c = new GridBagConstraints();
		c.gridy = 2;
		c.gridx = 2;
		c.anchor = GridBagConstraints.CENTER;
		add(whisper, c);
		
		c = new GridBagConstraints();
		c.gridy = 2;
		c.gridx = 3;
		c.anchor = GridBagConstraints.CENTER;
		add(init, c);
		
		c = new GridBagConstraints();
		c.gridy = 2;
		c.gridx = 4;
		c.anchor = GridBagConstraints.CENTER;
		add(save, c);
		
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
