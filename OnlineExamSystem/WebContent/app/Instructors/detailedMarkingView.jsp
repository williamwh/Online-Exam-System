<%@ page import="mappers.ExamMapper, domain.Exam, domain.ExamAnswer, mappers.ExamAnswerMapper, domain.Question, 
			mappers.QuestionMapper, domain.QuestionAnswer, mappers.QuestionAnswerMapper, domain.Choice, mappers.ChoiceMapper,
			java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="${pageContext.request.contextPath}/styling/examMarking.css" type="text/css" rel="stylesheet" />
<meta charset="UTF-8">
<title>Detailed Marking View</title>
</head>
<body>
	<%@include file="../header.jsp"  %>
	<%  int examAnswerID = Integer.parseInt(request.getParameter("examAnswerID"));
		ExamAnswer examAnswer = ExamAnswerMapper.getExamAnswer(examAnswerID);
		int examID = examAnswer.getExamID();
		Exam exam = ExamMapper.getExam(examID);
/* 		System.out.println(examAnswer);
		System.out.println(exam); */
	%>
	
	<form name="detailUpdateMarks" action="${pageContext.request.contextPath}/detailUpdateMarks" method="post">
		<div style="width:80%;float:left">
			<h1 align="left">SWEN90007 Project Results for <%=examAnswer.getStudentUsername()%></h1>
			<hr class="rounded">
			<p align="left">
				Score for this quiz: <b><%=examAnswer.getTotalMarks() %></b> out of <%=exam.getTotalMarks() %> <br>
				Submitted <%=examAnswer.getDateCreated().toLocaleString() %> <br>
				<!-- Submitted Jul 27 at 11:39 <br>  -->
				<!-- This attempt took less than 1 minute. -->
			</p>
		
		    <div align="center">
		    	<br>
		    	<%  List<Question> questions = QuestionMapper.getAllQuestionsFromExam(examID);
		    		session.setAttribute("questions", questions);
		    		session.setAttribute("examAnswerID", examAnswerID);
		    		for (Question question : questions){

		    			QuestionAnswer questionAnswer = QuestionAnswerMapper.getQuestionAnswer(examAnswerID, question.getQuestionID());

		    	%>
		    		<table  style="width:90%">
			            <tr>
			                <th style="background-color:LightGray">
				                Question <%=question.getQuestionIndex() %>
				                <div align="right">
				                	<input style="width:6%" type="number" min="0" max=<%=question.getMarks()%> 
				                		name=<%=Integer.toString(questionAnswer.getQuestionAnswerID())%>
				                		value=<%=questionAnswer.getMarks()%>>
				                	/<%=question.getMarks()%>pts
				                </div>
			                </th>
			            </tr>
			            <tr>
			            	<td style="background-color:white">
			            		<%=question.getQuestionText() %> <br><br>
			            		
				            	<% if (question.getQuestionType().equals("MC")){ 
				            		List<Choice> choices = ChoiceMapper.getAllChoicesFromQuestion(question.getQuestionID());
				            	%>
				            		Your Answer: <br><br>
				            		<% for (Choice choice : choices){ %>
				            		    <%=choice.getChoiceText() %>
				            		    <% 
				            		    if (!questionAnswer.getStudentAnswer().equals("") && choice.getChoiceId() == Integer.parseInt(questionAnswer.getStudentAnswer())){%>
				            		    	<input type="radio" checked="checked" name=<%=Integer.toString(question.getQuestionID()) %> value=<%=choice.getChoiceId() %>>
				            		    <%}else { %>
				            		    	<input type="radio" name=<%=Integer.toString(question.getQuestionID()) %> value=<%=choice.getChoiceId() %> disabled>
				            			<%} %>
				            			<br>
			            			<%} %>

				            	<%}else {%>
					            	Your Answer: <br>
					            	<%= questionAnswer.getStudentAnswer()%>
				            	<%} %>
			            	</td>
			            </tr>
			        </table>
			        <br><br>
		    	<%}
		    	%>
		    </div>
		    <button class="button" type="submit" style="float:right"> Update Scores</button>
		    
	    </div>
	    
	    <div class="vl"></div>
	    
	    <div style="width:20%;float:left;font-size:14px">
	    	<p align="left" style="margin-left:20px">
				Submitted <%=examAnswer.getDateCreated().toLocaleString() %>
			</p>
		    <div style="float:left;margin-left:20px;margin-top:40px"> 
		    	<p><b>Assessment</b><br>Grade out of <%=exam.getTotalMarks() %><br></p>
		    	<input style="width:20%" type="number" name="ExamAnswerMark" value=<%=examAnswer.getTotalMarks() %> disabled>
		    </div>
	    
		    <div style="float:left;margin-left:20px;margin-top:50px"> 
		    	<p><b>Exam Comments</b></p>
		    	<textarea name="comment" rows="4" cols="17"><%=examAnswer.getComment().toString() %></textarea>
		    	<button class="button" type="submit" style="float:right"> Submit</button>
		    </div>
		</div>
	</form>
    
</body>
</html>