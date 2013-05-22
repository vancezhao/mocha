/**
 * 
 */
package com.mocha.cooperate.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.coral.foundation.security.model.BasicUser;
import com.coral.foundation.spring.bean.SpringContextUtils;
import com.google.common.collect.Lists;
import com.mocha.cooperate.basic.dao.CommentDao;
import com.mocha.cooperate.basic.dao.DiscussDao;
import com.mocha.cooperate.basic.dao.NotifyLineDao;
import com.mocha.cooperate.basic.dao.StatusDao;
import com.mocha.cooperate.basic.dao.SubToDoItemDao;
import com.mocha.cooperate.basic.dao.TimeLineDao;
import com.mocha.cooperate.basic.dao.ToDoDao;
import com.mocha.cooperate.model.Attachment;
import com.mocha.cooperate.model.Comment;
import com.mocha.cooperate.model.Discuss;
import com.mocha.cooperate.model.NotifyLine;
import com.mocha.cooperate.model.Status;
import com.mocha.cooperate.model.TimeLine;
import com.mocha.cooperate.model.ToDo;

/**
 * @author Coral.Ma
 *
 */
public class TimeLineService {
	
	private TimeLineDao timeLineDao = SpringContextUtils.getBean(TimeLineDao.class);
	private StatusDao statusDao = SpringContextUtils.getBean(StatusDao.class);
	private DiscussDao discussDao = SpringContextUtils.getBean(DiscussDao.class);
	private CommentDao commentDao = SpringContextUtils.getBean(CommentDao.class);
	private NotifyLineDao notifyLineDao = SpringContextUtils.getBean(NotifyLineDao.class);
	private ToDoDao toDoDao = SpringContextUtils.getBean(ToDoDao.class);
	private SubToDoItemDao subToDoDao = SpringContextUtils.getBean(SubToDoItemDao.class);
	
	
	@Deprecated
	public List<TimeLine> loadTimeLine(BasicUser basicUser) {
//		List<TimeLine> timelines = timeLineDao.findAll();
//		timelines = Lists.reverse(timelines);
//		return timelines;
		return loadTimeLine(basicUser,0);
	}
	
	public List<TimeLine> loadTimeLine(BasicUser basicUser, int page) {
//		List<TimeLine> timelines = timeLineDao.findAll();
//		timelines = Lists.reverse(timelines);
		List<TimeLine> timelines = timeLineDao.loadTimeLine(basicUser, page);
		return timelines;
	}
	
	public List<TimeLine> loadTimeLineByUser(BasicUser basicUser, Date date, int page) {
		List<TimeLine> timelines = timeLineDao.loadTimeLineByUser(basicUser, date, page);
		return timelines;
	}
	
	public List<TimeLine> loadStatus(BasicUser basicUser, int page) {
		return timeLineDao.loadStatus(basicUser, page);
	}
	
	public List<TimeLine> loadStatusByUser(BasicUser basicUser, int page) {
		return timeLineDao.loadStatusByUser(basicUser, page);
	}
	
	public List<TimeLine> loadDiscuss(BasicUser basicUser, int page) {
		return timeLineDao.loadDiscuss(basicUser, page);
	}
	
	public List<TimeLine> loadDiscussByUser(BasicUser basicUser, int page) {
		return timeLineDao.loadDiscussByUser(basicUser, page);
	}
	
	public List<TimeLine> loadTodo(BasicUser basicUser, int page) {
		return timeLineDao.loadTodo(basicUser, page);
	}
	
	public List<TimeLine> loadTodoByUser(BasicUser basicUser, int page) {
		return timeLineDao.loadTodoByUser(basicUser, page);
	}
	
//	public List<TimeLine> loadActivityTodo(BasicUser basicUser) {
//		return timeLineDao.loadActivityTodo(basicUser);
//	}
	
	public TimeLine saveStatus(Status status, BasicUser creator, Set<BasicUser> notifiedUsers, List<Attachment> attachments) {
		TimeLine timeLine = new TimeLine();
		status.setCreator(creator);
		status = statusDao.merge(status);
		// store notification
		List<NotifyLine> notifyLines = Lists.newArrayList();
		if(notifiedUsers != null) {
			for(BasicUser notifiedUser : notifiedUsers) {
				NotifyLine notifyLine = new NotifyLine();
				notifyLine.setNotifiedUser(notifiedUser);
				notifyLine.setStatus(status);
				notifyLines.add(notifyLine);
			}
		}
		status.setNotifyLines(notifyLines);
//		for(Attachment attachment : attachments) {
//			attachment.set
//		}
		status.setAttachments(attachments);
		timeLine.setStatus(status);
		timeLine.setCreator(creator);
		timeLineDao.merge(timeLine);
		return timeLine;
	}
	
