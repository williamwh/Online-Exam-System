<%@ page import="mappers.SubjectMapper, domain.Subject, mappers.InstructorMapper, domain.Instructor" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create Exam</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/Instructors/CreateExam.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
<script src="http://malsup.github.com/jquery.form.js"></script> 
</head>
<body onload="poistiveNumberValidation()">
	<%@include file="../header.jsp"  %>
	<form id="AddExamForm" name="AddExamForm" action="${pageContext.request.contextPath}/createExam" method="post">
		<div id="ExamHeader">
			<label>Exam Name: </label>
			<input type="text" name="examName" id="ExamNameInput">
			<input type="hidden" name="subjectCode" value="<%=request.getParameter("subjectCode")%>">
			<input type="hidden" id="AmountOfQuestionInput" name="amountOfQuestions" value="0">
			<input type="hidden" id="isPublishedInput" name="isPublished" value="false">
			<input type="hidden" id="modifiedByInput" name="modifiedBy" value="<%=(String) session.getAttribute("username")%>">
			
			<label>Time Allowed (Minutes):</label>
			<input type="number" id="timeAllowedInput" name="timeAllowed" min="0" value="0">
		</div>
		<div id="QuestionBody">
		</div>
		</form>
		<div class="AddQuestion" id="AddQuestionDiv">
		</div>
		
		<div class="Bottom" id="Bottom">
		<button id="newQuestionBtn" onclick="newQuestion()" style="float:left;">New Question</button>
			<ul>
				<li>
					<button type="button" onclick="saveExam()">Save</button>
				</li>
				<li>
					<button type="button" onclick="savePublishExam()">Save & Publish</button>
				</li>
				<li>
					<form name="cancelForm" action="${pageContext.request.contextPath}/app/Instructors/Exams.jsp" method="get">
                    		<input type="hidden" name="subjectCode" value=<%=request.getParameter("subjectCode")%>>
                    		<input id="cancelSaveButton" type="submit" value="Cancel">
                   	</form>
				</li>
			</ul>
		</div>
	
</body>
 
