/**
 * 
 */
package com.mocha.cooperate.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.coral.foundation.security.model.BasicUser;
import com.coral.foundation.spring.bean.SpringContextUtils;
import com.google.common.collect.Lists;
import com.mocha.cooperate.basic.dao.SubToDoItemDao;
import com.mocha.cooperate.basic.dao.ToDoDao;
import com.mocha.cooperate.model.SubToDoItem;
import com.mocha.cooperate.model.ToDo;
import com.sun.jersey.api.json.JSONWithPadding;

/**
 * @author Coral
 * 
 */
@Service("todoService")
// Use Spring IoC to create and manage this bean.
public class ToDoServiceImpl implements ToDoService {

	private ToDoDao toDoDao = SpringContextUtils.getBean(ToDoDao.class);
	private SubToDoItemDao subItemDao = SpringContextUtils.getBean(SubToDoItemDao.class);

	@Override
	public List<ToDo> loadActivityTodo(BasicUser basicUser) {
		List<ToDo> todoList = Lists.newArrayList();
		addToDoList(todoList, toDoDao.loadActivityTodo(basicUser));
		addToDoList(todoList, toDoDao.loadActivityItem(basicUser));
		return todoList;
	}

	@Override
	public List<ToDo> loadCompletedTodo(BasicUser basicUser) {
		List<ToDo> todoList = Lists.newArrayList();
		addToDoList(todoList, toDoDao.loadCompletedTodo(basicUser));
		addToDoList(todoList, toDoDao.loadCompletedItem(basicUser));
		return todoList;
	}

	private void addToDoList(List<ToDo> todoList, List<ToDo> resultList) {
		for (ToDo todo : resultList) {
			boolean isExisted = false;
			for (ToDo existedTodo : todoList) {
				if (existedTodo.getID() == todo.getID()) {
					isExisted = true;
				}
			}
			if (!isExisted) {
				todoList.add(todo);
			}
		}
	}

	public void merge(ToDo todo) {
		toDoDao.merge(todo);
	}

	public void removeSubItem(SubToDoItem subItem) {
		subItemDao.remove(subItem.getID());
	}

	public static void main(String args[]) {
		try {
			JAXBContext context = JAXBContext.newInstance(com.coral.foundation.security.model.BasicUser.class);
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONWithPadding getFullToDos(String uuid, String callback, String emptyParam) {
		List<ToDo> returnTodos = new ArrayList<ToDo>();
		returnTodos.addAll(toDoDao.loadAll());
		JSONWithPadding returnJSONP = new JSONWithPadding(new GenericEntity<List<ToDo>>(returnTodos) {
		}, callback);
		System.out.println("uuid is:" + uuid);
		System.out.println("callback is: " + callback);
		return returnJSONP;
	}
}