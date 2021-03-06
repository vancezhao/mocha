/**
 * 
 */
package com.mocha.soicalAPI;

import java.util.ArrayList;
import java.util.List;

import com.coral.foundation.linkedin.LinkedinImpl;
import com.coral.foundation.oauth.APIKeys;
import com.coral.foundation.security.basic.dao.BasicUserDao;
import com.coral.foundation.security.model.BasicUser;
import com.coral.foundation.security.model.LinkedinConnection;
import com.coral.foundation.security.model.LinkedinPersonProfile;
import com.coral.foundation.security.model.SoicalApp;
import com.coral.foundation.spring.bean.SpringContextUtils;
import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.Reindeer;

public class AppAuthenciateWindow extends Window implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VerticalLayout contentLayout = new VerticalLayout();
	BasicUser user;
	String callBackUrl = APIKeys.LinkedinCallBackUrl;
	String linkedInAPIId = APIKeys.linkedInAPIId;
	String linkedInSecertKey = APIKeys.linkedInSecertKey;
	BasicUserDao buDao = SpringContextUtils.getBean(BasicUserDao.class);
	final VerticalLayout mainLayout = new VerticalLayout();
	LinkedInAccessToken linkedinAccessToken;

	public AppAuthenciateWindow(LinkedInAccessToken linkedinAccessToken, BasicUser user) {
		this.user = user;
		this.linkedinAccessToken = linkedinAccessToken;
		this.setCaption("Application Authenciation");
		this.center();
		this.addStyleName("mocha-app");
		this.setWidth("860px");
		this.setClosable(true);
		this.setResizeLazy(true);
		this.setResizable(true);
		this.setModal(true);
		this.addStyleName(Reindeer.WINDOW_LIGHT);
		this.setImmediate(true);
		this.setHeight("400px");
	}

	@Override
	public void attach() {
		addComponent(mainLayout);
		mainLayout.setSizeFull();
		// Label userHasTokenLabel = null;
		final Refresher refresher = new Refresher();

		if (linkedinAccessToken != null) {
			buildPersonInfo();
			// buildConnectsInfo();
		}
		else {
			LinkedinImpl linkedinImpl = new LinkedinImpl(linkedInAPIId, linkedInSecertKey, callBackUrl);
			LinkedInRequestToken linkedinRequestToken = linkedinImpl.getLinkedInRequestToken();
			String token = linkedinRequestToken.getToken();
			String tokenSecret = linkedinRequestToken.getTokenSecret();

			SoicalApp soicalApp = new SoicalApp();
			soicalApp.setName("linkedin");
			soicalApp.setRequesToken(token);
			soicalApp.setRequesTokenSecret(tokenSecret);

			final String linkedAuthUrl = linkedinRequestToken.getAuthorizationUrl();
			if (linkedAuthUrl != null) {
				user.getSoicalApp().add(soicalApp);
				buDao.merge(user);
			}
			getApplication().getMainWindow().open(new ExternalResource(linkedAuthUrl), "", -1, -1, Window.BORDER_DEFAULT);
			buildWaitForAuthLayout();
			setImmediate(true);

			final NativeButton linkedinBtn = new NativeButton("Authencation On LinkedIn");
			linkedinBtn.addStyleName("mocha-button");
			// mainLayout.addComponent(linkedinBtn);
			linkedinBtn.addListener(new ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {

					refresher.addListener(new RefreshListener() {
						private static final long serialVersionUID = 1L;

						public void refresh(Refresher source) {
							mainLayout.requestRepaintAll();
						}
					});
					LinkedinImpl linkedinImpl = new LinkedinImpl(linkedInAPIId, linkedInSecertKey, callBackUrl);
					LinkedInRequestToken linkedinRequestToken = linkedinImpl.getLinkedInRequestToken();
					String token = linkedinRequestToken.getToken();
					String tokenSecret = linkedinRequestToken.getTokenSecret();
					SoicalApp soicalApp = new SoicalApp();
					soicalApp.setName("linkedin");
					soicalApp.setRequesToken(token);
					soicalApp.setRequesTokenSecret(tokenSecret);
					final String linkedAuthUrl = linkedinRequestToken.getAuthorizationUrl();
					if (linkedAuthUrl != null) {
						user.getSoicalApp().add(soicalApp);
						buDao.merge(user);
					}
					// mainLayout.addComponent(refresher);
					getApplication().getMainWindow().open(new ExternalResource(linkedAuthUrl), "", -1, -1, Window.BORDER_DEFAULT);
					buildWaitForAuthLayout();
					setImmediate(true);
				}

				// wait for user authencation from linkedIn
				private void buildWaitForAuthLayout() {
					setImmediate(true);
					final NativeButton doneAuthBtn = new NativeButton("Done with LinkedIn Authencation");
					doneAuthBtn.addStyleName("mocha-button");
					mainLayout.replaceComponent(linkedinBtn, doneAuthBtn);
					doneAuthBtn.addListener(new ClickListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							getApplication().getMainWindow().requestRepaintAll();
							mainLayout.requestRepaintAll();
							mainLayout.removeComponent(refresher);
							BasicUser bu = buDao.findUserByUserName(user.getUserName());
							for (SoicalApp soicalApp : bu.getSoicalApp()) {
								if (soicalApp.getName().equals("linkedin") && soicalApp.getAuthToken() != null) {
									LinkedinImpl linkedinImpl = new LinkedinImpl();
									LinkedInAccessToken linkedinAccessToken = new LinkedInAccessToken(soicalApp.getAuthToken(), soicalApp.getAuthTokenSecret());
									Person person = linkedinImpl.getProfileForCurrentUser(linkedinAccessToken);
									// buildPersonInfo();
								}
							}
						}
					});
				}
			});
		}

		// Needed to hack around problem with panel scroll-to-bottom
		// final Refresher refresher = new Refresher();
		// refresher.addListener(new RefreshListener() {
		// private static final long serialVersionUID = 1L;
		//
		// public void refresh(Refresher source) {
		// // BasicUser bu=buDao.findUserByUserName(user.getUserName());
		// // for(SoicalApp soicalApp:bu.getSoicalApp()){
		// // if(soicalApp.getName().equals("linkedin") && soicalApp.getAuthToken()!=null){
		// // LinkedinImpl linkedinImpl=new LinkedinImpl();
		// // LinkedInAccessToken linkedinAccessToken=new LinkedInAccessToken(soicalApp.getAuthToken(),soicalApp.getAuthTokenSecret());
		// // person=linkedinImpl.getProfileForCurrentUser(linkedinAccessToken);
		// // buildPersonInfo();
		// // }
		// // }
		// mainLayout.requestRepaintAll();
		// getApplication().getMainWindow().requestRepaintAll();
		// }
		//
		// });
		// refresher.setRefreshInterval(500);
		// mainLayout.addComponent(refresher);
	}

	private void buildWaitForAuthLayout() {
		setImmediate(true);
		final NativeButton doneAuthBtn = new NativeButton("I'm done with LinkedIn Authencation");
		doneAuthBtn.addStyleName("mocha-button");
		mainLayout.addComponent(doneAuthBtn);
		doneAuthBtn.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				buildPersonInfo();
			}
		});
	}

	protected void buildPersonInfo() {
		mainLayout.removeAllComponents();
		Label userHasTokenLabel = new Label("Here are your personal LinkedIn profile:");
		mainLayout.addComponent(userHasTokenLabel);
		mainLayout.setComponentAlignment(userHasTokenLabel, Alignment.TOP_RIGHT);

		GridLayout userInfoLayout = new GridLayout(2, 1);
		userInfoLayout.setSpacing(true);
		userInfoLayout.addStyleName("linkedinUserInfoLayout");
		mainLayout.addComponent(userInfoLayout);
		mainLayout.setComponentAlignment(userInfoLayout, Alignment.MIDDLE_CENTER);
		BasicUser bu = buDao.findUserByUserName(user.getUserName());
		if (bu != null) {
			for (SoicalApp soicalApp : bu.getSoicalApp()) {
				List<LinkedinPersonProfile> profiles = soicalApp.getLinkedinPersonProfiles();
				for (LinkedinPersonProfile p : profiles) {
					userInfoLayout.removeAllComponents();
					Label userName = new Label(p.getFirstName() + "." + p.getLastName());
					userInfoLayout.addComponent(userName);
					Label userHead = new Label(p.getHeadline());
					userInfoLayout.addComponent(userHead);
				}
			}
		}

		NativeButton doneAuthBtn = new NativeButton("View Connections");
		doneAuthBtn.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		doneAuthBtn.addStyleName("mocha-button");
		userInfoLayout.addComponent(doneAuthBtn);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		System.out.println(event.getSource());
	}

	public class LinkButton extends NativeButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String url;

		public LinkButton(String url, String caption) {
			super(caption);
			this.url = url;

			setImmediate(true);
			addListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					LinkButton.this.getWindow().open(new ExternalResource(LinkButton.this.url));
				}
			});
		}
	}

}
