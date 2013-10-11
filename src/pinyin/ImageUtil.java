package pinyin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

/**
 * ��ȡһ���ļ����µ��ļ��������е�ͼƬ�ļ��������������ָĳ�ƴ������ŵ�ָ��λ�ò���ɿͻ���js�ļ�����Ҫ����������ַ�
 * ������Ŀ���ļ����µ�result.txt�ļ���
 * 
 * @author sunxianping
 * 
 */
public class ImageUtil {
	// ͼƬλ��
	//public static final String imagePath = "E://develop/document/ͼƬ����/UI��ͼ����/ս��/���õı��ּ���";
	 //public static final String imagePath = "E://ͨ�ü���";
	public static final String imagePath = "E://Ͷʯ������";
	// �����ͼƬ��λ�� �Լ��ĵ�����
	public static final String savePath = "e://tupian";

	// js ��loadImage�ĵ�ַ
	public static final String LOADIMAGEPATH = "/battle2/anim/bingzhong/";

	public static final String addImageTemplet = "gbox.addImage(\"%s\", image_path + \""
			+ LOADIMAGEPATH + "%s?v=1\");";
	public static final String addTilesTemplet = "gbox.addTiles({id :'%s',image : '%s',tilew : %s,tileh : %s,tilerow : 1,gapx : 0,gapy : 0});";

	// gbox.addImage("qibingxiafangbeiji", image_path +
	// "/battle2/anim/bingzhong/qibingxiafangbeiji.png?v=1");
	// gbox.addTiles({id :'qibingxiafangbeiji',image :
	// 'qibingxiafangbeiji',tilew : 1623,tileh : 247,tilerow : 1,gapx : 0,gapy :
	// 0});

	/**
	 * ִ�з���
	 * 
	 * @throws Exception
	 */
	public static void execute() throws Exception {
		//��ʼ�� Ϊ��ɾ��result.txtǰ��ļ�¼
		File resultFile = new File(savePath + "/loadImage.txt");
		resultFile.delete();
		//��ȡ�ļ�
		List<File> files = new ArrayList<File>();
		// ͼƬĿ¼�µ������ʼ�
		getAllFile(new File(imagePath), files);
		// ͼƬ�ļ�
		List<File> imageFiles = getImageFiles(files);
		// ��ͼƬ��map ��ƴ�������� Ϊ�˲��Ƿ��ظ�
		Map<String, File> image_pinyin = new HashMap<String, File>();
		// �ظ���ͼƬ
		List<File> imageRepeat = new ArrayList<File>();
		// ����ͼƬ������
		for (File file : imageFiles) {
			// ͼƬ��������
			String fileName = file.getName().split("\\.")[0];
			// ͼƬ��ƴ����
			System.out.println(fileName);
			
			String pinyin = PinyinUtil.getPinyin(fileName);
			// ����ƴ���ļ��� ����չ��׺��
			String saveName = pinyin + "." + file.getName().split("\\.")[1];
			// �ж��ļ����Ƿ��ظ�
			boolean isRepeat = image_pinyin.get(pinyin) != null;
			// ����ظ�
			if (isRepeat) {
				imageRepeat.add(file);
				// System.out.println("�ظ� ��ַΪ = "+file.getAbsolutePath());
			} else {
				// ���ظ��Ļ� ���ļ��Ž�һ��map
				BufferedImage image = getImage(file);
				String width = "" + image.getWidth();
				String hight = "" + image.getHeight();
				// �Ž�map
				image_pinyin.put(pinyin, file);
				copyFile(file, savePath, saveName);
				// System.out.println("���ڱ���");
				// ����ģ��ƴ���ַ�
				String addImage = "".format(addImageTemplet, pinyin, saveName);
				String addTiles = "".format(addTilesTemplet, pinyin, pinyin,width, hight);
				// �����Ž�list
				List<String> s = new ArrayList<String>();
				s.add(addImage);
				s.add(addTiles);
				// �����д��Ŀ���ļ��µ�result.txt�ļ�
				writeString(s, savePath + "/loadImage.txt");
				System.out.println(addImage);
				System.out.println(addTiles);

			}
		}

		for (File repeat : imageRepeat) {
			String repeatName = repeat.getName().split("\\.")[0];
			String s = image_pinyin.get(PinyinUtil.getPinyin(repeatName)).getAbsolutePath();
			System.err.println("д���ļ���ͼƬ : " + s);
			System.err.println("����ļ��ظ� :  " + repeat.getAbsolutePath() + "\r\n");
		}
	}
	
