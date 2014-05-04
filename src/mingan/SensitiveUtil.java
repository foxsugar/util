/*
 * 分词用ikAnalyzer实现,src目录加入了几个配置文件,
 * 分别是IKAnalyzer.cfg.xml,stopword.dic,keyword.dic(这个是我们自己扩展的)
 * 加入一个jar包 IKAnalyzer2012_u6.jar
 */
package mingan;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import file.FileUtils;

public class SensitiveUtil {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(SensitiveUtil.class);
	//默认字符
	private static final String DEFAULTWORD= "";
	//不合法标志
	private static final boolean ILLEGAL = false;
	//敏感字典名称
	private static final String SENSITIVEFILENAME = "keyword.dic";
	//敏感词集合
	public static Map<Character,List<String>> SensitiveMap = null;
	 static Set<String> SensitiveWord = null;
	//将敏感词加载到set中
	 
	 
	static {
		try{
			loadSensitiveWord();
		}catch(Exception e){
			logger.error("加载敏感词失败");
		}
	}
	
	
	/**
	 * 替换
	 * @param text
	 * @return
	 */
	public static String replace(String text){
		long start = System.currentTimeMillis();
		//text转成字符数组
		char[] textArray = text.toCharArray();
		try {
			//text的长度
			int length = text.length();
			//遍历输入的每个字符
			for(int i=0;i<length;i++){
				Character findC = Character.valueOf(textArray[i]);
				boolean isFind = SensitiveMap.containsKey(findC);
				//如果首字找到
				if(isFind){
					List<String> list = SensitiveMap.get(findC);
					//list 中的集合 {共产党,共产,共匪}
					for(String s:list){
						//判断集合中的每个字符串
						int count = 0;
						for(int j=0;j<s.length();j++){
							//如果敏感词库中的词长度大于剩余字符长度 或者 匹配不成功
							if((s.length()>length-i)||textArray[i+j]!=s.charAt(j)){
								continue;
							}else{
								count++;
							}
							//如果每个字符都匹配到 也就是找到敏感词
							if(count==s.length()){
								System.out.print("--匹配到=="+s+"   ");
								//修改字符数组
								for(int sl = 0;sl<s.length();sl++){
									System.out.print(textArray[i+sl]+"=改成 *    ");
									textArray[i+sl] = '*';
								}
								//将指针向后推字符长度的值 比如 共产党 三个
								i +=s.length()-1;
								//退出当前循环
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("敏感词替换错误");
			return DEFAULTWORD;
		}
		System.err.println(String.valueOf(textArray));
		return String.valueOf(textArray);
	}
	
	
	
	
	public static boolean isLegal(String text) {
		try{
			int length = text.length();
			//text转成字符数组
			char[] textArray = text.toCharArray();
			//遍历输入的每个字符
			for(int i=0;i<length;i++){
				Character findC = Character.valueOf(textArray[i]);
				boolean isFind = SensitiveMap.containsKey(findC);
				//如果首字找到
				if(isFind){
					List<String> list = SensitiveMap.get(findC);
					//list 中的集合 {共产党,共产,共匪}
					for(String s:list){
						//判断集合中的每个字符串
						int count = 0;
						for(int j=0;j<s.length();j++){
							//如果敏感词库中的词长度大于剩余字符长度 或者 匹配不成功
							if((s.length()>length-i)||textArray[i+j]!=s.charAt(j)){
								continue;
							}else{
								count++;
							}
							//如果每个字符都匹配到 也就是找到敏感词
							if(count==s.length()){
								return false;
							}
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("敏感词验证错误 起名失败");
			return ILLEGAL;
		}
		//没有敏感词
		return true;
	}
	
	
	
	public static void loadSensitiveWord (){
		//敏感词
		Set<String> SensitiveWord = new HashSet<String>(FileUtils.readLines(new File(getFilePath())));
		SensitiveMap = new HashMap<Character,List<String>>();
		//将敏感词以首字为key进行索引
		Iterator< String> it = SensitiveWord.iterator();
		while(it.hasNext()){
			String s = it.next();
			Character c = s.charAt(0);
			List<String> list = SensitiveMap.get(c);
			if(list==null){
				list  = new ArrayList<String>();
			}
			list.add(s);
			SensitiveMap.put(c, list);
		}
		//排序
		sort();
	}
	/**
	 * 对SensitiveMap中的value 进行排序 字数多的排在前面字数少的在后面
	 */
	public static void sort(){
		Iterator<Entry<Character, List<String>>> it = SensitiveMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Character, List<String>> entry = it.next();
			List<String> list = entry.getValue();
			Collections.sort(list, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					if(o1.length()>o2.length()){
						return -1;
					}else if(o1.length()<o2.length()){
						return 1;
					}else{
						return 0;
					}
				}
			});
		}
	}
	
	/**
	 * 替换text中的屏蔽词 将屏蔽词替换为*** 
	 * @param text 需要验证的字符串
	 * @return 如果捕获到异常将返回空字符串
	 */
	@Deprecated
	public static String replace2 (String text) {
		try {
			StringReader sr=new StringReader(text);
			//采用智能分词 (有两种分词方式 另一种是最细粒度分词 第二个参数为false)
			IKSegmenter ik=new IKSegmenter(sr, false);
			//得到的分词
			Lexeme lex=null;
			while((lex=ik.next())!=null){
				String s = lex.getLexemeText();
				if(SensitiveWord.contains(s)){
					text  = text.replaceAll(lex.getLexemeText(), "***");
				}
			}
		} catch (Exception e) {
			logger.error("敏感词替换错误");
			return DEFAULTWORD;
		}
		return text;
	}
	
	/**
	 * 判断所给的字符串是否合法
	 * @param text
	 * @return 合法返回true 不合法返回false  若捕获异常返回false
	 */
	@Deprecated
	public static boolean isLegal1(String text) {
		boolean isLegal = true;
		try {
			StringReader sr=new StringReader(text);
			IKSegmenter ik=new IKSegmenter(sr, true);
			Lexeme lex=null;
			while((lex=ik.next())!=null){
				if(SensitiveWord.contains(lex.getLexemeText())){
					isLegal = false;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("敏感词验证错误 起名失败");
			return ILLEGAL;
		}
		return isLegal;
	}
	
	
	/**
	 * 替换敏感词 用字符串数组的方式存储
	 * 效率高一点 但是会有漏下敏感词的情况
	 * @param text
	 * @return
	 */
	@Deprecated
	private static String replaceUseCharArray (String text) {
		long start = System.currentTimeMillis();
		char[] c = text.toCharArray();
		try {
			StringReader sr=new StringReader(text);
			//采用智能分词 (有两种分词方式 另一种是最细粒度分词 第二个参数为false)
			IKSegmenter ik=new IKSegmenter(sr, true);
			//得到的分词
			Lexeme lex=null;
			while((lex=ik.next())!=null){
				String s = lex.getLexemeText();
				System.out.print(s+" ");
				int len = s.length();
				int index = lex.getBeginPosition();
				if(SensitiveWord.contains(s)){
					for(int i=0;i<len;i++){
						c[index+i] = '*';
					}
				}
			}
		} catch (Exception e) {
			return DEFAULTWORD;
		}
		long end = System.currentTimeMillis();
		System.out.println("替换字符数组所需的时间为  ="+(end-start));
		return String.valueOf(c);
	}
	
	/**
	 * 得到敏感字典的文件位置
	 * @return
	 */
	private static String getFilePath() {
		return FileUtils.ROOTPATH+SENSITIVEFILENAME;
	}
	
	public static void main(String[] args) {
		System.out.println(isLegal("学习平凡"));
	}
}
