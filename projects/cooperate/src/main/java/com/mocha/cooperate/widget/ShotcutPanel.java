/**
 * 
 */
package com.mocha.cooperate.widget;

import java.util.List;
import java.util.Map;

import com.coral.foundation.core.impl.MochaEventBus;
import com.coral.foundation.utils.Message;
import com.coral.vaadin.controller.ContentChangeEvent;
import com.coral.vaadin.controller.PageChangeEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.mocha.cooperate.InnerStyle;
import com.mocha.cooperate.PresenterProperty;
import com.mocha.cooperate.SystemProperty;
import com.mocha.cooperate.model.Shotcut;
import com.mocha.cooperate.model.ShotcutItem;
import com.mocha.cooperate.page.event.HomePageEvent.ChangeHeadMenuStyleEvent;
import com.mocha.cooperate.page.event.HomePageEvent.ChangeShotCutEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Administrator
 *
 */
public class ShotcutPanel extends VerticalLayout {
	
	private MochaEventBus eventBus;
	private List<ShotcutSection> shotcutPanelList = Lists.newArrayList();
	private Message message;
	private List<Shotcut> shotcuts;
	private Map<String, Integer> notifyMap;
	
	public ShotcutPanel(MochaEventBus eventBus, List<Shotcut> shotcuts) {
		this.eventBus = eventBus; 
		this.shotcuts = shotcuts; 
		eventBus.register(this);
	}
	
	public void attach() {
		message = new Message(getApplication().getLocale());
//		List<Shotcut> shotcuts = ExampleData.getShotcut();
		for(Shotcut shotcut : shotcuts) {
			ShotcutSection shotcutPanel = new ShotcutSection(shotcut, eventBus);
			shotcutPanelList.add(shotcutPanel);
			this.addComponent(shotcutPanel);
		}
		// show the notification number.
		refreshNotification();
	}
	
	public void refreshNotification() {
		if(notifyMap != null) {
			for(String action : notifyMap.keySet()) {
				setActionNotifyNumber(action, notifyMap.get(action));
			}
		}
	}
	
	public void setActionNotifyNumber(String action, int number) {
		for(ShotcutSection section : shotcutPanelList) {
			section.setShotcutLabel(action, number);
		}
	}
	
	@Subscribe
	public void changeShotcutStyle(ChangeShotCutEvent event) {
		for(ShotcutSection section : shotcutPanelList) {
			for(NativeButton shotcutBtn :section.getShotcuts()) {
				ShotcutItem shotcut = (ShotcutItem)shotcutBtn.getData();
				if(event.getAction().equals(shotcut.getAction())) {
					shotcutBtn.addStyleName("v-nativebutton-selected");
				} else {
					shotcutBtn.removeStyleName("v-nativebutton-selected");
				}
			}
		}
	}

	public class ShotcutSection extends CustomComponent implements Button.ClickListener {
		private static final long serialVersionUID = 5763483966017706934L;
		private Shotcut shotcut;
		private MochaEventBus eventBus;
		private NativeButton notifyButton;
		private List<NativeButton> shotcuts = Lists.newArrayList();
		private Map<String, NativeButton> shotcutMaps = Maps.newHashMap();
		
		public ShotcutSection(Shotcut shotcut, MochaEventBus eventBus) {
			this.eventBus = eventBus;
			this.shotcut = shotcut;
		}
	
		@Override
	    public void attach() {
			super.attach();
			Panel panel = new Panel();
			panel.setSizeFull();
			panel.addStyleName(Reindeer.PANEL_LIGHT);
			panel.addStyleName("mocha-shotcut");
			VerticalLayout noMarginLayout = new VerticalLayout();
			noMarginLayout.setSizeFull();
			panel.setContent(noMarginLayout);
			// panel title.
			panel.setCaption(message.getString(shotcut.getTitle()));
			
			for(ShotcutItem item : shotcut.getShotcutItems()) {
				NativeButton itemButton = createNativeButton(item);
				panel.addComponent(itemButton);
			}
			setCompositionRoot(panel);
		}
		
		public NativeButton createNativeButton(ShotcutItem item) {
			NativeButton itemButton = new NativeButton(message.getString(item.getLabel()));
			itemButton.setHtmlContentAllowed(true);
			itemButton.setWidth("100%");
			String icon = item.getIcon();
			if(icon != null) {
				itemButton.setIcon(new ThemeResource(icon));
			}
			itemButton.setData(item);
			itemButton.addListener(this);
			if(PresenterProperty.HOME.equals(item.getAction())) {
				itemButton.addStyleName("v-nativebutton-selected");
			}
			if(PresenterProperty.NOTIFICATION.equals(item.getAction())) {
				notifyButton = itemButton;
			}
			shotcuts.add(itemButton);
			shotcutMaps.put(item.getAction(), itemButton);
			return itemButton;
		}
		
		public void setShotcutLabel(String action, int number) {
			NativeButton itemButton = shotcutMaps.get(action);
			if(itemButton != null) {
				ShotcutItem item = (ShotcutItem) itemButton.getData();
				if(item.getNumber() != number) {
					if(number == 0) {
						itemButton.setCaption(message.getString(item.getLabel()));
						item.setNumber(Long.valueOf(number));
					} else {
						itemButton.setCaption(message.getString(item.getLabel()) + "  <span style=\"" + InnerStyle.notify_Number + "\">" + number + "</span>");
						item.setNumber(Long.valueOf(number));
					}
				}
			}
		}

		@Override
		public void buttonClick(ClickEvent event) {
			Button btn = event.getButton();
			ShotcutItem item = (ShotcutItem) btn.getData();
			String action = item.getAction();
			if(SystemProperty.PAGE_TYPE.equals(item.getType())) {
				PageChangeEvent pageChangeEvent = new PageChangeEvent(action);
				eventBus.post(pageChangeEvent);
			} else {
				ContentChangeEvent changeEvent = new ContentChangeEvent();
				changeEvent.setPresenterName(action);
				eventBus.post(changeEvent);
			}
			// change the head menu to home page.
			changeToHomeMenuStyle();
			// change shotcut menu style.
			changeShotCutStyle(action);
		}
		
		public void changeToHomeMenuStyle() {
			ChangeHeadMenuStyleEvent event = new ChangeHeadMenuStyleEvent();
			event.setSelectedMenu(PresenterProperty.HOME);
			eventBus.post(event);
		}
		
		public void changeShotCutStyle(String action) {
			if(action == null) {
				return;
			}
			ChangeShotCutEvent event = new ChangeShotCutEvent();
			event.setAction(action);
			eventBus.post(event);
		}
		

		/**
		 * @return the notifyButton
		 */
		public NativeButton getNotifyButton() {
			return notifyButton;
		}

		/**
		 * @return the shotcuts
		 */
		public List<NativeButton> getShotcuts() {
			return shotcuts;
		}
	}

	/**
	 * @return the notifyMap
	 */
	public Map<String, Integer> getNotifyMap() {
		return notifyMap;
	}

	/**
	 * @param notifyMap the notifyMap to set
	 */
	public void setNotifyMap(Map<String, Integer> notifyMap) {
		this.notifyMap = notifyMap;
	}
}