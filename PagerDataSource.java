package com.pvgwt.widgets.client.utils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSDataFormat;

public class PagerDataSource extends RestDataSource {

	private static int Action = 0;
	private static int TotalRows = 0;
	private static int TotalPages = 0;
	private static int CurrentPage = 1;
	private static int Pagesize = 300;
	private static int StartRow = 0;
	private static int EndRow = 0;
	private static int CurrentRows = 0;
	private static String SumarizationRow;
	private static com.google.gwt.json.client.JSONObject SumarizationRow_JSN;

	public int getTotalRecors() {
		return TotalRows;
	}

	public int getTotalPages() {
		return TotalPages;
	}

	public int getCurrentPage() {
		return CurrentPage;
	}

	public void setCurrentPage(int val) {
		CurrentPage = val;
	}

	public int getPagesize() {
		return Pagesize;
	}

	public void setPagesize(int val) {
		Pagesize = val;
	}

	public int getStartRow() {
		return StartRow;
	}

	public int getEndRow() {
		return EndRow;
	}

	public int getCurrentRecors() {
		return CurrentRows;
	}

	public String getSumarizationRow(String Key, String Value) {
		if (SumarizationRow_JSN == null) {
			SumarizationRow_JSN = JSONParser.parseLenient(SumarizationRow)
					.isObject();
		}
		//System.out.println("proccesed:" + Key + ":" + Value);
		JSONValue a = SumarizationRow_JSN.get(Key);
		JSONValue b = a.isObject().get(Value);
		return b.toString();
	}

	public PagerDataSource(String Url) {
		super();
		setDataURL(Url);
		setShowPrompt(true);
		setDataFormat(DSDataFormat.JSON);
		//setRecordXPath("/records");
		setClientOnly(false);
		setUseHttpProxy(false);
		setDisableQueuing(true);
	}

	@Override
	protected void transformResponse(DSResponse response, DSRequest request,
			Object data) {
		super.transformResponse(response, request, data);
		Action = Integer.parseInt(Utilities.getStringFromJson(data, "/response/action"));
		if (Action == 0) { // Action=0 means is a normal fetch , not a upd/del/add
			TotalRows = Integer.parseInt(Utilities.getStringFromJson(data,"/response/totalRows"));
			TotalPages = Integer.parseInt(Utilities.getStringFromJson(data,	"/response/totalPages"));
			CurrentPage = Integer.parseInt(Utilities.getStringFromJson(data,"/response/page"));
			StartRow = Integer.parseInt(Utilities.getStringFromJson(data,"/response/startRow"));
			EndRow = Integer.parseInt(Utilities.getStringFromJson(data,	"/response/endRow"));
			CurrentRows = Integer.parseInt(Utilities.getStringFromJson(data,"/response/currentRows"));
		}
		//response.setTotalRows(CurrentRows);
		
		int status = Integer.parseInt(Utilities.getStringFromJson(data,"/response/status"));
		if (status < 0 ){
            response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);  
            JSONArray errors = XMLTools.selectObjects(data, "/response/errors");  
            response.setErrors(errors.getJavaScriptObject());  
        }  

		//System.out.println(TotalRows);
		//System.out.println(TotalPages);
		//System.out.println(CurrentPage);
		//System.out.println(StartRow);
        
		// Set global errors
		//int status = Integer.parseInt(Utilities.getStringFromJson(data,"/response/status"));
		//if (status < 0 ){
		//	response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
			//JSONArray errors = XMLTools.selectObjects(data, "/response/errors");
			//response.setErrors(errors.getJavaScriptObject());			
		//}
		/*JSONArray value = XMLTools.selectObjects(data, "/response/status");
		String status = ((JSONString) value.get(0)).stringValue();
		if (!status.equals("1")) {
			response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
			JSONArray errors = XMLTools.selectObjects(data, "/response/errors");
			response.setErrors(errors.getJavaScriptObject());
		}*/

	}

}