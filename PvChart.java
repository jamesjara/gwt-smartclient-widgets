package com.pvgwt.widgets.client.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.Offline;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawHandler;

public class PvChart extends HTMLPane {

	private String graphs_js	= "";
	private String fullDate		= "";
	private String nidList		= "";
	private String cidList		= "";
	private String ColsList		= "";
	private String spanDays		= "";
	Map<String,String> columns = new HashMap<String,String>();

	public PvChart(String id) {
		setID( id );
		setShowEdges(true);
		setContentsType(ContentsType.PAGE);
		setScrollbarSize(0);
		setOverflow(Overflow.HIDDEN);		
		
		//Load value from cookie
		final String IdFromCookie = (String) Offline.get( getID() );
		
		if ( Utilities.ifExistsPrefference(IdFromCookie)  ){
				setContentsURL(IdFromCookie);
		} else {
				//refresh();
		}
			
	}

	public void setUrl(String graphs_js) {
		this.graphs_js = graphs_js;		
	}

	public void setDate(String fullDate) {
		this.fullDate = fullDate;	
	}

	public void setNetworkList(String nidList) {
		this.nidList = nidList;				
	}

	public void setClientList(String cidList) {
		this.cidList = cidList;	
	}

	public void setSpanDays(String spanDays) {
		this.spanDays = spanDays;	
	}

	public void setColsList(String ColsList)  {
		this.ColsList = ColsList;	
	}
	
	public void refresh() {
		String ViewState = "";
		
		//Get columns	
		for( Map.Entry<String, String> entry : columns.entrySet() ){
			ViewState =  ViewState + entry.getKey() +"~|~"+ entry.getValue() + "~||~";
		}
		
		String dataUrl =  graphs_js 	+
				"?v=57&" 				+
				"fullDate=" + fullDate 	+
				"&nidList=" + nidList 	+ 
				"&cidList=" + cidList	+ 
				"&spanDays="+ spanDays 	+ 
				"&colList=" + ColsList+ 
				"&viewState=" + ViewState.replace("#", ";;NUM;;");

		setContentsURL(dataUrl);
		Offline.put( getID() , dataUrl );
	}

	public void addColumn(String key, String value, Integer integer) {
		columns.put(key, value+";;OR;;"+Integer.toString(integer));		
	}

	public void deleteColumn(String key) {
		columns.remove(key)	;
	}

	public void editColumn(String key, String value, Integer integer) {
		columns.put(key, value+";;OR;;"+Integer.toString(integer));		
	}

	public void addColumns(Map<String, String> chartObjectColumns) {
		columns.clear();
		int x = 0;
		for( Map.Entry<String, String> entry : chartObjectColumns.entrySet()  ){
			addColumn( entry.getKey() , entry.getValue() , x++ );
		}		
	}
}


