/*
Copy and paste the code in pgAdmin's Query tool
*/


DROP Table IF EXISTS locks;
DROP TABLE IF EXISTS exams CASCADE;
DROP TABLE IF EXISTS instructor_subject_mapping CASCADE;
DROP TABLE IF EXISTS student_subject_mapping CASCADE;
DROP TABLE IF EXISTS subjects CASCADE;
DROP TABLE IF EXISTS instructors CASCADE;
DROP TABLE IF EXISTS choices CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS admins CASCADE;
DROP TABLE IF EXISTS student_exam_answers CASCADE;
DROP TABLE IF EXISTS student_question_answers CASCADE;
DROP TYPE IF EXISTS question_types CASCADE;


CREATE TABLE locks (
	lockable INT UNIQUE,
	owner VARCHAR(20) NOT NULL
	);

CREATE TABLE instructors (
	username VARCHAR(20) NOT NULL,
	email VARCHAR(100),
	password VARCHAR(20) NOT NULL,
	PRIMARY KEY (username)
	);
	

CREATE TABLE subjects (
	subject_code VARCHAR(20) NOT NULL,
	name VARCHAR(50) NOT NULL,
	/*coordinator VARCHAR(50) NOT NULL,*/
	PRIMARY KEY (subject_code)
	/*CONSTRAINT fk_coordinator
		FOREIGN KEY(coordinator)
			REFERENCES instructors(username)*/
	);

CREATE TABLE exams(
	exam_id INT GENERATED ALWAYS AS IDENTITY,
	subject_code VARCHAR (20) NOT NULL,
	exam_name VARCHAR(255) NOT NULL,
	exam_description VARCHAR(8000) DEFAULT 'This Exam has no description',
	date_created TIMESTAMP NOT NULL DEFAULT NOW(),
	date_published TIMESTAMP,
	date_start TIMESTAMP,
	date_closed TIMESTAMP,
	time_allowed INT DEFAULT 0,
    is_closed BOOLEAN DEFAULT FALSE,
    is_published BOOLEAN DEFAULT FALSE,
	total_marks INT DEFAULT 0,
	ver INT DEFAULT 1,
	modified_by VARCHAR (20) NOT NULL,
	modified_time TIMESTAMP NOT NULL DEFAULT NOW(),
	PRIMARY KEY(exam_id),
	CONSTRAINT fk_subject
		FOREIGN KEY(subject_code)
			REFERENCES subjects(subject_code)
);	

CREATE TYPE question_types AS ENUM('MC', 'Short');
CREATE TABLE questions(
	question_id INT GENERATED ALWAYS AS IDENTITY,
	exam_id INT NOT NULL,
	question_index INT NOT NULL,
	question_text VARCHAR(8000),
	marks INT,
	question_type question_types,
	PRIMARY KEY(question_id),
	CONSTRAINT fk_exam
		FOREIGN KEY(exam_id)
			REFERENCES exams(exam_id)
			ON DELETE CASCADE
);	

CREATE TABLE choices(
	choice_id INT GENERATED ALWAYS AS IDENTITY,
	question_id INT,
	choice_index INT NOT NULL,
	text VARCHAR(8000),
	PRIMARY KEY(choice_id),
	CONSTRAINT fk_question
		FOREIGN KEY(question_id)
			REFERENCES questions(question_id)
			ON DELETE CASCADE
);	

CREATE TABLE students (
	username VARCHAR(20),
	email VARCHAR(100),
	password VARCHAR(20),
	PRIMARY KEY (username)
);

CREATE TABLE instructor_subject_mapping (
	id INT GENERATED ALWAYS AS IDENTITY,
	instructor_username VARCHAR(20),
	subject_code VARCHAR(20),
	CONSTRAINT fk_subject
		FOREIGN KEY(subject_code)
			REFERENCES subjects(subject_code),
	CONSTRAINT fk_instructor
		FOREIGN KEY(instructor_username)
			REFERENCES instructors(username)
);

CREATE TABLE student_subject_mapping (
	id INT GENERATED ALWAYS AS IDENTITY,
	student_username VARCHAR(20),
	subject_code VARCHAR(20),
	CONSTRAINT fk_subject
		FOREIGN KEY(subject_code)
			REFERENCES subjects(subject_code),
	CONSTRAINT fk_student
		FOREIGN KEY(student_username)
			REFERENCES students(username)
);

CREATE TABLE student_exam_answers (
	student_exam_answers_id INT GENERATED ALWAYS AS IDENTITY,
	exam_id INT,
	student_username VARCHAR(20),
	is_marked BOOLEAN,
	total_marks INT DEFAULT 0,
	date_created TIMESTAMP NOT NULL DEFAULT NOW(),
	marking_comment VARCHAR(8000) DEFAULT '',
	PRIMARY KEY (student_exam_answers_id),
	CONSTRAINT fk_exam
		FOREIGN KEY(exam_id)
			REFERENCES exams(exam_id),
	CONSTRAINT fk_student
		FOREIGN KEY(student_username)
			REFERENCES students(username)
	);

