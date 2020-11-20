<%@ page import="domain.Exam, mappers.ExamMapper, mappers.ExamAnswerMapper, domain.ExamAnswer,
				java.util.List" %>
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
	padding: 2px;
	background-color: #349eeb;
	font-weight: bold;
}
</style>
<meta charset="UTF-8">
<title>LMS System</title>
</head>
<body>
	<%@include file="../header.jsp"  %>
    <div align="center">
        <table  style="width:90%">
            <tr>
                <th>Exam ID</th>
                <th>Title</th>
                <th>Closed?</th>
                <th>Total Marks</th>
                <th>Total Questions</th>
                <th>Attempt</th>
            </tr>
            
                <tr>
 			<%
 				String message = (String)request.getAttribute("alertMsg");
	        	if (request.getParameter("subjectCode") != null){
	        		session.setAttribute("subjectCode", request.getParameter("subjectCode"));
	        	}
	        	String subjectCode = (String) session.getAttribute("subjectCode");
				List<Exam> exams = ExamMapper.getSubjectExams(subjectCode);
      		 	for (Exam exam : exams)  {
       		 %>
       		        <td><%= exam.getExamID() %></td>
                    <td><%= exam.getName() %></td>
                    <td><%= exam.getIsClosed() %></td>
                    <td><%= exam.getTotalMarks() %></td>
                    <td><%= exam.getQuestions().size() %></td>
                    <td>
                    	<% if (exam.canBeAnswered() && 
                    			ExamAnswerMapper.getExamAnswers(exam.getExamID(), (String) session.getAttribute("username")).isEmpty()){%>
	                    	<form name="answerExam" action="${pageContext.request.contextPath}/startAnswerExam" method="post">
	                    		<input type="hidden" name="examID" value=<%=exam.getExamID()%>>
	                    		<input type="submit" value="Answer">
	                    	</form>
	                    <%}%>
                    </td>
                </tr>
            <%
          		  } // for loop
        	%>
        </table>
    </div>
    
  <hr class="rounded">
  <hr class="rounded">
    
    <div align="center">
    
    </div>
    
<script type="text/javascript">
    var msg = "<%=message%>";
    if (msg != "null"){
        alert(msg);
    }
</script>
</body>
</html>