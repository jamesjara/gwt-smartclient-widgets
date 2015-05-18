package com.pvgwt.widgets.client.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.ExportDisplay;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridComponent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.util.JSONEncoder;
import com.smartgwt.client.util.Offline;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.FormulaUpdated;
import com.smartgwt.client.widgets.events.FormulaUpdatedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.ViewStateChangedEvent;
import com.smartgwt.client.widgets.grid.events.ViewStateChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import com.smartgwt.client.widgets.toolbar.ToolStripSeparator;

public class PagerGrid extends ListGrid {

	private Paginator 		PaginatorPlugin;
	private boolean 		canAddHilites;
	private PreferencesGrid PreferencesPlugin;
	Map<String, String> params 				= new HashMap<String, String>();
	private PagerGrid 	self 				= this;
	private String 		viewStateCookieId 	= "";
	private boolean 	BindGrid			= true;
	private boolean 	CanChangeTitle		= true;
	private boolean 	CanFormat			; //Use to allow add format to the visible value
	private PvChart 	chartObject			;
	//chartObjectColumns: Read Only array to avoid looping the grid with the visible chart column, we just send this array rather than the loop
	Map<String,String> chartObjectColumns = new HashMap<String,String>();
	private SumarizationDataSource SumarizationStore; 
	private Menu 				menu 				= new Menu();  
	private ToolStripMenuButton gridOpts 			= new ToolStripMenuButton("Tools", menu);  
	ToolStrip 					gridEditControls 	= new ToolStrip();

