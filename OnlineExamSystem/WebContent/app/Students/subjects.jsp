<%@ page import="mappers.SubjectMapper, domain.Subject, mappers.InstructorMapper, domain.Instructor, 
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
</style>
  
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
 				 String username = (String) session.getAttribute("username");
 				 SubjectMapper subjectMapper = new SubjectMapper();
 				 List<Subject> subjects = subjectMapper.getStudentSubjects(username);
           		 for (Subject subject : subjects) {
       		 %>
       		        <td><%= subject.getSubjectCode() %></td>
                    <td><%= subject.getName() %></td>
                    <td>
                    	<form name="openSubjectView" action="${pageContext.request.contextPath}/app/Students/Exams.jsp" method="get">
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
    
    <div align="center">
    
    </div>
</body>
</html>