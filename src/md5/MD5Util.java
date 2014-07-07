package md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public final class MD5Util {
	
	private final static String[] hexDigits = { 
	      "0", "1", "2", "3", "4", "5", "6", "7", 
	      "8", "9", "a", "b", "c", "d", "e", "f"}; 
	
	

	/**
	 * 获得文件的MD5值
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		//16进制 将获得32位或31位字符串
		return bigInt.toString(16);
	}

	/**
	 * 获得文件夹的MD5
	 * @param file
	 * @param listChild
	 * @return
	 */
	public static Map<String, String> getDirMD5(File file, boolean listChild) {
		if (!file.isDirectory()) {
			return null;
		}
		// <filepath,md5>
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
			} else {
				md5 = getFileMD5(f);
				if (md5 != null) {
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}
	
	  /**
	   * 获得字符串的MD5
	   * @param origin
	   * @return
	   */
	  public static String getStringMD5(String origin) { 
	    String resultString = null; 
	
	    try { 
	      resultString=origin; 
	      MessageDigest md = MessageDigest.getInstance("MD5"); 
	      resultString=byteArrayToHexString(md.digest(resultString.getBytes())); 
	    } 
	    catch (Exception ex) { 
	
	    } 
	    return resultString; 
	  } 
	  
	  
	  
	  public static String byteArrayToHexString(byte[] b) { 
		    StringBuffer resultSb = new StringBuffer(); 
		    for (int i = 0; i < b.length; i++) { 
		      resultSb.append(byteToHexString(b[i])); 
		    } 
		    return resultSb.toString(); 
		  } 
		
		  private static String byteToHexString(byte b) { 
		    int n = b; 
		    if (n < 0) 
		      n = 256 + n; 
		    int d1 = n / 16; 
		    int d2 = n % 16; 
		    return hexDigits[d1] + hexDigits[d2]; 
		  } 
		

	public static void main(String[] args) {

		String url = System.getProperty("user.dir") + File.separator
				+ "Webcontent" + File.separator + "barrack.js";
		System.out.println(getFileMD5(new File(url)));// 5f4b94a0b075b72f322452908ab8d8f1

	}
}
