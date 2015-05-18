package com.pvgwt.widgets.client.utils;

import java.util.ArrayList;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.util.JSONEncoder;
import com.smartgwt.client.util.Offline;
import com.smartgwt.client.widgets.grid.ListGridField;

public class Utilities {
	
	public static String getUrl(){    	
		return Window.Location.getQueryString()+"&ws=true&".replace("network_id", "XXX").replace("client_id", "XXX").replace("XXX", "ignore");//+"&ws=true";
	}
	
	public static ArrayList<String> GetKeyasStringFromJson( Object data , String key ){
		ArrayList<String> tmp = new ArrayList<String>();
		JSONArray value 	= XMLTools.selectObjects( data , key );
		for (int i = 0; i < value.size(); i++) {
			JSONValue jsValue 	= value.get(i);
			//System.out.println( jsValue.toString()  );
			tmp.add(  jsValue.toString() );
		}
		return tmp;
	}
	
	public native void hideFilterImg() /*-{
    	$wnd.isc.ListGrid.changeDefaults("filterEditorDefaults", {filterImg:"images/gray.gif"});
    	$wnd.isc.RecordEditor.changeDefaults("actionButtonDefaults", {title:""});
	}-*/;
	
	
	/**
	 * Hack to force JAVASCRIPT To download a page.
	 * @param Url
	 * @param Varname
	 * @param myFileContents
	 */
	public native static  void downloadFile(String Url , String  Varname, String myFileContents)/*-{
        var iframe;
        iframe = document.getElementById("download_container");
        if (iframe === null)
        {
            iframe = document.createElement('iframe');  
            iframe.name = "download_container";
            iframe.style.visibility = 'hidden';
            document.body.appendChild(iframe);
        }
        var form;
        form = document.getElementById("download_container_form");
        if (form === null)
        {
            form = document.createElement('form');  
            form.id = "download_container_form";
            form.method = 'POST';
            form.action = Url;
            form.target = "download_container";
            form.style.visibility = 'hidden';
            document.body.appendChild(form);
        }
        if (form){
        	var data;
		    data = document.createElement("input"); 
		    data.type = 'hidden';
		    data.name = Varname;
		    data.value = myFileContents;
            form.appendChild(data);
        	document.getElementById('download_container_form').submit();
        }
       }-*/;

		
	public static ArrayList<String> GetMapFromJson( Object data , String key ){		
		
		String ViewState = "[{\"totals_all\": [{\"clicks\":3596586,\"impressions\":49241280}]   , \"unallocated_account\":\"ab\"}]";
		JavaScriptObject jsObj = JSON.decode( ViewState );	

		//Use False Strict Quoting to avoid quotes.
		JSONEncoder settings = new JSONEncoder();
		settings.setStrictQuoting(false);
		settings.setPrettyPrint(false);
		settings.setSkipInternalProperties(false);
				
		//Ugly way to construc new state , NOTICE: needs strict JSON format 
		String ViewState2 = "("+JSON.encode( jsObj , settings )+")" ;
	
		//System.out.println( ViewState2  );
		/*
		JSONArray value1 	= XMLTools.selectObjects( data , key );
		JSONValue jsValue2 	= value1.get(0);
		JSONValue	 	jsonV 	= JSONParser.parseStrict( "([{totals_all:[{clicks:3596586,impressions:49241280}],unallocated_account:\"ab\"}])" );
		//JSONValue	 	jsonV 	= JSONParser.parseLenient("[{\"totals_all\": [{\"clicks\":3596586,\"impressions\":49241280}]   , \"unallocated_account\":\"ab\"}]");
		//JSONValue	 	jsonV 	= JSONParser.parseLenient("["+jsValue2+"]");
	    JSONArray jsonArr = JSONParser.parseStrict( "totals_all:[{clicks:3596586,impressions:49241280}],unallocated_account:2" ).isArray();
*/

	    //  String result="{\"error\": null, \"id\": 1, \"result\": [{\"name\": \"\u0420\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0443\u044e\u0442\"}, {\"name\": \"\u0418\u0441\u0442\u043e\u0440\u0438\u044f \u043e\u0446\u0435\u043d\u043e\u043a\"}]}";
	      String result=  "([{result:[{clicks:3596586,impressions:49241280}],unallocated_account:\"ab\"}])";
	      JSONValue value = JSONParser.parseLenient(result).isObject().get("result");
	      JSONArray catsAry = value.isArray();
	      JavaScriptObject jsObj2 = catsAry.getJavaScriptObject();
	      Record[] recs = Record.convertToRecordArray(jsObj2);
	      
	      //System.out.println( recs.length   );

		return null;			
	}	

