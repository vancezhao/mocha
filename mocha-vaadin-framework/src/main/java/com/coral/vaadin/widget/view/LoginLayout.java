package com.coral.vaadin.widget.view;

import org.vaadin.jouni.animator.Disclosure;

import com.coral.foundation.utils.StrUtils;
import com.coral.vaadin.widget.Field;
import com.coral.vaadin.widget.field.ActionButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class LoginLayout extends VerticalLayout {
	public static String WRAP_TYPE = "wrap";
	public static String TITLE_TYPE = "title";
	
	private Disclosure wrap;
	private GridLayout gridLayout;
	private VerticalLayout buttonLayout;
	private HorizontalLayout btnsLayout;
	private int columns;
	
	public LoginLayout(int columns, int rows) {
		this(null,columns,rows);
	}
    public LoginLayout(String label, int columns, int rows) {
    	this(null,columns,rows,null);
    }
    
    public LoginLayout(String label, int columns, int rows, String type) {
    	this.columns = columns;
    	this.setWidth("100%");
    	this.setSpacing(true);
//    	this.setStyleName("content");
    	
    	
    	gridLayout = new GridLayout(columns, rows);
    	
    	gridLayout.setComponentAlignment(this, Alignment.MIDDLE_CENTER);
    	
    	gridLayout.setMargin(false,true,false,true);
    	gridLayout.setWidth("100%");
    	gridLayout.setHeight("50%");
    	gridLayout.setSpacing(true);
    	gridLayout.setDescription("sample_descirption_here");
    	gridLayout.setStyleName("login_content");
    	
    	
    	
    	if(WRAP_TYPE.equals(type)) {
//    		VerticalLayout vLayout = new VerticalLayout();
//            vLayout.setWidth("100%");
	    	wrap = new Disclosure(label,gridLayout);
	    	wrap.setWidth("100%");
	    	wrap.open();
	    	addComponent(wrap);
//	    	addComponent(vLayout);
    	} else {
    		if(!StrUtils.isEmpty(label)) {
    			if(TITLE_TYPE.equals(type)) {
    				addComponent(createTitleSectionLabel(label));
    			} else {
    				addComponent(createSectionLabel(label));
    			}
	    	}
    		addComponent(gridLayout);
    	}
    	
    	
    }
    
    public Label createSectionLabel(String label) {
	    Label sectionLabel = new Label(label);
	    sectionLabel.setStyleName("c-section-title");
	    return sectionLabel;
	}
    
    public Label createTitleSectionLabel(String label) {
	    Label sectionLabel = new Label(label);
	    sectionLabel.setStyleName("c-section-title-normal");
	    return sectionLabel;
	}
    
    public void addField(Component field) {
    	gridLayout.addComponent(field);
    	gridLayout.setColumnExpandRatio(1, 3);
    	gridLayout.setColumnExpandRatio(2, 3);
    	gridLayout.setColumnExpandRatio(3, 3);
    	gridLayout.setComponentAlignment(field, Alignment.MIDDLE_LEFT);
    }
    
    
    public void addField(Component field,Alignment alignment) {
    	gridLayout.addComponent(field);
    	gridLayout.setColumnExpandRatio(1, 3);
    	gridLayout.setColumnExpandRatio(2, 3);
    	gridLayout.setColumnExpandRatio(3, 3);
    	gridLayout.setComponentAlignment(field, alignment);
    }
    
    public void addField(Component field, int col1, int row1, int col2, int row2) {
    	gridLayout.addComponent(field,col1,row1,col2,row2);
    }
    
    public void setButtons(ActionButton... buttons) {
         for(ActionButton button : buttons) {
        	 addButton(button);
         }
    }
    
    public void addButton(ActionButton button) {
    	if(buttonLayout == null) {
    		buttonLayout = new VerticalLayout();
    		buttonLayout.setMargin(false, true, false, false);
    		buttonLayout.setWidth("100%");
    		addComponent(buttonLayout);
    		btnsLayout = new HorizontalLayout();
            btnsLayout.setSpacing(true);
            btnsLayout.setMargin(false, true, false, false);
            buttonLayout.addComponent(btnsLayout);
            buttonLayout.setComponentAlignment(btnsLayout, Alignment.BOTTOM_LEFT);
    	}
        btnsLayout.addComponent(button);
    }
    
    public void setSectionMargin(boolean margin) {
    	if(margin) {
    		gridLayout.setMargin(true);
    	} else {
    		gridLayout.setMargin(false,true,false,true);
    	}
    }
	/**
	 * @return the columns
	 */
	public String getFieldWidth() {
		if(columns == 2) {
			return Field.TWO_COLUMN_WIDTH;
		} else if(columns == 3) {
			return Field.THREE_COLUMN_WIDTH;
		}
		return null;
	}
	
	public void open() {
		if(wrap != null) {
			wrap.open();
		}
	}
	
	public void hide() {
		if(wrap != null) {
			wrap.close();
		}
	}
}
