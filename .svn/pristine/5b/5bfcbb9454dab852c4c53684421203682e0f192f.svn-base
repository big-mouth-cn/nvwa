package doug;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.transcoders.TokyoTyrantTranscoder;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		
//		new XMemcachedClientBuilder().set
		
		XMemcachedClient xmc = new XMemcachedClient("172.16.3.148",30015);//172.16.4.14:40000
		
		
		//ttserver
		xmc.setTranscoder(new TokyoTyrantTranscoder());
		
		byte[] content = (byte[])xmc.get("mytest");//5b42c2f22f266ebeeda05411c6920718
		System.out.println(new String(content));
		System.out.println(content.length);
		
//		xmc.set("haha", 0, "1234567890");
		
		//xmc.setTranscoder(new SerializingTranscoder());

//		String s = "test";
//		xmc.set("aabb", 0, s);
		
//		Map<String,String> m = new HashMap<String,String>();
//		m.put("k1", "v1");
//		m.put("k2", "v2");
//		m.put("k3", "v3");
//		m.put("k4", "v4");
//		
//		xmc.set("map", 0, m);
//		
//		Object o = xmc.get("map");
		
		
//		xmc.set("okok", 0, "yesyes");
//		System.out.println("no exp:"+xmc.get("okok"));
		
//		xmc.set("okok", 3, "yesyes");
//		Thread.sleep(1000);
//		System.out.println("no exp:"+xmc.get("okok"));
//		Thread.sleep(3000);
//		System.out.println("exp:"+xmc.get("okok"));
		
//		xmc.shutdown();
	}
}
