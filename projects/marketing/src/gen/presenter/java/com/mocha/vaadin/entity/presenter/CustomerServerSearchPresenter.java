package com.mocha.vaadin.entity.presenter;

import java.util.List;

import com.coral.foundation.core.impl.MochaEventBus;
import com.coral.foundation.jpa.search.SearchFilter;
import com.coral.foundation.jpa.search.SearchFilterBuilder;
import com.coral.foundation.jpa.search.SearchFilterFactory;
import com.coral.foundation.persistence.BaseEntity;
import com.coral.foundation.spring.bean.SpringContextUtils;
import com.coral.vaadin.controller.Presenter;
import com.coral.vaadin.view.template.sat.panel.impl.SearchPanel.SearchListener;
import com.coral.vaadin.widget.component.GlobleSearchWidget.GlobleSearchListener;
import com.coral.vaadin.widget.view.AppCommonPresenter;
import com.mocha.marketing.dao.ServeDao;
import com.mocha.marketing.model.Serve;
import com.mocha.vaadin.entity.view.CustomerServerSearch;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class CustomerServerSearchPresenter extends AppCommonPresenter implements Presenter {

	private ServeDao dao = SpringContextUtils.getBean(ServeDao.class);
	
	public CustomerServerSearchPresenter(MochaEventBus eventBus) {
		this.eventBus = eventBus;
		this.viewer = new CustomerServerSearch();
		// load all data.
		List entities = dao.loadAll();
		viewer.setValue(entities);
	}

	@Override
	public String getPresenterName() {
		return "CustomerServerSearch";
	}
	
	@Override
	public void bind() {
		final CustomerServerSearch customerServerSearch = (CustomerServerSearch) viewer;
		customerServerSearch.getConditionPanel().getCreateBtn().addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				postViewer("CustomerServerView");
			}
		});
		customerServerSearch.getConditionPanel().getGlobleSearchWidget().setListener(new GlobleSearchListener() {
			@Override
			public void search(String condition) {
				List<Serve> customers = dao.fuzzySearch(buildFuzzySearch(condition));
				customerServerSearch.setValue(customers);
				customerServerSearch.buildSearchCardPanel();
			}
		});
		customerServerSearch.setListener(new SearchListener() {
			@Override
			public void handleAction(String name, String action, Object entity) {
				if("Edit".equals(action)) {
					postViewer("CustomerServerView",entity);
				} else if("Delete".equals(action)) {
					remove(entity);
					postViewer("CustomerServerSearch");
				}
			}
			@Override
			public void cardClick(Object value) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void remove(Object entity) {
		if(entity != null) {
			dao.remove(((BaseEntity)entity).getID());
		}
	}
	
	public SearchFilterBuilder buildFuzzySearch(String condition) {
		SearchFilterBuilder filterBuilder = SearchFilterFactory.buildFuzzySearchFilter(Serve.class);
		filterBuilder.getSearchFilters().add(SearchFilter.like("customerName", condition));
		filterBuilder.getSearchFilters().add(SearchFilter.like("type", condition));
		filterBuilder.getSearchFilters().add(SearchFilter.like("date", condition));
		return filterBuilder;
	}
	

}

