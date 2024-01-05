package lect.chat.client;
import javax.swing.*;
@SuppressWarnings("serial")
public class StatusBtn extends JButton{
	static final String CMD_ONLINE = "on";
	static final String CMD_OFFLINE = "off";

	public StatusBtn() {
		this(CMD_ONLINE);
	}
	public StatusBtn(String labelCmd) {
		this(labelCmd, labelCmd);
	}
	public StatusBtn(String label, String cmd) {
		super(label);
		setActionCommand(cmd);
	}
	public void changeButton(String cmd) {
		setActionCommand(cmd);
		setText(getActionCommand());
	}
}
