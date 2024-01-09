package lect.chat.client;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
@SuppressWarnings("serial")
public class SaveBtn extends JButton{
	static final String SAVE_FILE = "   💾   ";
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
	    }catch (NumberFormatException e) {
	    	JOptionPane.showMessageDialog(null, "Error");
	    	}
	}
	
	public void savetext(int startLine, int endLine) {
	         // �쁽�옱 梨꾪똿 �궡�슜�쓣 �씪�씤蹂꾨줈 �뙆�씪濡� ���옣
		String chatContent = chatDispArea.getText();
	
	     // �뙆�씪 �떎�씠�뼹濡쒓렇瑜� �넻�빐 ���옣�븷 寃쎈줈瑜� �꽑�깮�븯�룄濡� �븷 �닔�룄 �엳�뒿�땲�떎.
	     JFileChooser fileChooser = new JFileChooser();
	     int userChoice = fileChooser.showSaveDialog(null);
	
	     if (userChoice == JFileChooser.APPROVE_OPTION) {
	         try {
	             // �꽑�깮�븳 �뙆�씪�뿉 吏��젙�븳 踰붿쐞�쓽 �씪�씤�쓣 ���옣
	             String filePath = fileChooser.getSelectedFile().getAbsolutePath();
	             if (!filePath.toLowerCase().endsWith(".txt")) {
	                 filePath += ".txt"; // �뙆�씪 �솗�옣�옄媛� .txt濡� �걹�굹吏� �븡�쑝硫� 異붽�
	             }
	             try (FileWriter writer = new FileWriter(filePath)) {
	                 String[] lines = chatContent.split("\\n");
	                 for (int i = startLine - 1; i < endLine && i < lines.length; i++) {
	                     writer.write(lines[i] + System.lineSeparator());
	                 }
	             }
	             JOptionPane.showMessageDialog(null, "���옣 �셿猷�");
	         } catch (IOException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(null, "�떎�뙣");
	        }
	    }
	}
}
