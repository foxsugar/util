package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test5_4 {
	private static final int NUM = 100;
	
	private static ExecutorService es = Executors.newFixedThreadPool(50);
	
	public static List<Integer> list = new ArrayList<Integer>();
	
	public static void addElement(List list){
		list.clear();
		for(int i=0;i<NUM;i++){
			list.add(i);
		}
	}
	public static void main(String[] args) {
		addElement(list);
		List list1 = new ArrayList<Integer>();
		List list2 = new ArrayList<Integer>();
		for(int i=0;i<list.size();i++){
			if(i%2==0){
				list1.add(i);
			}else{
				list2.add(i);
			}
		}
		print(list1,"1");
		print(list2,"2");
		
	}
	
	
	public static void print(final List list,final String name){
		es.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<list.size();i++){
					System.out.println(name+"     ===    "+list.get(i));
				}
			}
		});
	}
}
