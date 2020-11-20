<%@ page import="domain.Exam, mappers.ExamMapper, mappers.QuestionMapper, mappers.ExamAnswerMapper" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
  background-color: #094183;
  color: white;
}

#CreateExamBtn {
  color: white;
  background-color: #094183;
  padding: 12px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

#CreateExamBtn:hover {
  background-color: #0C59B3;
}

#MarkExamBtn{
  color: white;
  background-color: #094183;
  padding: 12px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

#MarkExamBtn:hover {
  background-color: #0C59B3;
}

.buttonDiv form{
	display: inline;
	margin-left: 5px;
	margin-right:5px;
}
</style>  
<meta charset="UTF-8">
<title>LMS System</title>
</head>
<body>
	<%@include file="../header.jsp"  %>
	<% String subjectCode = request.getParameter("subjectCode"); %>
	<h1>Subject: <%= subjectCode %></h1>
    <div align="center">
        <table  style="width:90%">
            <tr>
                <th>Exam ID</th>
                <th>Title</th>
                <th>Version</th>
                <th>Status</th>
                <th>Total Marks</th>
                <th>Total Questions</th>
                <th>Publish</th>
                <th>Close</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
            
                <tr>
 			<%
      		 	for (Exam exam : ExamMapper.getSubjectExams(subjectCode))  {
      		 		int totalAnswers = ExamAnswerMapper.getTotalAnswers(exam.getExamID());
       		 %>
       		        <td><%= exam.getExamID() %></td>
                    <td><%= exam.getName() %></td>
                    <td><%= exam.getVer() %></td>
                    <td><%= exam.getIsPublishedVar() ? "Published" : "Unpublished" %>, <%=exam.getIsClosed() %></td>
                    <td><%= exam.getTotalMarks() %></td>
                    <td><%= QuestionMapper.getTotalQuestions(exam.getExamID()) %></td>
                    <td> 
                    <% if (!exam.getIsPublishedVar()){ %>
                   		 <form name="publishExam" action="${pageContext.request.contextPath}/updateExam" method="post">
                   		 	<input type="hidden" id="modifiedByInput" name="modifiedBy" value="<%=(String) session.getAttribute("username")%>">
                    		<input type="hidden" name="subjectCode" value=<%=subjectCode%>>
                    		<input type="hidden" name="examID" value=<%=exam.getExamID()%>>
                    		<input type="hidden" name="isPublished" value="1">
                    		<input type="hidden" name="isClosed" value="0">
                    		<input type="hidden" name="isDelete" value="0">
                    		<input type="submit" value="Publish">
                    	</form>
                    	
                   	<% } else {%>
                   	N/A
                   	<% }%>
                    </td>
                    <td>
                    <% if (!exam.getIsClosedVar()){ %>
                   		 <form name="closeExam" action="${pageContext.request.contextPath}/updateExam" method="post">
                   		 	<input type="hidden" id="modifiedByInput" name="modifiedBy" value="<%=(String) session.getAttribute("username")%>">
                   			<input type="hidden" name="subjectCode" value=<%=subjectCode%>>
                    		<input type="hidden" name="examID" value=<%=exam.getExamID()%>>
                    		<input type="hidden" name="isPublished" value="0">
                    		<input type="hidden" name="isClosed" value="1">
                    		<input type="hidden" name="isDelete" value="0">
                    		<input type="submit" value="Close">
                    	</form>
                   	<% } else {%>
                   	N/A
                   	<% }%>
                    </td>
                    <td>
                    <%if (totalAnswers == 0){%>
                    <form name="editExam" action="${pageContext.request.contextPath}/app/Instructors/EditExam.jsp" method="get">
                    		<input type="hidden" name="examID" value=<%=exam.getExamID()%>>
                    		<input type="submit" value="Edit">
                    	</form>
                    <%} else { %>
                   		N/A
                   	<%} %>	
                    </td>
                    <td>
                    <%if (totalAnswers == 0) {%>
                    	<form name="deleteExam" action="${pageContext.request.contextPath}/updateExam" method="post">
                    		<input type="hidden" id="modifiedByInput" name="modifiedBy" value="<%=(String) session.getAttribute("username")%>">
                    		<input type="hidden" name="subjectCode" value=<%=subjectCode%>>
                    		<input type="hidden" name="examID" value=<%=exam.getExamID()%>>
                    		<input type="hidden" name="isPublished" value="0">
                    		<input type="hidden" name="isClosed" value="0">
                    		<input type="hidden" name="isDelete" value="1">
                    		<input type="submit" value="Delete">
                    	</form>
                   	<%} else { %>
                   		N/A
                   	<%} %>
                    </td>
                </tr>
            <%
          		  } // for loop
        	%>
        </table>
        <div class="buttonDiv" align="center">
	        <form name="openTableMarkingView" action="${pageContext.request.contextPath}/app/Instructors/tableMarkingView.jsp" method="post">
		        <input type="hidden" name="subjectCode" value=<%=subjectCode%>>
		        <input type="submit" id="MarkExamBtn" value="Mark Exams">
	        </form>
	        <form action="${pageContext.request.contextPath}/app/Instructors/CreateExam.jsp" method="get">
				<input type="hidden" name="subjectCode" value="<%= subjectCode %>">
				<input type="submit" value="Create Exam" name="Submit" id="CreateExamBtn" />
			</form>
		</div>
    </div>
    
  <hr class="rounded">
  <hr class="rounded">
    
    <div align="center">
    
    </div>
</body>
</html>