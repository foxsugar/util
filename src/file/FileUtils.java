/*
 * 文件工具类 此类依赖三个jar包 编码嗅探
 * jar antlr.jar   chardet.jar  cpdetector_1.0.7.jar
 */
package file;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class FileUtils {
	

	public static CodepageDetectorProxy DETECTORPROXY =  null;
	public static final Charset DEFAULTENCODING = Charset.forName("utf-8");
	
	static{
		//加载编码嗅探器  此类需要三个依赖jar包 
		DETECTORPROXY = CodepageDetectorProxy.getInstance();
		DETECTORPROXY.add(new ParsingDetector(false));
		DETECTORPROXY.add(JChardetFacade.getInstance());
		DETECTORPROXY.add(ASCIIDetector.getInstance());
		DETECTORPROXY.add(UnicodeDetector.getInstance());
	}
	
	
	public static Charset getFileEncoding (File file){
		Charset charset = null;
		try {
			charset = DETECTORPROXY.detectCodepage(file.toURI().toURL());
		} catch (Exception e) {
			e.printStackTrace();
			//返回utf-8的编码
			return DEFAULTENCODING;
		}
		return charset;
	}
	
	
	/**
	 * 按行读文件
	 * @param file
	 * @return
	 */
	public static List<String> readLines (File file) {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), getFileEncoding(file)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		List<String> lines = new ArrayList<String>();
		try {
			while(bf.ready()){
				lines.add(bf.readLine().trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(bf!=null) bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lines;
	}
	
	/**
	 * 将字符串集合写进文件
	 * @param lines
	 * @param file
	 */
	public static void writeLines(List<String> lines , File file) {
		BufferedWriter bw = null;
		try {
			//写进utf-8的编码
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),DEFAULTENCODING));
			for(String line : lines){
				bw.write(line);//写进字符
				bw.newLine();//换行
			}
			bw.flush();//写进
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
					try {
						if(bw!=null) bw.close();
					}
					 catch (IOException e) {
						e.printStackTrace();
					}
			}
	}
	
	
	
	
	
	/**
	 * 得到jar包外图片
	 * @param file
	 * @return
	 */
	public static BufferedImage getImage(File file){
		BufferedImage image = null;
		try {
			 image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public static void main(String[] args) throws IOException {
//			readLines(new File("d://a.txt"));
//			writeLines(	readLines(new File("d://log.xml")),new File("d://b.txt"));
	}
}