	// Add custom MENU to each column
	@Override
	protected MenuItem[] getHeaderContextMenuItems(Integer fieldNum) {

		MenuItem[] superitems = super.getHeaderContextMenuItems(fieldNum);

		final ListGridField Column = super.getField(fieldNum);

		List<MenuItem> list = new ArrayList<MenuItem>();
		for (MenuItem item : superitems) {
			list.add(item);
		}

		list.add(new MenuItemSeparator());

		
		if(this.CanChangeTitle){			
			// Function to change the Column Name
			MenuItem changeTitleItem = new MenuItem("Change Caption");
			changeTitleItem.setIcon(namespaces.resources_dir + "rename.png");
			changeTitleItem.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					SC.askforValue("Column Name",
							"What would you like this column to be named?",
							new ValueCallback() {
								@Override
								public void execute(String value) {
									if (!value.isEmpty()) {
										Column.setTitle(value);
										self.setFieldTitle(Column.getName(), value);
										sentChangeToChart( Column , false );//Fire changes on chart.																				
									}
								}
							});
				}
			});
			list.add(changeTitleItem);
			list.add(new MenuItemSeparator());
		}
	
		//BUG 8855 - Add modal window to formatting decimals on derived columns.
		/*
		this.CanFormat = true;
		if(this.CanFormat && isFormulaField(Column) ){	
			// Function to change the Column Name
			MenuItem changeTitleItem = new MenuItem("Format");
			changeTitleItem.setIcon(namespaces.resources_dir + "rename.png");
			changeTitleItem.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {		
					FormatWindow FormaterWindow =  new FormatWindow(Column);
					FormaterWindow.show();
				}
			});
			list.add(changeTitleItem);
			list.add(new MenuItemSeparator());
		}
		*/
		
		// BUG 8803 -  CHART SYNC WITH GRID 
		if( Column.getAttributeAsBoolean("forChart") && this.BindGrid){	
			//System.out.println(  " get menu display: " +Column.getName() + " - " +  Column.getAttribute("isOnChart") );						
			final MenuItem addToCHart = new MenuItem("Display in the Chart");	
			addToCHart.setCheckIfCondition(new MenuItemIfFunction() {
				@Override
				public boolean execute(Canvas target, Menu menu, MenuItem item) {		
					if ( Column.getAttributeAsBoolean("isOnChart") == true ){	
						addToCHart.setAttribute("isOnChart", true);
						return true ;
					} else {
						addToCHart.setAttribute("isOnChart", false);
						return false;
					}
				}
	        });			
			addToCHart.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					sentChangeToChart( Column , true  );//Fire changes on chart.		
				}
			});
			list.add(addToCHart);
			list.add(new MenuItemSeparator());	
			
			// BUG 8803 -  CHART SYNC WITH GRID  - save column position as atribute
			Column.setAttribute("order", fieldNum ); 
			//self.setFieldTitle(Column.getName(), Integer.toString(fieldNum) + "x");
			if ( Column.getAttributeAsBoolean("isOnChart") == true ){	
				//Update position on the ds chart - this will not REDRAW the chart
				this.chartObject.editColumn( Column.getName() ,Column.getTitle() ,  Column.getAttributeAsInt("order")   );	 				
			}
		}
		

		// Function to add original column name
		MenuItem originalName = new MenuItem();
		originalName.setIcon( namespaces.resources_dir + "label.png");
		String originlDSname = Column.getAttributeAsString("originalName");
		if (originlDSname == null)
			originlDSname = Column.getAttributeAsString("name");
		originalName.setTitle(originlDSname);

		list.add(originalName);

		// Convert array to MenuItem Array.
		MenuItem[] newItems = new MenuItem[list.size()];
		list.toArray(newItems);

		return newItems;
	}

	public PagerGrid() {
		super();
				
		
		setAutoFetchData(false);		
		
		// setDataFetchMode(FetchMode.LOCAL);
		// ListGridDataSource.setCacheAllData(true);
		// ListGridDataSource.setAutoCacheAllData(true);
		// setCanEdit(true);
		// setEditEvent(ListGridEditEvent.NONE);
		// setLeaveScrollbarGap(true);
		
		setCanAddFormulaFields(true);
		setCanAddSummaryFields(false);
		setShowGridSummary(true);
		//setShowGroupSummary(false);
		setShowGroupSummaryInHeader(true);
		setShowRowNumbers(false);
		setShowFilterEditor(true);
		// setAllowFilterExpressions(true);
		setCanFreezeFields(false);
		setCanMultiSort(false);
		setGroupStartOpen(GroupStartOpen.ALL);
		setHoverMoveWithMouse(true);
		setSelectionType(SelectionStyle.SIMPLE);
		setSelectionAppearance(SelectionAppearance.CHECKBOX);
		setCanAddHilitesFields(false);
		setAlternateRecordStyles(true);
		setBaseStyle("psGridCell");
		setAutoSaveEdits(false);
		setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		setAutoFitFieldWidths(true);
		
		setMinHeight(150);

		// TOOLSTRIP
		gridEditControls.setWidth100();
		gridEditControls.setHeight(15);

		// Grid Plugins
		PaginatorPlugin = new Paginator(this);
		PreferencesPlugin = new PreferencesGrid(this);
		
		
		MenuItem exportItem 	= new MenuItem("Export View as CSV", namespaces.resources_dir+	"document_plain_new.png" ); 
		exportItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				self.ServerExportCSV();
			}
		});
		MenuItem formulastItem 	= new MenuItem("Derived Columns",namespaces.resources_dir+"table_column.png"); 
		formulastItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				self.addFormulaField(); 
			}
		});
		MenuItem refreshChartItem 	= new MenuItem("Refresh Chart", namespaces.resources_dir+"refresh_green.png" );
		refreshChartItem.setEnableIfCondition(new MenuItemIfFunction() {			
			public boolean execute(Canvas target, Menu menu, MenuItem item) {
				return BindGrid;
			}
		});
		refreshChartItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				self.chartObject.refresh();		
			}
		});      
		menu.addItem(exportItem);  
		//Formulas if it is enabled
        if ( self.getCanAddFormulaFields() ){
        	menu.addItem(formulastItem);
        }    
        //if there is chart add button , BUG 8803 -  CHART SYNC WITH GRID  	
		if ( this.BindGrid == true){ 		 //TODO: SHOULD BE ONLY VISIBLE IF THERE IS CHART ATACHED TO THE GRID.
        	menu.addItem(refreshChartItem);
		}		        
       
		gridEditControls.setMembers(gridOpts ,PreferencesPlugin, PaginatorPlugin );

		Object[] Plugins = new Object[] {
				// SumarizationPanel,
				ListGridComponent.HEADER, ListGridComponent.SUMMARY_ROW,
				ListGridComponent.FILTER_EDITOR, ListGridComponent.BODY,
				gridEditControls };

		setGridComponents(Plugins);

		//Handler for derivated columns problems
		addFormulaUpdatedHandler(new FormulaUpdatedHandler() {
			@Override
			public void onFormulaUpdated(FormulaUpdated event) {
				refreshFormulaFields(event.getField());
			}
		});
		addViewStateChangedHandler(new ViewStateChangedHandler() {
			@Override
			public void onViewStateChanged(ViewStateChangedEvent event) {	
				refreshFormulaFields();			
				sentViewToCookie();
			}
		});		
		//Load view state from cookie until object and childs be drawn
		addDataArrivedHandler(new DataArrivedHandler() {				
			@Override
			public void onDataArrived(DataArrivedEvent event) {	
					
			}
		});		
		addDrawHandler(new DrawHandler() {
			@Override
			public void onDraw(DrawEvent event) {
				loadViewFromCookie();		
			}
		});	
		/*
		setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,int colNum) {
				final ListGridField Column = getField(colNum);				
  				//If is float
  				if( Column.getType() == ListGridFieldType.FLOAT ){
  					//Add formater value
  					//Si no es null entonces setear este valor.
		  			if ( value != null ) {	  
		  				System.out.println( value.toString());
		  				System.out.println( Column.getAttributeAsInt("presicion") );
		  				if( Column.getAttribute("presicion") != null  )			  		  					
  		  					if ( Column.getAttributeAsInt("presicion") == 0 )
  		  						return Utilities.FormattoUSStringZero(  value.toString() );
  		  					else
  		  						return Utilities.FormattoUSString(  value.toString() , Column.getAttributeAsInt("presicion")  );
  		  				else
  		  					return value.toString();
		  				}
  				}
				return null;
			}
		});
		*/

		
	}
	
	
	
	@Override
	public DataSource getSummaryRowDataSource() {
		// TODO Auto-generated method stub
		System.out.println("getSummaryRowDataSource");
		return (SumarizationDataSource) super.getSummaryRowDataSource();
	}
	
	

	// BUG 8803 -  CHART SYNC WITH GRID 
	public void setChartObject(PvChart pvChart) {
		this.chartObject = pvChart;
	}
	//deprecated, chart object should save in cookies last url, and reload  it isolated. 11
	private void drawChart() {		
		System.out.println("drawChart chartObjectColumns.size(): " + chartObjectColumns.size());
		if(this.BindGrid){		
			if ( this.chartObject != null){ //defensive null check
				//Add columns to the chart
				this.chartObject.addColumns( chartObjectColumns );			
				this.chartObject.refresh();		
			}
		}		
	}	
	private void sentChangeToChart( ListGridField Column , boolean deactivateMenu ) {
		if( Column.getAttributeAsBoolean("forChart") && this.BindGrid){		
			if ( this.chartObject != null){ //defensive null check
				//If we only change the name, avoid unchecking the column
				if (deactivateMenu){				
					if (Column.getAttributeAsBoolean("isOnChart")){		
						Column.setAttribute("isOnChart" , false);	
						//Remove column from the chart
						this.chartObject.deleteColumn( Column.getName()  );					
						this.chartObject.refresh();		
						chartObjectColumns.remove(Column.getName());						
					} else {
						 Column.setAttribute("isOnChart" , true);	
						//Add new column to the chart
						this.chartObject.addColumn( Column.getName() , Column.getTitle() ,  Column.getAttributeAsInt("order")  );					
						this.chartObject.refresh();					
					}
				} else {
					if (Column.getAttributeAsBoolean("isOnChart")){		
						this.chartObject.editColumn( Column.getName() ,Column.getTitle() ,  Column.getAttributeAsInt("order")   );	
						this.chartObject.refresh();	
					}
				}
				sentViewToCookie();//Fire  viewstate change event.
			}				
		}
	}

	//Persisting grid view
	public void sentViewToCookie(){		
		//Save view as cookie 
		if (! viewStateCookieId.isEmpty() ){
			Offline.put( viewStateCookieId ,	getViewState());	
			//System.out.println( viewStateCookieId + " - Offline ViewStateFromCookie ");		
		}
	}
	public void loadViewFromCookie(){					
		final String ViewStateFromCookie = (String) Offline.get(viewStateCookieId);
		if ( ViewStateFromCookie != null &&  ! ViewStateFromCookie .isEmpty() ){
			setViewState(ViewStateFromCookie);	
			//System.out.println(ViewStateFromCookie+" - ViewStateFromCookie ");		
		}			
	}

	@Override
	public void fetchData() {
		super.fetchData();
	}

	public void PagerfetchData(Boolean invalidateCache) {
		
		//System.out.println("PagerfetchData");		
		params.put("ws", "true");
		params.put("metadata", "false");
		params.put("summary", "false");
		params.put("page", getPagerDataSource().getCurrentPage() + "");
		params.put("size", getPagerDataSource().getPagesize() + "");
		
		DSRequest dsRequest = new DSRequest();
		dsRequest.setParams(params);

		// REconfigure sumaritzation datasource
		DSRequest dsRequestSumarization = new DSRequest();
		params.put("summary", "true");
		dsRequestSumarization.setParams(params);
		setSummaryRowFetchRequestProperties(dsRequestSumarization);
		//SC.say(  SumarizationStore.getDataURL()   );
		SumarizationStore.setDefaultParams(params); //IMPORTANT TO SEND DINAMICLY VALUES
		SumarizationStore.invalidateCache();				
		
		if (invalidateCache) {
			invalidateCache();
		}

		fetchData(null, new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData,DSRequest request) {

				//System.out.println("5. PagerfetchData  requesting data for the final grid >");
				//System.out.println(" -Total paginas  :"+ new Integer(getPagerDataSource().getTotalPages()).toString());
				//System.out.println(" -numero de  pagina :"+ new Integer(getPagerDataSource().getCurrentPage()).toString());
				//System.out.println(" -rows en esta pagina :"	+ new Integer(getPagerDataSource().getTotalRecors()).toString());
				//System.out.println(" -Start row :"+ new Integer(getPagerDataSource().getStartRow()).toString());
				//System.out.println(" -End row :"+ new Integer(getPagerDataSource().getEndRow()).toString());
				///System.out.println(" -Sumarization rows");

				PaginatorPlugin.refreshUI();
							
			}
		},dsRequest);

	}

	PreferencesGrid getPreferencesPlugin() {
		return (PreferencesGrid) PreferencesPlugin;
	}

	PagerDataSource getPagerDataSource() {
		return (PagerDataSource) getDataSource();
	}

	public void addHeaderPlugins() {
	}

	public void addFooterPlugins() {
	}

	public boolean getCanAddHilitesFields() {
		return canAddHilites;
	}

	private void setCanAddHilitesFields(boolean b) {
		canAddHilites = b;
	}

	public void addCriteria(String key, String val) {
		params.put(key, val);
	}

	public void refreshHilities() {
		setHilites(getHilites());
	}
	

	public void setCookieId(String id){
		//System.out.println( " setViewStateCookieId - " + id);
		this.viewStateCookieId = id;
	}
	
	public String getViewStateCookieId(){
		return viewStateCookieId;
	}
	
	public boolean isCanAddHilites() {
		return canAddHilites;
	}

	public void setCanAddHilites(boolean canAddHilites) {
		this.canAddHilites = canAddHilites;
	}

	public boolean isBindGrid() {
		return BindGrid;
	}
	

	/*
	 * Will lookup for the Chart Componment with the grid id plus "_chart" for example ps1_chart
	 */
	public void setBindGrid(boolean bindGrid) {
		BindGrid = bindGrid;
	}

	public boolean isCanChangeTitle() {
		return CanChangeTitle;
	}

	public void setCanChangeTitle(boolean canChangeTitle) {
		CanChangeTitle = canChangeTitle;
	}
	
	
	/*
	 * Needed because when we create formulas the field does not have requerid
	 * configuration so we need to trigger this configuration afecter creation
	 * of the view state.
	 */
	protected void refreshFormulaFields(ListGridField field) {
		//System.out.println("refreshFormulaFields( field ) - formula added or updated");
		//System.out.println("refreshFormulaFields(): " + field.getName());
		field.setCanGroupBy(false);
		field.setCanSortClientOnly(true);
		field.setShowGridSummary(false);
		field.setShowGroupSummary(false);
		field.setCanFilter(true);
		field.setFilterEditorType(new CustomEmptyFilter());
		field.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				return Utilities.FormattoUSString(value.toString());
			}
		});
		// field.setHeaderTitle("cambio?");
		field.setHidden(true); // hack to redraw filter
		field.setHidden(false);
	}

	
	/*
	 * Use for recreate the viewstate with custom properties, used at high level
	 * to avoid overhead avoid use on unecesary cases.
	 */
	protected void refreshFormulaFields() {
		//System.out.println("ccheck for recreaing state from another state? ");
		boolean isFormula = false;
		for (ListGridField r : super.getAllFields()) {
			if (isFormulaField( r ) ==true) { // Only if it is formula		
				// recreate Wide state.
				//System.out.println(" state contains formula fields ");
				isFormula = true;
			}
		}
		if (isFormula)
			recreateFromFormulaState();
		//System.out.println("ccheck for recreaing state from another state? -> "	+ isFormula);
	}	
	private boolean isFormulaField(ListGridField r){
		if (r.getName().contains("formulaField"))  // Only if it is formula		
			return true;
		else
			return false;
	}

	/*
	 * We need to recreate the view state when we have formulas because is the
	 * unique way to set propierties to the formula columns. So basicly we take
	 * normal columns, plus formula columns(we set propierties) and we set this
	 * group to the grid. Should be called only from refreshFormulaFields
	 * function.
	 */
	protected void recreateFromFormulaState() {
		//System.out.println("recreateFromFormulaState");

		// Get normal fields
		ListGridField[] oldListGridFilds = super.getAllFields();
		List<ListGridField> newListGridFilds = new ArrayList<ListGridField>();
		for (ListGridField r : oldListGridFilds) {
			//System.out.println("recreateFromFormulaState - name: "+ r.getName());

			// Clone Column
			ListGridField field = new ListGridField(r.getJsObj());

			if (field.getName().contains("formulaField")) { // Only if it is formula field then we add new properties.

				field.setCanGroupBy(false);
				field.setCanSortClientOnly(true);
				field.setShowGridSummary(false);
				field.setShowGroupSummary(false);
				field.setCanFilter(true);
				field.setFilterEditorType(new CustomEmptyFilter());
				field.setCellFormatter(new CellFormatter() {
					@Override
					public String format(Object value, ListGridRecord record,
							int rowNum, int colNum) {
						return Utilities.FormattoUSString(value.toString());
					}
				});
			}
			// Add the fixed ListGridField
			newListGridFilds.add(field);
		}
		// Setup the grid with the new changes
		super.setFields(newListGridFilds.toArray(new ListGridField[0]));
	}

	public void addField(ListGridField field) {
		ArrayList<ListGridField> gridFields = new ArrayList<ListGridField>(Arrays.asList(getFields()));
		gridFields.add(field);
		setFields(gridFields.toArray(new ListGridField[gridFields.size()]));
		markForRedraw();
	}
	

	/*
	 * BUG 8855 - Add modal window to formatting decimals on derived columns.  
	 */
	private void setFormatFieldState(String viewState){		
		/*if(this.BindGrid){		
			chartObjectColumns.clear();
			System.out.println( "chartObjectColumns.size(): " + chartObjectColumns.size() );
			//Decode from viewstate, then decode from field key.
			JavaScriptObject 	jsObj 		= JSON.decode(viewState);
			JSONObject 			combinedObj = new JSONObject(jsObj);
			JSONString 			encoded_fe0	= combinedObj.get("field").isString(); //extract field json string
			String 				encoded_fe1 =  encoded_fe0.stringValue();
			//Extract and set custom atributes
			JSONValue jsonV = JSONParser.parseLenient( encoded_fe1 );
			JSONArray arr = jsonV.isArray();
			for (int i = 0; i < arr.size(); i++) {
				//Extract and set chart visibility
				
				// OBj
				JSONValue 	col = arr.get(i);
				JSONObject 	obj = col.isObject();

				// Name
				JSONString  name_ = obj.get("name").isString();
				String 		name  = name_.stringValue();
				
				// title
				JSONString  title_ = obj.get("title").isString();
				String 		title  = title_.stringValue();
				
				// isOnChart
				if (obj.get("isOnChart") != null) {
					JSONBoolean	chart_ = obj.get("isOnChart").isBoolean();		
					boolean isOnChart = chart_.booleanValue();
					super.getField(name).setAttribute("isOnChart",  isOnChart);
					//Also grab on global array the values  that should be displayed on the chart.
					if ( isOnChart )
						chartObjectColumns.put( name , title );
				}
			}
			drawChart();
		}*/				
	}
	
	/*
	 * BUG 8803 -  CHART SYNC WITH GRID 
	 * This is addional to the core setviewstate, because the core function only read 4 properties. we need to 
	 * extend it , in order to recovery chart property FROM VIEWSTATE. 
	 */
	private void setChartFieldsState(String viewState){		
		if(this.BindGrid){		
			chartObjectColumns.clear();
			System.out.println( "chartObjectColumns.size(): " + chartObjectColumns.size() );
			//Decode from viewstate, then decode from field key.
			JavaScriptObject 	jsObj 		= JSON.decode(viewState);
			JSONObject 			combinedObj = new JSONObject(jsObj);
			JSONString 			encoded_fe0	= combinedObj.get("field").isString(); //extract field json string
			String 				encoded_fe1 =  encoded_fe0.stringValue();
			//Extract and set custom atributes
			JSONValue jsonV = JSONParser.parseLenient( encoded_fe1 );
			JSONArray arr = jsonV.isArray();
			for (int i = 0; i < arr.size(); i++) {
				//Extract and set chart visibility
				
				// OBj
				JSONValue 	col = arr.get(i);
				JSONObject 	obj = col.isObject();

				// isOnChart
				if (obj.get("isOnChart") != null) {
					
					String name = null,title = null;
					
					// Name
					if (obj.get("name") != null) {
						JSONString  name_ = obj.get("name").isString();
						 			name  = name_.stringValue();
					}
					
					// title
					if (obj.get("title") != null) {
						JSONString  title_ = obj.get("title").isString();
						 			title  = title_.stringValue();
					}
					
					JSONBoolean	chart_ = obj.get("isOnChart").isBoolean();		
					boolean isOnChart = chart_.booleanValue();
					super.getField(name).setAttribute("isOnChart",  isOnChart);
					//Also grab on global array the values  that should be displayed on the chart.
					if ( isOnChart )						
						chartObjectColumns.put( name , title );
				}				
				
			}
			drawChart();
		}				
	}
	
	@Override
	public void setViewState(String viewState) {
		super.setFields(); // Use to invalidated and remove unsaved derivated columns. 
		super.setViewState(viewState);	
		setChartFieldsState(viewState); 
		//setFormatFieldState(viewState);   
	}
	
	
	/*
	 * To get ,parse return the fixed ViewState Metada from the grid.
	 * (non-Javadoc)
	 * 
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getViewState()
	 * 
	 * @example json: ({selected:null,field:
	 * "[{name:\"product_id\",width:null,autoFitWidth:null},{name:\"product\",width:null,autoFitWidth:null},{name:\"status\",width:null,autoFitWidth:null},{name:\"max_bid_cpc\",width:null,autoFitWidth:null},{name:\"network_id\",width:null,autoFitWidth:null}]"
	 * ,sort:"({fieldName:null,sortDir:\"ascending\"})",hilite:null,group:
	 * "([{fieldName:\"product\",groupingMode:null,groupGranularity:null,groupPrecision:null}])"
	 * })
	 */
	@Override
	public String getViewState() {

		String ViewState = super.getViewState();
		//System.out.println("getViewState:" + ViewState);

		JavaScriptObject jsObj = JSON.decode(ViewState);

		// *4Debugging //Custom propiertes
		for (ListGridField f : super.getFields()) {
			// SHow all Attributes
			//for (String A : f.getAttributes()) {
				//System.out.println("--a--");
				//System.out.println(A);
				//System.out.println(f.getAttribute(A));
				//System.out.println("----");
			//}
		}

		List<Map<String, Object>> fieldState = new ArrayList<Map<String, Object>>();

		String state = super.getFieldState();
		//System.out.println("getFieldState :" + state);
		JSONValue jsonV = JSONParser.parseLenient(state);
		JSONArray arr = jsonV.isArray();
		for (int i = 0; i < arr.size(); i++) {

			// Metadata MAP
			Map<String, Object> property = new HashMap<String, Object>();

			// OBj
			JSONValue col = arr.get(i);
			JSONObject obj = col.isObject();

			// Name
			JSONString name_ = obj.get("name").isString();
			String name = name_.toString().replace("\"", "");
			property.put("name", name);

			// Visibility
			boolean visible = true;
			if (obj.get("visible") != null) {
				JSONBoolean visible_ = obj.get("visible").isBoolean();
				visible = visible_ == null || visible_.booleanValue();
				property.put("visible", visible);
			}

			// Title - will be only aviable to get if the title is visible.
			ListGridField tmp_f = super.getField(name);
			if (visible) {
				String title = tmp_f.getTitle();
				//System.out.println(title);
				property.put("title", title);
				
				//Add width
				String width = tmp_f.getWidth();
				//System.out.println(width);
				property.put("width", width);
				
			}
			//System.out.println(name_ + " -> " + visible + ";");

			// user Formula - {name:\"formulaField1\",userFormula:{text:\"D +2\",formulaVars:{D:\"conversions\"}
			if (obj.get("userFormula") != null) {
				JSONObject userFormula = obj.get("userFormula").isObject();
				JavaScriptObject userFormula_obj = JSON.decode(userFormula.toString()); // encode to send as an object. NOT as// string
				property.put("userFormula", userFormula_obj);
				//System.out.println(name_ + " formula -> " + userFormula_obj	+ ";");
				// Disable filter for derivated columns
				property.put("canFilter", false);
				property.put("isNative", false);
				property.put("canGroup", false);
			}
			
			//BUG 8803 -  Get Chart State - must be readed from field because viewstate do not provide field	
			//  tmp_f only can be accesible if is it visible
			if (visible) {
				Boolean isOnChart = tmp_f.getAttributeAsBoolean("isOnChart");			
				//System.out.println( " getViewState(will set!):" + name + " - " +  isOnChart.toString() );
				if ( isOnChart != null) {
					property.put("isOnChart", isOnChart );	
				}
			}
			//property.put("width", null );
		    //property.put("autoFitWidth", true );

			fieldState.add(property);
		}

		// We nned to enconde with Some especial propiertes else will fail.
		JSONEncoder jsonEncoder = new JSONEncoder();
		jsonEncoder.setStrictQuoting(false);
		jsonEncoder.setPrettyPrint(false);
		jsonEncoder.setSkipInternalProperties(false);
		JSOHelper.setAttribute(jsObj, "field", JSON.encode(
				JSOHelper.convertToJavaScriptArray(fieldState.toArray()),
				jsonEncoder));

		// Use False Strict Quoting to avoid quotes.
		JSONEncoder settings = new JSONEncoder();
		settings.setStrictQuoting(false);
		settings.setPrettyPrint(false);
		settings.setSkipInternalProperties(false);

		// Ugly way to construc new state , NOTICE: needs strict JSON format
		ViewState = "(" + JSON.encode(jsObj, settings) + ")";

		// 4 Debugging , this should be the same else will get an error.
		// System.out.println( grid.getViewState() );
		// System.out.println( "------------" );
		//System.out.println("getViewState:" + ViewState);

		return ViewState;
	}
	
	
	/**
	 * Export data from a listrgrid
	 * @param listGrid the {@link ListGrid}
	 * @return a {@link StringBuilder} containing data in CSV format
	 */
	public StringBuilder exportCSV() {
		
		StringBuilder stringBuilder = new StringBuilder(); // csv data in here
 
		//Get titles
		for (ListGridField f : super.getFields()) {
			if ( ! f.getName().startsWith("_") ){
				stringBuilder.append("\"");
				stringBuilder.append(f.getTitle());
				stringBuilder.append("\",");
			}
		}

		stringBuilder.deleteCharAt(stringBuilder.length() - 1); // remove last ","
		stringBuilder.append("\n");

		int colNum = 0;
		int rowNum = 0;
		String formatedValue = "";
		
		//Iterate througth the data
		for(ListGridRecord r : super.getRecords()){
			for (ListGridField f : super.getFields()) {
				if ( ! f.getName().startsWith("_") ){
					stringBuilder.append("\"");
					
					//Parse Only FLOAT Values OR items with export cell value 
					if((f.getType() == ListGridFieldType.FLOAT) || f.getAttributeAsBoolean("exportCellValue")  ){
						colNum = super.getFieldNum(f.getName());
						rowNum = super.getRecordIndex(r);
						formatedValue = Utilities.getFormattedValue( super.getJsObj() , r , rowNum, colNum);
					} else {
						formatedValue = r.getAttribute(f.getName());
					}

					stringBuilder.append( formatedValue );
					stringBuilder.append("\",");
					
				}
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 1); // remove last ","
			stringBuilder.append("\n");
		}
		
		return stringBuilder;
	}
	/*
	 * We need to resend csv data to server, because javascript is not enable to generate runtime file and download it.
	 * This step is an unnecesary(to data)  but required process(to download).
	 */
	public void ServerExportCSV(){	
		StringBuilder exportedCSV = self.exportCSV();
		Utilities.downloadFile( namespaces.downloadUrl , "d" , exportedCSV.toString() );		
	}

	//TODO This should go in ListGridField extended class.
	public void setShowInChart(String name  , boolean show) {
		//super.getField(name).setAttribute("isOnChart", show ? "false" : "true");
		super.getField(name).setAttribute("isOnChart", show);
		//(entry.getValue()=="true" ? true : false ) 
	}
	//TODO This should go in ListGridField extended class.
	public String getShowInChart(String name) {
		return super.getField(name).getAttribute("isOnChart");
	}

	public void setSummaryDs(SumarizationDataSource sumarizationStore) {
		this.SumarizationStore = sumarizationStore;
	}
	
	/*
	 * add items to the toolbar menu TOOLS
	 */
	public void addToolMenu(MenuItem MenuItem){
		menu.addItem(MenuItem);  
	}
	/*
	 * drop items to the toolbar menu TOOLS
	 */
	public void dropToolMenu(String string) {
	}

	/*
	 * adds items to the toolbar menu
	 * menu
	 * position
	 */
	public void addToolStripMenuButton(ToolStripButton button,int Pos) {
		gridEditControls.addButton(button,Pos);
	}
	public void addToolStripMenuSeparator(int Pos) {
		gridEditControls.addMember(new ToolStripSeparator(), Pos);
	}
	

	/*
	 * Paint the grid layout into a fullscreen window, on close will return grid layout to original layout named parentlayout
	 * @Grid layout
	 * @Parent Layout
	 */
	public void setFullScreenStacks(final SectionStackSection section, final SectionStack sectionStack )
	{
		//Add Icon to tool menu
		final MenuItem FullScreenItem 	= new MenuItem("FullScreen Mode", namespaces.resources_dir+"refresh_green.png" ); 
		FullScreenItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				
				final FullScreenApp appWindow = new FullScreenApp( section  );
                 appWindow.addCloseClickHandler(new CloseClickHandler() {						
						@Override
						public void onCloseClick(CloseClickEvent event) {	
	                         	//Return to parent layout
								sectionStack.addSection(section);            
		                        appWindow.destroy();
		                   }
				});
		        appWindow.show();
			}
		});      
		menu.addItem(FullScreenItem);  
	}



      
	  
}