CREATE TABLE student_question_answers (
	student_question_answers_id INT GENERATED ALWAYS AS IDENTITY,
	student_exam_answers_id INT,
	question_id INT,
	student_answer VARCHAR(8000),
	marks INT DEFAULT 0,
	PRIMARY KEY (student_question_answers_id),
	CONSTRAINT fk_exam_answer
		FOREIGN KEY(student_exam_answers_id)
			REFERENCES student_exam_answers(student_exam_answers_id),
	CONSTRAINT fk_question
		FOREIGN KEY(question_id)
			REFERENCES questions(question_id)
	);
	
CREATE TABLE admins (
	username VARCHAR(20),
	email VARCHAR(100),
	password VARCHAR(20),
	PRIMARY KEY (username)
	);

CREATE OR REPLACE FUNCTION question_after_insert()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN 
UPDATE exams
SET total_marks = total_marks + new.marks
WHERE exam_id = new.exam_id;
RETURN new;
END;
$$;

CREATE TRIGGER check_total_marks_question_after_insert
	AFTER INSERT
	ON questions
	FOR EACH ROW
	EXECUTE PROCEDURE question_after_insert();
	
CREATE OR REPLACE FUNCTION question_after_delete()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN 
UPDATE exams
SET total_marks = total_marks - old.marks
WHERE exam_id = old.exam_id;
	RETURN old;
END;
$$;

CREATE TRIGGER check_total_marks_question_after_delete
AFTER DELETE
ON questions
FOR EACH ROW
EXECUTE PROCEDURE question_after_delete();

CREATE OR REPLACE FUNCTION answer_after_insert()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN 
UPDATE student_exam_answers
SET total_marks = total_marks + new.marks
WHERE student_exam_answers_id = new.student_exam_answers_id;
RETURN new;
END;
$$;

CREATE TRIGGER check_total_marks_answer_after_insert
 	AFTER INSERT
 	ON student_question_answers
 	FOR EACH ROW
 	EXECUTE PROCEDURE answer_after_insert();

CREATE TRIGGER check_total_marks_answer_after_update
	AFTER UPDATE
	ON student_question_answers
	FOR EACH ROW
	EXECUTE PROCEDURE answer_after_insert();
CREATE OR REPLACE FUNCTION question_after_update()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN 
UPDATE exams
SET total_marks = total_marks - old.marks + new.marks
WHERE exam_id = old.exam_id;

IF (old.question_type = 'MC') AND (new.question_type = 'Short') 
THEN
	DELETE FROM choices
	WHERE question_id = old.question_id;
END IF;
RETURN old;
END;
$$;

CREATE TRIGGER check_total_marks_question_after_update
AFTER UPDATE
ON questions
FOR EACH ROW
EXECUTE PROCEDURE question_after_update();
	
INSERT INTO admins (username, email, password)
VALUES ('admin', 'admin@unimelb.edu.au', 'admin');

INSERT INTO students (username, email, password)
VALUES ('student1', 'student1@unimelb.edu.au', 'student1'),
	   ('student2', 'student2@unimelb.edu.au', 'student2');


INSERT INTO instructors (username, email, password)
VALUES 
	('instructor', 'instructor@unimelb.edu.au', 'instructor'),
	('instructor2', 'instructor2@unimelb.edu.au', 'instructor2'),
	('instructor3', 'instructor3@unimelb.edu.au', 'instructor3');;
	
INSERT INTO subjects (subject_code, name)
VALUES
	('SWEN00001', 'Database Design'),
	('SWEN00002', 'Software Testing'),
	('SWEN00003', 'Cloud Computing');

INSERT INTO instructor_subject_mapping(instructor_username, subject_code)
VALUES
	('instructor', 'SWEN00001'),
	('instructor', 'SWEN00002'),
	('instructor2', 'SWEN00003'),
	('instructor2', 'SWEN00001'),
	('instructor', 'SWEN00003'),
	('instructor3', 'SWEN00001');
	

INSERT INTO exams (subject_code, exam_name, is_published, modified_by)
VALUES
	('SWEN00001', 'Exam 1', true, 'instructor'),
	('SWEN00002', 'Exam 2', false, 'instructor');

	
INSERT INTO student_subject_mapping(student_username, subject_code)
VALUES
	('student1', 'SWEN00001'),
	('student1', 'SWEN00002'),
	('student2', 'SWEN00001'),
	('student2', 'SWEN00003');

	
INSERT INTO questions (exam_id, question_index, question_text, marks, question_type)
VALUES
('1', '1', 'What is a database?', '2', 'Short'),
('1', '2', 'Choose one of the following choices', '1', 'MC'),
('1', '3', 'Question for exam 1', '1', 'Short'),
('1', '4', 'This is a short question that is worth 10 points', '10', 'Short'),
('1', '5', 'This is a multiple choice question with 3 choices and is worth 10 points', '10', 'MC'),
('2', '1', 'Question for exam 2', '2', 'Short');


INSERT INTO choices (question_id, choice_index, text)
VALUES
('2', '1', 'choice a'),
('2', '2', 'choice b'),
('2', '3', 'choice c'),
('2', '4', 'choice d'),
('5', '1', 'choice 1 for question 5'),
('5', '2', 'choice 2 for question 5'),
('5', '3', 'choice 3 for question 5');