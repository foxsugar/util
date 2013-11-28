package zip;

import java.io.File;

import org.zeroturnaround.zip.ZipUtil;

/**
 * 使用zt-zip的测试工具类 zero-turnround该公司还有热部署的项目
 * 依赖jar包 slf4j
 * git hub 地址 https://github.com/zeroturnaround/zt-zip
 * @author sunxianping
 *
 */
public class ZipTest {
	
	public static void main(String[] args) {
		
		//解压缩
		ZipUtil.unpack(new File("d://a.zip"), new File("d://test"));
		
		//压缩
		ZipUtil.pack(new File("d://test"), new File("d://test.zip"));
	}
}
