package time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
	public static void main(String[] args) throws ParseException {
		System.out.println("星碟开服时间 : "+ getDateLongTime("2014-04-28 11:00:00"));
		System.out.println("32wan开服时间 : "+ getDateLongTime("2014-04-28 10:00:00"));
		System.out.println("9166wan开服时间 : "+ getDateLongTime("2014-04-29 15:55:00"));
		System.out.println("73994开服时间 : "+ getDateLongTime("2014-05-01 16:00:00"));
		System.out.println("qu247开服时间 : "+ getDateLongTime("2014-05-04 12:00:00"));
		f(1398760512100L);
		System.out.println("当前时间:"+System.currentTimeMillis());
//		1398758456100
//		1398762622202
//		1398760512100
	}
	
	
	public static void f(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(time));
	}
	//2014-04-29 17:05:06,999 ERROR [qtp274064735-20423] platform.PlatformJoinAction (PlatformJoinAction.java:114)     - platform logined： param is invalid,[qid=1015092 serverId=S1 agent=dw_9166wan time=1398760512100 sign=575195e940cef8212b4a5fba80e6249d isAdult=1],describtion:时间超时
	
	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getDateLongTime(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(date);
		return d.getTime();
		
		
	}
}
