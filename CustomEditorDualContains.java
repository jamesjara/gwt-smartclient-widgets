package com.pvgwt.widgets.client.utils;

import com.pvgwt.widgets.client.utils.namespaces;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

/*
 * Class to generate a Custom Filter Type, to filter based on contains or not contains text.
 * TODO for some reason cant be used multiple times.
 */
public class CustomEditorDualContains extends TextItem {

	private OperatorId Operator = OperatorId.ICONTAINS;
	private static CustomEditorDualContains self;
	private static FormItemIcon icon = new FormItemIcon(); 
	public CustomEditorDualContains(String string) {

		self = this;

		self.setName(string);
		self.setOperator( Operator );
		
		self.setHint("Filter by");
		self.setShowHintInField(true);

		icon.setSrc( namespaces.resources_dir + "ICONTAINS.png" );
		
		icon.addFormItemClickHandler(new FormItemClickHandler() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				OperatorId value = push();
				event.getItem().setOperator(  value  );
				event.getIcon().setSrc(  namespaces.resources_dir + value.toString() + ".png" );//ICONTAINS.png,INOT_CONTAINS.png			
				event.getItem().setIcons(icon);	//Todo,for some reason we need to set icon again to redraw.		
			}
		});
		self.setIcons( icon );		
	}

	protected OperatorId push() {
		if ( Operator.equals(OperatorId.ICONTAINS) ) {
			Operator = OperatorId.INOT_CONTAINS;
		} else {
			Operator = OperatorId.ICONTAINS;			
		}
		self.setOperator(Operator);				
		self.redraw();
		//System.out.println( Operator.toString()  ); 
		return Operator;
	}

}
