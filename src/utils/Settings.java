package utils;

import java.util.ArrayList;

public class Settings {
	private static String _userName = "USERNAME";
	private static String _passWord = "PASSWORD";
	private static int _logonTypeCode = 0;
	private static int _browserCode = 0;
	
	
	
	public static String getUserName() {
		return _userName;
	}
	public static String getPassWord() {
		return _passWord;
	}
	public static String getLogonType() {
		switch(_logonTypeCode)
		{
			case 0:
				return "BASIC_AUTH";
			case 1:
				return "OAUTH";
		}
		return null;
		
	}
	public static ArrayList<String> getEnviromentNames() {
		ArrayList<String> enviromentNames = new ArrayList<String>();
		enviromentNames.add("dev12345");
		return enviromentNames;
		//Read a file called environments that has the names split by return carriages
	}
	public static String getBrowser() {
		switch(_browserCode)
		{
			case 0:
				return "Chrome";
			case 1:
				return "FireFox";
		}
		return null;
	}
	
}
