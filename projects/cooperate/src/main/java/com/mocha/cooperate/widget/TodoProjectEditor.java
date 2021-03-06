/**
 * 
 */
package com.mocha.cooperate.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vaadin.hene.expandingtextarea.ExpandingTextArea;

import com.coral.foundation.security.model.BasicUser;
import com.coral.foundation.utils.DateUtils;
import com.coral.foundation.utils.Message;
import com.coral.foundation.utils.StrUtils;
import com.coral.vaadin.widget.WidgetFactory;
import com.coral.vaadin.widget.component.UserComboBox;
import com.mocha.cooperate.InnerStyle;
import com.mocha.cooperate.SystemProperty;
import com.mocha.cooperate.model.SubToDoItem;
import com.mocha.cooperate.model.ToDo;
import com.mocha.cooperate.service.ToDoService;
import com.mocha.cooperate.widget.cards.StatusCard;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author Coral
 *
 */
public class TodoProjectEditor extends VerticalLayout {

	private ToDo todo;
	private boolean publisherStatus = false;
	private String contentWidth = "765px";
	private String contentTextWidth = "763px";
	private String contentLayoutWidth = "770px";
	private String selectFieldWidth = "100px";
	private ExpandingTextArea todoTitle = new ExpandingTextArea(); 
	private Label todoTitleLabel = new Label("",Label.CONTENT_XHTML);
	private VerticalLayout todoTitleLayout = new VerticalLayout();
	private HorizontalLayout todoTitleLabelLayout = new HorizontalLayout();
	private VerticalLayout todoListShowPanel = new VerticalLayout();
	private VerticalLayout titlePanel;
	private VerticalLayout addTaskLayout = new VerticalLayout();
	private Message message;
	
	private NativeButton addProjectBtn = new NativeButton();
	private UserComboBox userCombox;
	private DateField expireDateField = new DateField();
	private Button addTaskBtn;
	private List<TodoTaskEditor> todoTaskEditors = new ArrayList<TodoTaskEditor>();
	
	private ToDoService toDoService = new ToDoService();
	private BasicUser currentUser;
	
	public TodoProjectEditor(ToDo toDo, BasicUser currentUser) {
		this.todo = toDo;
		this.currentUser = currentUser;
		this.setWidth(SystemProperty.content_page_width);
		if(toDo.getID() == null) {
			this.addStyleName("new-todo-project-title");
		} else {
			this.addStyleName("todo-project-title");
		}
	}
	
	public boolean validate() {
		if(todoTitle.getValue() == null) {
		}
		return true;
	}
	
	public Object getValue() {
		Object value = todoTitle.getValue();
		if(StrUtils.isEmpty(value)) {
			return null;
		}
		todo.setName(value.toString());
		todo.setCreator((BasicUser) getApplication().getUser());
		BasicUser assignedUser = (BasicUser) userCombox.getValue();
		if(assignedUser == null) {
			assignedUser = todo.getCreator();
		}
		todo.setAssginedUser(assignedUser);
		todo.setExpiredDate((Date) expireDateField.getValue());
		// set value to sub item of todo list.
		for(TodoTaskEditor taskEditor : todoTaskEditors) {
			taskEditor.getValue();
		}
		return todo;
	}
	
	public void attach() {
		message = new Message(getApplication().getLocale());
		titlePanel = buildProjectTitlePanel();
		this.addComponent(titlePanel);
		
		this.addComponent(todoListShowPanel);
		this.addComponent(getAddTaskButton());
		
		// init the edit value to each panel.
		if(todo.getID() != null) {
			todoTitle.setValue(todo.getName());
			userCombox.resetDefaultUser(todo.getAssginedUser());
			expireDateField.setValue(todo.getExpiredDate());
			setTitlePanel();
			for(SubToDoItem subItem : todo.getSubToDoItems()) {
				addTaskPanel(subItem);
			}
		}
		bind();
	}
	
	private VerticalLayout buildProjectTitlePanel() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(contentLayoutWidth);

		todoTitleLayout.setWidth(contentWidth);
		todoTitleLayout.setSpacing(true);
		todoTitle.setInputPrompt(message.getString("cooperate.todo.newproject"));
		todoTitle.setWidth(contentTextWidth);
		todoTitleLayout.addComponent(todoTitle);
		HorizontalLayout addProjectLayout = new HorizontalLayout();
		addProjectLayout.setWidth(contentWidth);
		HorizontalLayout taskInfoLayout = new HorizontalLayout();
		taskInfoLayout.setSpacing(true);
		
		userCombox = WidgetFactory.createUserCombo((BasicUser)getApplication().getUser());
		userCombox.setInputPrompt(message.getString("cooperate.todo.Assigner"));
		userCombox.setWidth(selectFieldWidth);
		taskInfoLayout.addComponent(userCombox);
		
		expireDateField.setResolution(InlineDateField.RESOLUTION_DAY);
		expireDateField.setWidth(selectFieldWidth);
		expireDateField.setDescription("Expired Date");
		expireDateField.setValue(DateUtils.addDay(new Date(), 1));
		taskInfoLayout.addComponent(userCombox);
		taskInfoLayout.addComponent(expireDateField);
		
		addProjectBtn.addStyleName("mocha-button");
		addProjectBtn.setCaption(message.getString("cooperate.todo.AddProject"));
		addProjectLayout.addComponent(addProjectBtn);
		
		addProjectLayout.addComponent(taskInfoLayout);
		addProjectLayout.setComponentAlignment(taskInfoLayout, Alignment.MIDDLE_RIGHT);
		todoTitleLayout.addComponent(addProjectLayout);
		layout.addComponent(todoTitleLayout);
		
