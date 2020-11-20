<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	font-family: Arial, Helvetica, sans-serif;
	height: 100%;
}
form {
	border: 3px solid #f1f1f1;
	width: 60%;
}

input[type=text], input[type=password] {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

a {
	text-align: center;
}

button {
  background-color: #094183;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 75%;
}

button:hover {
  opacity: 0.8;
}

.cancelbtn {
  width: auto;
  padding: 10px 18px;
  background-color: #f44336;
}

.title {
	text-align: center;
}

.imgcontainer {
  text-align: center;
  margin: 24px 0 12px 0;
}

img.avatar {
  width: 40%;
  border-radius: 50%;
  height: 200px;
  width: 200px;
}

.container {
  padding: 16px;
  text-align: center;
}

button.signup {
  text-align: center;
  justify-content: center;
  padding-top: 16px;
  width: 25%;
  background-color: #834B09;
}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
  span.signup {
     display: block;
     float: none;
  }
  .cancelbtn {
     width: 100%;
  }
}
</style>
<meta charset="UTF-8">
<title>Sign Up</title>
</head>
<body>
	<div align="center">
		<form action="/OnlinExamBackend/signup" method="post">
  			<div class="imgcontainer">
    			<img src="images/Uni-of-Melbourne.jpg" alt="Avatar" class="avatar">
  			</div>
			<div class="title">
				<h1>Sign Up</h1>
			</div>
  			<div class="container">
  				<label for="uname"><b>University Username</b></label>
    			<input type="text" placeholder="Enter University Username" name="userName" required/>
    			
    			<label for="email"><b>Email</b></label>
    			<input type="text" placeholder="Enter Email" name="email" required/>
    			
   				<label for="psw"><b>Password</b></label>
    			<input type="password" placeholder="Enter Password" name="passWord" required/>
    			
    			<label for="roles">Choose a role:</label>
				<select id="roles" name="roles">
				  <option value="student">Student</option>
				  <option value="instructor">Instructor</option>
				</select>
				
        		<button type="submit" value="Signup">Sign Up</button>
    			
  			</div>
		</form>
	</div>
</body>
</html>