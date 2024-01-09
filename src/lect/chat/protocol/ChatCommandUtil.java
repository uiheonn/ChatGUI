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
//<<<<<<< HEAD
	public static final char DUPLICATE_USER = 'w'; // 동일한 사용자를 찾는 프로토콜
//=======
	public static final char MSG = 'g';
//>>>>>>> 02da9a7702f62a792aee7551b17bb1af952c66ef
	private ChatCommandUtil(){}
	public static char getCommandType(String msg) {
		if(msg.matches("\\[{1}[a-z]\\]{1}.*")) {
			return msg.charAt(1);
		} else {
			return ChatCommandUtil.UNKNOWN;
		}
	}
}
