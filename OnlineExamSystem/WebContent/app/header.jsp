<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Online Exam System</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
* {box-sizing: border-box;}

body { 
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
}

.logoImage {
	display: inline-block;
	float: left;
}

.logoImage img{
	width: 80px;
	height: 80px;
}

.header {
  overflow: hidden;
  background-color: #f1f1f1;
  padding: 10px 10px;
  margin: 0 0 20px 0;
}

.userMessage {
	float: left;
	padding-top: 30px;
	font-size: 20px;
	font-family: Arial, Helvetica, sans-serif;
}

.header a {
  float: left;
  color: black;
  text-align: center;
  padding: 12px;
  text-decoration: none;
  font-size: 18px; 
  line-height: 25px;
  border-radius: 4px;
}

.header a.logo {
  font-size: 25px;
  font-weight: bold;
}

.header a.active:hover {
  background-color: #0C59B3;
  color: white;
}

.header a.active {
  background-color: #094183;
  color: white;
}

.header a.logout {
   background-color: #D52B2A;
   color: white;
   margin-left: 2px;
}

.header a.logout:hover {
	background-color: #DE5654;
	color: white;
}	

.header-right {
  float: right;
  padding: 15px;
}

@media screen and (max-width: 500px) {
  .header a {
    float: none;
    display: block;
    text-align: left;
  }
  
  .header-right {
    float: none;
  }
}
</style>
</head>
<body>
<div class="header">
  <div class="logoImage">
  	<img src="${pageContext.request.contextPath}/images/Uni-of-Melbourne.jpg" alt="uniMelbLogo" class="logo">
  </div>
  <div class="userMessage">
  	<font>Welcome, ${username}</font>
  </div>
  <div class="header-right">
    <a class="active" href="${pageContext.request.contextPath}/app/${role}/subjects.jsp">Subjects</a>
    <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
  </div>
</div>
</body>
</html>