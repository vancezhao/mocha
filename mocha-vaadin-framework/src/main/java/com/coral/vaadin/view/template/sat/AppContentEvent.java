/**
 * 
 */
package com.coral.vaadin.view.template.sat;

/**
 * @author Coral
 *
 */
public class AppContentEvent {

	String viewName;
	String customizeClass;
	Long reportId;
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return the customizeClass
	 */
	public String getCustomizeClass() {
		return customizeClass;
	}
	/**
	 * @param customizeClass the customizeClass to set
	 */
	public void setCustomizeClass(String customizeClass) {
		this.customizeClass = customizeClass;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	
}
