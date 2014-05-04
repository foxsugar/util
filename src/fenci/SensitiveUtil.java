/*
 * 分词用ikAnalyzer实现,src目录加入了几个配置文件,
 * 分别是IKAnalyzer.cfg.xml,stopword.dic,keyword.dic(这个是我们自己扩展的)
 * 加入一个jar包 IKAnalyzer2012_u6.jar
 */
package fenci;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import file.FileUtils;

public class SensitiveUtil {
	//默认字符
	public static final String DEFAULTWORD= "";
	//不合法标志
	public static final boolean NOTLAWFUL = false;
	//敏感字典名称
	public static final String SENSITIVEFILENAME = "keyword.dic";
	//敏感词集合
	public static Set<String> SensitiveWord = null;
	//将敏感词加载到set中
	static {
		SensitiveWord = new HashSet<String>(FileUtils.readLines(new File(getFilePath())));
	}
	
	/**
	 * 替换text中的屏蔽词 将屏蔽词替换为*** 
	 * @param text 需要验证的字符串
	 * @return 如果捕获到异常将返回空字符串
	 */
	public static String replace (String text) {
		try {
			StringReader sr=new StringReader(text);
			//采用智能分词 (有两种分词方式 另一种是最细粒度分词 第二个参数为false)
			IKSegmenter ik=new IKSegmenter(sr, true);
			//得到的分词
			Lexeme lex=null;
			while((lex=ik.next())!=null){
				if(SensitiveWord.contains(lex.getLexemeText())){
					text  = text.replaceAll(lex.getLexemeText(), "***");
				}
			}
		} catch (Exception e) {
			return DEFAULTWORD;
		}
		return text;
	}
	
	/**
	 * 判断所给的字符串是否合法
	 * @param text
	 * @return 合法返回true 不合法返回false  若捕获异常返回false
	 */
	public static boolean isLawful(String text) {
		boolean isLawFul = true;
		try {
			StringReader sr=new StringReader(text);
			IKSegmenter ik=new IKSegmenter(sr, true);
			Lexeme lex=null;
			while((lex=ik.next())!=null){
				if(SensitiveWord.contains(lex.getLexemeText())){
					isLawFul = false;
					break;
				}
			}
		} catch (Exception e) {
			return NOTLAWFUL;
		}
		return isLawFul;
	}
	
	/**
	 * 得到敏感字典的文件位置
	 * @return
	 */
	public static String getFilePath() {
		return FileUtils.getRootPath()+SENSITIVEFILENAME;
	}
	
}
