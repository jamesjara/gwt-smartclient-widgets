package com.pvgwt.widgets.client.utils;

import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.layout.HLayout;

/*
 * Class to create an empty filter, used on derivated columns because 
 * a derivated columns always set textfield item as a filter , creating 
 * an issue.
 */
public class CustomEmptyFilter extends CanvasItem {
	
	public CustomEmptyFilter() {
	
		setCanvas( new HLayout() );
		
	}

}
