package com.mocha.email.cooperate.template

import com.coral.foundation.security.model.BasicUser

class UserRegister {
	
	def String getSubject() {
		return "Welcome to Mocha Platform!";
	}
	
	def getEmailContent(BasicUser user) '''
	<div style="font-size: 12px; font-family: Arial; color: #52555A">
	Dear «user.realName»<br/><br/>
	Thanks for joining the Mocha Platform! You can login the URL: <a href="www.mocha-platform.com/cooperate/">www.mocha-platform.com/cooperate</a><br/> 
	Your Mocha Platform login is : <span style="color: #0174DF;">«user.userName»</span><br/>
	Your Mocha Platform password is : <span style="color: #0174DF;">«user.password»</span><br/>
	<div style="width=600px;">Mocha Platform makes it easier to communicate, collaborate and software operation.By unifying all your workflows, by breaking-barriers of hierarchy, task, topic discuss and file management,you can now transform the way you communicate and collaborate with your colleagues.</div><br/>
	Best Regards,<br/>
	Mocha Platform team.<br/>
	<br/>
	<div style="font-size: 10px; color: #A4A4A4">
	Please do not reply to this email. This mailbox is not monitored and you will not receive a response.
	</div>
	</div>
	'''
}