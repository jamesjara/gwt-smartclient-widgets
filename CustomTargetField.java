package com.pvgwt.widgets.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemInitHandler;

/*
 * IMCOMPLETE: 
 * ignore this class
 */
public class CustomTargetField  extends CanvasItem {
	
    private DynamicForm form;
    
    public CustomTargetField (JavaScriptObject config) { }
    
    public CustomTargetField(String name) {
    	super();
    	this.setShouldSaveValue(true);
    	
		setInitHandler(new FormItemInitHandler() {	
			@Override
			public void onInit(FormItem item) {
                // correct
                //new CustomTargetField(item.getJsObj()).customMethod();
                // incorrect, will throw an error
                // ((MyCustomItem)item).customMethod();
								
                form = new DynamicForm();
                form.setNumCols(2);

                TextItem asd = new TextItem();
                
                
		        SelectItem divisionItem = new SelectItem();  
		        divisionItem.setValueMap("Marketing", "Sales", "Manufacturing", "Services");  
                
		        
                
                form.setItems(asd, divisionItem);

                //setCanvas(form);
                
				CanvasItem canvasItem = new CanvasItem(item.getJsObj());
				//System.out.println("FormItemInitHandler.onInit called for FormItem: " + canvasItem);
				canvasItem.setCanvas( form );
			}
		});

    }

	void customMethod() {  }

}
