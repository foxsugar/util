/*
 * 分词
 */
package fenci;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class test {
	public static void main(String[] args) throws IOException {
		StringReader sr = new StringReader("学习近平");
		//参数为true 为只能分词
		IKSegmenter ik = new IKSegmenter(sr,false);
		Lexeme lx = null;
		while((lx = ik.next())!=null){
			System.out.println(lx.getLexemeText());
		}
	}
}
