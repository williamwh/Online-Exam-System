<%@ page import="mappers.ExamMapper, domain.Exam, mappers.ExamAnswerMapper, domain.ExamAnswer, 
					domain.Question, mappers.QuestionMapper, domain.QuestionAnswer, mappers.QuestionAnswerMapper, 
					java.util.List, mappers.ChoiceMapper, domain.Choice"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="${pageContext.request.contextPath}/styling/examMarking.css" type="text/css" rel="stylesheet" />
<meta charset="UTF-8">
<title>Exam Answering View</title>
</head>
<body>
	<%@include file="../header.jsp"  %>
	<%  int examID = Integer.parseInt(request.getParameter("examID"));
		Exam exam = ExamMapper.getExam(examID);
	%>
	
	<form name="answerExam" action="${pageContext.request.contextPath}/answerExam" method="post">
		<div style="align:center">
			<h1 align="left"><%=exam.getSubjectCode() + " " + exam.getName()%></h1>
			<hr class="rounded">
		
		    <div align="center">
		    	<br>
		    	<%  List<Question> questions = exam.getQuestions(); 
		    		session.setAttribute("examID", exam.getExamID());
		    		session.setAttribute("studentExamAnswerID", request.getAttribute("studentExamAnswerID"));
		    		session.setAttribute("studentUsername", request.getAttribute("studentUsername"));
		    		for (Question question : questions){
		    	%>
		    		<table  style="width:90%">
			            <tr>
			                <th style="background-color:LightGray">
				                Question <%=question.getQuestionIndex() %>
				                <div align="right">
				                	<%=question.getMarks()%>pts
				                </div>
			                </th>
			            </tr>
			            <tr>
			            	<td style="background-color:white">
			            		<%=question.getQuestionText() %> <br><br>
				            	<% if (question.getQuestionType().equals("MC")){ 
				            		List<Choice> choices = ChoiceMapper.getAllChoicesFromQuestion(question.getQuestionID());
				            	%>
				            		Please select your answer: <br><br>
				            		<% for (Choice choice : choices){ %>
				            		    <%=choice.getChoiceText() %>
				            			<input type="radio" name=<%=Integer.toString(question.getQuestionID()) %> value=<%=choice.getChoiceId() %>>
				            			<br>
			            			<%} %>

				            	<%}else {%>
				            		Your Answer: <br>
				            		<textarea name=<%=Integer.toString(question.getQuestionID()) %> rows="4" cols="80"></textarea>
				            	<%} %>
			            	</td>
			            </tr>
			        </table>
			        <br><br>
		    	<%}
		    	%>

		    </div>
		    <button class="button" type="submit" style="float:right"> Submit</button>
		    
	    </div>
	    
	</form>

</body>
</html>