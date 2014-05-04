import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class excle {
	
	public static void main(String[] args) throws IOException {
		
		List<String> list = new ArrayList<String>();
		for(int i=0;i<10;i++){
			String s  = ""+i;
			list.add(s);
		}
		FileUtils.writeLines(new File("d://testExcle.csv"), list);
		
	}
	
	
}
