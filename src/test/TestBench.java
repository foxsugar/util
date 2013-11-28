package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TestBench {
	public static void main(String[] args) {
//		System.out.println(Runtime.getRuntime().availableProcessors());
//		System.out.println(Runtime.getRuntime().freeMemory());
//		Map<String ,String> m = new HashMap<String,String>();
//		Iterator it = m.entrySet().iterator();
//		while(it.hasNext()){
//			Entry entry = (Entry)it.next();
//			String key = (String) entry.getKey();
//		}
		List<String> list  = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		List<String> newList =  Collections.unmodifiableList(list);
		//newList.add("z");
		System.out.println(newList.toString());
		System.out.println(newList.get(1));
		newList = list;
		newList.add("a");
		System.out.println(newList);
	}
}
