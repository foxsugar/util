package pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class PinyinUtil {

	/**
	 * 得到字符串的拼音
	 * @param chinese
	 * @return
	 */
	public static String getPinyin(String chinese){
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		//小写
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		//没有音调
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		//字符串转成字符数组
		char[] manyChinese = getCharArray(chinese);
		StringBuffer  sb = new StringBuffer();
		
		String add = "";
		
		for(int i=0;i<manyChinese.length;i++){
			try {
				char c = manyChinese[i];
				//如果是标点
				if(c>=65281&&c<=65374)
					c = punctuationConver(c);
				
				//不是中文将原文输出
				if(!isChinese(c)){
					add = String.valueOf(c);
				}else{
					add = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0];
				}
				sb.append(add);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 *得到字符串的字符数组
	 * @param s
	 * @return
	 */
	private static char[] getCharArray(String s){
		return s.toCharArray();
	}

	/**
	 * 判断一个字符是不是汉字
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c){
		//右移八位如果不是0 判断为汉字 百度来的
		return c>>8!=0;
	} 
	
	
	/**
	 * 标点转换
	 */
	public static char punctuationConver(char c){
		//中文标点范围 大于65281 小雨65374
		int charNum = (int)c;
		if(c>=65281&&c<=65374){
			//减掉65248 转换成英文标点
			charNum -=65248;
		}
		return (char)charNum;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getPinyin("哈哈:：haha"));
	 }
	
	
}

