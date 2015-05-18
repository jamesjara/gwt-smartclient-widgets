package com.pvgwt.widgets.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.JSON;

/*
 * Special datasource field to add new properties to each column.
 * Add custom attributtes here.
 */
public class SpecialDataSourceField extends DataSourceField {
	
	public SpecialDataSourceField(){
		super();
	}

	public SpecialDataSourceField(JavaScriptObject object) {
		super(object);
		
		//Custom Atributess
		String[] componentsj = JSOHelper.getProperties(object);
		for(  String e : componentsj ){ 
			if ( e.toString().equals("FilterEditorType") ){
				if ( JSOHelper.getAttribute( object , "FilterEditorType" ) != null ){
					if ( JSOHelper.getAttribute( object , "FilterEditorType" ).toString().equals("CustomEditorDualContains") ){
						//System.out.println(  JSOHelper.getAttribute( object , "name" ) + " es CustomEditorDualContains."  );
						CustomEditorDualContains filterEditorType = new CustomEditorDualContains( getName() );
						setFilterEditorType(filterEditorType);
					}
				}
			} 
			//Support to export visual or raw value
			if ( e.toString().equals("exportCellValue") ){
				if ( JSOHelper.getAttribute( object , "exportCellValue" ) != null ){
					setAttribute("exportCellValue",  JSOHelper.getAttributeAsBoolean( object , "exportCellValue" ));
				}
			} 
			//Support to add to the chart 
			if ( e.toString().equals("forChart") ){
				if ( JSOHelper.getAttribute( object , "forChart" ) != null ){
					setAttribute("forChart",  JSOHelper.getAttributeAsBoolean( object , "forChart" ));
					//setAttribute("isOnChart", true );
				}
			} 
			//Support if we have display it in the chart 
			if ( e.toString().equals("isOnChart") ){
				if ( JSOHelper.getAttribute( object , "isOnChart" ) != null ){
					setAttribute("isOnChart",  JSOHelper.getAttributeAsBoolean( object , "isOnChart" ));
				}
			} 
			
		}
		JavaScriptObject asJson =  getJsObj();	
		String s1 = JSON.encode(asJson);				
	}

	public void setFilterEditorType(CustomEditorDualContains filterEditorType) {
        String type = filterEditorType.getAttribute("editorType");
        if (type == null) type = filterEditorType.getType();
        setAttribute("filterEditorType", type);
        JavaScriptObject editorConfig = filterEditorType.getConfig();
        setAttribute("filterEditorProperties", editorConfig);
	}
}
