package lect.chat.client;

import java.awt.Component;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


import lect.chat.client.UserList.UserListTransferHandler;

public class MyList extends JList {
	public MyList(){
		// 사용자 목록 표시를 위함
		super(new DefaultListModel());
		this.setCellRenderer(new CellRenderer()); // 목록 스타일 설정
		DefaultListModel model = (DefaultListModel) getModel();
		model.addElement(null);
	}
	
	class CellRenderer extends JLabel implements ListCellRenderer {
		// 사용자 목록 스타일 설정 선택된거랑 안된거 배경색 설정
		public CellRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(
				JList list, Object value, int index, 
				boolean isSelected, boolean cellHasFocus) {
			setText(value == null? "": value.toString());
			if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }      
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
		}
	}
}
