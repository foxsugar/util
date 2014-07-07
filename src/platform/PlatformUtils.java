package platform;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;


public class PlatformUtils {
	
	private static String[] whiteIPs = new String[]{"",""}; 
	
	/**
	 * 验证ip
	 * @param ip
	 * @return
	 */
	public static boolean verifyIP(String ip){
		for(int i=0,len = whiteIPs.length;i<len;i++){
			if(ip.equals(whiteIPs[i])){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 验证ip
	 * @param request
	 * @return
	 */
	public static boolean verifyIP(HttpServletRequest request){
		return verifyIP(getRemoteAddress(request));
	}
	
	/**
	 * 获得ip
	 * @param request
	 * @return
	 */
    public static String getRemoteAddress(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    } 
    
    
    
    public static String getMD5(String... parm){
    	StringBuilder result = new StringBuilder("");
    	int index = 0;
    	for(String s:parm){
    		result.append(s).append(index==parm.length-1?"":',');
    		index++;
    	}
    	//System.out.println(result.toString());
    	return DigestUtil.MD5(result.toString());
    }
    public static void main(String[] args) {

//    	String qid = "testUser";
//    	String server_id = "testServerId";
//    	String time = Float.toString(0L);//测试时间
//    	String agent = "testAgent";
//   String key = "testKEY";
//	//需要的sign
////一定要保证顺序
//String sign = getMD5(qid,server_id,time,agent,key);
////结果 sign= c7964be1cdfd585451e86554ebcebc76
//System.out.println(sign);
System.out.println(DigestUtil.MD5("水晶石-ds"));
System.out.println(System.currentTimeMillis());
System.out.println("1398146253");
    
	}
}
