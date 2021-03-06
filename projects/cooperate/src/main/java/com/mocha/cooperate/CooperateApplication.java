/**
 * 
 */
package com.mocha.cooperate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.coral.foundation.constant.RuntimeConstant;
import com.coral.foundation.oauth.APIKeys;
import com.coral.foundation.oauth.OauthHandler;
import com.coral.foundation.oauth.SimpleOAuthHandler;
import com.coral.foundation.security.model.BasicUser;
import com.coral.foundation.utils.FileUtils;
import com.coral.vaadin.app.MochaApplication;
import com.google.code.linkedinapi.schema.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author Administrator
 * 
 */
public class CooperateApplication extends MochaApplication {
	private static final long serialVersionUID = 5003822334158074903L;

	@Override
	public void init() {
		// init language
		if (cookieLanguage != null) {
			if (RuntimeConstant.SUPPORTED_LANGUAGES[1].equals(cookieLanguage)) {
				setLocale(Locale.CHINESE);
			}
			else {
				setLocale(Locale.ENGLISH);
			}
		}
		else {
			setLocale(((WebApplicationContext) getContext()).getBrowser().getLocale());
		}
		// get Web Browser info.
		// WebBrowser webBrowser = ((WebApplicationContext)getContext()).getBrowser();
		// webBrowser.isTouchDevice();

		// real code
		super.init();

		setTheme("fresh");
		checkDocumentPath();

		this.addWindow(new FileShareWindow());
		ApplicationContext ac = getContext();
//		System.out.println(ac);
	}

	public void checkDocumentPath() {
		// FileUtils.createDir(SystemProperty.initFilePath());
		FileUtils.createDir(SystemProperty.USER_PHOTO_PATH);
	}

	@Override
	public String applicationModel() {
		return "cooperateModel.xml";
	}

	@Override
	public String applicationTitle() {
		return "Mocha Platform";
	}

	@Override
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		BasicUser currentUser = null;
		if (getUser() != null) {
			currentUser = (BasicUser) getUser();
//			System.out.println("User Login with name " + currentUser.getUserName());
		}
		String referrer = request.getHeader("referer");
		// getMainWindow().removeAllComponents();
		if (referrer != null && referrer.contains("?ebaytkn=&tknexp=")) {
			setMainWindow(new SouceApplictionCalbackWindow(referrer));
		}
		// linkedin case
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (SystemProperty.COOKIE_USERNAME.equals(cookies[i].getName()))
					// Log the user in automatically
					cookieUsername = cookies[i].getValue();
				if (SystemProperty.COOKIE_LANGUAGE.equals(cookies[i].getName()))
					cookieLanguage = cookies[i].getValue();
			}
		}
		// System.out.println("cookieUsername is: "+cookieUsername);
		if (referrer != null) {
			if (referrer.toString().contains("oauth")) {
				SimpleOAuthHandler simpleOA = new SimpleOAuthHandler(request);
				simpleOA.setReferrUrl(referrer);
				simpleOA.setUserName(cookieUsername);
				simpleOA.setSoicalAppName("linkedin");
				simpleOA.saveUserAuthenToken((BasicUser) getUser());
//				boolean needCloseWin = simpleOA.saveUserAuthenToken(request);
//				if (needCloseWin) {
//					getMainWindow().executeJavaScript("window.close()");
//				}
			}
			else if (referrer.startsWith(APIKeys.facebookCallBackUrl) && getUser() != null) {
				SimpleOAuthHandler simpleOA = new SimpleOAuthHandler(request);
				simpleOA.setReferrUrl(referrer);
				Object user = getMainWindow().getApplication().getUser();
				simpleOA.setUserName(currentUser.getUserName());
				simpleOA.setSoicalAppName("facebook");
				simpleOA.saveUserFBAccessToken((BasicUser) getUser());
			}
		}
		// Store the reference to the response object for using it in event listeners
		this.response = response;
	}

	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
