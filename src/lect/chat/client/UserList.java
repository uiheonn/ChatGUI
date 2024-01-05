package lect.chat.client;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import java.util.*;
import java.util.List;

import lect.chat.client.p2p.FileSender;

@SuppressWarnings("serial")
public class UserList extends MyList {
	public UserList() {
		// 사용자 목록 표시를 위함
		super();
		
		this.setDropMode(DropMode.ON);
		this.setTransferHandler(new UserListTransferHandler()); // 드래그앤 드롭 지원을 위해 설정
	}
	
	public void addNewChatUsers(ArrayList <ChatUser> users) {
		// 데이터 받아서 목록에 하나씩 추가
		DefaultListModel newModel = new DefaultListModel();
		for(ChatUser user: users) {
			newModel.addElement(user);
		}
		setModel(newModel);
	}

	public void transferFileDropped(List<File> files) {
		// 파일 드롭되면 호출
		ChatUser userToSendFile = (ChatUser)this.getSelectedValue();
		try {
			// 파일 전송 클래스 생성해 실행
			FileSender sender = new FileSender(userToSendFile.getHost(), files);
			sender.setListener(StatusBar.getStatusBar());
			sender.execute();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public class UserListTransferHandler extends TransferHandler {
		public boolean canImport(TransferHandler.TransferSupport info) {
			if (!info.isDrop())
				return false;
			JList list = (JList) info.getComponent();
			JList.DropLocation dLoc = (JList.DropLocation) info.getDropLocation();
			int idxOverJList = dLoc.getIndex();
			if (idxOverJList == -1)
				return false;
			list.setSelectedIndex(dLoc.getIndex());
			// DataFlavor [] flavors = info.getDataFlavors();
			return (info.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
		}

		public int getSourceActions(JComponent c) {
			return TransferHandler.NONE;
		}

		@SuppressWarnings("unchecked")
		public boolean importData(TransferHandler.TransferSupport info) {
			// 드롭된 데이터 처리
			if (!info.isDrop()) {
				return false;
			}
			Transferable t = info.getTransferable();
			List<File> data;
			try {
				data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
				UserList.this.transferFileDropped(data); // 파일 전송 수행
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
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
