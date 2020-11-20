package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.Subject;
import domain.Exam;
import domain.ExamAnswer;
import mappers.SubjectMapper;
import mappers.ExamAnswerMapper;
import mappers.ExamMapper;

/**
 * Servlet Filter implementation class InstructorFilter
 */
@WebFilter(urlPatterns = {"/app/Instructors/Exams.jsp", "/app/Instructors/CreateExam.jsp", 
		"/app/Instructors/tableMarkingView.jsp", "/app/Instructors/EditExam.jsp", 
		"/app/Instructors/detailedMarkingView.jsp"})
public class InstructorFunctionFilter implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        
        String requestPath = request.getRequestURI();
        System.out.println(requestPath);
        
        if (requestPath.contains("/app/Instructors/EditExam.jsp")) {
        	int examId = Integer.parseInt(request.getParameter("examID")); 
        	Exam exam = ExamMapper.getExam(examId);
        	SubjectMapper subjectMapper = new SubjectMapper();
            boolean var = false;
        	for (Subject subject : subjectMapper.getInstructorSubjects((String) session.getAttribute("username"))){
        		if (subject.getSubjectCode().equals(exam.getSubjectCode())) {
        			var = true;
        		}
        	}
        	//System.out.println("----" + var);
        	if (var == true) {
        		chain.doFilter(req, res);
        	}else {
        		response.sendRedirect(request.getContextPath() + "/error.jsp");
            } 
        	
        }else if (requestPath.contains("/app/Instructors/detailedMarkingView.jsp")) {
        	int examAnswerID = Integer.parseInt(request.getParameter("examAnswerID"));
    		ExamAnswer examAnswer = ExamAnswerMapper.getExamAnswer(examAnswerID);
    		int examID = examAnswer.getExamID();
    		Exam exam = ExamMapper.getExam(examID);
        	SubjectMapper subjectMapper = new SubjectMapper();
            boolean var = false;
        	for (Subject subject : subjectMapper.getInstructorSubjects((String) session.getAttribute("username"))){
        		if (subject.getSubjectCode().equals(exam.getSubjectCode())) {
        			var = true;
        		}
        	}
        	//System.out.println("----" + var);
        	if (var == true) {
        		chain.doFilter(req, res);
        	}else {
        		response.sendRedirect(request.getContextPath() + "/error.jsp");
            } 
        	
        }else {
            //System.out.println("----" + request.getParameter("subjectCode"));
            
        	SubjectMapper subjectMapper = new SubjectMapper();
            boolean var = false;
        	for (Subject subject : subjectMapper.getInstructorSubjects((String) session.getAttribute("username"))){
        		if (subject.getSubjectCode().equals(request.getParameter("subjectCode"))) {
        			var = true;
        		}
        	}
        	//System.out.println("----" + var);
        	if (var == true) {
        		chain.doFilter(req, res);
        	}else {
        		response.sendRedirect(request.getContextPath() + "/error.jsp");
            } 
        }
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
