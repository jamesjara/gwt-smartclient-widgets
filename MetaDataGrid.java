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
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Hilite;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;

/*
 * Intelligent seudo Class Grid for sumarization.
 * Used to generate a Grid based on metadata send it by the Backend.
 */
public class MetaDataGrid {

	protected  boolean BindGrid = true;
	private Criteria Criteria_Sum = new Criteria();
	private PagerDataSource DynamicDS;
	private PagerGrid ListGrid = new PagerGrid();
	private Hilite[] default_hilites;
	private ListGridField[] ListGridFields;
	private String Url;
	private String id;
	private VLayout bmainLayout;
	Map<String, String> params_metadata = new HashMap<String, String>();
	private String CookieId = "";

	public MetaDataGrid(String id, String Url, VLayout bmainLayout) {
		this.id =  id;
		this.Url = Url + Utilities.getUrl();
		this.bmainLayout = bmainLayout;
	}

	public void Init() {
		//System.out.println("1. Creating EMPTY PagerDataSource ");
		DynamicDS = new PagerDataSource(Url);
		DynamicDS.setID("DynamicDS_" + this.id);

		// Add loading to layout
		//bmainLayout.setContents(" Loading MetaData: " + (new java.util.Date()).getTime());
		bmainLayout.setContents(  namespaces.SavingGif  );
		
		//System.out.println("2.Loading MetaData  ");

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
				bmainLayout.setContents("<br><br><br><b>Loading... please wait.</b> <br> If page doesn't load correctly, please click here <a href='#' onClick='window.location.reload();return false;'>Reload</a> ");
				//System.out.println(" 4.MetaData Loaded , Generating Grid  ");
				
				ListGrid.setDataSource(DynamicDS);
				ListGrid.setID(id);				
				ListGrid.setBindGrid(BindGrid);
				
				//Persist cookis at lglobal key
				//System.out.println( " setViewStateCookieId - " + CookieId);
				ListGrid.setCookieId( CookieId );
							
				
				// Adds sumarization
				SumarizationDataSource SumarizationStore = new SumarizationDataSource();
				SumarizationStore.setDataURL(Url);
				SumarizationStore.setShowPrompt(false);
				SumarizationStore.setDataFormat(DSDataFormat.JSON);
				SumarizationStore.setRecordXPath("SumarizationRow");
				//SumarizationStore.setClientOnly(true);
				SumarizationStore.setUseHttpProxy(false);
				SumarizationStore.setCriteriaMap(Criteria_Sum.getValues());
				SumarizationStore.setCacheAllData(true);				
				ListGrid.setSummaryRowDataSource((SumarizationDataSource)SumarizationStore);
				//ListGrid.setSummaryRowHeight(40);	 //Bug 9181 - @jamesjara - Hide subtotal if there is only one page.			
				ListGrid.setSummaryDs(SumarizationStore);
				
					
				// Add Hilites
				if (default_hilites != null) {
					ListGrid.setHilites(default_hilites);
				}
				
				// Add Fields
				if (ListGridFields != null) {					
					ListGrid.setFields(ListGridFields);
				}
				
				bmainLayout.addMember(ListGrid);
				
				// Fetch data
				ListGrid.PagerfetchData(false);

			}
		}, requestProperties);
	}

	
	public PagerGrid getGrid() {
		return ListGrid;
	}

	/*
	 * Add params to Request URL
	 */
	public void addRequiredVars(String Key, String Val) {
		Criteria_Sum.addCriteria(Key, Val);
		params_metadata.put(Key, Val);
		ListGrid.addCriteria(Key, Val);
		ListGrid.getPreferencesPlugin().addRequiredVars(Key, Val);
	}

	public void setHilites(Hilite[] default_hilites) {
		this.default_hilites = default_hilites;
	}

	public void setFields(ListGridField... fields) {
		this.ListGridFields = fields;

	}
	
	public void setReadOnly(boolean isReadOnly) {
		ListGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE);
	}

	public void setGlobalCookie( String ViewStateId) {
		this.CookieId = ViewStateId;	
	}

	public String getGlobalCookie( ) {
		return this.CookieId;	
	}
	
	public void setBindGrid(boolean bindGrid) {		
		BindGrid = bindGrid;
		ListGrid.setBindGrid(BindGrid);
	}


}
