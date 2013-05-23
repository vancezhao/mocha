/**
 * 
 */
package com.coral.vaadin.widget;

import com.coral.foundation.constant.RuntimeConstant;
import com.coral.vaadin.widget.listener.EnterClickListener;
import com.vaadin.data.Property;
import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Coral
 *
 */
public class WidgetFactory {
	
	/**
	 * Create Mocha button.
	 */
	public static Button createButton(String caption) {
		return createButton(caption, null);
	}
	public static Button createButton(String caption, Object data) {
		return createButton(caption, null, null);
	}
	public static Button createButton(String caption, Object data, ClickListener listener) {
		Button mochaButton = new NativeButton(caption);
		mochaButton.addStyleName("mocha-button");
		mochaButton.setData(data);
		if(listener != null) {
			mochaButton.addListener(listener);
		}
		return mochaButton;
	}

	/**
	 * Create Link button 
	 */
	public static Button createLink(String caption) {
		return createLink(caption, null);
	}
	public static Button createLink(String caption, Object data) {
		return createLink(caption, data, null);
	}
	public static Button createLink(String caption, Object data, ClickListener listener) {
		Button linkBtn = new Button(caption);
		linkBtn.addStyleName(BaseTheme.BUTTON_LINK);
		linkBtn.setData(data);
		if(listener != null) {
			linkBtn.addListener(listener);
		}
		return linkBtn;
	}

	/**
	 * Create label 
	 */
	public static Label createCaptionLabel(String caption, String value) {
		Label label = new Label();
		label.setCaption(caption + ":");
		label.setValue(value);
		return label;
	}
	public static Label createCaptionLabel(String caption, Property property) {
		Label label = new Label();
		label.setCaption(caption + ":");
		label.setPropertyDataSource(property);
		return label;
	}
	public static Label createLabel(String value) {
		Label label = new Label(value);
		return label;
	}
	
	/**
	 * Create binding property
	 */
	public static Property createProperty(Object instance, String propertyName) { 
		Property property = new NestedMethodProperty(instance, propertyName);
		return property;
	}
	
	/**
	 * Create textfield
	 */
	private static String defaultTextFieldWidth = "210px";
	
	public static TextField createSearchTextField(String inputPrompt, String data, final EnterClickListener enterClickListener) {
		final TextField textField = new TextField();
		textField.setImmediate(true);
		textField.setNullRepresentation("");
		textField.setData(data);
		textField.setInputPrompt(inputPrompt);
		textField.setWidth(defaultTextFieldWidth);
		textField.addShortcutListener(new ShortcutListener(data, KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				enterClickListener.handleEnter((String)textField.getValue());
			}
		});
//		textField.addListener(new BlurListener() {
//			@Override
//			public void blur(BlurEvent event) {
//				enterClickListener.handleEnter((String)textField.getValue());
//			}
//		});
		return textField; 
	}
	public static TextField createTextField(String caption, Property property) {
		TextField textField = new TextField(caption);
		textField.setWidth(defaultTextFieldWidth);
		textField.setPropertyDataSource(property);
		textField.setNullRepresentation("");
		return textField;
	}
	public static PasswordField createPasswordField(String caption) {
		PasswordField passwordField = new PasswordField(caption);
		passwordField.setWidth(defaultTextFieldWidth);
		return passwordField;
	}
	
	/**
	 * Create combobox 
	 */
	public static ComboBox createLanguageCombo(String caption, Property property) {
		ComboBox comboBox = new ComboBox(caption);
		comboBox.setImmediate(true);
		String[] langs = RuntimeConstant.SUPPORTED_LANGUAGES;
		for(String lang : langs) {
			comboBox.addItem(lang);
		}
		if(property != null) {
			comboBox.setPropertyDataSource(property);
		}
		comboBox.setWidth(defaultTextFieldWidth);
		return comboBox;
	}
	
	/**
	 * Create Window
	 */
	public static Window createWindow(String caption, String windowWidth) {
		Window window = new Window(caption);
		window.center();
		window.setWidth(windowWidth);
		window.addStyleName(Reindeer.WINDOW_LIGHT);
		return window;
	}
	
	/**
	 * Create Date 
	 */
	public static DateField createDateField(String caption) {
		DateField datePickup = new DateField(caption);
		datePickup.setImmediate(true);
		datePickup.setResolution(InlineDateField.RESOLUTION_DAY);
		return datePickup;
	}
}