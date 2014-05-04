package chacter;

import httpClient.DigestUtil;

import java.io.UnsupportedEncodingException;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "哈哈";
		byte[] chars = s.getBytes();
		String s1 = new String(chars,"utf-8");
		String s2 = new String(chars,"GBK");
		
		System.out.println(DigestUtil.MD5(s1));
		System.out.println(DigestUtil.MD5(s2));
		
	}
}
