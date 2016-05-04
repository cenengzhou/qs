package com.gammon.qs.client.factory;

import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FieldFactory {
	
	public static DatePickerListenerAdapter updateDatePickerWidthListener(){
		return new DatePickerListenerAdapter(){
			@Override
			public void onRender(Component component) {
				updateDatePickerWidth();
				super.onRender(component);
			}			
		};
	}

	/**
	 * fix IE8 DateField show partial DatePicker
	 */
	private static native void updateDatePickerWidth() /*-{
		var ua = $wnd.navigator.userAgent;
		var msie = ua.indexOf("MSIE ");
		if (msie > 0) {
			$wnd.$('.x-form-trigger.x-form-date-trigger').on('click',function(){
				setTimeout(function(){
		   			$wnd.$('div.x-layer.x-menu.x-menu-plain.x-date-menu').outerWidth(177);
				}, 50);
	   	   	});
		}
	}-*/;
	
	public static DateField createDateField(){
		DateField field = new DateField();
		field.addListener(FieldFactory.updateDatePickerWidthListener());
		field.setAllowBlank(false);
		field.setSelectOnFocus(true);
		field.setFormat("j/n/Y");
		
		return field;
		
	}
	
	public static DateField createDateFieldAllowNull(){
		DateField field = new DateField();
		field.addListener(FieldFactory.updateDatePickerWidthListener());
		field.setAllowBlank(true);
		field.setSelectOnFocus(true);
		field.setFormat("j/n/Y");		
		return field;
		
	}
	
	public static DateField createDateTimeFieldAllowNull(){
		DateField field = new DateField();
		field.addListener(FieldFactory.updateDatePickerWidthListener());
		field.setAllowBlank(true);
		field.setSelectOnFocus(true);
		field.setFormat("Y-m-d H:i:s");
		return field;
		
	}
	
	public static NumberField createNegativeNumberField(int decimalPrecision){
		NumberField field = createNegativeNumberField();
		field.setDecimalPrecision(decimalPrecision);
		return field;
	}
	
	public static NumberField createNegativeNumberField(){
		NumberField field = new NumberField();
		field.setAllowBlank(false);
		field.setAllowNegative(true);
		field.setDecimalPrecision(4);
		field.setDecimalSeparator(".");
		field.setSelectOnFocus(true);		
		
		return field;
	}

	public static NumberField createNumberField(int decimalPrecision){
		NumberField field = createNumberField();
		field.setDecimalPrecision(decimalPrecision);
		return field;
	}
	
	public static NumberField createPositiveNumberField(int decimalPrecision){
		NumberField field = createNumberField();
		field.setDecimalPrecision(decimalPrecision);
		field.setAllowNegative(false);
		return field;
	}
	
	public static NumberField createNumberField() {
		NumberField field = new NumberField();
		field.setAllowBlank(false);
		field.setAllowNegative(false);
		field.setDecimalPrecision(4);
		field.setDecimalSeparator(".");
		field.setSelectOnFocus(true);
		
		/*
		field.addListener(new FieldListenerAdapter(){
			
			public void onFocus(Field field)
			{
				field.set
			}
		});
		*/
		return field;
	}

	public static NumberField createNumberFieldAllowNull(int decimalPrecision){
		NumberField field = createNumberFieldAllowNull();
		field.setDecimalPrecision(decimalPrecision);
		return field;
	}	
	
	public static NumberField createNumberFieldAllowNull() {
		NumberField field = new NumberField();
		//field.setAllowBlank(false);
		//field.setAllowNegative(false);
		field.setDecimalPrecision(3);
		field.setDecimalSeparator(".");
		field.setSelectOnFocus(true);
		return field;
	}
	
	public static TextField createTextField() {
		TextField field = new TextField();
		field.setAllowBlank(false);
		field.setSelectOnFocus(true);
		return field;
	}
	
	public static TextField createTextFieldAllowNull() {
		TextField field = new TextField();
		//field.setAllowBlank(false);
		field.setSelectOnFocus(true);
		return field;
	}
	
	public static ComboBox createComboBox(){
		final String valueField = "VALUE";
		final String displayField = "DISPLAY";

		ComboBox newComboBox = new ComboBox();
		newComboBox.setCtCls("table-cell");
		newComboBox.setMinChars(1);
		newComboBox.setDisplayField(displayField);
		newComboBox.setValueField(valueField);
		newComboBox.setEditable(true);
		newComboBox.setVisible(true);
		newComboBox.setForceSelection(false);
		newComboBox.setMode(ComboBox.LOCAL);
		newComboBox.setTriggerAction(ComboBox.ALL);
		newComboBox.setSelectOnFocus(true);		
		newComboBox.setTypeAhead(true);
		return newComboBox;
	}
	public static ToolTip createToolTip(){
		ToolTip tempToolTip = new ToolTip();
		tempToolTip.setDismissDelay(15000);
		tempToolTip.setWidth(200);
		tempToolTip.setTrackMouse(true);
		return tempToolTip;
	}
}