	public static  String getStringFromJson( Object data , String key ){
		System.out.println(key);
		JSONArray value 	= XMLTools.selectObjects( data , key );
		JSONValue jsValue 	= value.get(0);
		return jsValue.toString().replace("\"", "");
	}
	
	public static JSONArray getArrayFromJson( Object data , String key ){
		JSONArray value 	= XMLTools.selectObjects( data , key );
		return value ;
	}
	
	
	
	public static String getLocalPrefference( String id, Object type ){
		Object var =  Offline.get( id  );
		if( var == null ){
			if (type.equals("bool")) 	return "false";
			if (type.equals("int")) 	return "0" ;
			return "false";
		}
		else 
			if (type.equals("bool")) 	return var+"";
			if (type.equals("int")) 	return var+"";
			return  var+""; 
	}

	public static  Boolean getLocalBooleanPrefference( String id ){
		return ( (Utilities.getLocalPrefference( id , "bool").equals("true" ) ) ? true : false );
	}

	public static  int getLocalIntPrefference( String id ){
		return   Integer.parseInt( Utilities.getLocalPrefference( id , "int") );
	}

	public static  Boolean ifExistsPrefference( String id ){
		Object var =  Offline.get( id  );
		if( var == null )
			return false;
		else
			return true;
	}
	
	public static native void executeJS(String js) /*-{
		$wnd.alert("os: "+js);
	}-*/;

	public static native void changePage(String url) /*-{
		$doc.location.href = url;
	}-*/;
	
	/*
	 * Convert a string to decimal formating
	 */
	public static native String FormattoUSStringZero(String val) /*-{
 		return  $wnd.isc.Format.toUSString( parseFloat( val ) );
	}-*/;
	public static native String FormattoUSString(String val) /*-{
 		return  $wnd.isc.Format.toUSString( parseFloat( val ),2 );
	}-*/;
	public static native String FormattoUSString(String val, Integer pos ) /*-{
	 	return  $wnd.isc.Format.toUSString( parseFloat( val ), pos  );
	}-*/;

	
	public static String substring(String string, int i) {
		if (string.isEmpty()==false){
			return string.substring(i);
		}
		return string;
	}

	public static native void changeDictonaryValue( String Dictonary, String Key, String Value)	 /*-{
		Dictonary.Key = Value;
	}-*/;
	
	/*
	 * Debbunging with firebug
	 */
	public static native void log(Object f) /*-{
		console.log(f);
	}-*/;
	
    /**
     * Get the value for some cell with formatters applied. 
     * @param Grid object
     * @param record the cell's record object
     * @param rowNum rowNum for the cell
     * @param colNum colNum for the cell
     *
     * @return Cell value with formatters applied
     */
	public native static String getFormattedValue (JavaScriptObject grid, Record record, int rowNum, int colNum) /*-{
        return grid.getCellValue(record.@com.smartgwt.client.core.DataClass::getJsObj()(), rowNum, colNum);
	}-*/;

	/*
	 * bug 8804 -  fix summary row bug
	 */
	public native static void fix8804()/*-{					
		$wnd.$(document).ready(function() {
			$wnd.$("[eventproxy$=_summaryRow_bodyLayout]").css("pointer-events","none");	
		});
	}-*/;

	public native static void hideExternalDiv(String div ) /*-{
		$wnd.document.getElementById(div).style.display="none";		
	}-*/;
	
	public native static void showExternalDiv(String div ) /*-{
	$wnd.document.getElementById(div).style.display="";		
	}-*/;

	private native int fetchScrollHeight(JavaScriptObject obj) /*-{
		return obj.body.getScrollHeight();
	}-*/;
	
	public native static int fetchScrollTop(JavaScriptObject obj) /*-{
		return obj.body.scrollTop;
	}-*/;

	//Just for the supergrid
	public native static void refreshIndicator(int totalRecords) /*-{	
		$wnd.refreshIndicator(  totalRecords );
	}-*/;
	
}

 