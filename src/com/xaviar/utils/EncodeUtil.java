package com.xaviar.utils;

import java.io.IOException;

import android.util.Base64;

public class EncodeUtil {
	
	public static String encode( byte[] bytes) throws IOException {    		
		  // String encodedStr = DatatypeConverter.printBase64Binary(bytes);		   
		 String encodedStr = Base64.encodeToString(bytes,Base64.DEFAULT);		   
		 return encodedStr;
		  }

	
	public static byte [] decode(String str) {    		
		  // byte [] decode =  javax.xml.bind.DatatypeConverter.parseBase64Binary(str);
		    try {
		   byte[] decode = Base64.decode(str, Base64.DEFAULT);
		   //String string = new String(bytes, "UTF-8");
		   return decode;
		    }
		    catch(Exception e)
		    {
		     	
		     return null;	
		    }
		  }

	
	

}
