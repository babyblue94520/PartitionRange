package com.tt;

import java.util.regex.Pattern;

/**
 * 還懶得解釋
 * @author Clare
 * @date 2017年6月2日 
 */
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile("^[^:/]+://[^/]+/dfasdf(/js|/css|/images|/EmperorGive)/");
		String url = "http://127.0.0.1:8090/dfasdf/EmperorGive";
		System.out.println(p.matcher(url).find());
	}

}
