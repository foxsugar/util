package platform;

public class genPlatformKey {
	public static void main(String[] args) {
		String s = "水晶石-jy";
		System.out.println(DigestUtil.MD5(s));
	}
}
