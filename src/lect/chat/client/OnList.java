package lect.chat.client;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class OnList extends MyList {
	class OnListModel extends DefaultListModel<Integer> {
        public void fireContentsChanged(JList list, int index0, int index1) {
            super.fireContentsChanged(list, index0, index1);
        }
    }
	
    public OnList() {
        super();
    }

    public void addUserStatus(ArrayList<ChatUser> users) {
        // 데이터 받아서 목록에 하나씩 추가
        DefaultListModel newModel = new DefaultListModel();
        for (ChatUser user : users) {
            if (user.status == 1) {
            	newModel.addElement("     OFF");
            } else {
            	newModel.addElement("     ON");
            }
        }
        setModel(newModel);
    }
 
}
