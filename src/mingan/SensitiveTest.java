package mingan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SensitiveTest {
	public static Map<Character,List<String>> map = null;
	
	
	
	public static void load (){
		map = new HashMap<Character,List<String>>();
		Iterator< String> it = SensitiveUtil.SensitiveWord.iterator();
		while(it.hasNext()){
			String s = it.next();
			Character c = s.charAt(0);
			s.charAt(0);
			List<String> list = map.get(c);
			if(list==null){
				list  = new ArrayList<String>();
			}
			list.add(s);
			map.put(c, list);
		}
	}
	public static void sort(){
		Iterator<Entry<Character, List<String>>> it = map.entrySet().iterator();
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
	 * 替换
	 * @param text
	 * @return
	 */
	public static String replace(String text){
		long start = System.nanoTime();
		//text的长度
		int length = text.length();
		//text转成字符数组
		char[] textArray = text.toCharArray();
		//遍历输入的每个字符
		for(int i=0;i<length;i++){
			Character findC = Character.valueOf(textArray[i]);
			boolean isFind = map.containsKey(findC);
			//如果首字找到
			if(isFind){
				List<String> list = map.get(findC);
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
		long end = System.nanoTime();
		System.out.println("消耗时间======="+(end-start)/10000000.0);
		System.out.println();
		System.err.println(String.valueOf(textArray));
		return String.valueOf(textArray);
		
	}
	
	
	public static void main(String[] args) {

		load();
		sort();
		replace("习近平啦啦啦啦啦了胡锦涛啦啦啦啦啦啦啦啦啦啦");
		replace("习近平傻×胡锦涛");
		replace("無界瀏覽器");
		replace("武 林 三 国");
		replace("法$$伦");
		replace("睾");
		replace("aaa习近平dfdfha测试测试自由民主");
		replace("核心提示： 一名美国官员称，两架美国B-52轰炸机从西太平洋的关岛起飞，于华盛顿时间25日晚间7时左右，进入中国东海防空识别区。");
		System.out.println(map.get('5'));
		System.out.println(map.get('a'));
		
//		System.out.println(map.toString());
//		List list = new ArrayList();
//		list.add("ad");
//		list.add("abc");
//		Collections.sort(list);
//		Collections.reverse(list);
//		System.out.println(list.toString());
		
//		System.out.println("haha".charAt(0));
	
	}
}
