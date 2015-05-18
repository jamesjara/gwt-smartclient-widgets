package com.pvgwt.widgets.client.GridApp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.pvgwt.widgets.client.utils.namespaces;
import com.smartgwt.client.core.Function;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Criterion;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Hilite;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.ListGridComponent;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.State;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.util.Offline;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.drawing.DrawLine;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellSelectionChangedEvent;
import com.smartgwt.client.widgets.grid.events.CellSelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.client.widgets.grid.events.ViewStateChangedEvent;
import com.smartgwt.client.widgets.grid.events.ViewStateChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/*
 * Class to create a Grid for Campaign summary only. 
 */
public class Navigator extends IButton {

	ListGrid NetworksGrid 	= new ListGrid();  
    ListGrid ProductsGrid 	= new ListGrid();  
    ListGrid CampaignsGrid 	= new ListGrid();  
    ListGrid AdgroupsGrid 	= new ListGrid(); 
    ListGrid KeywordsGrid 	= new ListGrid();  
    Criteria filter 	  	= new Criteria();
    Window 	 window 		= new Window();   
    int		 count			= 1;
    boolean	 pushed			= false;
    IButton	 IButton		= null;
    HLayout TopContent 		= new HLayout();
    HLayout BottomContent 	= new HLayout();
    HLayout MainContent 	= new HLayout();

	String rows	= "90000";
	public Map<String, String> filters =  new HashMap<String, String>();
	private Store store		= new Store() ;	

	IButton NetworksGridIbutton =  getButton( "All Networks");
	IButton ProductsGridIbutton =  getButton( "All Products");
	IButton CampaignsGridIbutton = getButton( "All Campaigns");
	IButton AdgroupsGridIbutton =  getButton( "All Adgroups");
	IButton Select 	= new IButton("Select");
	
	public String getParam(String key){
		return store.getParam(key);
	}	
	
	public DataSource NetworksGridStore(){
		DataSource MetadataStore = new DataSource();
		MetadataStore.setDataURL( "/ajax/navigator/get_networks.php?" + "&sc=true"); 
		MetadataStore.setShowPrompt(false);
		MetadataStore.setDataFormat(DSDataFormat.JSON);
		MetadataStore.setRecordXPath("/rows");
		MetadataStore.setUseHttpProxy(false);	
		return MetadataStore;
	}
	
	public DataSource ProductsGridStore(){
		DataSource MetadataStore = new DataSource();
		MetadataStore.setDataURL( "/webservice/navigator/product_viewer.ctl.php"); 
		MetadataStore.setShowPrompt(false);
		MetadataStore.setDataFormat(DSDataFormat.JSON);
		MetadataStore.setRecordXPath("/rows");
		MetadataStore.setUseHttpProxy(false);		
		return MetadataStore;
	}

	public DataSource CampaignsGridStore(){
		DataSource MetadataStore = new DataSource();
		MetadataStore.setDataURL( "/webservice/navigator/campaign_viewer.ctl.php"); 
		MetadataStore.setShowPrompt(false);
		MetadataStore.setDataFormat(DSDataFormat.JSON);
		MetadataStore.setRecordXPath("/rows");
		MetadataStore.setUseHttpProxy(false);		
		return MetadataStore;
	}
	
	public DataSource AdgroupsGridStore(){
		DataSource MetadataStore = new DataSource();
		MetadataStore.setDataURL( "/webservice/navigator/adgroup_viewer.ctl.php"); 
		MetadataStore.setShowPrompt(false);
		MetadataStore.setDataFormat(DSDataFormat.JSON);
		MetadataStore.setRecordXPath("/rows");
		MetadataStore.setUseHttpProxy(false);		
		return MetadataStore;
	}
	
	public DataSource KeywordsGridStore(){
		DataSource MetadataStore = new DataSource();
		MetadataStore.setDataURL( "/webservice/navigator/keyword_viewer.ctl.php");
		MetadataStore.setShowPrompt(false);
		MetadataStore.setDataFormat(DSDataFormat.JSON);
		MetadataStore.setRecordXPath("/rows");
		MetadataStore.setUseHttpProxy(false);		
		return MetadataStore;
	}
	
