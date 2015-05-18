package com.pvgwt.widgets.client.utils;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.RestDataSource;

public class SuperDataSource extends RestDataSource {

	
	@Override
	protected void transformResponse(DSResponse response, DSRequest request, Object data) {

    

		super.transformResponse(response, request, data);

    }
	
	
	
}