		todoTitleLabelLayout.setWidth(contentWidth);
		todoTitleLabelLayout.setSpacing(true);
		todoTitleLabelLayout.setVisible(false);
		todoTitleLabel.addStyleName("todo-title-label");
		todoTitleLabelLayout.addComponent(todoTitleLabel);
		
		layout.addComponent(todoTitleLabelLayout);
		return layout;
	}
	
	public void setTitlePanel() {
		if(!StrUtils.isEmpty(todoTitle.getValue())) {
			// create the project title content.
			String userName = ((BasicUser)getApplication().getUser()).getRealName();
			if(userCombox.getValue() != null)
				userName = ((BasicUser)userCombox.getValue()).getRealName();
			String expireDate = DateUtils.date2String(DateUtils.addDay(new Date(),1), "MM-dd");
			if(expireDateField.getValue() != null)
				expireDate = DateUtils.date2String((Date) expireDateField.getValue(), "MM-dd");
			String contentValue = StrUtils.asciiToXhtml(todoTitle.getValue().toString())
					+ "&nbsp;<span style=\""+ InnerStyle.todo_assigner +"\">" + userName + "</span>&nbsp;"
					+ "<span style=\""+ InnerStyle.todo_dueto +"\">" + expireDate + "</span>";
			
			todoTitleLabel.setValue(contentValue);
			todoTitleLayout.setVisible(false);
			todoTitleLabelLayout.setVisible(true);
			addTaskLayout.setVisible(true);
			addProjectBtn.setCaption("Save");
			titlePanel.addStyleName("todo-inputField-border");
		}
	}
	
	public void addTaskPanel(SubToDoItem subToDoItem) {
		if(subToDoItem.getID() == null) {
			todo.getSubToDoItems().add(subToDoItem);
		}
		final TodoTaskEditor taskPanel = new TodoTaskEditor(subToDoItem, currentUser);
		if(!publisherStatus) {
			taskPanel.setContentWidth("760px");
			taskPanel.setContentLayoutWidth("765px");
			taskPanel.setSubtaskContentWidth("750px");
		}
		taskPanel.getAddTaskButton().addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				taskPanel.setDisplay(false);
				taskPanel.setAdded(true);
			}
		});
		taskPanel.getCancelTaskButton().addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!taskPanel.isAdded()) {
					todoListShowPanel.removeComponent(taskPanel);
				} else {
					taskPanel.setDisplay(false);
				}
			}
		});
		taskPanel.getTaskDeleteButton().addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = new ConfirmDialog("Do you want to delete this task ?") {
					@Override
					public void confirm() {
						SubToDoItem subToDoItem = taskPanel.getSubToDoItem();
						if(subToDoItem.getID() == null) {
							todo.getSubToDoItems().remove(subToDoItem);
						} else {
							todo.getSubToDoItems().remove(subToDoItem);
							toDoService.merge(todo);
							toDoService.removeSubItem(subToDoItem);
						}
						todoListShowPanel.removeComponent(taskPanel);
						todoTaskEditors.remove(taskPanel);
					}
					@Override
					public void cancel() {
					}
				};
				TodoProjectEditor.this.getWindow().addWindow(confirmDialog);
			}
		});
		todoTaskEditors.add(taskPanel);
		todoListShowPanel.addComponent(taskPanel);
	}
	
	public void bind() {
		addProjectBtn.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				setTitlePanel();
			}
		});
		addTaskBtn.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				SubToDoItem subToDoItem = new SubToDoItem();
				addTaskPanel(subToDoItem);
			}
		});
		todoTitleLabelLayout.addListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				todoTitleLabelLayout.setVisible(false);
				todoTitleLayout.setVisible(true);
			}
		});
	}
	
	private VerticalLayout getAddTaskButton() {
		addTaskLayout.setVisible(false);
		addTaskLayout.addStyleName("add-task-button");
//		addTaskBtn = new Button(message.getString("cooperate.todo.AddTask"));
//		addTaskBtn.addStyleName(BaseTheme.BUTTON_LINK);
		addTaskBtn = WidgetFactory.createButton(message.getString("cooperate.todo.NewTask"));
		addTaskLayout.addComponent(addTaskBtn);
		return addTaskLayout;
	}

	/**
	 * @return the contentWidth
	 */
	public String getContentWidth() {
		return contentWidth;
	}

	/**
	 * @param contentWidth the contentWidth to set
	 */
	public void setContentWidth(String contentWidth) {
		this.contentWidth = contentWidth;
	}

	/**
	 * @return the contentLayoutWidth
	 */
	public String getContentLayoutWidth() {
		return contentLayoutWidth;
	}

	/**
	 * @param contentLayoutWidth the contentLayoutWidth to set
	 */
	public void setContentLayoutWidth(String contentLayoutWidth) {
		this.contentLayoutWidth = contentLayoutWidth;
	}

	/**
	 * @return the publisherStatus
	 */
	public boolean isPublisherStatus() {
		return publisherStatus;
	}

	/**
	 * @param publisherStatus the publisherStatus to set
	 */
	public void setPublisherStatus(boolean publisherStatus) {
		this.publisherStatus = publisherStatus;
	}

	/**
	 * @return the contentTextWidth
	 */
	public String getContentTextWidth() {
		return contentTextWidth;
	}

	/**
	 * @param contentTextWidth the contentTextWidth to set
	 */
	public void setContentTextWidth(String contentTextWidth) {
		this.contentTextWidth = contentTextWidth;
	}
}
