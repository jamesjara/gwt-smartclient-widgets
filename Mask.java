package com.pvgwt.widgets.client.utils;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;

/*
 * Class to create a Mask for different actiones that takes time to wait for the user.
 * This class should block al interface while actiones be processing.
 */
public class Mask extends Window {

	public Mask(String Contents) {

		// setEdgeSize(2);

		setIsModal(true);
		setShowModalMask(true);
		setAutoCenter(true);

		DynamicForm asd = new DynamicForm();

		StaticTextItem items = new StaticTextItem();
		items.setShowTitle(false);
		items.setValue(Contents);
		asd.setItems(items);

		addItem(asd);

		// setAutoSize(true);
		setShowTitle(false);
		setShowHeader(false);

		setCanDrag(false);
		setCanDragReposition(false);
		setCanDragResize(false);

		setShowCloseButton(false);
		setShowMinimizeButton(false);

		setWidth(110);
		setHeight(62);
		 

	}

	public void show() {
		animateShow(AnimationEffect.FLY);
	}

	public void hide() {
		animateHide(AnimationEffect.SLIDE);
	}

}
