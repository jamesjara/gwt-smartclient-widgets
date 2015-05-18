package com.pvgwt.widgets.client.utils;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;


public class FullScreenApp extends Window {
	
	private VLayout vLayout = new VLayout();
	
	public FullScreenApp(SectionStackSection stackSection ) {
         setTitle("Demo Application"); 
         setWidth100();
         setHeight100();
         setShowMinimizeButton(false);
         setShowCloseButton(true);
         setCanDragReposition(false);
         setCanDragResize(false);
         setShowShadow(false);         
         setZIndex(999999999);    
         
         
 		 SectionStack sectionStack = new SectionStack();
 		 sectionStack.setWidth100();
 		 sectionStack.setHeight100();
 		 sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
 		 sectionStack.setAnimateSections(true);
 		 sectionStack.setOverflow(Overflow.HIDDEN);
 		 
 		  	
 		 
 		 
 		 
 		 sectionStack.setSections(stackSection);
 		 
 		 
 		 vLayout.addMember(sectionStack); 		
 		 addItem( vLayout );
     }
	
	public FullScreenApp(VLayout vLayout ) {
		
	}
 }