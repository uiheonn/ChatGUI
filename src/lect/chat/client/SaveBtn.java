package lect.chat.client;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
@SuppressWarnings("serial")
public class SaveBtn extends JButton{
	static final String SAVE_FILE = "   📂   ";
	private ChatTextPane chatDispArea;

	public SaveBtn(ChatTextPane chatDispArea) {
		this(SAVE_FILE);
	    this.chatDispArea = chatDispArea;
	
	}
	public SaveBtn(String labelCmd) {
		this(labelCmd, labelCmd);
	}
	public SaveBtn(String label, String cmd) {
		super(label);
		setActionCommand(cmd);
	}
	public void changeButton(String cmd) {
		setActionCommand(cmd);
		setText(getActionCommand());
	}


	public void saveline() {
	    String startLineStr = JOptionPane.showInputDialog(null, "Start line:");
	    String endLineStr = JOptionPane.showInputDialog(null, "End line:");

	    try {
	        int startLine = startLineStr.isEmpty() ? 1 : Integer.parseInt(startLineStr);
	        int endLine = endLineStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(endLineStr);
	        savetext(startLine, endLine);
	    } catch (NumberFormatException e) {
	    	JOptionPane.showMessageDialog(null, "Error");
	    }
	}

	public void savetext(int startLine, int endLine) {
	    // 현재 채팅 내용을 라인별로 파일로 저장
		String chatContent = chatDispArea.getText();
	    // 파일 다이얼로그를 통해 저장할 경로를 선택하도록 할 수도 있습니다.
	    JFileChooser fileChooser = new JFileChooser();
	    int userChoice = fileChooser.showSaveDialog(null);
		
	    if (userChoice == JFileChooser.APPROVE_OPTION) {
	        try {
	            // 선택한 파일에 지정한 범위의 라인을 저장
	            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
	            if (!filePath.toLowerCase().endsWith(".txt")) {
	            	filePath += ".txt"; // 파일 확장자가 .txt로 끝나지 않으면 추가
	        	}
	            try (FileWriter writer = new FileWriter(filePath)) {
	                String[] lines = chatContent.split("\\n");
	                for (int i = startLine - 1; i < endLine && i < lines.length; i++) {
	                	writer.write(lines[i] + System.lineSeparator());
	                }
	            }
	        	JOptionPane.showMessageDialog(null, "저장 완료");
	        } catch (IOException ex) {
	        	ex.printStackTrace();
	        	JOptionPane.showMessageDialog(null, "실패");
	       }
		}
	}
}	