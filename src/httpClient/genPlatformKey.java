package httpClient;

public class genPlatformKey {
	public static void main(String[] args) {
		String s = "水晶石-km";
		System.out.println(DigestUtil.MD5(s));
	}
}
