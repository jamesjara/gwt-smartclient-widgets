package com.pvgwt.widgets.client.utils;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/*
 * Class to create a Mask for different actiones that takes time to wait for the user.
 * This class should block al interface while actiones be processing.
 */
public class FormatWindow extends Window {

	public FormatWindow(final ListGridField column) {

		// setEdgeSize(2);

		setIsModal(true);
		setShowModalMask(true);
		setAutoCenter(true);

		final DynamicForm asd = new DynamicForm();
		
		final SpinnerItem spinnerItem = new SpinnerItem();  
        spinnerItem.setTitle("Decimal Precision");  
        spinnerItem.setDefaultValue(5);  
        spinnerItem.setMin(0);  
        spinnerItem.setMax(5);  
        spinnerItem.setStep(0.5f); 
        
        IButton saveButton = new IButton("Save");  
        saveButton.addClickHandler(new ClickHandler() {  
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {  
                if(asd.validate()) {        
	  				final Integer presicion = Integer.parseInt(spinnerItem.getValueAsString());	  				
	  				column.setAttribute("presicion", presicion);	
                }  
            }  
        });  		
		
		asd.setItems(spinnerItem);

		addItem(asd);
        addMember(saveButton);  
        

		// setAutoSize(true);
		//setShowTitle(false);
		//setShowHeader(false);

		setCanDrag(false);
		setCanDragReposition(false);
		setCanDragResize(false);

		//setShowCloseButton(false);
		//setShowMinimizeButton(false);

		///setWidth(63);
		//setHeight(32);
		
		setPadding(5);

	}

	public void show() {
		animateShow(AnimationEffect.FLY);
	}

	public void hide() {
		animateHide(AnimationEffect.SLIDE);
	}

}
