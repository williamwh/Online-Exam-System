<%@ page import="mappers.SubjectMapper, domain.Subject, domain.Student, domain.Exam, domain.Instructor" %>
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

* {
  box-sizing: border-box;
}

input[type=text], select, textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: vertical;
}

label {
  padding: 12px 12px 12px 0;
  display: inline-block;
}

input[type=submit] {
  color: white;
  background-color: #094183;
  padding: 12px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin: auto;
}

input[type=submit]:hover {
  background-color: #0C59B3;
}

.formHeader {
  background-color: #094183;
  color: white;
  padding: 5px 20px 5px 20px;
}

.container {
  border-radius: 5px;
  background-color: #f2f2f2;
  width: 70%;
  margin: auto;
}

.row {
  padding: 5px 20px 5px 20px;
}

.submitRow {
  padding: 5px 20px 5px 20px;
  justify-content: center;
  margin: auto;
  float: right;
}

.col-25 {
  float: left;
  width: 25%;
  margin-top: 6px;
}

.col-75 {
  float: left;
  width: 75%;
  margin-top: 6px;
}

/* Clear floats after the columns */
.row:after {
  content: "";
  display: table;
  clear: both;
}

.subjectHeader{
	margin-left:15%;
}
</style>
  
<meta charset="UTF-8">
<title>LMS System</title>
</head>
	<body>
		<%@include file="../header.jsp"  %>
		<% 
			String subjectCode = request.getParameter("subjectCode"); 
			Subject subject = SubjectMapper.find(subjectCode);
		%>
		<div class="subjectHeader">
		<h1><%=subjectCode%> <%=subject.getName()%></h1>
	    </div>
	    
	    <div align="center">
	    	<table  style="width:70%">
	            <tr>
	                <th>Coordinator</th>
	                <th>Email</th>
	            </tr>
					<% for (Instructor instructor : subject.getCoordinators()) {  %>
	           	<tr>
		           	<td><%=instructor.getUsername()%></td>
		           	<td><%=instructor.getEmail()%></td>
	           	</tr>
	           	<% } %>
	        </table>
	        <br>
	        <table  style="width:70%">
	            <tr>
	                <th>Student</th>
	                <th>Email</th>
	            </tr>
					<% for (Student student : subject.getStudents()) {  %>
	           	<tr>
		           	<td><%=student.getUsername()%></td>
		           	<td><%=student.getEmail()%></td>
	           	</tr>
	           	<% } %>
	        </table>
	        <br>
	        <table  style="width:70%">
	            <tr>
	                <th>Exams</th>
	            </tr>
					<% for (Exam exam : subject.getExams()) {  %>
	           	<tr>
		           	<td><%=exam.getName()%></td>
	           	</tr>
	           	<% } %>
        </table>
	    </div>
	</body>
</html>