	/**
	 * ���js�������ļ�
	 * @throws Exception
	 */
	public static void aniUtil() throws Exception{
		String firstLine = "battlefield.animationFactory.%s = function()";
		String lastLine = "battlefield.animationFactory.%s.prototype = new Animation;";
		String mainHtmlJs = "<script type=\"text/javascript\" src=\"js/client/battleSkill/%s.js?v=1\"></script> ";
		
		List<File> allFiles = new ArrayList<File>();
		getAllFile(new File(imagePath), allFiles);
		//txt files
		for(File f:allFiles){
			String action_wuzi = "";
			String action_you = "";
			
			
			if(f.getName().endsWith(".txt")){
				//��������
				String chineseName = f.getName().split("\\.")[0];
				//System.out.println(chineseName);
				//�ļ��е�ƴ��
				String pinyin = PinyinUtil.getPinyin(chineseName);
				//��ƴ�����ļ���
				String pinyinName_suf =pinyin+".js";
				//�ļ���ÿһ��
				List<String> lines = FileUtils.readLines(f);
				for(String line:lines){
					if(line.startsWith("this.action =")){
						action_wuzi = line;
					}
					if(line.startsWith("this.action[")){
						action_you = line;
					}
				}
				
				String speed = action_you.split("\\(")[1].split(",")[0];
				String action_array = '['+(action_you.split("\\[")[2].split("\\)")[0]);
				
				String action_1 = "this.action = "+action_array+";";
				String action_2 = "this.speed = "+speed+";";
				
				List<String> lastString = new ArrayList<String>();
				//��һ�м����
				lastString.add("//"+chineseName);
				//�ڶ��м���ͷ
				lastString.add("".format(firstLine,pinyin ));
				for(String line:lines){
					if(line.startsWith("this.action =")){
						lastString.add(action_1);
						lastString.add(action_2);
					}else
					if(line.startsWith("this.action[")){
						//do nothing
					}else{
						lastString.add(line);
					}
				}
				//����β
				lastString.add("".format(lastLine, pinyin));
				
				FileUtils.writeLines(new File(savePath+"/"+pinyinName_suf),"UTF-8", lastString);
				
				//���main.html��ĵ���js�ļ�
				System.out.println("".format(mainHtmlJs, pinyin));
				
			}	
		}
		
		
		
	}

	/**
	 * ���һϵ���ļ�������ͼƬ�ļ�
	 * 
	 * @param files
	 * @return
	 */
	public static List<File> getImageFiles(List<File> files) {
		System.out.println(files);
		List<File> ImageFiles = new ArrayList<File>();
		// ͼƬ��׺
		String[] pic_suffix = { ".png", ".jpg", ".bmp", ".gif" };
		for (File f : files) {
			for (int i = 0; i < pic_suffix.length; i++) {
				if (f.getName().endsWith(pic_suffix[i])) {
					ImageFiles.add(f);
				}
			}
		}
		return ImageFiles;
	}

	/**
	 * �õ��ļ��µ������ļ� ���������ļ���ӽ�list����
	 * 
	 * @param file
	 *            �ļ�
	 * @param list
	 *            ��ӵ��˼�����
	 * @throws Exception
	 */
	public static void getAllFile(File file, List<File> list) throws Exception {
		if (file == null)
			return;
		if (file.isFile()) {// �ж��Ƿ����ļ�
			String name = file.getName();// ȡ���ļ�������
			// �ļ�������û��ȥ����׺����δ������Լ�д�ɡ�
			name = name == null ? "" : name.trim();
			String path = file.getPath();// ȡ���ļ�·��
			// System.out.println(file);
			list.add(file);
		}
		File[] files = file.listFiles();// ȡ���ļ����а���ļ����ļ���
		if (files == null || files.length <= 0)
			return;// ���û������û���ļ����ļ��У�����
		for (File file2 : files) {// ѭ�����������ļ����ļ���
			getAllFile(file2, list);// �ݹ�
		}
	}

	/**
	 * ��ȡjar������ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedImage getImage(File file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * �����ļ���ָ���ļ�������
	 * 
	 * @param src
	 *            ԭ�ļ�
	 * @param destPath
	 *            Ŀ���ļ���
	 * @param fileName
	 *            Ŀ���ļ���
	 */
	public static void copyFile(File src, String destPath, String fileName) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(new File(destPath + "/" + fileName));
			// System.out.println(destPath + "/" + src.getName());

			byte[] buf = new byte[1024];
			int len = in.read(buf);// ���ļ��������������ݷ��뵽buf�����У����ص��Ƕ����ĳ���
			while (len != -1) {
				out.write(buf, 0, len);
				len = in.read(buf);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(in!=null) in.close();
				if(out!=null) out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * ���ַ�д���ļ� ��׷�ӷ�ʽ
	 * 
	 * @param list
	 *            �ַ�����
	 * @param path
	 *            ������ļ�·��
	 */
	public static void writeString(List<String> list, String path) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path, true);// �ڶ��������趨�Ƿ�׷���ļ�
			PrintWriter pw = new PrintWriter(fos);
			for (String line : list) {
				pw.write(line + "\r\n");
			}
			pw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
	//	execute();
//		aniUtil();
	}
}
