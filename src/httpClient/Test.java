package httpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;




public class Test {

	private static HttpClient hc = new DefaultHttpClient();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		test_login();
		test_exchange();
//		getCharacter();
	}
	
	public static void test_login(){

    	String qid = "testrr";
    	String server_id = "s1";
    	String time = ""+System.currentTimeMillis();//测试时间
    	String agent = "dw_9166wan";
    	String key = "d51eaef3a5ded9c5a6f5d632a438db33";
    	String isAdult = "0";
    	System.out.println(time);
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("qid", qid));
		params.add(new BasicNameValuePair("server_id", server_id));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("isAdult", isAdult));
		params.add(new BasicNameValuePair("agent", agent));
		
		String sign = PlatformUtils.getMD5(qid,server_id,time,agent,key);
		params.add(new BasicNameValuePair("sign", sign));
		
		String url = "http://s1.9166wan.dw.stby.sjsgame.com/wok/login4p.json";
//		String url = "http://s1.094wan.xd.sjsgame.com/wok/login4p.json";

		String body = post(url, params);
		System.out.println(body);
		
	}
	public static void test_exchange(){

    	String qid = "testrr";
    	String server_id = "s1";
    	String time = ""+System.currentTimeMillis();//测试时间
    	String agent = "dw_9166wan";
    	String key = "d51eaef3a5ded9c5a6f5d632a438db33";
    	String order_id = ""+System.currentTimeMillis();
    	String order_amount = "1";
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("qid", qid));
		params.add(new BasicNameValuePair("server_id", server_id));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("agent", agent));
		params.add(new BasicNameValuePair("order_id", order_id));
		params.add(new BasicNameValuePair("order_amount", order_amount));
//		System.out.println(Float.toString("1"));
//		String authKey = PlatformUtils.getMD5(username,serverId,Float.toString(time),agent,order_id,Integer.toString(order_amount),getPlatformInfo(agent,serverId).getPay_key());
		String sign = PlatformUtils.getMD5(qid,server_id,time,agent,order_id,order_amount,key);
		System.out.println(sign);
		params.add(new BasicNameValuePair("sign", sign));
	
		
//		String url = "http://gamedev.sjsgame.com/wok/exchange.json";
//		String url = "http://s1.094wan.xd.sjsgame.com/wok/exchange.json";
		String url = "http://s1.9166wan.dw.stby.sjsgame.com/wok/exchange.json";
		String body = post(url, params);
		System.out.println(body);
		
	}
	
	private static void getCharacter(){
	   	String qid = "testrr";
    	String server_id = "s1";
    	String time = ""+System.currentTimeMillis();//测试时间
    	String agent = "dw_9166wan";
    	String key = "d51eaef3a5ded9c5a6f5d632a438db33";
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("qid", qid));
		params.add(new BasicNameValuePair("server_id", server_id));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("agent", agent));
		
		String sign = PlatformUtils.getMD5(qid,server_id,time,agent,key);
		System.out.println(sign);
		params.add(new BasicNameValuePair("sign", sign));
		
//		String url = "http://gamedev.sjsgame.com/wok/getCharacter.json";
		String url = "http://s1.9166wan.dw.stby.sjsgame.com/wok/getCharacter.json";
//		http://s1.ufojoy.xd.sjsgame.com/wok/

		String body = post(url, params);
		System.out.println(body);
		
	}
	

	/**
	 * Get请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, List<NameValuePair> params) {
		String body = null;
		try {
			// Get请求
			HttpGet httpget = new HttpGet(url);
			// 设置参数
			String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			// 发送请求
			HttpResponse httpresponse = hc.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * // Post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, List<NameValuePair> params) {
		String body = null;
		try {
			// Post请求
			HttpPost httppost = new HttpPost(url);
			// 设置参数
			httppost.setEntity(new UrlEncodedFormEntity(params));
			// 发送请求
			HttpResponse httpresponse = hc.execute(httppost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

}
