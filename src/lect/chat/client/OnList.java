package lect.chat.client;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class OnList extends MyList {
    class OnListCellRenderer extends CellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Integer) {
                // 정수인 경우
                int status = (Integer) value;
                String symbol = getStatusSymbol(status);

                setText(symbol);
                setForeground(getSymbolColor(status));
            } else {
                // 그 외의 경우
                setText("");
                setForeground(Color.BLACK);
            }

            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }

        private String getStatusSymbol(int status) {
            // 상태에 따라 표시할 문자열 반환
            return (status == 1) ? "●" : "●";
        }

        private Color getSymbolColor(int status) {
            // 상태에 따라 글자 색 반환
            return (status == 1) ? Color.GREEN : Color.RED;
        }
    }

    public OnList() {
        super();
        // 새로운 CellRenderer 설정
        setCellRenderer(new OnListCellRenderer());
    }

    public void addUserStatus(ArrayList<ChatUser> users) {
        // 데이터 받아서 목록에 하나씩 추가
        DefaultListModel newModel = new DefaultListModel();
        for (ChatUser user : users) {
            newModel.addElement(user.getStatus());
        }
        setModel(newModel);
    }
}
