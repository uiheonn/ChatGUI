package lect.chat.client;
import java.io.*;
import java.text.*;
import javax.swing.text.*;
import javax.swing.*;
public class ChatTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	private int linesToHold = 50;
	private int maxLines = 100;
	private boolean recordRemovedMsg;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	public ChatTextArea () {
		setEditable(false);
		// 사용자 편집할 수 없게 설정
	}
	public void setLinesToHold(int num) {
		linesToHold = num;
		// 50줄로 제한
	}
	public void setMaxLines(int num) {
		maxLines = num;
		// 100행으로 제한
	}
	public void setRecordRemovedMsg(boolean rrm) {
		recordRemovedMsg = rrm;
		// 제거된 메세지 기록 여부 설정, 근데 호출되지 않았음..?
	}
	public void append(String str) {
		super.append(str);
		int count = getLineCount();
		if(count >= maxLines) {
			// 길이가 길면 이전 메세지 제거하고, 제거된 메세지 기록한다고 설정되어있으면 파일에 저장
			try {
				int endOffset = getLineEndOffset(count - 1 - linesToHold);
				if(recordRemovedMsg) saveBeforeRemove(endOffset);
			this.replaceRange("", 0, endOffset);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		setCaretPosition(getDocument().getLength());
	}
	private void saveBeforeRemove(int len) throws BadLocationException {
		// 현재시간으로 파일 이름 생성해 저장
		String contentToSave = getText(0, len);
		FileWriter fw = null;
		try {
			File f = new File("saved_messages");
			if(!f.exists()) {
				f.mkdir();
			}
			f = new File(f, formatter.format(new java.util.Date()) + ".txt");
			fw = new FileWriter(f);
			fw.write(contentToSave);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(fw != null) try { fw.close(); } catch(IOException e) {}
		}
	}
}