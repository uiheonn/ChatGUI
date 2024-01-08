package lect.chat.protocol;

public class ChatCommandUtil {
	public static final char WHISPER = 'a';
	public static final char NORMAL = 'b';
	public static final char INIT_ALIAS = 'c';
	public static final char USER_LIST = 'd';
	public static final char ENTER_ROOM = 'e';
	public static final char EXIT_ROOM = 'f';
	public static final char UNKNOWN = 'z';
	public static final char INITIALIZE = 'i';
	public static final char CHANGE_STATUS = 'r';
	public static final char MSG = 'g';
	private ChatCommandUtil(){}
	public static char getCommandType(String msg) {
		if(msg.matches("\\[{1}[a-z]\\]{1}.*")) {
			return msg.charAt(1);
		} else {
			return ChatCommandUtil.UNKNOWN;
		}
	}
}
