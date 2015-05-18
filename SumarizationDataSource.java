package com.pvgwt.widgets.client.utils;

import java.util.Map;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DataSource;

public class SumarizationDataSource extends DataSource {
 

	private Map<String, String> Criteria;

	public SumarizationDataSource() {
		super();
	}
	
	@Override
	public void setDataURL(String dataURL) throws IllegalStateException {
		super.setDataURL(dataURL);
	}
	
	@Override
	protected Object transformRequest(DSRequest dsRequest) {		
		//fix to set DS as dynamic DS
		this.Criteria.put("metadata", 	"false");
		this.Criteria.put("ws", 		"true");
		this.Criteria.put("sumary", 	"true");
		dsRequest.setParams(this.Criteria);	
		return super.transformRequest(dsRequest);
	}

	
	public void setCriteriaMap(Map<String, String> params) {
		this.Criteria = params;
	}

}