	public Navigator( Store Store ) {	
		 super();
		 this.IButton = this;
		 this.store = Store;
		 
		
		 setAutoWidth();
		 setOverflow(Overflow.VISIBLE);		 
		 setBaseStyle("navigatorBtn");
		 setMargin(0);
		 setPadding(0);
		 setTop(0); 	
		 
		//window
        window.setTitle("Shortcut Navigator");  
        window.setShowHeader(false);
        window.setShowTitle(false);
        window.setWidth("70%");
        window.setHeight(300);   
        window.setStyleName("NavigatorBK");
        window.setBorder("0px");
        window.setShowShadow(true);
        window.setShadowSoftness(5);  
        window.setShadowOffset(5) ;
        window.setBodyStyle("odyStyle");
  
        window.setCanDragReposition(false);  
        window.setCanDragResize(false);  
        window.setAnimateMinimize(false); 
        window.setShowCloseButton(true);
        window.setShowMinimizeButton(false);   
        
        
		//Common fields.	
		DataSourceField idField 	= new DataSourceField("id" 		, FieldType.INTEGER); 
		idField.setPrimaryKey(true);
		DataSourceTextField nameField = new DataSourceTextField("name", "name", 128); 
		DataSourceField cidField 	= new DataSourceField("cid" 	, FieldType.INTEGER); 
		DataSourceField nidField 	= new DataSourceField("nid" 	, FieldType.INTEGER);  
		DataSourceField pidField 	= new DataSourceField("pid" 	, FieldType.INTEGER);  
		DataSourceField cpid 		= new DataSourceField("cpid" 	, FieldType.INTEGER);  
		DataSourceField aid 		= new DataSourceField("aid" 	, FieldType.INTEGER); 
		DataSourceField status 		= new DataSourceField("status" 	, FieldType.ANY);  
		//Field for the networks grid
		DataSourceField network_id			= new DataSourceField("network_id" 	, FieldType.INTEGER);  
		DataSourceTextField network_name 	= new DataSourceTextField("network_name", "Network", 128); 
    
		//Datasources.
		DataSource NetworksGridStore =  NetworksGridStore();
		NetworksGridStore.setFields(network_id,network_name);
		
		DataSource ProductsGridStore =  ProductsGridStore();
		ProductsGridStore.setFields(idField,nameField,cidField,nidField,status);   
		
		DataSource CampaignsGridStore =  CampaignsGridStore();
		CampaignsGridStore.setFields(idField,nameField,cidField,nidField,pidField,status);   
		
		DataSource AdgroupsGridStore =  AdgroupsGridStore();
		AdgroupsGridStore.setFields(idField,nameField,cidField,nidField,pidField,cpid,status);   
		
		DataSource KeywordsGridStore =  KeywordsGridStore();
		KeywordsGridStore.setFields(idField,nameField,cidField,nidField,pidField,cpid,aid,status);     
	
		
        //Grids.
		NetworksGrid.setBaseStyle("navigatorGrid");
		NetworksGrid.setStyleName("navigatorGridElement");
		NetworksGrid.setLeaveScrollbarGap(false);
		NetworksGrid.setShowHeader(false);
        NetworksGrid.setShowAllRecords(true);  
        NetworksGrid.setSelectionType(SelectionStyle.SIMPLE);  
        NetworksGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE); 
        NetworksGrid.setHeight100();  
        NetworksGrid.setDataSource(NetworksGridStore);  
        NetworksGrid.setAutoFetchData(true); 
        NetworksGrid.setAlternateRecordStyles(false);
		ListGridField NetworksGridField = new ListGridField();
		NetworksGridField.setTitle("Network");
		NetworksGridField.setName("network_name");
		NetworksGrid.setFields( NetworksGridField );		
		NetworksGrid.setGridComponents(new Object[] {  
                getGridToolbar( NetworksGrid ,  NetworksGridIbutton ),
                ListGridComponent.FILTER_EDITOR,   
                ListGridComponent.BODY  
        }); 
		NetworksGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				NetworksGridIbutton.setState(State.STATE_UP);
				NetworksGridIbutton.setBaseStyle("navigatorGridHeader");	
				NetworksGridIbutton.setSelected(false);				
				
