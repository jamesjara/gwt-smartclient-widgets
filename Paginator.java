package com.pvgwt.widgets.client.utils;

import java.util.LinkedHashMap;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class Paginator extends ToolStrip {
	
	private String str_img_bt_start		 = "";
	private String str_img_bt_end		 = "";
	private String str_img_bt_next		 = "";
	private String str_img_bt_forward	 = "";

	private int curentPage	= 1;
	private int pageSize	= 300;

	protected TextItem txt_count;
	protected ImgButton bt_start;
	protected ImgButton bt_next;
	protected ImgButton bt_forward;
	protected ImgButton bt_end;
	protected Label Pag_totalLabel;
	protected Label totalsLabel;
		
	private PagerGrid 		grid;
	
	public Paginator( PagerGrid listGrid ) {
		this.grid = listGrid;
    	
		str_img_bt_start 		= namespaces.resources_dir + "pag_start2.png";
		str_img_bt_end 			= namespaces.resources_dir + "pag_end2.png";
		str_img_bt_next 		= namespaces.resources_dir + "pag_end.png";
		str_img_bt_forward 		= namespaces.resources_dir + "pag_start.png";
		
		bt_start = new ImgButton();
		bt_start.setMargin(1);
		bt_start.setWidth(16);
		bt_start.setSrc( str_img_bt_start );
		bt_start.setShowRollOver(true);
		bt_start.setShowDown(false);
		bt_start.setShowFocused(false);
		bt_start.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				changePage(1);				
			}
		});

		bt_forward = new ImgButton();
		bt_forward.setMargin(1);
		bt_forward.setWidth(16);
		bt_forward.setSrc( str_img_bt_forward );
		bt_forward.setShowRollOver(true);
        bt_forward.setShowDown(false);
        bt_forward.setShowFocused(false);
		bt_forward.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				changePage(curentPage - 1);			
			}
		});

		bt_next = new ImgButton();
		bt_next.setMargin(1);
		bt_next.setWidth(16);
		bt_next.setSrc(str_img_bt_next  );
		bt_next.setShowRollOver(true);
		bt_next.setShowDown(false);
		bt_next.setShowFocused(false);
		bt_next.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				changePage(curentPage + 1);			
			}
		});

		bt_end = new ImgButton();
		bt_end.setMargin(1);
		bt_end.setWidth(16);
		bt_end.setSrc( str_img_bt_end );
		bt_end.setShowRollOver(true);
		bt_end.setShowDown(false);
		bt_end.setShowFocused(false);
		bt_end.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				changePage( grid.getPagerDataSource().getTotalPages ());
			}
		});
		
		txt_count = new TextItem();
		txt_count.setShowTitle(false);
		txt_count.setWidth(40);
		txt_count.addChangedHandler(new ChangedHandler() {			
			@Override
			public void onChanged(ChangedEvent event) {
				if (event.getValue() != null)
				{
					changePage(Integer.parseInt(event.getValue().toString()));
				}				
			}
		});

		Pag_totalLabel = new Label();
		Pag_totalLabel.setMargin(2);
		Pag_totalLabel.setWrap(false);
		Pag_totalLabel.setAutoWidth();

		totalsLabel = new Label();
		totalsLabel.setMargin(2);
		totalsLabel.setWrap(false);
		totalsLabel.setAutoWidth();
		totalsLabel.setContents("0 records");
		
		final ComboBoxItem pager = new ComboBoxItem("NewCombo");		
        pager.setShowTitle(false);  
        pager.setWidth(70);  
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();   
        valueMap.put("300", "300");  
        valueMap.put("600", "600");  
        valueMap.put("1500", "1500");  
        valueMap.put("3000", "3000");  
        valueMap.put("5000", "5000");   
		pager.setValueMap(valueMap);
		pager.setDefaultToFirstOption(true);
		pager.addChangedHandler(new ChangedHandler() {			
			@Override
			public void onChanged(ChangedEvent event) {	
				///ds.setPagesize(  Integer.parseInt( pager.getValueAsString() )  );
				pageSize =  Integer.parseInt( pager.getValueAsString() ) ;
				changePage(curentPage);
			}
		});
		final DynamicForm pager_form = new DynamicForm();		
		pager_form.setFields(pager);

		addMember(totalsLabel);	
		addSeparator();  
		addMember(bt_start);
		addMember(bt_forward);
		addFormItem(txt_count);
		addMember(Pag_totalLabel);
		addMember(bt_next);
		addMember(bt_end);	
		addSeparator();  	
		addMember(pager_form);
		
		
		setBackgroundColor("red");		
		setAlign(Alignment.RIGHT);			

		setBorder("0px");

	}

	public void changePage( int toPage )
	{
		/*if (curentPage == this.curentPage)
		{
			updatePagerControls(pages);
			return;
		}*/
		grid.getPagerDataSource().setCurrentPage( 	toPage	   );
		grid.getPagerDataSource().setPagesize( 		pageSize   );
		grid.PagerfetchData( true ); 
	}
	
	
	public void refreshUI() {
		
    	Integer TotalPages	= grid.getPagerDataSource().getTotalPages ();
    	Integer TotalRecords= grid.getPagerDataSource().getTotalRecors() ;
    	curentPage			= grid.getPagerDataSource().getCurrentPage() ;
    	 
		txt_count.setValue(curentPage);	
		if ((curentPage == 1)||(curentPage==0))
		{
			if (!bt_start.isDisabled())
				bt_start.setDisabled(true);	
			if (!bt_forward.isDisabled())
				bt_forward.setDisabled(true);	
				
		} else
		{
			if (bt_start.isDisabled())	
				bt_start.setDisabled(false);						
			if (bt_forward.isDisabled())	
				bt_forward.setDisabled(false);	
		}	
		if (curentPage == TotalPages)
		{
			if (!bt_end.isDisabled())
				bt_end.setDisabled(true);	
			if (!bt_next.isDisabled())
				bt_next.setDisabled(true);	
		} else
		{
			if (bt_end.isDisabled())	
				bt_end.setDisabled(false);						
			if (bt_next.isDisabled())	
				bt_next.setDisabled(false);	
		}
		if (TotalPages >= 4)
		{
			if (txt_count.isDisabled())
				txt_count.setDisabled(false);	
		} else
		{
			if (!txt_count.isDisabled())
				txt_count.setDisabled(true);	
		}
		//Pag_totalLabel.setContents("(" + curentPage + "/" + TotalPages + "));
		totalsLabel.setContents( TotalRecords + " Records" );
		Pag_totalLabel.setContents(  "of" +" "+ TotalPages + " Pages" );
	
	}
	

	
	
	
}