package com.pvgwt.widgets.client.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.NamedFrame;
import com.pvgwt.widgets.client.ProductSummary.MatchingProductsGrid;
import com.pvgwt.widgets.client.utils.namespaces;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.util.Offline;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.KeyPressEvent;
import com.smartgwt.client.widgets.events.KeyPressHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FileItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

public class PreferencesGrid extends ToolStrip {

	private PagerGrid 		grid;
    private SuperDataSource preferencesDS;     
    private SelectItem 		preferenceSelectItem; 
    private Record  		record = new Record();
	private String 			preferenceCookieId = "";
	Criteria criteria = new Criteria();
    
    public void setBaseUrl(String dataURL){
    	preferencesDS.setDataURL( dataURL );
    }

	public PreferencesGrid( final PagerGrid listGrid ) {
		this.grid = listGrid;
	//public PreferencesGrid( final ListGrid listGrid ) {
	//	this.grid = (PagerGrid) listGrid;
		
		//System.out.println("--PreferencesGrid " );
		
        //create a "preferences" DataSource to bind to SelectItem and Preferences ListGrid  
        preferencesDS = new SuperDataSource();         
        preferencesDS.setShowPrompt(false);
        preferencesDS.setDataFormat(DSDataFormat.JSON);
        preferencesDS.setClientOnly(false);
        preferencesDS.setUseHttpProxy(false);
        preferencesDS.setDataURL( namespaces.mod_grid_perspectives );      
        
        
        DataSourceIntegerField pkField = new DataSourceIntegerField("cugp_id");  
        pkField.setPrimaryKey(true);          
        DataSourceTextField preferenceField = new DataSourceTextField("title", "Name");  
        DataSourceTextField stateField = new DataSourceTextField("value", "View State");  
        DataSourceTextField is_public = new DataSourceTextField("is_public");  
        DataSourceTextField description = new DataSourceTextField("description");  
        preferencesDS.setFields(pkField, preferenceField, stateField , is_public  , description );  
        
        
        //Formulas will be on tools menu
        /*ToolStripButton formulaButton = new ToolStripButton("Derived Columns",namespaces.resources_dir+"table_column.png");  
        formulaButton.setAutoFit(true);  
        formulaButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	grid.addFormulaField();  
            }  
        });*/ 
		  
        ToolStripButton editHilites = new ToolStripButton("Hilites");  
        editHilites.setAutoFit(true);  
        editHilites.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	grid.editHilites();
            }  
        });  
        
        ToolStripButton summaryBuilder = new ToolStripButton("Summary Builder");  
        summaryBuilder.setAutoFit(true);  
        summaryBuilder.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	grid.addSummaryField();  
            }  
        });   
        addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {

				//System.out.println( event.getKeyName());
				if (  event.isAltKeyDown()  && ("S".equals(event.getKeyName()) == true ) ) {
	            	grid.addSummaryField();  
				}
				if (  event.isAltKeyDown()  && ("H".equals(event.getKeyName())== true)) {
	            	grid.editHilites();  
				}
				if (  event.isAltKeyDown()  && ("F".equals(event.getKeyName())== true)) {
	            	grid.addFormulaField();  
				}
			}
		});
        
  
        preferenceSelectItem = new SelectItem(); 
        preferenceSelectItem.setValueField("cugp_id");
        preferenceSelectItem.setDisplayField("title");
        preferenceSelectItem.setTitle("Views");          
        ListGrid pickListProperties = new ListGrid();
        pickListProperties.setEmptyMessage("No Saved Views");  
        pickListProperties.setAutoFetchData( false );
        preferenceSelectItem.setPickListProperties(pickListProperties);  
        preferenceSelectItem.setOptionDataSource(preferencesDS);
        preferenceSelectItem.setPickListCriteria(criteria);     
      
        preferenceSelectItem.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {				
                final Record record = preferenceSelectItem.getSelectedRecord();
                //System.out.println( getPreferenceCookieId() + " addChangedHandler  getPreferenceCookieId -" );
				//Save selecitem as cookie 
				if (! getPreferenceCookieId().isEmpty() ){		 
					//System.out.println( record.getAttributeAsString("cugp_id")  + " - Offline getPreferenceCookieId ");		
					Offline.put( getPreferenceCookieId() ,	 record.getAttributeAsString("cugp_id")  );
				}					
				//load view state
                if (record != null) {  
	                String viewState = record.getAttribute("value");  
	                SetState(viewState);  
                }
			}
		});         
        
        Menu menu = new Menu();  
        menu.setShowShadow(true);  
        menu.setShadowDepth(3);
 
        MenuItem newItem 	= new MenuItem("New", namespaces.resources_dir+	"document_plain_new.png" ); 
        newItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
                SC.askforValue("Enter View name :", new ValueCallback() {  
                    @Override  
                    public void execute(final String value) {  
                        if (value != null && !value.equals("")) {                          	
                        	Record record = new Record();
                            record.setAttribute("title"		, value);
                        	addPreference(record,new DSCallback() {								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									//System.out.println( response.getStatus() );
									if (response.getStatus() < 0) {
						                // Error handling here
										UIMessages.showSimpleErrorValidation();			 
						            } else {
						                // Normal processing here
						            	final String id = Utilities.getStringFromJson(rawData, "cugp_id");
										preferenceSelectItem.setValue( id ); 
										UIMessages.showSuccess();	
										
										//---Save preference created
						                final Record record = preferenceSelectItem.getSelectedRecord();
						                if (record != null) {  
						                	savePreference( record , new DSCallback() {								
						            			@Override
						            			public void execute(DSResponse response, Object rawData, DSRequest request) {
													//System.out.println( response.getStatus() );
													if (response.getStatus() < 0) {
										                // Error handling here
														UIMessages.showSimpleErrorValidation();			 
										            } else {
										                // Normal processing here
										            	UIMessages.showSuccess();	
										            }	
						            			}
						            		});
						                }
						              //---Save 
						            }						              	
								}
							});          
                        }  
                    }  
                }); 
			}
		});
        MenuItem saveItem	= new MenuItem("Save", namespaces.resources_dir+	"disk_blue.png"	);   
        saveItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
                final Record record = preferenceSelectItem.getSelectedRecord();
                if (record != null) {  
                	savePreference( record , new DSCallback() {								
            			@Override
            			public void execute(DSResponse response, Object rawData, DSRequest request) {
							//System.out.println( response.getStatus() );
							if (response.getStatus() < 0) {
				                // Error handling here
								UIMessages.showSimpleErrorValidation();			 
				            } else {
				                // Normal processing here
				            	UIMessages.showSuccess();	
				            }	
            			}
            		});
                }
			}
		});
        MenuItem delItem	= new MenuItem("Delete", namespaces.resources_dir+	"pag_start2.png"	);  
        delItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {                
                Record record = preferenceSelectItem.getSelectedRecord();
                if (record != null) {    
                    preferencesDS.removeData( record , new DSCallback() {								
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							//System.out.println( response.getStatus() );
							if (response.getStatus() < 0) {
				                // Error handling here
								UIMessages.showSimpleErrorValidation();			 
				            } else {
				                // Normal processing here
				            	preferenceSelectItem.setValue( "" );  
				            	UIMessages.showSuccess();	
				            }			                
						}
					}   );  
                }   
			}
		});
        MenuItem editItem	= new MenuItem("Edit View", 	namespaces.resources_dir+	"edit.png"	);   
        editItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
                final Record record = preferenceSelectItem.getSelectedRecord();
                if (record != null) {  
                	IeditView( record );    
                }
			}
		});
        
        MenuItem catalogItem = new MenuItem("Catalog Views");  
        catalogItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				ICatalog(  );
			}
		});
        

        menu.setItems( catalogItem , new MenuItemSeparator()   , newItem, saveItem, delItem , new MenuItemSeparator()   , editItem );  
        ToolStripMenuButton menuButton = new ToolStripMenuButton("Options", menu);  
        menuButton.setWidth(100);  
        
        //Formulas will be on tools menu
       /* if ( grid.getCanAddFormulaFields() ){
        	addButton(formulaButton);
        }*/
        if ( grid.getCanAddSummaryFields() ){
        	addButton(summaryBuilder);
        }
        if ( grid.getCanAddHilitesFields() ){
        	addButton(editHilites);
        }      
        
        addSeparator();
        addFormItem( preferenceSelectItem );
        addMenuButton(menuButton);
        
		setBackgroundColor("red");		
		setAlign(Alignment.LEFT);			

		setBorder("0px");
		
		//set and get the cookie  until child preferences is generated
		addDrawHandler(new DrawHandler() {			
			@Override
			public void onDraw(DrawEvent event) {
				//Set cookie id
				//setPreferenceCookieId(listGrid.getViewStateCookieId());
				setPreferenceCookieId(((PagerGrid) listGrid).getViewStateCookieId());
				
				//Load value from cookie
				final String IdFromCookie = (String) Offline.get( getPreferenceCookieId() );
				//System.out.println( getPreferenceCookieId()  + "*****"+ IdFromCookie );		
				if ( IdFromCookie != null && ! IdFromCookie .isEmpty() ){
			        preferenceSelectItem.addDataArrivedHandler(new com.smartgwt.client.widgets.form.fields.events.DataArrivedHandler() {
						@Override
						public void onDataArrived(com.smartgwt.client.widgets.form.fields.events.DataArrivedEvent event) {
								//System.out.println( " addDataArrivedHandler Offline.get set pref id: " +IdFromCookie );
								preferenceSelectItem.setValue(IdFromCookie);
						}
					});
				}				
			}
		});
	}

	protected void SetState( String viewState ){
		System.out.println( "SetState:" + viewState);
		grid.setViewState( viewState  ); 
	}
	
	protected String GetState( ){
		System.out.println( "GetState:");
		return grid.getViewState();		
	}

	public void addRequiredVars(String key, String val) {
		record.setAttribute(key, val);
		criteria.setAttribute(key, val);
	}

	private void addPreference(Record record , DSCallback dSCallback ){ 
        String viewState = GetState();
		record.setAttribute("value",viewState);
        preferencesDS.addData( record , dSCallback   );  
	}	

	private void importPreference(Record record , DSCallback dSCallback ){ 
	    DSRequest properties = new DSRequest();
	    properties.setWillHandleError(true);
        preferencesDS.addData( record , dSCallback  , properties );  
	}	
	
	private void savePreference(Record record , DSCallback dSCallback){
        String viewState = GetState();
		record.setAttribute("value",viewState);		
        preferencesDS.updateData( record , dSCallback   );  
	}


	protected void ICatalog() {
		final Window winModal = new Window();
		winModal.setAutoSize(true);
		winModal.setTitle("Catalog");
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.setWidth(400);
		winModal.setHeight(300);

		winModal.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClickEvent event) {
				winModal.destroy();
			}
		});
			
		RestDataSource gridDataSource = new RestDataSource();
		gridDataSource.setDataURL(  namespaces.mod_grid_perspectives_catalog  );
		gridDataSource.setDataFormat(DSDataFormat.JSON);
		gridDataSource.setClientOnly(false);
		
		DataSourceField cugp_id = new DataSourceField();
		
		cugp_id.setName("cugp_id");
		cugp_id.setPrimaryKey(true);
		cugp_id.setHidden(true);
		DataSourceField title = new DataSourceField();
		title.setName("title");
		DataSourceField description = new DataSourceField();
		description.setName("description");
		DataSourceField autor = new DataSourceField();
		autor.setName("username");
		DataSourceField view = new DataSourceField();
		view.setName("value");
		gridDataSource.setFields(cugp_id, title, description, description, view );
		
        final ListGrid CatalogGrid = new ListGrid();    
        CatalogGrid.setShowAllRecords(true);  
        CatalogGrid.setWrapCells(true);  
        CatalogGrid.setCellHeight(35);  
		CatalogGrid.setDataSource(gridDataSource);
		CatalogGrid.setWidth(400);
		CatalogGrid.setHeight(300);
		CatalogGrid.setMargin(5);
		CatalogGrid.setAutoFetchData(true);
		CatalogGrid.setCanGroupBy(false);
		CatalogGrid.setShowCellContextMenus(false);
		
		
	

		ListGridField id = new ListGridField();
		id.setName("cugp_id");
		id.setHidden(true);
		ListGridField titulo = new ListGridField();
		titulo.setName("title");
		titulo.setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "<b>"+value+"</b>";
			}
		});
		ListGridField descripcion = new ListGridField();
		descripcion.setName("description");
		descripcion.setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "<div style=' text-align: right;'>"+value+"<div style='color: blue;'> "+record.getAttributeAsString("username")+"</div></div>";
			}
		});
		CatalogGrid.setFields(id,titulo,descripcion);
		
		
		final ToolStripButton importView = new ToolStripButton( "Make a local copy of the View",	namespaces.resources_dir+	"import.png"	 );  
        importView.setAutoFit(true);  
        importView.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {       
            	final Record record = CatalogGrid.getSelectedRecord();
            	record.setAttribute("original_source", record.getAttributeAsString("cugp_id"));    	
            	importPreference( record ,new DSCallback() {					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {							
						//System.out.println( response.getStatus() );
						if (response.getStatus() < 0) {
			                // Error handling here
							UIMessages.showSimpleErrorValidation();			 
			            } else {
			                // Normal processing here
							final String id = Utilities.getStringFromJson(rawData, "cugp_id");	
							preferenceSelectItem.setValue( id );
					        SetState( record.getAttribute("value") );  
							winModal.hide();	
							UIMessages.showSuccess();	
			            }						
					}
				});
            }  
        });	
		CatalogGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (CatalogGrid.getSelectedRecords().length >=1){
					importView.setDisabled(false);
				} else 
					importView.setDisabled(true);
			}
		});
		CatalogGrid.addDataArrivedHandler(new DataArrivedHandler() {
			@Override
			public void onDataArrived(DataArrivedEvent event) {
				importView.setDisabled(true);
			}
		});
		
        ToolStrip CatalogGridToolStrip = new ToolStrip();  
        CatalogGridToolStrip.setWidth(400);
		CatalogGridToolStrip.addButton(importView);  

		winModal.addItem(CatalogGridToolStrip);
		winModal.addItem(CatalogGrid);
		winModal.centerInPage();
		winModal.show();
		winModal.centerInPage();
		
	}
	
	protected void IeditView(Record record) {

		final Window winModal = new Window();
		winModal.setAutoSize(true);
		winModal.setTitle("Edit View");
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);

		winModal.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClickEvent event) {
				winModal.destroy();
			}
		});
			
		final DynamicForm form = new DynamicForm();
		form.setHeight100();
		form.setWidth100();
		form.setPadding(5);

        TextItem Title = new TextItem();
        Title.setName("title");
        Title.setTitle("Title");  
        Title.setRequired(true);   

        TextAreaItem Descripcion = new TextAreaItem();  
        Descripcion.setName("description");
        Descripcion.setTitle("Description");  
        Descripcion.setRequired(true);  

		LinkedHashMap<String,Boolean> map = new LinkedHashMap<String,Boolean>();
		map.put("0",false);
		map.put("1",true);
        CheckboxItem isPublic = new CheckboxItem();   
        isPublic.setName("is_public");
        isPublic.setTitle("Is Public"); 
        isPublic.setValueMap(map);
        
        form.setFields(Title,Descripcion,isPublic);

		form.setDataSource(preferencesDS);
		form.editRecord(record);
		
        final IButton saveButton = new IButton("Save");  
        saveButton.addClickHandler(new ClickHandler() {  
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {  
                if(form.validate()) {
                	saveButton.disable();	
                	final Record record = form.getValuesAsRecord();
                	savePreference( record , new DSCallback() {								
            			@Override
            			public void execute(DSResponse response, Object rawData, DSRequest request) {
							//System.out.println( response.getStatus() );
							if (response.getStatus() < 0) {
				                // Error handling here
								UIMessages.showSimpleErrorValidation();			 
				            } else {
				                // Normal processing here
				            	winModal.hide();	
				            	UIMessages.showSuccess();	
				            }
            			}
            		});
                }  
            }  
        });  

		winModal.addItem(form);
		winModal.addItem(saveButton);
		winModal.centerInPage();
		winModal.show();
		winModal.centerInPage();
	}
	
	/*
	 * UI to Import presset.
	 */
	private void pv_Import() {

		final Window winModal = new Window();
		winModal.setAutoSize(true);
		winModal.setTitle("Import Presset");
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);

		winModal.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClickEvent event) {
				winModal.destroy();
			}
		});
	
		// initialise the hidden frame
		NamedFrame frame = new NamedFrame("upload_iframe");
		frame.setWidth("1x");
		frame.setHeight("1px");
		frame.setVisible(false);
		
		final DynamicForm form = new DynamicForm();
		form.setTarget("upload_iframe");
		form.setAction( namespaces.mod_grid_perspectives + "?save=1&" + namespaces.act_var + "=" + namespaces.importt );
		form.setHeight100();
		form.setWidth100();
		form.setPadding(5);
		form.setLayoutAlign(VerticalAlignment.BOTTOM);
		
		UploadItem file = new UploadItem("value");
		file.setTitle("File");
		final HiddenItem callbackItem = new HiddenItem("callbackName");
		
		 
		ButtonItem continueItem = new ButtonItem();
		continueItem.setTitle("Save");
		continueItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
					@Override
					public void onClick( com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
						String callbackName = JavaScriptMethodHelper.registerCallbackFunction(new JavaScriptMethodCallback() {
							public void execute(JavaScriptObject obj) {
								preferenceSelectItem.fetchData();
								winModal.hide();
							}		
						});
						callbackItem.setValue(callbackName);
						form.submitForm();
					}
				});
		form.setFields(  callbackItem, file,	continueItem);

		winModal.addItem(frame);
		winModal.addItem(form);
		winModal.centerInPage();
		winModal.show();
		winModal.centerInPage();
	}

	public String getPreferenceCookieId() {
		return preferenceCookieId;
	}

	public void setPreferenceCookieId(String preferenceCookieId) {
		//System.out.println( " setPreferenceCookieId - " + preferenceCookieId+"_select");
		this.preferenceCookieId = preferenceCookieId+"_select";
	}
		
}
