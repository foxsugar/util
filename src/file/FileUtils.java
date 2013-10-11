package file;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class FileUtils {
	
	/**
	 * 按行读文件
	 * @param file
	 * @return
	 */
	public static List<String> readLines (File file) {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		List<String> lines = new ArrayList<String>();
		try {
			while(bf.ready()){
				lines.add(bf.readLine());
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
			bw = new BufferedWriter(new FileWriter(file));
			for(String line : lines){
				bw.write(line);//写进字符�?
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
