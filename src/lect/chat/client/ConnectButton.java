package lect.chat.client;
import javax.swing.*;
@SuppressWarnings("serial")
public class ConnectButton extends JButton{
	static final String CMD_DISCONNECT = "Disconnect";
	static final String CMD_CONNECT = "Connect";
	public ConnectButton() {
		this(CMD_CONNECT);
	}
	public ConnectButton(String labelCmd) {
		this(labelCmd, labelCmd);
	}
	public ConnectButton(String label, String cmd) {
		super(label);
		setActionCommand(cmd);
	}
	public void changeButtonStatus(String cmd) {
		setActionCommand(cmd);
		setText(getActionCommand());
	}
}
