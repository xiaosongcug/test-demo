package com.ReadDLTJ;

import java.io.IOException;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JX_JSYD_YS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String json = null;
		try {
			json = new DLTJ_MJ_HZ().readFile("X:\\Users\\Administrator\\Desktop\\test.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(json);
		JSONObject result =new JSONObject();
		int size = jsonArray.size();
		JSONArray  r=new JSONArray();
		for(int i=0;i<size;i++) {
			JSONObject res = jsonArray.getJSONObject(i);
			JSONArray res0 = (JSONArray) jsonArray.getJSONObject(i).get("result");
			int res0Size=res0.size();
			String sourceLayer=(String) res.get("sourceLayer");
			String attribute=(String) res.get("attribute");
			String value=(String) res.get("value");
			 JSONObject t = new JSONObject();
			 t.put("sourceLayer", sourceLayer); 
			 t.put("attribute", attribute);
			 t.put("value", value);
			 JSONObject sc = null;
	       for(int k=0;k<res0Size;k++) {
	    	   JSONObject l=res0.getJSONObject(k);
	    	   String layerName=l.getString("layerName");
	    	   boolean ys=layerName.contains("GHXZYS");
	    	   boolean pc=layerName.contains("JSYD");
	    	    sc = (JSONObject) res0.get(k);
	    	   if(ys||pc) {
	    		 //sc.put("layerName", layerName);
	    		   sc.put("layerName", layerName);
	    		   sc.put("overinfo", "123");
	    		
	    	   }else {
	    		   sc.put("layerName", layerName);
	    		   sc.put("overinfo", "123");
	    		 
	    	   }
	    	   
	    	   //System.out.println(sourceLayer+","+attribute+","+value);
	    	  
	       }
	       t.put("result",sc);
	       r.add(t);
	       		
				
		}
		
		System.out.println(r);	
	}

}
