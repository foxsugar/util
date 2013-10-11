package work.clientVersion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * 更新客戶端每個文件的版本信息
 */
public class ClientVersionUtil {
	//文件根目录
	public static final String FILEPATH = System.getProperty("user.dir")+File.separator+"Webcontent"+File.separator;
	//正则表达式
	public static final String RegJs = "js+\\S+\\.+js+\\?+v=+\\w+";
	public static final String RegPic = "images+\\S+\\.+(png|jpg)+\\?+v=+\\w+";
	
	/**
	 * 逻辑
	 */
	public static void logic(){
		//所有js html文件的列表
		List<File> FileList = new ArrayList<File>();
		try {
			getAllFile(new File(FILEPATH), FileList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//匹配js
		
		Pattern patJs = Pattern.compile(RegJs);
		//匹配图片
		Pattern patPic = Pattern.compile(RegPic);
		
		for(File f:FileList){
			List<String> lines = readLines(f);
			List<String> resultLines = new ArrayList<String>(); 
			boolean flag = false;
			for(String line:lines){
				Matcher matJs = patJs.matcher(line);  
				Matcher matPic = patPic.matcher(line);  
				
				boolean findJs = matJs.find();
				boolean findPic = matPic.find();
				//如果是注释不替换
				if(line.startsWith("//")&&(findJs||findPic)){
					findJs = false;
					findPic = false;
				}
				if(findJs){
					flag = true;
					String path = matJs.group(0).split("\\?")[0];
					String version = getFileMD5(new File(FILEPATH+path));
					String result = path+"?v="+version;
					line = line.replaceAll(RegJs, result);
//					System.out.println(version);
					System.out.println(line);
				}
				if(findPic){
					flag = true;
					String path = matPic.group(0).split("\\?")[0];
					String version = getFileMD5(new File(FILEPATH+path));
					String result = path+"?v="+version;
					line = line.replaceAll(RegPic, result);
					System.out.println(line);
				}
				resultLines.add(line);
			}
			//覆盖掉匹配的文件
			if(flag){
				writeLines(f, resultLines); 
			}
		}
	}
	
	/**
	 * 拿到文件下的所有js html文件 并将所有文件添加进list集合
	 * 
	 * @param file
	 *            文件
	 * @param list
	 *            添加到此集合中
	 * @throws Exception
	 */
	public static void getAllFile(File file, List<File> list) throws Exception {
		if (file == null)
			return;
		if (file.isFile()) {// 判断是否是文件
			String name = file.getName();// 取得文件的名字
			name = name == null ? "" : name.trim();
			String path = file.getPath();// 取得文件路径
			// System.out.println(file);
			if(path.endsWith(".js")||path.endsWith(".html")){
				list.add(file);
			}
		}
		File[] files = file.listFiles();// 取得文件夹中包含的文件及文件夹
		if (files == null || files.length <= 0)
			return;// 如果没有其中没有文件或文件夹，返回
		for (File file2 : files) {// 循环其下所有文件及文件夹
			getAllFile(file2, list);// 递归
		}
	}
	
	
	/**
	 * 拿到文件的md5值
	 * @param file
	 * @return
	 */
	 public static String getFileMD5(File file) {
		    if (!file.isFile()){
		      return null;
		    }
		    MessageDigest digest = null;
		    FileInputStream in=null;
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
		    }
		    BigInteger bigInt = new BigInteger(1, digest.digest());
		    //获取8到24位的16位md5
		    return bigInt.toString(16).substring(8, 24);
	 }
	
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
		public static void writeLines(File file,List<String> lines) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file));
				for(String line : lines){
					bw.write(line);//写进字符串
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
	 
	public static void main(String[] args) {
		logic();
	}
}
