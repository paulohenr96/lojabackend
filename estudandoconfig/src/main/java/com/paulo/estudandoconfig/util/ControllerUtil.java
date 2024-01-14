package com.paulo.estudandoconfig.util;

public class ControllerUtil {

	
	
	public String responseJson(String s) {
		return "{'response':'%s'".format(s);
	}
}
