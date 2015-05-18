package com.pvgwt.widgets.client.utils;

public class UIMessages {

	public static  void showSimpleErrorValidation( ) {
		notify( "Error!" , " ", namespaces.resources_dir + namespaces.failure );
	}
	
	public static  void showSuccess(  ) {
		notify( "Done!" , " ", namespaces.resources_dir + namespaces.success  );
	}
	
	public static native String notify( String title_val , String text_val , String icon )/*-{
		$wnd.$.gritter.add({
			title: 	title_val,
			text: 	text_val ,
			image : icon ,
			time: 3000
		});	
	}-*/;

}