<script type="text/javascript">
	function poistiveNumberValidation(){
		var timeAllowedInput = document.getElementById("timeAllowedInput");
		timeAllowedInput.onkeydown = function(e) {
		    if(!((e.keyCode > 95 && e.keyCode < 106)
		      || (e.keyCode > 47 && e.keyCode < 58) 
		      || e.keyCode == 8)) {
		        return false;
		    }
		}
	}
	
	function addQuestion() {
		
		// create html tags and input content
		var question = document.createElement("div");
		question.setAttribute("class", "Question");
		var questionHeader = document.createElement("div");
		questionHeader.setAttribute("class", "QuestionHeader")
		var ulHeader = document.createElement("ul");
		var questionName = document.createElement("li");
		questionName.textContent = document.getElementById("newQuestionIndex").textContent;
		questionName.setAttribute("class", "QuestionIndexLabel");
		var points = document.createElement("li");
		points.setAttribute("style", "float:right");
		points.textContent =  document.getElementById("PtsInput").value + " pts";
		var questionContent = document.createElement("div");
		questionContent.setAttribute("class", "QuestionContent");
		questionContent.textContent = document.getElementById("addQuestionTextArea").value;
		var questionOptions = document.createElement("div");
		questionOptions.setAttribute("class", "QuestionOptions");
		var ulOptions = document.createElement("ul");
		var liEdit = document.createElement("li");
		var editButton = document.createElement("button");
		editButton.textContent = "Edit";
		editButton.setAttribute("type", "button");
		editButton.setAttribute("class", "editButton")
		editButton.setAttribute("onclick", "editQuestion(this)");
		var liDelete = document.createElement("li");
		var deleteButton = document.createElement("button");
		deleteButton.textContent = "Delete";
		deleteButton.setAttribute("type", "button");
		deleteButton.setAttribute("onclick", "deleteQuestion(this)");
		deleteButton.setAttribute("class", "deleteButton")
		
		//createHiddenForm
		var questionIndex = document.getElementById("newQuestionIndex").textContent.substring(9, document.getElementById("newQuestionIndex").textContent.length);
		
		var hiddenFormDiv = document.createElement("div");
		hiddenFormDiv.setAttribute("style", "display:none");
		hiddenFormDiv.setAttribute("id", "hiddenForm" + questionIndex);
		
		
		var examIdInput = document.createElement("input");
		examIdInput.setAttribute("type", "hidden");
		examIdInput.setAttribute("name", "examId" + questionIndex);
		examIdInput.setAttribute("value", "-1");
		
		var questionIndexInput = document.createElement("input");
		questionIndexInput.setAttribute("type", "hidden");
		questionIndexInput.setAttribute("name", "questionIndex" + questionIndex);
		questionIndexInput.setAttribute("value", questionIndex);
		
		var questionTextInput = document.createElement("input");
		questionTextInput.setAttribute("type", "hidden");
		questionTextInput.setAttribute("name", "questionText" + questionIndex);
		questionTextInput.setAttribute("value", document.getElementById("addQuestionTextArea").value);
		
		var pointsInput = document.createElement("input");
		pointsInput.setAttribute("type", "hidden");
		pointsInput.setAttribute("name", "points" + questionIndex);
		pointsInput.setAttribute("value", document.getElementById("PtsInput").value);
		
		var questionTypeInput = document.createElement("input");
		questionTypeInput.setAttribute("type", "hidden");
		questionTypeInput.setAttribute("name", "questionType" + questionIndex);
		questionTypeInput.setAttribute("value", document.getElementById("newQuestionType").value);
		
		// format html tags
		ulHeader.appendChild(questionName);
		ulHeader.appendChild(points);
		questionHeader.appendChild(ulHeader);
		liEdit.appendChild(editButton);
		liDelete.appendChild(deleteButton);
		ulOptions.appendChild(liEdit);
		ulOptions.appendChild(liDelete);
		questionOptions.appendChild(ulOptions);
		
		question.appendChild(questionHeader);
		question.appendChild(questionContent);
		// HTML If MC question
		if (document.getElementById("newQuestionType").value == "MC"){
			var questionChoices = document.createElement("div");
			questionChoices.setAttribute("class", "QuestionChoices")
			var AddChoicesDiv = document.getElementById("AddChoicesDiv");
			for (i = 1; i < AddChoicesDiv.childNodes.length; i++){
				var choiceNumberText = document.createElement("label");
				choiceNumberText.setAttribute("id", "choiceNumberText"+i);
				choiceNumberText.textContent = AddChoicesDiv.childNodes[i].childNodes[0].textContent;
				var choiceContent = document.createElement("label");
				choiceContent.setAttribute("id", "choiceContent"+i);
				choiceContent.textContent = AddChoicesDiv.childNodes[i].childNodes[1].value;
				var choiceDiv = document.createElement("div");
				choiceDiv.appendChild(choiceNumberText);
				choiceDiv.appendChild(choiceContent);
				questionChoices.appendChild(choiceDiv);
			};
			question.appendChild(questionChoices);
		}
		question.appendChild(questionOptions);
		
		// add hidden form to question
		hiddenFormDiv.appendChild(examIdInput);
		hiddenFormDiv.appendChild(questionIndexInput);
		hiddenFormDiv.appendChild(questionTextInput);
		hiddenFormDiv.appendChild(pointsInput);
		hiddenFormDiv.appendChild(questionTypeInput);
		// Hidden form If MC question
		if (document.getElementById("newQuestionType").value == "MC"){
			var AddChoicesDiv = document.getElementById("AddChoicesDiv");
			for (i = 1; i < AddChoicesDiv.childNodes.length; i++){
				// hidden form
				var choiceInput = document.createElement("input");
				choiceInput.setAttribute("type", "hidden");
				choiceInput.setAttribute("name", "q" +questionIndex + "Choice" + i);
				choiceInput.setAttribute("id", "q" +questionIndex + "Choice" + i);
				choiceInput.setAttribute("value", AddChoicesDiv.childNodes[i].childNodes[1].value);
				hiddenFormDiv.appendChild(choiceInput);
				
				var questionIdInput = document.createElement("input");
				questionIdInput.setAttribute("type", "hidden");
				questionIdInput.setAttribute("name", "questionId" + questionIndex + "Choice" + i);
				questionIdInput.setAttribute("id", "questionId" + questionIndex + "Choice" + i);
				questionIdInput.setAttribute("value", "-1");
				hiddenFormDiv.appendChild(questionIdInput);
			};
			var amountOfChoices = AddChoicesDiv.childNodes.length - 1
			var amountOfChoicesInput = document.createElement("input");
			amountOfChoicesInput.setAttribute("type", "hidden");
			amountOfChoicesInput.setAttribute("name", "q" +questionIndex + "AmountOfChoices");
			amountOfChoicesInput.setAttribute("id", "q" +questionIndex + "AmountOfChoices");
			amountOfChoicesInput.setAttribute("value", amountOfChoices);
			hiddenFormDiv.appendChild(amountOfChoicesInput);	
		}
		question.appendChild(hiddenFormDiv);
		
		// add question to html
		var questionBody = document.getElementById("QuestionBody");
		questionBody.appendChild(question);
		
		// Increment amount of questions
		var amountOfQuestionInput = document.getElementById("AmountOfQuestionInput");
		var totalValue = parseInt(amountOfQuestionInput.value) + 1;
		amountOfQuestionInput.setAttribute("value", totalValue);
		
		// remove question form
		var addQuestionForm = document.getElementById("AddQuestionForm");
		addQuestionForm.parentNode.removeChild(addQuestionForm);
		
		// var newQuestionBtn = document.getElementById("newQuestionBtn");
		// newQuestionBtn.setAttribute("style", "display:block;");
		
		// Show Question Options
		var questionOptionsInBody = document.getElementsByClassName("QuestionOptions")
		for(var i = 0; i < questionOptionsInBody.length; i++){
			questionOptionsInBody[i].style["display"] = "block";
		}
		
		document.getElementById("Bottom").style["display"] = "block";
	}
	
	
	
	
	
	
	function newQuestion(){
		
		
		var questionsIndexLabel = document.getElementsByClassName("QuestionIndexLabel");
		var newIndex = 1
		if (questionsIndexLabel.length > 0){
			lastQuestionText = questionsIndexLabel[questionsIndexLabel.length-1].textContent
			newIndex = parseInt(lastQuestionText.substring(9, lastQuestionText.length)) + 1;
		}
		
		// create html tags and input content
		var addQuestionForm = document.createElement("form");
		addQuestionForm.setAttribute("id", "AddQuestionForm");
		var addQuestionHeader = document.createElement("div");
		addQuestionHeader.setAttribute("class", "AddQuestionHeader");
		var addQuestionHeaderUl = document.createElement("ul");
		var questionIndexLi = document.createElement("li");
		questionIndexLi.setAttribute("id", "newQuestionIndex");
		questionIndexLi.textContent = "Question " + newIndex;
		var questionTypeLi = document.createElement("li");
		var questionTypeSelect = document.createElement("select");
		questionTypeSelect.setAttribute("id", "newQuestionType");
		var EQOption = document.createElement("option");
		EQOption.setAttribute("value", "Short");
		EQOption.textContent = "Essay Question";
		var MCOption = document.createElement("option");
		MCOption.setAttribute("value", "MC");
		MCOption.textContent = "Multiple Choice";
		var pointsLi = document.createElement("li");
		pointsLi.setAttribute("style", "float:right; margin-right:0px;");
		var pointsLabel = document.createElement("label");
		pointsLabel.textContent = "pts:";
		var pointsInput = document.createElement("input");
		pointsInput.setAttribute("type", "number");
		pointsInput.setAttribute("id", "PtsInput");
		pointsInput.setAttribute("value", "0");
		pointsInput.setAttribute("min", "0");
		pointsInput.onkeydown = function(e) {
		    if(!((e.keyCode > 95 && e.keyCode < 106)
		      || (e.keyCode > 47 && e.keyCode < 58) 
		      || e.keyCode == 8)) {
		        return false;
		    }
		}

		var addQuestionContentDiv = document.createElement("div");
		addQuestionContentDiv.setAttribute("class", "AddQuestionContent");
		addQuestionContentDiv.setAttribute("id", "AddQuestionContent");
		var questionLabel = document.createElement("label");
		questionLabel.textContent = "Question:";
		var br = document.createElement("br");
		var addQuestionTextArea = document.createElement("textarea");
		addQuestionTextArea.setAttribute("rows", "5");
		addQuestionTextArea.setAttribute("form", "AddQuestionForm");
		addQuestionTextArea.setAttribute("placeholder", "Enter Question Here...");
		addQuestionTextArea.setAttribute("id", "addQuestionTextArea");
		var cancelButton = document.createElement("button");
		cancelButton.setAttribute("type", "button")
		cancelButton.textContent = "Cancel";
		cancelButton.setAttribute("onclick", "cancelAddQuestion()")
		var addQuestionButton = document.createElement("input");
		addQuestionButton.setAttribute("type", "button");
		addQuestionButton.setAttribute("value", "Add");
		addQuestionButton.setAttribute("name", "Add Question");
		addQuestionButton.setAttribute("onclick", "addQuestion()");
		addQuestionButton.setAttribute("id", "AddQuestionBtn");
		
		// format html tags
		questionTypeSelect.appendChild(EQOption);
		questionTypeSelect.appendChild(MCOption);
		questionTypeLi.appendChild(questionTypeSelect);
		addQuestionHeaderUl.appendChild(questionIndexLi);
		addQuestionHeaderUl.appendChild(questionTypeLi);
		pointsLi.appendChild(pointsLabel);
		pointsLi.appendChild(pointsInput);
		addQuestionHeaderUl.appendChild(pointsLi);
		addQuestionHeader.appendChild(addQuestionHeaderUl);
		addQuestionContentDiv.appendChild(questionLabel);
		addQuestionContentDiv.appendChild(br);
		addQuestionContentDiv.appendChild(addQuestionTextArea);
		addQuestionContentDiv.appendChild(br);
		addQuestionForm.appendChild(addQuestionHeader);
		addQuestionForm.appendChild(addQuestionContentDiv);
		addQuestionForm.appendChild(cancelButton);
		addQuestionForm.appendChild(addQuestionButton);
		
		var addQuestionDiv = document.getElementById("AddQuestionDiv");
		addQuestionDiv.appendChild(addQuestionForm);
		
		// var newQuestionBtn = document.getElementById("newQuestionBtn");
		// newQuestionBtn.setAttribute("style", "display:none;")
		
		var questionOptionsInBody = document.getElementsByClassName("QuestionOptions")
		for(var i = 0; i < questionOptionsInBody.length; i++){
			questionOptionsInBody[i].style["display"] = "none";
		}
		document.getElementById("Bottom").style["display"] = "none";
	}
	
	
	
	function cancelAddQuestion(){
		// remove question form
		var addQuestionForm = document.getElementById("AddQuestionForm");
		addQuestionForm.parentNode.removeChild(addQuestionForm);
		
		var questionOptionsInBody = document.getElementsByClassName("QuestionOptions")
		for(var i = 0; i < questionOptionsInBody.length; i++){
			questionOptionsInBody[i].style["display"] = "block";
		}
		
		document.getElementById("Bottom").style["display"] = "block";
		//  var newQuestionBtn = document.getElementById("newQuestionBtn");
		//  newQuestionBtn.setAttribute("style", "display:block;");
	}
	
	
	
	/*
	function deleteQuestion(self){
		var question = self.parentNode.parentNode.parentNode.parentNode
		
		//change move other question's index up by 1
		var deletedIndexLabel = question.childNodes[0].childNodes[0].childNodes[0].textContent;
		var deletedIndex = parseInt(deletedIndexLabel.substring(9, deletedIndexLabel.length));
		var questionsIndexLabels = document.getElementsByClassName("QuestionIndexLabel")
		var questions = document.getElementsByClassName("Question")
		for (var i = deletedIndex; i < questionsIndexLabels.length; i++) {
			questionsIndexLabels[i].textContent = "Question " + i;
			// Hidden Form
			questions[i].childNodes[3].setAttribute("name", "examId" + i)
			questions[i].childNodes[4].setAttribute("name", "questionIndex" + i)
			questions[i].childNodes[5].setAttribute("name", "questionText" + i)
			questions[i].childNodes[6].setAttribute("name", "points" + i)
			questions[i].childNodes[7].setAttribute("name", "questionType" + i)
		};
		
		// delete selected question
		question.parentNode.removeChild(question);
		
		var amountOfQuestionInput = document.getElementById("AmountOfQuestionInput");
		var totalValue = parseInt(amountOfQuestionInput.value) - 1;
		amountOfQuestionInput.setAttribute("value", totalValue);
	}
	*/
	
	function deleteQuestion(self){
		var question = self.parentNode.parentNode.parentNode.parentNode;

		//change move other question's index up by 1
		var deletedIndexLabel = question.childNodes[0].childNodes[0].childNodes[0].textContent;
		var deletedIndex = parseInt(deletedIndexLabel.substring(9, deletedIndexLabel.length));
		var questionsIndexLabels = document.getElementsByClassName("QuestionIndexLabel")
		var questions = document.getElementsByClassName("Question")
		//questions = removeTextNodes(questions);
		for (var i = deletedIndex; i < questionsIndexLabels.length; i++) {
			questionsIndexLabels[i].textContent = "Question " + i;
			// Hidden Form
			questions[i].lastChild.childNodes[0].setAttribute("name", "examId" + i)
			questions[i].lastChild.childNodes[1].setAttribute("name", "questionIndex" + i)
			questions[i].lastChild.childNodes[1].setAttribute("value", i);
			questions[i].lastChild.childNodes[2].setAttribute("name", "questionText" + i)
			questions[i].lastChild.childNodes[3].setAttribute("name", "points" + i)
			questions[i].lastChild.childNodes[4].setAttribute("name", "questionType" + i)
			if (questions[i].childNodes[2].className === "QuestionChoices"){
				for (j = 5; j < questions[i].lastChild.childNodes.length - 1; j+=2){
					questions[i].lastChild.childNodes[j].name = replaceChoiceStr(questions[i].lastChild.childNodes[j].name, i, 1);
					questions[i].lastChild.childNodes[j].id = replaceChoiceStr(questions[i].lastChild.childNodes[j].id, i, 1);
					questions[i].lastChild.childNodes[j+1].name = replaceChoiceStr(questions[i].lastChild.childNodes[j+1].name, i, 10);
					questions[i].lastChild.childNodes[j+1].id = replaceChoiceStr(questions[i].lastChild.childNodes[j+1].id, i, 10);
				}
				questions[i].lastChild.lastChild.name = replaceChoiceAmountStr(questions[i].lastChild.lastChild.name , i);
				questions[i].lastChild.lastChild.id = replaceChoiceAmountStr(questions[i].lastChild.lastChild.id , i);
			}
		};
		
		
		// delete selected question
		question.parentNode.removeChild(question);
		
		var amountOfQuestionInput = document.getElementById("AmountOfQuestionInput");
		var totalValue = parseInt(amountOfQuestionInput.value) - 1;
		amountOfQuestionInput.setAttribute("value", totalValue);

	}
	
	function replaceChoiceStr(currentStr, replacement, startIndex){
		// startindex = 1 for q2choice1
		// startindex = 10 for questionId2choice1
		var endIndex;
		for (var i = 0; i < currentStr.length; i++){	
			if (currentStr[i] === "C"){
				endIndex = i;
			}
		}
		return currentStr.substr(0, startIndex) + replacement + currentStr.substr(endIndex, currentStr.length)
	}
	
	function replaceChoiceAmountStr(currentStr, replacement){
		var startIndex = 1;
		var endIndex;
		for (var i = 0; i < currentStr.length; i++){	
			if (currentStr[i] === "A"){
				endIndex = i;
			}
		}
		return currentStr.substr(0, startIndex) + replacement + currentStr.substr(endIndex, currentStr.length)
	}

	
	
	
	function editQuestion(self){
		var question = self.parentNode.parentNode.parentNode.parentNode
		
		for(var i = 0; i < question.childNodes.length; i++){
			question.childNodes[i].style["display"] = "none";
		}
		
		//Check if MC question
		var pointsValue;
		var newIndex;
		var textAreaValue;
		var multipleChoice;
		if (question.childNodes[2].className === "QuestionChoices"){
			multipleChoice = true
			newIndex = question.childNodes[4].childNodes[1].value;
			pointsValue = question.childNodes[4].childNodes[3].value;
			textAreaValue = question.childNodes[4].childNodes[2].value;
		} else {
			multipleChoice = false
			newIndex = question.childNodes[3].childNodes[1].value;
			pointsValue = question.childNodes[3].childNodes[3].value;
			textAreaValue = question.childNodes[3].childNodes[2].value;
		}
			

		// create html tags and input content
		var addQuestionForm = document.createElement("form");
		addQuestionForm.setAttribute("id", "AddQuestionForm");
		var addQuestionHeader = document.createElement("div");
		addQuestionHeader.setAttribute("class", "AddQuestionHeader");
		var addQuestionHeaderUl = document.createElement("ul");
		var questionIndexLi = document.createElement("li");
		questionIndexLi.setAttribute("id", "newQuestionIndex");
		questionIndexLi.textContent = "Question " + newIndex;
		var questionTypeLi = document.createElement("li");
		var questionTypeSelect = document.createElement("select");
		questionTypeSelect.setAttribute("id", "newQuestionType");
		var EQOption = document.createElement("option");
		EQOption.setAttribute("value", "Short");
		EQOption.textContent = "Essay Question";
		var MCOption = document.createElement("option");
		MCOption.setAttribute("value", "MC");
		MCOption.textContent = "Multiple Choice";
		var pointsLi = document.createElement("li");
		pointsLi.setAttribute("style", "float:right; margin-right:0px;");
		var pointsLabel = document.createElement("label");
		pointsLabel.textContent = "pts:";
		var pointsInput = document.createElement("input");
		pointsInput.setAttribute("type", "number");
		pointsInput.setAttribute("id", "PtsInput");
		pointsInput.setAttribute("value", pointsValue);
		pointsInput.setAttribute("min", "0");
		pointsInput.onkeydown = function(e) {
		    if(!((e.keyCode > 95 && e.keyCode < 106)
		      || (e.keyCode > 47 && e.keyCode < 58) 
		      || e.keyCode == 8)) {
		        return false;
		    }
		}
	
		var addQuestionContentDiv = document.createElement("div");
		addQuestionContentDiv.setAttribute("class", "AddQuestionContent");
		addQuestionContentDiv.setAttribute("id", "AddQuestionContent");
		var questionLabel = document.createElement("label");
		questionLabel.textContent = "Question:";
		var br = document.createElement("br");
		var addQuestionTextArea = document.createElement("textarea");
		addQuestionTextArea.setAttribute("rows", "5");
		addQuestionTextArea.setAttribute("form", "AddQuestionForm");
		addQuestionTextArea.setAttribute("id", "addQuestionTextArea");
		addQuestionTextArea.textContent = textAreaValue;
		var cancelButton = document.createElement("button");
		cancelButton.setAttribute("type", "button")
		cancelButton.textContent = "Cancel";
		cancelButton.setAttribute("onclick", "cancelUpdateQuestion(this)")
		var addQuestionButton = document.createElement("input");
		addQuestionButton.setAttribute("type", "button");
		addQuestionButton.setAttribute("value", "Update");
		addQuestionButton.setAttribute("name", "Update Question");
		addQuestionButton.setAttribute("onclick", "UpdateQuestion(this)");
		addQuestionButton.setAttribute("id", "UpdateQuestionBtn");
		
			
		
		questionTypeSelect.appendChild(EQOption);
		questionTypeSelect.appendChild(MCOption);
		questionTypeLi.appendChild(questionTypeSelect);
		addQuestionHeaderUl.appendChild(questionIndexLi);
		addQuestionHeaderUl.appendChild(questionTypeLi);
		pointsLi.appendChild(pointsLabel);
		pointsLi.appendChild(pointsInput);
		addQuestionHeaderUl.appendChild(pointsLi);
		addQuestionHeader.appendChild(addQuestionHeaderUl);
		addQuestionContentDiv.appendChild(questionLabel);
		addQuestionContentDiv.appendChild(br);
		addQuestionContentDiv.appendChild(addQuestionTextArea);
		addQuestionContentDiv.appendChild(br);
		addQuestionForm.appendChild(addQuestionHeader);
		addQuestionForm.appendChild(addQuestionContentDiv);
		addQuestionForm.appendChild(cancelButton);
		addQuestionForm.appendChild(addQuestionButton);
		
		
		if (multipleChoice){
			MCOption.setAttribute('selected', 'selected');
			// Choices container
			var addChoicesDiv = document.createElement("div");
			addChoicesDiv.setAttribute("id", "AddChoicesDiv");
			
			var addChoiceBtn = document.createElement("button");
			addChoiceBtn.setAttribute("type", "button");
			addChoiceBtn.textContent = "Add New Choice"
			addChoiceBtn.setAttribute("onclick", "addChoice(this)");
			
			addChoicesDiv.appendChild(addChoiceBtn);
			
			var amountOfChoices = document.getElementById('q'+ newIndex +'AmountOfChoices').value;
			for (var i = 1; i <= amountOfChoices; i++){
				var choiceValue = document.getElementById('q' + newIndex + 'Choice' + i).value;
				var choiceDiv = document.createElement("div");
				var choiceLabel = document.createElement("label");
				choiceLabel.textContent = "Choice " + i;
				var choiceInput = document.createElement("input");
				choiceInput.setAttribute("type", "text");
				choiceInput.setAttribute("name", "choice1");
				choiceInput.setAttribute("value", choiceValue);
				var deleteChoiceBtn = document.createElement("button");
				deleteChoiceBtn.setAttribute("type", "button")
				deleteChoiceBtn.setAttribute("onclick", "deleteChoice(this)")
				deleteChoiceBtn.setAttribute("class", "deleteChoiceBtn")
				deleteChoiceBtn.textContent = "Delete";
				
				choiceDiv.appendChild(choiceLabel);
				choiceDiv.appendChild(choiceInput);
				choiceDiv.appendChild(deleteChoiceBtn);
				
				addChoicesDiv.appendChild(choiceDiv);
			}
			addQuestionContentDiv.appendChild(addChoicesDiv);
			
			
		} else {
			EQOption.setAttribute('selected', 'selected');
		}	
		
		
		question.appendChild(addQuestionForm);
		
		
		//Disable other buttons
		var questionOptionsInBody = document.getElementsByClassName("QuestionOptions")
		for(var i = 0; i < questionOptionsInBody.length; i++){
			questionOptionsInBody[i].style["display"] = "none";
		}
		// document.getElementById("newQuestionBtn").style["display"] = "none";
		document.getElementById("Bottom").style["display"] = "none";
		
		var allDeleteChoiceBtn = document.getElementsByClassName("deleteChoiceBtn");
		console.log(allDeleteChoiceBtn.length);
		if (allDeleteChoiceBtn.length === 2){
			for(var i =0; i < allDeleteChoiceBtn.length; i++)
			allDeleteChoiceBtn[i].style["display"] = "none";
		}
	}
	
	
	function cancelUpdateQuestion(self){
		var question = self.parentNode.parentNode
		for(var i = 0; i < question.childNodes.length; i++){
			question.childNodes[i].style["display"] = "block";
		}
		
		var questionOptionsInBody = document.getElementsByClassName("QuestionOptions")
		for(var i = 0; i < questionOptionsInBody.length; i++){
			questionOptionsInBody[i].style["display"] = "block";
		}
		
		var updateQuestionForm = self.parentNode;
		question.removeChild(updateQuestionForm);
		
		document.getElementById("Bottom").style["display"] = "block";
	}
	
	
	
	
	function UpdateQuestion(self){
		var question = self.parentNode.parentNode;		
		
		var childLength = question.childNodes.length;
		
		var multipleChoice;
		if (document.getElementById("newQuestionType").value == "MC"){
			multipleChoice = true;
		} else{
			multipleChoice = false;
		}
		
		var questionHeader = document.createElement("div");
		questionHeader.setAttribute("class", "QuestionHeader")
		var ulHeader = document.createElement("ul");
		var questionName = document.createElement("li");
		questionName.textContent = document.getElementById("newQuestionIndex").textContent;
		questionName.setAttribute("class", "QuestionIndexLabel");
		var points = document.createElement("li");
		points.setAttribute("style", "float:right");
		points.textContent =  document.getElementById("PtsInput").value + " pts";
		var questionContent = document.createElement("div");
		questionContent.setAttribute("class", "QuestionContent");
		questionContent.textContent = document.getElementById("addQuestionTextArea").value;
		var questionOptions = document.createElement("div");
		questionOptions.setAttribute("class", "QuestionOptions");
		var ulOptions = document.createElement("ul");
		var liEdit = document.createElement("li");
		var editButton = document.createElement("button");
		editButton.textContent = "Edit";
		editButton.setAttribute("type", "button");
		editButton.setAttribute("class", "editButton")
		editButton.setAttribute("onclick", "editQuestion(this)");
		var liDelete = document.createElement("li");
		var deleteButton = document.createElement("button");
		deleteButton.textContent = "Delete";
		deleteButton.setAttribute("type", "button");
		deleteButton.setAttribute("onclick", "deleteQuestion(this)");
		deleteButton.setAttribute("class", "deleteButton")
		
		//createHiddenForm
		var questionIndex = document.getElementById("newQuestionIndex").textContent.substring(9, document.getElementById("newQuestionIndex").textContent.length);
		
		var hiddenFormDiv = document.createElement("div");
		hiddenFormDiv.setAttribute("style", "display:none");
		hiddenFormDiv.setAttribute("id", "hiddenForm" + questionIndex);
		
		
		var examIdInput = document.createElement("input");
		examIdInput.setAttribute("type", "hidden");
		examIdInput.setAttribute("name", "examId" + questionIndex);
		examIdInput.setAttribute("value", "-1");
		
		var questionIndexInput = document.createElement("input");
		questionIndexInput.setAttribute("type", "hidden");
		questionIndexInput.setAttribute("name", "questionIndex" + questionIndex);
		questionIndexInput.setAttribute("value", questionIndex);
		
		var questionTextInput = document.createElement("input");
		questionTextInput.setAttribute("type", "hidden");
		questionTextInput.setAttribute("name", "questionText" + questionIndex);
		questionTextInput.setAttribute("value", document.getElementById("addQuestionTextArea").value);
		
		var pointsInput = document.createElement("input");
		pointsInput.setAttribute("type", "hidden");
		pointsInput.setAttribute("name", "points" + questionIndex);
		pointsInput.setAttribute("value", document.getElementById("PtsInput").value);
		
		var questionTypeInput = document.createElement("input");
		questionTypeInput.setAttribute("type", "hidden");
		questionTypeInput.setAttribute("name", "questionType" + questionIndex);
		questionTypeInput.setAttribute("value", document.getElementById("newQuestionType").value);
		
		// add hidden elements to hidden form
		hiddenFormDiv.appendChild(examIdInput);
		hiddenFormDiv.appendChild(questionIndexInput);
		hiddenFormDiv.appendChild(questionTextInput);
		hiddenFormDiv.appendChild(pointsInput);
		hiddenFormDiv.appendChild(questionTypeInput);
		
		// Hidden form If MC question
		if (multipleChoice){
			var AddChoicesDiv = document.getElementById("AddChoicesDiv");
			for (i = 1; i < AddChoicesDiv.childNodes.length; i++){
				// hidden form
				var choiceInput = document.createElement("input");
				choiceInput.setAttribute("type", "hidden");
				choiceInput.setAttribute("name", "q" +questionIndex + "Choice" + i);
				choiceInput.setAttribute("id", "q" +questionIndex + "Choice" + i);
				choiceInput.setAttribute("value", AddChoicesDiv.childNodes[i].childNodes[1].value);
				hiddenFormDiv.appendChild(choiceInput);
				
				var questionIdInput = document.createElement("input");
				questionIdInput.setAttribute("type", "hidden");
				questionIdInput.setAttribute("name", "questionId" + questionIndex + "Choice" + i);
				questionIdInput.setAttribute("id", "questionId" + questionIndex + "Choice" + i);
				questionIdInput.setAttribute("value", "-1");
				hiddenFormDiv.appendChild(questionIdInput);
			};
			var amountOfChoices = AddChoicesDiv.childNodes.length - 1
			var amountOfChoicesInput = document.createElement("input");
			amountOfChoicesInput.setAttribute("type", "hidden");
			amountOfChoicesInput.setAttribute("name", "q" +questionIndex + "AmountOfChoices");
			amountOfChoicesInput.setAttribute("id", "q" +questionIndex + "AmountOfChoices");
			amountOfChoicesInput.setAttribute("value", amountOfChoices);
			hiddenFormDiv.appendChild(amountOfChoicesInput);	
		}
			
		// format html tags
		ulHeader.appendChild(questionName);
		ulHeader.appendChild(points);
		questionHeader.appendChild(ulHeader);
		liEdit.appendChild(editButton);
		liDelete.appendChild(deleteButton);
		ulOptions.appendChild(liEdit);
		ulOptions.appendChild(liDelete);
		questionOptions.appendChild(ulOptions);

		
		question.appendChild(questionHeader);
		question.appendChild(questionContent);
		// HTML If MC question
		if (multipleChoice){
			var questionChoices = document.createElement("div");
			questionChoices.setAttribute("class", "QuestionChoices")
			var AddChoicesDiv = document.getElementById("AddChoicesDiv");
			for (i = 1; i < AddChoicesDiv.childNodes.length; i++){
				var choiceNumberText = document.createElement("label");
				choiceNumberText.setAttribute("id", "choiceNumberText"+i);
				choiceNumberText.textContent = AddChoicesDiv.childNodes[i].childNodes[0].textContent;
				var choiceContent = document.createElement("label");
				choiceContent.setAttribute("id", "choiceContent"+i);
				choiceContent.textContent = AddChoicesDiv.childNodes[i].childNodes[1].value;
				var choiceDiv = document.createElement("div");
				choiceDiv.appendChild(choiceNumberText);
				choiceDiv.appendChild(choiceContent);
				questionChoices.appendChild(choiceDiv);
			};
			question.appendChild(questionChoices);
		}
		question.appendChild(questionOptions);
		question.appendChild(hiddenFormDiv);
		

		for (var i = 0; i < childLength; i ++){
			question.removeChild(question.firstChild);
		}

		
		var questionOptionsInBody = document.getElementsByClassName("QuestionOptions")
		for(var i = 0; i < questionOptionsInBody.length; i++){
			questionOptionsInBody[i].style["display"] = "block";
		}
		
		document.getElementById("Bottom").style["display"] = "block";
	}
	
	function saveExam(){
		var timeAllowedInput = document.getElementById("timeAllowedInput");
		if (!timeAllowedInput.value){
			timeAllowedInput.value = 0;
		}
		//document.forms["AddExamForm"].submit();
		
		Form = document.getElementById("AddExamForm");

	    $.ajax({
	        url: Form.action,
	        type: 'post',
	        data: $('#AddExamForm').serialize(),
	        success: function(response){
	        	if(response.localeCompare('success') == 1){
	        		document.forms["cancelForm"].submit();
	        	} else {
	        		alert(response);
	        	}
	        }
	    });
	}
	
	function savePublishExam(){
		document.getElementById("isPublishedInput").value = "true";
		saveExam();
	}
	
	function deleteChoice(self){
		
		addChoicesDiv = self.parentNode.parentNode;
		var deletedChoiceLabel = self.parentNode.childNodes[0].textContent;
		var deletedChoiceIndex = parseInt(deletedChoiceLabel.substring(7, deletedChoiceLabel.length));
		for (var i = deletedChoiceIndex + 1; i < addChoicesDiv.childNodes.length; i++) {
			var choiceIndex = i-1;
			addChoicesDiv.childNodes[i].childNodes[0].textContent = "Choice " + choiceIndex; // label
			addChoicesDiv.childNodes[i].childNodes[1].setAttribute("name", "choice" + choiceIndex); //input
		};
		self.parentNode.remove(self);
		
		var allDeleteChoiceBtn = document.getElementsByClassName("deleteChoiceBtn");
		console.log(allDeleteChoiceBtn.length);
		if (allDeleteChoiceBtn.length === 2){
			for(var i =0; i < allDeleteChoiceBtn.length; i++)
			allDeleteChoiceBtn[i].style["display"] = "none";
		}
	}
	
	function addChoice(self){
		newChoiceIndex = self.parentNode.childNodes.length
		
		var newChoiceDiv = document.createElement("div");
		var newChoiceLabel = document.createElement("label");
		newChoiceLabel.textContent = "Choice " + newChoiceIndex;
		var newChoiceInput = document.createElement("input");
		newChoiceInput.setAttribute("type", "text");
		newChoiceInput.setAttribute("name", "choice" + newChoiceIndex);
		var deleteNewChoiceBtn = document.createElement("button");
		deleteNewChoiceBtn.setAttribute("type", "button")
		deleteNewChoiceBtn.setAttribute("onclick", "deleteChoice(this)")
		deleteNewChoiceBtn.setAttribute("class", "deleteChoiceBtn")
		deleteNewChoiceBtn.textContent = "Delete";
		
		newChoiceDiv.appendChild(newChoiceLabel);
		newChoiceDiv.appendChild(newChoiceInput);
		newChoiceDiv.appendChild(deleteNewChoiceBtn);
		
		self.parentNode.appendChild(newChoiceDiv);
		
		var allDeleteChoiceBtn = document.getElementsByClassName("deleteChoiceBtn");
		console.log(allDeleteChoiceBtn.length);
		if (allDeleteChoiceBtn.length > 2){
			for(var i =0; i < allDeleteChoiceBtn.length; i++)
			allDeleteChoiceBtn[i].style["display"] = "inline";
		}
	}
	
	$('body').on('change', 'select', function() {
	  if (this.value === "MC"){
		  // add choices form
		  
		  
		  // Choice 1
		  var choice1Div = document.createElement("div");
		  var choice1Label = document.createElement("label");
		  choice1Label.textContent = "Choice 1";
		  var choice1Input = document.createElement("input");
		  choice1Input.setAttribute("type", "text");
		  choice1Input.setAttribute("name", "choice1");
		  var deleteChoice1Btn = document.createElement("button");
		  deleteChoice1Btn.setAttribute("type", "button")
		  deleteChoice1Btn.setAttribute("onclick", "deleteChoice(this)")
		  deleteChoice1Btn.setAttribute("class", "deleteChoiceBtn")
		  deleteChoice1Btn.textContent = "Delete";
		  
		  choice1Div.appendChild(choice1Label);
		  choice1Div.appendChild(choice1Input);
		  choice1Div.appendChild(deleteChoice1Btn);
		  
		  // Choice 2
		  var choice2Div = document.createElement("div");
		  var choice2Label = document.createElement("label");
		  choice2Label.textContent = "Choice 2";
		  var choice2Input = document.createElement("input");
		  choice2Input.setAttribute("type", "text");
		  choice2Input.setAttribute("name", "choice2");
		  var deleteChoice2Btn = document.createElement("button");
		  deleteChoice2Btn.setAttribute("type", "button")
		  deleteChoice2Btn.setAttribute("onclick", "deleteChoice(this)");
		  deleteChoice2Btn.setAttribute("class", "deleteChoiceBtn")
		  deleteChoice2Btn.textContent = "Delete";
		  
		  choice2Div.appendChild(choice2Label);
		  choice2Div.appendChild(choice2Input);
		  choice2Div.appendChild(deleteChoice2Btn);
		  
		  
		  // Choices container
		  var addChoicesDiv = document.createElement("div");
		  addChoicesDiv.setAttribute("id", "AddChoicesDiv");
		  
		  var addChoiceBtn = document.createElement("button");
		  addChoiceBtn.setAttribute("type", "button");
		  addChoiceBtn.textContent = "Add New Choice"
		  addChoiceBtn.setAttribute("onclick", "addChoice(this)");
		  
		  addChoicesDiv.appendChild(addChoiceBtn);
		  addChoicesDiv.appendChild(choice1Div);
		  addChoicesDiv.appendChild(choice2Div);
		  document.getElementById("AddQuestionContent").appendChild(addChoicesDiv);
		  
		  var allDeleteChoiceBtn = document.getElementsByClassName("deleteChoiceBtn");
			console.log(allDeleteChoiceBtn.length);
			if (allDeleteChoiceBtn.length === 2){
				for(var i =0; i < allDeleteChoiceBtn.length; i++)
				allDeleteChoiceBtn[i].style["display"] = "none";
			}
	  } else {
		  // remove choices form
		  document.getElementById("AddQuestionContent").removeChild(document.getElementById("AddChoicesDiv"))
	  }
	});
	
</script>
</html>