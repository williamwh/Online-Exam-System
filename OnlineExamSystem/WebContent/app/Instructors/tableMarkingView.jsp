<%@ page import="mappers.ExamMapper, domain.Exam, mappers.ExamAnswerMapper, 
				mappers.StudentSubMapper, domain.ExamAnswer, java.util.List, domain.Student" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="${pageContext.request.contextPath}/styling/examMarking.css" type="text/css" rel="stylesheet" />
<meta charset="UTF-8">
<title>Table Marking View</title>
</head>
<body>
	<%@include file="../header.jsp"  %>
	<h1 align="center">Subject Marking</h1>
    <div align="center">
        <table  style="width:90%">
            <tr>
                <th>Student Name</th>
            <%
            	if (request.getParameter("subjectCode") != null){
            		session.setAttribute("subjectCode", request.getParameter("subjectCode"));
            	}
            	String subjectCode = (String) session.getAttribute("subjectCode");
            	System.out.println(subjectCode);
            	List<Exam> exams = ExamMapper.getSubjectExams(subjectCode);
        		for (Exam exam : exams) {		
        	 %>
        			<th><%= exam.getName() %> <p style="color:red; font-size:80%;"> <%= exam.getIsPublished()%></p></th>
        	<%
        		}
            %>
         
			<%  
            	for (Student student : StudentSubMapper.findStudents(subjectCode)) {
            %>
			<tr>
           			<td><%= student.getUsername()%></td>
            		<% for (Exam exam : exams) {	
            			List<ExamAnswer> examAnswers = ExamAnswerMapper.getExamAnswers(exam.getExamID(), student.getUsername());
            		%>
            		<td>
	            		<%for (ExamAnswer examAnswer : examAnswers) {%>
            					
            						<form name="tableUpdateMarks" action="${pageContext.request.contextPath}/tableUpdateMarks" method="post">
	            						<input style="width:15%" type="number" min="0" max=<%=Integer.toString(exam.getTotalMarks())%> 
	            							value=<%=Integer.toString(examAnswer.getTotalMarks())%> name="mark">
	            						/<%=exam.getTotalMarks()%>
	            						<input type="hidden" name="examAnswerID" value=<%=Integer.toString(examAnswer.getExamAnswerID())%>>
	            						<input type="submit" value="Save">
	            						<!-- <input type="button" onclick="location.href='detailedMarkingView.jsp';" value="Detail"> -->
	            					</form>
	            					<form style="float:right" name="openDetailedView" action="${pageContext.request.contextPath}/app/Instructors/detailedMarkingView.jsp" method="post">
	            						<input type="hidden" name="examAnswerID" value=<%=Integer.toString(examAnswer.getExamAnswerID())%>>
	            						<input type="submit" value="->Detail">
	 
	            					</form>
            					
           		        <%}%>
         		    </td>
           		    <%}%>
			</tr>
        		<%}%>
            
        </table>
    </div>

</body>
</html>