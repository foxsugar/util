package time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
	public static void main(String[] args) throws ParseException {
		System.out.println("星碟2开服时间 : "+ getDateLongTime("2014-05-05 11:00:00"));
		System.out.println("32wan开服时间 : "+ getDateLongTime("2014-04-28 10:00:00"));
		System.out.println("9166wan开服时间 : "+ getDateLongTime("2014-04-29 15:55:00"));
		System.out.println("73994开服时间 : "+ getDateLongTime("2014-05-01 16:00:00"));
		System.out.println("qu247开服时间 : "+ getDateLongTime("2014-05-04 12:00:00"));
		System.out.println("76ju开服时间 : "+ getDateLongTime("2014-05-07 11:00:00"));
		System.out.println("ya247开服时间 : "+ getDateLongTime("2014-05-10 15:00:00"));
		System.out.println("dq247开服时间 : "+ getDateLongTime("2014-05-11 13:00:00"));
		System.out.println("125wan开服时间 : "+ getDateLongTime("2014-05-08 11:00:00"));
		System.out.println("789hi开服时间 : "+ getDateLongTime("2014-05-12 14:00:00"));
		System.out.println("jiayuan开服时间 : "+ getDateLongTime("2014-05-12 16:00:00"));
		System.out.println("帝舜开服时间 : "+ getDateLongTime("2014-05-15 10:00:00"));
		System.out.println("ya2开服时间 : "+ getDateLongTime("2014-05-16 15:00:00"));
		System.out.println("dq2开服时间 : "+ getDateLongTime("2014-05-16 13:00:00"));
		System.out.println("5qwan开服时间 : "+ getDateLongTime("2014-05-22 19:00:00"));
		System.out.println("32wan 2 开服时间 : "+ getDateLongTime("2014-05-19 10:00:00"));
		System.out.println("125wan 2 开服时间 : "+ getDateLongTime("2014-05-20 15:00:00"));
		System.out.println("5611 2 开服时间 : "+ getDateLongTime("2014-05-22 15:00:00"));
		System.out.println("52game 开服时间 : "+ getDateLongTime("2014-05-21 11:00:00"));
		System.out.println("qu247 2 开服时间 : "+ getDateLongTime("2014-05-25 12:00:00"));
		System.out.println("682wan  开服时间 : "+ getDateLongTime("2014-05-27 13:00:00"));
		System.out.println("130wan  开服时间 : "+ getDateLongTime("2014-05-24 13:00:00"));
		System.out.println("5qwan 2 开服时间 : "+ getDateLongTime("2014-05-28 18:00:00"));
		System.out.println("125wan 3 开服时间 : "+ getDateLongTime("2014-05-28 11:00:00"));
		System.out.println("ya247 3 开服时间 : "+ getDateLongTime("2014-05-28 15:00:00"));
		System.out.println("nextsee 2 开服时间 : "+ getDateLongTime("2014-05-29 10:00:00"));
		System.out.println("dq247 2 开服时间 : "+ getDateLongTime("2014-05-29 14:00:00"));
		System.out.println("511  开服时间 : "+ getDateLongTime("2014-05-29 14:00:00"));
		System.out.println("5611 3  开服时间 : "+ getDateLongTime("2014-05-31 15:00:00"));
		System.out.println("32wan 3  开服时间 : "+ getDateLongTime("2014-05-31 10:00:00"));
		System.out.println("5qwan 3  开服时间 : "+ getDateLongTime("2014-06-04 19:00:00"));
		System.out.println("对账  开服时间start : "+ getDateLongTime("2014-05-01 00:00:00"));
		System.out.println("对账  开服时间 end: "+ getDateLongTime("2014-05-31 23:59:59"));
		System.out.println("511wan 2  开服时间 : "+ getDateLongTime("2014-06-05 19:00:00"));
		System.out.println("76ju 2  开服时间 : "+ getDateLongTime("2014-06-05 11:00:00"));
		System.out.println("ya247 4  开服时间 : "+ getDateLongTime("2014-06-06 15:00:00"));
		System.out.println("5611 4  开服时间 : "+ getDateLongTime("2014-06-06 15:00:00"));
		
		System.out.println("125wan 4  开服时间 : "+ getDateLongTime("2014-06-11 11:00:00"));
		System.out.println("511wan 2  开服时间 : "+ getDateLongTime("2014-06-12 19:00:00"));
		System.out.println("nextsee 3  开服时间 : "+ getDateLongTime("2014-06-13 10:00:00"));
		System.out.println("5qwan 4  开服时间 : "+ getDateLongTime("2014-06-13 21:00:00"));
		System.out.println("32wan 4  开服时间 : "+ getDateLongTime("2014-06-16 15:00:00"));
		System.out.println("511 3  开服时间 : "+ getDateLongTime("2014-06-19 19:00:00"));
		System.out.println("5qwan 5  开服时间 : "+ getDateLongTime("2014-06-20 21:00:00"));
		System.out.println("32wan 5  开服时间 : "+ getDateLongTime("2014-06-23 15:00:00"));
		System.out.println("5611 5  开服时间 : "+ getDateLongTime("2014-06-26 15:00:00"));
		System.out.println("ds 4 开服时间 : "+ getDateLongTime("2014-06-27 10:00:00"));
		System.out.println("ds 4 开服时间 : "+ getDateLongTime("2014-06-27 14:00:00"));
		System.out.println("5qwan 开服时间 : "+ getDateLongTime("2014-06-27 19:00:00"));
	
		f(1399261341000L);
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