				Record[] records = NetworksGrid.getSelectedRecords();
				if( records.length==0 ){
					
					//Get all networks
					String nid = ""; boolean firsttime = true;
					for( ListGridRecord listGridRecord : NetworksGrid.getRecords() ) {			
						nid  =  nid + ((firsttime==true) ? "" : "," )+ listGridRecord.getAttributeAsString("network_id");
						firsttime = false;
					}	
					
					store.setNetworkId( nid );
					store.setNetworkName(" All Networks");
					refreshTitle();
					NetworksGridIbutton.setSelected(true);
					resetLevels(0);
				} else {
					String nid = ""; boolean firsttime = true;
					for( ListGridRecord listGridRecord : NetworksGrid.getSelectedRecords() ) {			
						nid  =  nid + ((firsttime==true) ? "" : "," )+ listGridRecord.getAttributeAsString("network_id");
						firsttime = false;
					}	
					
					if( records.length> 1 ){
						store.setNetworkId(nid);
						store.setNetworkName( records.length + " Networks");
					} else {
						Record record = NetworksGrid.getSelectedRecord();
						store.setNetworkId(record.getAttributeAsString("network_id"));
						store.setNetworkName(record.getAttributeAsString("network_name"));						
					}			

					refreshTitle();
				} 
				refreshData();	
			}
		});
		NetworksGridIbutton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				
				Record[] records = NetworksGrid.getSelectedRecords();
				if( records.length==0 )
					NetworksGridIbutton.setSelected(true);
				
				//Get all networks
				String nid = ""; boolean firsttime = true;
				for( ListGridRecord listGridRecord : NetworksGrid.getRecords() ) {			
					nid  =  nid + ((firsttime==true) ? "" : "," )+ listGridRecord.getAttributeAsString("network_id");
					firsttime = false;
				}	
				
				store.setNetworkId(nid);
				store.setNetworkName(" All Networks");
				refreshTitle();
				NetworksGrid.deselectAllRecords();
				resetLevels(0);
				refreshData();	
			}
		});

		ProductsGrid.setShowAllRecords(false); 
		ProductsGrid.setBaseStyle("navigatorGrid");
		ProductsGrid.setStyleName("navigatorGridElement");
		ProductsGrid.setLeaveScrollbarGap(false);
		ProductsGrid.setShowHeader(false);
		ProductsGrid.setShowAllRecords(true); 
        ProductsGrid.setShowFilterEditor(true);
        ProductsGrid.setWidth("30%");  
        ProductsGrid.setHeight100();  
        ProductsGrid.setDataSource(ProductsGridStore);  
        ProductsGrid.setAutoFetchData(false); 
        ProductsGrid.setSelectionType(SelectionStyle.SIMPLE);  
        ProductsGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE); 
        ProductsGrid.setCanGroupBy(false);
        ProductsGrid.setCanMultiSort(false);
        ProductsGrid.setAlternateRecordStyles(false);
        //ProductsGrid.setHilites(default_hilites);
		ListGridField ProductsGridField = new ListGridField();
		ProductsGridField.setTitle("Product");
		ProductsGridField.setName("name");
		ProductsGridField.setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,int colNum) {
				String name = record.getAttributeAsString("name");
				String cid = record.getAttributeAsString("cid");
				String nid = record.getAttributeAsString("nid");	
				String pid = record.getAttributeAsString("id");	
				
				String icon 	= "<img  width='14'  heigth='14' src='"+namespaces.resources_dir+"/network_"+nid+".png'> ";		
				return icon+ getStatus( record )  +" <span href='/campaign_summary.php?product_id="+pid+"&page=1&pos_c=0&client_id="+cid+"&network_id="+nid+"'>"+name+"</span>";  
				
			}
		});
		ProductsGrid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {			
			@Override
			public void onSelectionUpdated(SelectionUpdatedEvent event) {
				filterById(0, ProductsGrid.getSelectedRecords() ,false );	
				filterById(1, CampaignsGrid.getSelectedRecords() ,false  );	
			}
		});
		ProductsGrid.setFields( ProductsGridField );
		ProductsGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				persistCriteria( 0 );
			}
		});
		ProductsGrid.addViewStateChangedHandler(new ViewStateChangedHandler() {			
			@Override
			public void onViewStateChanged(ViewStateChangedEvent event) {
				persistSelection(0);
			}
		});
		ProductsGrid.setGridComponents(new Object[] {  
                getGridToolbar( ProductsGrid ,  ProductsGridIbutton ),
                ListGridComponent.FILTER_EDITOR,   
                ListGridComponent.BODY  
        }); 
		ProductsGrid.addCellClickHandler( new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				ProductsGridIbutton.setState(State.STATE_UP);
				ProductsGridIbutton.setSelected(false);		
			}
		});
		ProductsGridIbutton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) { 
				
				Record[] records = ProductsGrid.getSelectedRecords();
				if( records.length==0 )
					ProductsGridIbutton.setSelected(true);
				
				store.setProductId("-1");
				store.setProductName(" All Products");
				refreshTitle();
				ProductsGrid.deselectAllRecords();
				resetLevels(1);
			}
		});
		ProductsGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				ProductsGridIbutton.setSelected(false);				
				
				Record[] records = ProductsGrid.getSelectedRecords();
				if( records.length==0 ){
					store.setProductId("-1");
					store.setProductName(" All Products");
					refreshTitle();
					ProductsGridIbutton.setSelected(true);
					resetLevels(1);
				} else {
					String ids = ""; boolean firsttime = true;
					for( ListGridRecord listGridRecord : ProductsGrid.getSelectedRecords() ) {			
						ids  =  ids + ((firsttime==true) ? "" : "," )+ listGridRecord.getAttributeAsString("id");
						firsttime = false;
					}
					
					if( records.length > 1 ){
						store.setProductId(ids);
						store.setProductName( records.length + " Products");
					} else {
						Record record = ProductsGrid.getSelectedRecord();
						store.setProductId(record.getAttributeAsString("id"));
						store.setProductName(record.getAttributeAsString("name"));						
					}		

					refreshTitle();
				} 				
			}
		});


		CampaignsGrid.setShowAllRecords(false); 
		CampaignsGrid.setBaseStyle("navigatorGrid");
		CampaignsGrid.setStyleName("navigatorGridElement");
		CampaignsGrid.setLeaveScrollbarGap(false);
		CampaignsGrid.setShowHeader(false);
		CampaignsGrid.setShowAllRecords(true); 
        CampaignsGrid.setShowFilterEditor(true);
        CampaignsGrid.setWidth("30%");  
        CampaignsGrid.setHeight100();  
        CampaignsGrid.setDataSource(CampaignsGridStore);  
        CampaignsGrid.setAutoFetchData(false); 
        CampaignsGrid.setSelectionType(SelectionStyle.SIMPLE);  
        CampaignsGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE); 
        CampaignsGrid.setCanGroupBy(false);
        CampaignsGrid.setCanMultiSort(false);
        CampaignsGrid.setAlternateRecordStyles(false);
        //CampaignsGrid.setHilites(default_hilites);
		ListGridField CampaignsGridField = new ListGridField();
		CampaignsGridField.setTitle("Product");
		CampaignsGridField.setName("name");
		CampaignsGridField.setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,int colNum) {
				String name = record.getAttributeAsString("name");
				String cid = record.getAttributeAsString("cid");
				String nid = record.getAttributeAsString("nid");	
				String pid = record.getAttributeAsString("id");	
				
				String icon 	= "<img  width='14'  heigth='14' src='"+namespaces.resources_dir+"/network_"+nid+".png'> ";		
				return icon+ getStatus( record )  +" <span href='/campaign_summary.php?product_id="+pid+"&page=1&pos_c=0&client_id="+cid+"&network_id="+nid+"'>"+name+"</span>";  
				
			}
		});
		CampaignsGrid.setFields( CampaignsGridField );
		CampaignsGrid.setGridComponents(new Object[] {  
                getGridToolbar( CampaignsGrid ,  CampaignsGridIbutton ),
                ListGridComponent.FILTER_EDITOR,   
                ListGridComponent.BODY  
        }); 
		CampaignsGrid.addCellClickHandler( new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				CampaignsGridIbutton.setState(State.STATE_UP);
				CampaignsGridIbutton.setSelected(false);		
			}
		});
		CampaignsGridIbutton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {				
				Record[] records = CampaignsGrid.getSelectedRecords();
				if( records.length==0 )
					CampaignsGridIbutton.setSelected(true);
				
				store.setCampaignId("-1");
				store.setCampaignName(" All Campaigns");
				refreshTitle();
				CampaignsGrid.deselectAllRecords();
				resetLevels(2);
			}
		});
		CampaignsGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				CampaignsGridIbutton.setSelected(false);				
				
				Record[] records = CampaignsGrid.getSelectedRecords();
				if( records.length==0 ){
					store.setCampaignId("-1");
					store.setCampaignName(" All Campaigns");
					refreshTitle();
					CampaignsGridIbutton.setSelected(true);
					resetLevels(2);
				} else {
					String ids = ""; boolean firsttime = true;
					for( ListGridRecord listGridRecord : CampaignsGrid.getSelectedRecords() ) {			
						ids  =  ids + ((firsttime==true) ? "" : "," )+ listGridRecord.getAttributeAsString("id");
						firsttime = false;
					}
					
					if( records.length > 1 ){
						store.setCampaignId(ids);
						store.setCampaignName( records.length + " Campaigns");
					} else {
						Record record = CampaignsGrid.getSelectedRecord();
						store.setCampaignId(record.getAttributeAsString("id"));
						store.setCampaignName(record.getAttributeAsString("name"));						
					}	
					refreshTitle();
				} 	
			}
		});
		CampaignsGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				filterById(1, CampaignsGrid.getSelectedRecords() ,false  );	
			}
		});
		CampaignsGrid.setFields( CampaignsGridField  );
		CampaignsGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				persistCriteria( 1 );
			}
		});
		CampaignsGrid.addViewStateChangedHandler(new ViewStateChangedHandler() {			
			@Override
			public void onViewStateChanged(ViewStateChangedEvent event) {
				persistSelection(1);
			}
		});
		CampaignsGrid.addCellClickHandler( new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				CampaignsGridIbutton.setState(State.STATE_UP);				
			}
		});
		CampaignsGridIbutton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				CampaignsGrid.deselectAllRecords();
			}
		});
		

		AdgroupsGrid.setShowAllRecords(false); 
		AdgroupsGrid.setBaseStyle("navigatorGrid");
		AdgroupsGrid.setStyleName("navigatorGridElement");
		AdgroupsGrid.setLeaveScrollbarGap(false);
		AdgroupsGrid.setShowHeader(false);
		AdgroupsGrid.setShowAllRecords(true); 
        AdgroupsGrid.setShowFilterEditor(true);
        AdgroupsGrid.setWidth("30%");  
        AdgroupsGrid.setHeight100();  
        AdgroupsGrid.setDataSource(AdgroupsGridStore);  
        AdgroupsGrid.setAutoFetchData(false); 
        AdgroupsGrid.setSelectionType(SelectionStyle.SIMPLE);  
        AdgroupsGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE); 
        AdgroupsGrid.setCanGroupBy(false);
        AdgroupsGrid.setCanMultiSort(false);
        AdgroupsGrid.setAlternateRecordStyles(false);
        //AdgroupsGrid.setHilites(default_hilites);
		ListGridField AdgroupsGridField = new ListGridField();
		AdgroupsGridField.setTitle("Product");
		AdgroupsGridField.setName("name");
		AdgroupsGridField.setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,int colNum) {
				String name = record.getAttributeAsString("name");
				String cid = record.getAttributeAsString("cid");
				String nid = record.getAttributeAsString("nid");	
				String pid = record.getAttributeAsString("id");	
				
				String icon 	= "<img  width='14'  heigth='14' src='"+namespaces.resources_dir+"/network_"+nid+".png'> ";		
				return icon+ getStatus( record )  +" <span href='/campaign_summary.php?product_id="+pid+"&page=1&pos_c=0&client_id="+cid+"&network_id="+nid+"'>"+name+"</span>";  
				
			}
		});
		AdgroupsGrid.setFields( AdgroupsGridField );
		AdgroupsGrid.setGridComponents(new Object[] {  
                getGridToolbar( AdgroupsGrid ,  AdgroupsGridIbutton ),
                ListGridComponent.FILTER_EDITOR,   
                ListGridComponent.BODY  
        }); 
		AdgroupsGrid.addCellClickHandler( new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				AdgroupsGridIbutton.setState(State.STATE_UP);
				AdgroupsGridIbutton.setSelected(false);		
			}
		});
		AdgroupsGridIbutton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Record[] records = AdgroupsGrid.getSelectedRecords();
				if( records.length==0 )
					AdgroupsGridIbutton.setSelected(true);
				
				store.setAdgroupId("-1");
				store.setAdgroupName(" All Adgroups");
				refreshTitle();
				AdgroupsGrid.deselectAllRecords();
			}
		});
		AdgroupsGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				AdgroupsGridIbutton.setSelected(false);				
				
				Record[] records = AdgroupsGrid.getSelectedRecords();
				if( records.length==0 ){
					store.setAdgroupId("-1");
					store.setAdgroupName(" All Adgroups");
					refreshTitle();
					AdgroupsGridIbutton.setSelected(true);
				} else {
					String ids = ""; boolean firsttime = true;
					for( ListGridRecord listGridRecord : AdgroupsGrid.getSelectedRecords() ) {			
						ids  =  ids + ((firsttime==true) ? "" : "," )+ listGridRecord.getAttributeAsString("id");
						firsttime = false;
					}
					
					if( records.length > 1 ){
						store.setAdgroupId(ids);
						store.setAdgroupName( records.length + " Adgroups");
					} else {
						Record record = AdgroupsGrid.getSelectedRecord();
						store.setAdgroupId(record.getAttributeAsString("id"));
						store.setAdgroupName(record.getAttributeAsString("name"));						
					}	
					refreshTitle();
				} 	
			}
		});
		AdgroupsGrid.setFields( AdgroupsGridField  );
		AdgroupsGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				persistCriteria( 2 );
			}
		}); 
		AdgroupsGrid.addCellClickHandler( new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				AdgroupsGridIbutton.setState(State.STATE_UP);				
			}
		});
		AdgroupsGridIbutton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				AdgroupsGrid.deselectAllRecords();
			}
		});

		        
        //MainContent.setBackgroundColor("green");       
        MainContent.setStyleName("NavigatorMainContentBK");
        MainContent.setShowEdges(false);  
        MainContent.setHeight100();  
        MainContent.setMembersMargin(0);  
        MainContent.setLayoutMargin(15);  
        MainContent.addMember(NetworksGrid);  
        MainContent.addMember(ProductsGrid);
        MainContent.addMember(CampaignsGrid);
        MainContent.addMember(AdgroupsGrid);          

		TopContent.setBackgroundColor("#898989");  //setBackgroundColor("#f2f2f2");  
		TopContent.setHeight(1);
		TopContent.setAlign(Alignment.RIGHT);
                
        BottomContent.setStyleName("NavigatorBottomContentBK");
        //BottomContent.setBackgroundColor("yellow");
        BottomContent.setShowEdges(false);  
        BottomContent.setHeight(30);  
        BottomContent.setMembersMargin(5);  
        BottomContent.setLayoutMargin(5);  
        BottomContent.setAlign(VerticalAlignment.CENTER);
        BottomContent.setAlign(Alignment.RIGHT);

        IButton Cancel 	= new IButton("Cancel");
        Cancel.setBaseStyle("btn");
        Cancel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				hideNavigator();				
			}
		});
        IButton Reset 	= new IButton("Reset");
        Reset.setBaseStyle("btn");
        Reset.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				clearFilters();
			}
		});
        Select.setBaseStyle("btnSelect");
        Select.setHeight(30);
        BottomContent.addMember(Cancel);
        BottomContent.addMember(Reset);
        BottomContent.addMember(Select);
        
        Select.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				hideNavigator();
			}
		});

        window.addItem(TopContent); 
        window.addItem(MainContent); 
        window.addItem(BottomContent);   
        
        this.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if ( pushed == false){
					pushed = true;
					showNavigator();
				} else {
					pushed = false;
					hideNavigator();
				}
			}
		});  
        
		
        init();
	}
	
	public void init( ) {
		resetConfigs();
		refreshTitle();
		
		NetworksGrid.addDataArrivedHandler(new DataArrivedHandler() {			
			@Override
			public void onDataArrived(DataArrivedEvent event) {						
				//Mark current values.
				//Record record = NetworksGrid.getRecordList().find("network_id", getParam("nid"));
				//NetworksGrid.selectSingleRecord(record);
			}
		});		    
	}
	

	public void resetLevels(  int Level ){
		if(Level==0){ //Products
			store.setProductId("-1");
			store.setProductName(" All Products");			
			resetLevels(1);	
			resetLevels(2);
			refreshTitle();
			ProductsGrid.deselectAllRecords();
			ProductsGridIbutton.setSelected(true);
			
		} else if(Level==1){ //campaing c	
			store.setCampaignId("-1");
			store.setCampaignName(" All Campaigns");
			resetLevels(2);
			refreshTitle();
			CampaignsGrid.deselectAllRecords();
			CampaignsGridIbutton.setSelected(true);
		}else if(Level==2){ //adgroups	
			store.setAdgroupId("-1");
			store.setAdgroupName(" All Adgroups");	
			refreshTitle();	
			AdgroupsGrid.deselectAllRecords();
			AdgroupsGridIbutton.setSelected(true);
		}		
	}
	
	public void showNavigator( ){
		window.setTop( 	this.getAbsoluteTop() + this.getHeight()  );
		window.setLeft(	this.getAbsoluteLeft() );		
		window.animateShow(AnimationEffect.SLIDE);
		IButton.setBaseStyle("navigatorBtnPushed");
		rePaintWindow();		
	}
	public void hideNavigator( ){ 
		window.animateHide(AnimationEffect.SLIDE);
		IButton.setBaseStyle("navigatorBtn");
	}
	
	public void  rePaintWindow(){
		System.out.println( IButton.getOffsetWidth() );
		int ibuttonW	=	IButton.getOffsetWidth();
		int windowW		=	window.getOffsetWidth();
		int borderW		= 	windowW - ibuttonW;
		//TopContentA.setWidth( borderW - 4 ); 
	    //TopContent.setStyleName("NavigatorTopContentABK");
	    //TopContentA.setContents("<div class='NavigatorTopContentABK' style='width:100%;'></div>");
	    //TopContent.setContents("aaaaaaaaaaaAA");
		TopContent.setWidth( borderW + 1 ); 
		TopContent.setLeft( ibuttonW - 1 );  
		
		window.addChild(TopContent);
		
	}
	

	public void refreshData( ){
		
		//Reset data
		ProductsGrid.setData(new ListGridRecord[0]);
		CampaignsGrid.setData(new ListGridRecord[0]);
		AdgroupsGrid.setData(new ListGridRecord[0]);

		String dateA 	=  store.getDateA();  
		String dateB 	=  store.getDateB();		
		String cid		=  store.getClientId(); 
		
		System.out.println( dateA );
		System.out.println( dateB );
		System.out.println( cid );			
		
		//Get selected networks
		String nid = "";
		nid = store.getNetworkId();
		System.out.println( nid );
		
		
		if( nid.isEmpty() == false ){				

			//Basic filters
			filters.put("q", 		 "1");
			filters.put("network_id", nid);
			filters.put("begin_date", 	dateA);
			filters.put("end_date", 	dateB);
			filters.put("client_id",  cid);
			filters.put("groupbyname","false");
			filters.put("_search",	"false");
			filters.put("rows",		this.rows);
			filters.put("page",		"1");
			filters.put("sord",		"asc");
			filters.put("sc",		"true");	
			filters.put("sidx",	"product");							
			
			ProductsGrid.invalidateCache();	
			CampaignsGrid.invalidateCache();	
			AdgroupsGrid.invalidateCache();	
			
			filter.setAttribute("nid", "");

			DSRequest DSRquest1 = new DSRequest();
			DSRquest1.setParams( filters );
			
			DSRequest DSRquest2 = new DSRequest();
			DSRquest2.setParams( filters );		

			DSRequest DSRquest3 = new DSRequest();
			DSRquest3.setParams( filters );		
			
			//Set filters.
			ProductsGrid.fetchData(null,new DSCallback() {				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {					
					loadCriteria(0);				
					loadSelection(0);					
				}
			},DSRquest1);	
	
			CampaignsGrid.fetchData(null,new DSCallback() {	
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					loadCriteria(1);			
					loadSelection(1);
				}
			},DSRquest2);
			
			AdgroupsGrid.fetchData(null,new DSCallback() {				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					loadCriteria(2);						
				}
			},DSRquest3);
		}
		
	}
	
	public void refreshData2( ){
		
		//Reset data
		ProductsGrid.setData(new ListGridRecord[0]);
		CampaignsGrid.setData(new ListGridRecord[0]);
		AdgroupsGrid.setData(new ListGridRecord[0]);				

		/*
		String dateA 	= getParam("dateA");
		String dateB 	= getParam("dateB");		
		String cid		= getParam("cid"); 
		*/
		String dateA 	= store.getDateA();
		String dateB 	= store.getDateB();	
		String cid		= store.getClientId();

		System.out.println( dateA );
		System.out.println( dateB );
		System.out.println( cid );
		
		
		//Get selected networks
		String nid = ""; boolean firsttime = true;
		for( ListGridRecord listGridRecord : NetworksGrid.getSelectedRecords() ) {			
			nid  =  nid + ((firsttime==true) ? "" : "," )+ listGridRecord.getAttributeAsString("network_id");
			firsttime = false;
		}	
				
		if( nid.isEmpty() == false ){	

			//Basic filters 	
			filters.put("q", 		 "1");
			filters.put("network_id", nid);
			filters.put("begin_date", 	dateA);
			filters.put("end_date", 	dateB);
			filters.put("client_id",  cid);
			filters.put("groupbyname","false");
			filters.put("_search",	"false");
			filters.put("rows",		this.rows);
			filters.put("page",		"1");
			filters.put("sord",		"asc");
			filters.put("sc",		"true");	
			filters.put("sidx",	"product");							
			
			ProductsGrid.invalidateCache();	
			CampaignsGrid.invalidateCache();	
			AdgroupsGrid.invalidateCache();	

			filter.setAttribute("nid", "");

			DSRequest DSRquest1 = new DSRequest();
			DSRquest1.setParams( filters );
			
			DSRequest DSRquest2 = new DSRequest();
			DSRquest2.setParams( filters );		

			DSRequest DSRquest3 = new DSRequest();
			DSRquest3.setParams( filters );				
			//Set filters.
			ProductsGrid.fetchData(null,new DSCallback() {				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {					
					loadCriteria(0);				
					loadSelection(0);					
				}
			},DSRquest1);	
					
			CampaignsGrid.fetchData(null,new DSCallback() {	
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					loadCriteria(1);			
					loadSelection(1);
				}
			},DSRquest2);
			
			AdgroupsGrid.fetchData(null,new DSCallback() {				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					loadCriteria(2);						
				}
			},DSRquest3);	
			
	
		}  		
		
	}	
	
	public void filterById(  int Level, ListGridRecord[] listGridRecords , boolean recall ){
		Criteria criteria2 = new Criteria();
		ArrayList<Integer> tofilter = new ArrayList<Integer>();	
		if(Level==0){ //Product click - filter next level :  campaing	
			for (ListGridRecord listGridRecord : listGridRecords) {
				tofilter.add(listGridRecord.getAttributeAsInt("id"));				
			}	
			Integer ia[] = new Integer[tofilter.size()];
			criteria2.addCriteria( "pid", tofilter.toArray(ia)  );
			
			//Si no hay record filtrar por todo
			if(listGridRecords.length==0)
				criteria2.addCriteria( "pid", "" );			
				
			if ( recall == false)
				CampaignsGrid.filterData( criteria2 );
			else {
				CampaignsGrid.filterData(criteria2, new DSCallback() {					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						loadCriteria(1);			
						loadSelection(1);
					}
				});
			}

		} else if(Level==1){ //campaing click - filter next level :  adgroup		
			for (ListGridRecord listGridRecord : listGridRecords) {
				tofilter.add(listGridRecord.getAttributeAsInt("id"));				
			}	
			Integer ia[] = new Integer[tofilter.size()];
			criteria2.addCriteria( "cpid", tofilter.toArray(ia)  );	

			//Si no hay record filtrar por todo
			if(listGridRecords.length==0)
				criteria2.addCriteria( "cpid", "" );	
			
			AdgroupsGrid.filterData( criteria2 );
		}
	}
	
	public void clearFilters(){		
		resetLevels(0);	
		resetLevels(1);	
		resetLevels(2);	
	}

	public void persistCriteria(int Level){		
		if(Level==0){ //Product click 					
			Offline.put( "nav_p" , JSON.encode( ProductsGrid.getFilterEditorCriteria().getJsObj())	);				
		} else if(Level==1){ //campaing click
			Offline.put( "nav_c" ,  JSON.encode(CampaignsGrid.getFilterEditorCriteria().getJsObj())	);				
		}	else if(Level==2){ //campaing click
			Offline.put( "nav_a" ,  JSON.encode(AdgroupsGrid.getFilterEditorCriteria().getJsObj())	);				
		}		
	}	
	public void persistSelection(int Level){				
		if(Level==0){ //Product click 	
			Offline.put( "navs_p" ,  ProductsGrid.getViewState() );				
		} else if(Level==1){ //campaing click
			Offline.put( "navs_c" ,  CampaignsGrid.getViewState());				
		}		
	}	
	
	public void resetConfigs(){		
		Offline.put( "navs_p" , "");
		Offline.put( "navs_c" , "");
		Offline.put( "nav_a" ,  "");
		Offline.put( "nav_c" ,  "");
		Offline.put( "nav_p" ,  "");

		/*Offline.remove("navs_p" );
		Offline.remove("nav_a" );
		Offline.remove("navs_c" );
		Offline.remove("nav_c" );
		Offline.remove("nav_p" );*/
	}	
	

	public void loadCriteria(int Level){
		if(Level==0){ //Product click 	
			String FromCookie = (String) Offline.get("nav_p");
			if ( FromCookie != null &&  ! FromCookie.isEmpty() ){
				Criteria criteria = new Criteria( JSON.decode(FromCookie)  );
				ProductsGrid.setFilterEditorCriteria(criteria)	;	
				ProductsGrid.filterByEditor();
			}		
		} else if(Level==1){ //campaing click
			String FromCookie = (String) Offline.get("nav_c");
			if ( FromCookie != null &&  ! FromCookie.isEmpty() ){
				Criteria criteria = new Criteria( JSON.decode(FromCookie)  );
				CampaignsGrid.setFilterEditorCriteria(criteria)	;		
				CampaignsGrid.filterByEditor();			
			}			
		}	else if(Level==2){ //adgroup click
			String FromCookie = (String) Offline.get("nav_a");
			if ( FromCookie != null &&  ! FromCookie.isEmpty() ){
				Criteria criteria = new Criteria( JSON.decode(FromCookie)  );
				AdgroupsGrid.setFilterEditorCriteria(criteria)	;		
				AdgroupsGrid.filterByEditor();			
			}			
		}		
	}	

	public void loadSelection(int Level){
		if(Level==0){ //Product click 	
			String FromCookie = (String) Offline.get("navs_p");
			if ( FromCookie != null &&  ! FromCookie.isEmpty() ){
				ProductsGrid.setViewState( FromCookie  );	
			}		
		} else if(Level==1){ //campaing click
			String FromCookie = (String) Offline.get("navs_c");
			if ( FromCookie != null &&  ! FromCookie.isEmpty() ){
				CampaignsGrid.setViewState( FromCookie  );			
			}			
		}		
	}		
	
	public void refreshTitle( ){
		String title = new StringBuilder()
	    	//.append( store.getClientName()		+	" | "	)
		    .append( store.getNetworkName()		+	" | "	)
		    .append( store.getProductName()		+	" | "	)
		    .append( store.getCampaignName()	+	" | "	)
		    .append( store.getAdgroupName())
	    .toString();
		setTitle(title);
		rePaintWindow();
	}
	
	public String getStatus( Record record ){

		String state		= "";
		String stateColor	= "";
		String status 		= "";
		if ( record.getAttributeAsString("status") != null ){
			if( record.getAttributeAsString("status").toLowerCase(). contains("active")){
				state		= "Active";
				stateColor	= "#3cc051";
			} else if( record.getAttributeAsString("status").toLowerCase().contains("paused")){
				state		= "Paused";
				stateColor	= "#fcb322";
				
			} else if( record.getAttributeAsString("status").toLowerCase().contains("deleted")){
				state		= "Deleted";
				stateColor	= "#ed4e2a";			
			}
			status 	= "<span style='color: white;font-size: 12px;padding: 1px 2px 1px 4px;background-color:  "+stateColor+" ;font-weight: 300;background-image: none !important;font-weight: bold;position: absolute;right: 5px;'>"+state+"</span>";
		}		
		return  status;		
	}  

	private IButton getButton( String Title ) {
	    IButton Button = new IButton("Stretch Button");  
	    Button.setWidth100();  
	    Button.setShowRollOver(true);  
	    Button.setShowDisabled(true);  
	    Button.setShowDown(true);  
	    Button.setTitleStyle("stretchTitle");           
	    Button.setTitle(Title);  
	    Button.setActionType(SelectionType.CHECKBOX);  
	    Button.setBaseStyle("navigatorGridHeader");	
	    return Button;
	}	
	private Object getGridToolbar( ListGrid Grid , IButton Button  ) {        
        VLayout VLayout = new VLayout();
        VLayout.addMember(Button);       
		return VLayout;
	}

	public void onDataChange(ClickHandler clickHandler) {
		Select.addClickHandler(clickHandler);
	}
	
}