package com.mocha.vaadin.entity.view;

import java.util.List;

import com.coral.vaadin.view.template.sat.panel.impl.SearchPanel;
import com.coral.vaadin.widget.fields.FieldStatus;
import com.google.common.collect.Lists;
import com.mocha.ib.model.InsuranceCustomerServe;

public class InsCustomerServeSearch extends SearchPanel {
	
	private List entities = Lists.newArrayList();


	public void build() {
		FieldStatus fieldStatus = null;
		fieldStatus = FieldStatus.create().setLabel("Customer Name").setPath("customerName").setType("String");
		createFieldWidget(fieldStatus);
		
		fieldStatus = FieldStatus.create().setLabel("Type").setPath("type").setType("String");
		createFieldWidget(fieldStatus);
		
		fieldStatus = FieldStatus.create().setLabel("Date").setPath("date").setType("String");
		createFieldWidget(fieldStatus);
		
	}

	public Class getEntityCardClass() {
		return InsCustomerServeSearchCard.class;
	}
	
	public String getViewerTitle() {
		return "Search Insurance Customer Serve";
	}
	
	@Override
	public List getEntityList() {
		return entities;
	}
	
	@Override
	public void setValue(Object value) {
		if(value != null) {
			entities = (List) value;
		}
	}

}

