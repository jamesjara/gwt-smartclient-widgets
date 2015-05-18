package com.pvgwt.widgets.client.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Hilite;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaDataGridSimple {

	private DataSource DynamicDS;
	private ListGrid ListGrid = new ListGrid();
	private ListGridField[] ListGridFields;
	private String Url;
	private String id;
	private VLayout bmainLayout;
	Map<String, String> params_metadata = new HashMap<String, String>();

	public MetaDataGridSimple(String id, String Url, VLayout bmainLayout) {
		this.id =  id;
		this.Url = Url + Utilities.getUrl();
		this.bmainLayout = bmainLayout;
	}

	public void Init() {
		DynamicDS = new PagerDataSource(Url);
		DynamicDS.setID("DynamicDSS_" + this.id);
		bmainLayout.setContents(  namespaces.SavingGif  );

		// Request Metadata
		DataSource MetadataStore = new DataSource();
		MetadataStore.setDataURL(Url);
		MetadataStore.setShowPrompt(false);
		MetadataStore.setDataFormat(DSDataFormat.JSON);
		MetadataStore.setRecordXPath("");
		MetadataStore.setClientOnly(false);
		MetadataStore.setUseHttpProxy(false);
		DSRequest requestProperties = new DSRequest();
		params_metadata.put("ws", "true");
		params_metadata.put("metadata", "true");
		requestProperties.setParams(params_metadata);
		
		ListGrid.setShowHeaderContextMenu(false);
		ListGrid.setCanReorderFields(false);
		
		//Call backend data for Grid generation.
		MetadataStore.fetchData(null, new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData,DSRequest request) {

				// Create Dynamic datasource
				//System.out.println(" 3.A fetching Data MetadataStore Columns ");
				ArrayList<String> columnas = Utilities.GetKeyasStringFromJson(rawData, "/metadata");
				for (String columna : columnas) {
					JavaScriptObject object = JSON.decode(columna.toString());
					SpecialDataSourceField Field = new SpecialDataSourceField(object);
					DynamicDS.addField(Field);
				}

				// Overwrite loading to layout with new Grid
				bmainLayout.setContents(" MetaData Loaded , Generating Grid ");

				ListGrid.setDataSource(DynamicDS);
				ListGrid.setID(id);			

				// Add Fields
				if (ListGridFields != null) {					
					ListGrid.setFields(ListGridFields);
				}

				bmainLayout.addMember(ListGrid);

				// Fetch data
				ListGrid.fetchData();

			}
		}, requestProperties);
	}

	public ListGrid getGrid() {
		return ListGrid;
	}


	public void setFields(ListGridField... fields) {
		this.ListGridFields = fields;

	}
	
	public void setReadOnly(boolean isReadOnly) {
		ListGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE);
	}

}
