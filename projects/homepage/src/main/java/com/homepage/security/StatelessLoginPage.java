package com.homepage.security;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

public class StatelessLoginPage extends WebPage {

	public StatelessLoginPage() {
		final LoginForm form = new LoginForm("loginForm");
		add(form);

	}

	private static class LoginForm extends StatelessForm {

		private static final long serialVersionUID = -6826853507535977683L;

		private String username;
		private String password;

		public LoginForm(String id) {
			super(id);
			setModel(new CompoundPropertyModel(this));
			add(new Label("usernameLabel", getString("login.username.label",
					null, "Username")));
			add(new RequiredTextField("username"));
			add(new Label("passwordLabel", getString("login.password.label",
					null, "Username")));
			add(new PasswordTextField("password"));
			add(new FeedbackPanel("feedback"));

		}

		@Override
		protected void onSubmit() {
			AuthenticatedWebSession session = AuthenticatedWebSession.get();
			if (session.signIn(username, password)) {
				setDefaultResponsePageIfNecessary();
			} else {
				error(getString("login.failed.badcredentials"));
			}
		}

		private void setDefaultResponsePageIfNecessary() {
			setResponsePage(getApplication().getHomePage());
		}
	}

}
