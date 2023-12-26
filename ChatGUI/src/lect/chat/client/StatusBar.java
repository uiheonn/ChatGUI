package lect.chat.client;
import lect.chat.client.event.*;
import lect.chat.client.p2p.event.FileProgressListener;

import java.awt.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class StatusBar extends JPanel implements ChatStatusListener, FileProgressListener {
	private JLabel statusText;
	private JProgressBar progBar;
	private static StatusBar statusBar;
	private GridBagConstraints c = new GridBagConstraints();
	private StatusBar() {
		super(new GridBagLayout());
		statusText = new JLabel();
		progBar = new JProgressBar();
		progBar.setStringPainted(true);
		statusText.setHorizontalAlignment(SwingConstants.LEFT);
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(statusText, c);
		statusText.setText("Initialized ...");
		//this.add(progBar, c);
	}
	public static StatusBar getStatusBar() {
		if(statusBar == null) statusBar = new StatusBar();
		return statusBar;
	}
	public void chatStatusChanged(Object obj) {
		statusText.setText(obj.toString());
	}
	public void fileProgressed(String msg, int progress) {
		progBar.setValue(progress);
		progBar.setString(msg);
	}
	@Override
	public void progressWillStart() {
		add(progBar, c);
		validate();
	}
	@Override
	public void progressFinished() {
		remove(progBar);
		validate();
		repaint();
	}
}
