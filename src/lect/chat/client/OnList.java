package lect.chat.client;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class OnList extends MyList{
	class OnListCellRenderer extends CellRenderer {
	    @Override
	    public Component getListCellRendererComponent(
	            JList list, Object value, int index,
	            boolean isSelected, boolean cellHasFocus) {
	        // 여기에서 1이면 "O" 초록색, 0이면 짙은 회색으로 표시
	        if (value instanceof Integer) {
	            setText(((Integer) value == 1 ? "O" : ""));
	            setForeground(((Integer) value == 1) ? Color.GREEN : Color.DARK_GRAY);
	        } else {
	            setText("");  // 또는 다른 기본 값으로 설정
	            setForeground(Color.BLACK);  // 또는 다른 기본 색상으로 설정
	        }
	        
	        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    }
	}

    public OnList() {
        super();
        // 새로운 CellRenderer 설정
        setCellRenderer(new OnListCellRenderer());
    }

	public void addUserStatus(ArrayList <ChatUser> users) {
		// ������ �޾Ƽ� ��Ͽ� �ϳ��� �߰�
		DefaultListModel newModel = new DefaultListModel();
		for(ChatUser user: users) {
			newModel.addElement(getStatusSymbol(user.getStatus()));
		}
		setModel(newModel);
	}

	private String getStatusSymbol(int status) {
        // 상태에 따라 표시할 문자열 반환
        return (status == 1) ? "O" : "X";
    }

	
	public void addNewChatUsers(ArrayList <ChatUser> users) {
		// ������ �޾Ƽ� ��Ͽ� �ϳ��� �߰�
		DefaultListModel newModel = new DefaultListModel();
		for(ChatUser user: users) {
			newModel.addElement(user);
		}
		setModel(newModel);
	}
	
	public ChatUser getUserByChatName(String chatName) {
		for (int i = 0; i < getModel().getSize(); i++) {
	        ChatUser user = (ChatUser) getModel().getElementAt(i);
	        if (user.getName().equals(chatName)) {
	            return user;
	        }
	    }
	    return null;
	}
	
}
