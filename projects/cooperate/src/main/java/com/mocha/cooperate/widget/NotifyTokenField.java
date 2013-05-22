/**
 * 
 */
package com.mocha.cooperate.widget;

import java.util.List;
import java.util.Set;

import org.vaadin.tokenfield.TokenField;

import com.coral.foundation.security.model.BasicUser;
import com.mocha.cooperate.service.UserService;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

/**
 * @author Coral.Ma
 * 
 */
public class NotifyTokenField extends TokenField {

	private static final long serialVersionUID = 2388872097084900291L;

	// private BasicUser
	public NotifyTokenField() {
		this.setContainerDataSource(basicUserContainer());
		this.setTokenCaptionPropertyId("realName"); 
		this.setRememberNewTokens(false); 
		this.setNewTokensAllowed(false);
	}
	
	private Container basicUserContainer() {
		BeanItemContainer<BasicUser> container = new BeanItemContainer<BasicUser>(BasicUser.class);
		UserService userService = new UserService();
		List<BasicUser> basicUsers = userService.loadAllBasicUser();
		container.addAll(basicUsers);
		return container;
	}

	@SuppressWarnings("unchecked")
	protected void onTokenInput(Object tokenId) {
		Set<Object> set = (Set<Object>) getValue();
		if (set != null && set.contains(tokenId)) {
			// duplicate
			getWindow().showNotification(
					getTokenCaption(tokenId) + " is already added");
		} else {
			addToken(tokenId);
		}
	}

	// just delete, no confirm
    protected void onTokenDelete(Object tokenId) {
        this.removeToken(tokenId);
    }
}
