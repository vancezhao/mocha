/**
 * 
 */
package com.mocha.report;

import java.util.ArrayList;
import java.util.List;

import com.coral.vaadin.widget.Viewer;
import com.coral.vaadin.widget.WidgetFactory;
import com.coral.vaadin.widget.component.ToolbarAdvance;
import com.coral.vaadin.widget.view.CommonViewer;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author Coral
 * 
 */
public class ReportDisplayViewer extends CommonViewer implements Viewer {

	private String reportTitle = "Achieved Campaign Report";
	private Button backLink = WidgetFactory.createLink("Back to report list");
	private Button editButton;
	private Button newButton;
	private Button deleteButton;
	private Long reportId;
	private ReportCategoryListener listener;
	
	private IndexedContainer contactContainer = new IndexedContainer();


	public ReportDisplayViewer() {
	
	}

	public ReportDisplayViewer(Long reportId) {
		this.reportId=reportId;
	}

	public void attach() {
		super.attach();

		ToolbarAdvance toolbar = new ToolbarAdvance();
		backLink.setIcon(new ThemeResource("icons/back.png"));
		setEditButton(WidgetFactory.createButton("Edit"));
		setNewButton(WidgetFactory.createButton("Create"));
		setDeleteButton(WidgetFactory.createButton("Delete"));
		toolbar.setNeedRightSeperate(false);
		toolbar.setLeftAlignment(Alignment.MIDDLE_LEFT);
		toolbar.addLeftComponent(backLink);
		toolbar.addRightComponent(getEditButton());
		toolbar.addRightComponent(getNewButton());
		toolbar.addRightComponent(getDeleteButton());
		
		
		this.addComponent(toolbar);

		VerticalLayout filterLayout = new VerticalLayout();
		filterLayout.addStyleName("report-filter-layout");
		filterLayout.setWidth("745px");
		HorizontalLayout filterFieldLayout = new HorizontalLayout();
		filterFieldLayout.setSpacing(true);
		filterFieldLayout.addComponent(getAvailableColumnField());
		filterFieldLayout.addComponent(WidgetFactory
				.createDateField("Start Date"));
		filterFieldLayout.addComponent(WidgetFactory
				.createDateField("End Date"));
		Button filterBtn = WidgetFactory.createButton("Filter");
		filterFieldLayout.addComponent(filterBtn);
		filterFieldLayout.setComponentAlignment(filterBtn,
				Alignment.BOTTOM_LEFT);
		filterLayout.addComponent(filterFieldLayout);
		this.addComponent(filterLayout);

		// table area
		VerticalLayout reportLayout = new VerticalLayout();
		reportLayout.addStyleName("report-table-layout");
		reportLayout.setWidth("745px");

		Table table = new Table();
		table.setWidth("745px");
		table.setHeight("128px");

		
		if (getReportId() == null) {		
		
			getContactContainer().addContainerProperty("Customer Name",
					String.class, "");
			getContactContainer().addContainerProperty("Serve Type", String.class,
					"");
			getContactContainer().addContainerProperty("Serve Date", String.class,
					"");
			setDemoReportData(getContactContainer());
		}

		table.setContainerDataSource(getContactContainer());
		reportLayout.addComponent(table);
		this.addComponent(reportLayout);
	}

	

	public ComboBox getAvailableColumnField() {
		List<String> datas = new ArrayList<String>();
		datas.add("Serve Date");
		datas.add("Create Date");
		ComboBox box = new ComboBox("Column", datas);
		return box;
	}

	@Override
	public String getViewerTitle() {
		return reportTitle;
	}

	public void setDemoReportData(IndexedContainer contactContainer) {
		String c = "Customer Name";
		String t = "Serve Type";
		String d = "Serve Date";
		Item item = contactContainer.addItem("1");
		item.getItemProperty(c).setValue("Judy");
		item.getItemProperty(t).setValue("Call");
		item.getItemProperty(d).setValue("2012-10-12");

		item = contactContainer.addItem("2");
		item.getItemProperty(c).setValue("Caroline");
		item.getItemProperty(t).setValue("Call");
		item.getItemProperty(d).setValue("2012-5-20");

		item = contactContainer.addItem("3");
		item.getItemProperty(c).setValue("Victoria");
		item.getItemProperty(t).setValue("Visit");
		item.getItemProperty(d).setValue("2013-1-1");

		item = contactContainer.addItem("4");
		item.getItemProperty(c).setValue("Hellen");
		item.getItemProperty(t).setValue("Call");
		item.getItemProperty(d).setValue("2013-1-5");

		item = contactContainer.addItem("5");
		item.getItemProperty(c).setValue("Ivy");
		item.getItemProperty(t).setValue("Visit");
		item.getItemProperty(d).setValue("2013-2-4");
	}

	/**
	 * @return the backLink
	 */
	public Button getBackLink() {
		return backLink;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public IndexedContainer getContactContainer() {
		return contactContainer;
	}

	public void setContactContainer(IndexedContainer contactContainer) {
		this.contactContainer = contactContainer;
	}

	public Button getEditButton() {
		return editButton;
	}

	public void setEditButton(Button editButton) {
		this.editButton = editButton;
	}

	public Button getNewButton() {
		return newButton;
	}

	public void setNewButton(Button newButton) {
		this.newButton = newButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

}
