<%@ page import="mappers.SubjectMapper, domain.Subject, mappers.InstructorMapper, domain.Instructor, domain.Student, mappers.StudentMapper" %>
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
</style>l
  
<meta charset="UTF-8">
<title>LMS System</title>
</head>
<body>
	<%@include file="../header.jsp"  %>
    <div align="center">
        <table  style="width:70%">
            <tr>
                <th>Subject Code</th>
                <th>Subject Name</th>
                <th>Details</th>
            </tr>
            
                <tr>
 			<%
           		 for (Subject subject : SubjectMapper.getAllSubjects()) {
       		 %>
       		        <td><%= subject.getSubjectCode() %></td>
                    <td><%= subject.getName() %></td>
                    
                    <td>
                    	<form name="openSubjectView" action="${pageContext.request.contextPath}/app/Admins/viewSubject.jsp" method="get">
                    		<input type="hidden" name="subjectCode" value=<%=subject.getSubjectCode()%>>
                    		<input type="submit" value="View">
                    	</form>
                   	</td>
                </tr>
            <%
          		  } // for loop
        	%>
        </table>
    </div>
    
  <hr class="rounded">
  <hr class="rounded">
  
  <div class="container">
	  <form name="AddSubjectForm" action="addSubject" method="post">
	  	<div class="formHeader">
	  		<h2>Add Subject</h2>
	  	</div>
	    <div class="row">
	      <div class="col-25">
	        <label for="code">Subject Code</label>
	      </div>
	      <div class="col-75">
	        <input type="text" id="code" name="code" placeholder="Subject Code.." required>
	      </div>
	    </div>
	    <div class="row">
	      <div class="col-25">
	        <label for="name">Subject Name</label>
	      </div>
	      <div class="col-75">
	        <input type="text" id="name" name="name" placeholder="Subject Name.." required>
	      </div>
	    </div>
	    <div class="row">
	      <div class="col-25">
	        <label for="coordinator">Coordinator</label>
	      </div>
	      <div class="col-75">
	        <select id="coordinators" name="coordinators" multiple style="height:100px" required>
	          <% for (Instructor instructor : InstructorMapper.findAll()) { %>
         		<option><%=instructor.getUsername() %></option>
         	  <% } %>
	        </select>
	      </div>
	    </div>
	    <div class="row">
	      <div class="col-25">
	        <label for="students">Students</label>
	      </div>
	      <div class="col-75">
	        <select id="students" name="students" multiple style="height:200px" required>
	         	<% for (Student student : StudentMapper.findAll()) { %>
	                <option><%=student.getUsername() %></option> 
	            <% } %>
	        </select>
	      </div>
	    </div>
	    <div class="submitRow">
	      <input type="submit" value="Add New Subject">
	    </div>
  </form>
</div>
</body>
</html>