package com.coral.foundation.report;

import java.util.Collection;

import com.coral.foundation.security.model.MochaReport;

public interface ReportService {
	//validate the markup template is valid
	public void validateReportTemplate(MochaReport mochaReport);
	//load report template from db
	public void loadReportTemplate(MochaReport mochaReport );
	//run the template report
	public Collection executeMochaReportQuery();
	//build report
	public String buildReport();
}