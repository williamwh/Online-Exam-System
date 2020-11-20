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

import domain.Exam;
import domain.Subject;
import mappers.ExamMapper;
import mappers.SubjectMapper;


@WebFilter(urlPatterns = {"/app/Students/Exams.jsp", "/app/Students/AnswerExam.jsp"})
public class StudentFunctionFilter implements Filter {

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
        
        if (requestPath.contains("/app/Students/AnswerExam.jsp")) {
        	int examId = Integer.parseInt(request.getParameter("examID")); 
        	Exam exam = ExamMapper.getExam(examId);
        	SubjectMapper subjectMapper = new SubjectMapper();
            boolean var = false;
        	for (Subject subject : subjectMapper.getStudentSubjects((String) session.getAttribute("username"))){
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
           // System.out.println("----" + request.getParameter("subjectCode"));
            
        	SubjectMapper subjectMapper = new SubjectMapper();
            boolean var = false;
        	for (Subject subject : subjectMapper.getStudentSubjects((String) session.getAttribute("username"))){
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