	public TimeLine saveDiscuss(Discuss discuss, BasicUser creator, Set<BasicUser> notifiedUsers, List<Attachment> attachments) {
		TimeLine timeLine = new TimeLine();
		discuss = discussDao.merge(discuss);
		// store notification
		List<NotifyLine> notifyLines = Lists.newArrayList();
		if(notifiedUsers != null) {
			for(BasicUser notifiedUser : notifiedUsers) {
				NotifyLine notifyLine = new NotifyLine();
				notifyLine.setNotifiedUser(notifiedUser);
				notifyLine.setDiscuss(discuss);
				notifyLines.add(notifyLine);
			}
		}
		discuss.setNotifyLines(notifyLines);
		discuss.setAttachments(attachments);
		timeLine.setDiscuss(discuss);
		timeLine.setCreator(creator);
		timeLineDao.merge(timeLine);
		return timeLine;
	}
	
	public TimeLine saveToDo(ToDo todo, BasicUser creator, Set<BasicUser> notifiedUsers, List<Attachment> attachments) {
		TimeLine timeLine = new TimeLine();
		todo = toDoDao.merge(todo);
		// store notification
		List<NotifyLine> notifyLines = Lists.newArrayList();
		if(notifiedUsers != null) {
			for(BasicUser notifiedUser : notifiedUsers) {
				NotifyLine notifyLine = new NotifyLine();
				notifyLine.setNotifiedUser(notifiedUser);
				notifyLine.setTodo(todo);
				notifyLines.add(notifyLine);
			}
		}
		todo.setNotifyLines(notifyLines);
		todo.setAttachments(attachments);
		timeLine.setTodo(todo);
		timeLine.setCreator(creator);
		timeLineDao.merge(timeLine);
		return timeLine;
	}
	
	
	public void removeTimeLine(TimeLine timeLine) {
		Status status = timeLine.getStatus();
		Discuss discuss = timeLine.getDiscuss();
		ToDo todo=timeLine.getTodo();
		if(status != null) {
			List<NotifyLine> notifyLines = status.getNotifyLines();
			for(int i=0; i < notifyLines.size(); i++) {
				removeNotifyLine(notifyLines.get(i));
			}
			List<Comment> comments = status.getComments();
			for(int i=0; i < comments.size(); i++) {
				removeComment(comments.get(i));
			}
			timeLineDao.remove(timeLine.getID());
			statusDao.remove(status.getID());
		} else if(discuss != null) {
			for(Comment comment : discuss.getComments()) {
				commentDao.remove(comment.getID());
			}
			timeLineDao.remove(timeLine.getID());
			discussDao.remove(discuss.getID());
		} else if(todo !=null){
//			for(SubToDoItem subToDo : todo.getSubToDoItems()) {
//				todo.getSubToDoItems().remove(subToDo);
//				toDoDao.merge(todo);
//			}
			timeLineDao.remove(timeLine.getID());
			toDoDao.remove(todo.getID());
		
		}
	
	}
	
	public void saveComment(Comment comment) {
		commentDao.merge(comment);
	}

 	public Status updateStatus(Status status) {
 		return statusDao.merge(status);
	}
	
	public Discuss updateDiscuss(Discuss discuss) {
		return discussDao.merge(discuss);
	}
	
	public ToDo updateToDo(ToDo todo) {
		return toDoDao.merge(todo);
	}
	
	public void removeNotifyLine(NotifyLine notifyLine) {
		Status status = notifyLine.getStatus();
		Discuss discuss = notifyLine.getDiscuss();
		if(status != null) {
			status.getNotifyLines().remove(notifyLine);
			statusDao.merge(status);
		}
		if(discuss != null) {
			discuss.getNotifyLines().remove(notifyLine);
			statusDao.merge(status);
		}
		notifyLineDao.remove(notifyLine.getID());
	}
	
	public void removeComment(Comment comment) {
		Status status = comment.getStatus();
		Discuss discuss = comment.getDiscuss();
		if(status != null) {
			status.getComments().remove(comment);
			statusDao.merge(status);
		}
		if(discuss != null) {
			discuss.getComments().remove(comment);
			discussDao.merge(discuss);
		}
		commentDao.remove(comment.getID());
	}
	
	public Date queryOldestData(BasicUser user) {
		TimeLine oldestTimeLine = timeLineDao.queryOldestData(user);
		// return the create time of oldest timeline. if there no timeline return current date.
		if(oldestTimeLine != null) {
			return oldestTimeLine.getCreateTime();
		} else {
			return new Date();
		}
	}
}
