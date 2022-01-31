package it.polimi.tiw.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.services.UserService;

@WebServlet("/CheckRegister")
public class CheckRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	@EJB(name = "it.polimi.db2.services/UserService")
	private UserService usrService;

	public CheckRegister() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// obtain and escape params
		String usrn = null;
		String pwd = null;
		String email = null;
		boolean admin=false;
		String errorMsg="Incorrect value";
		String tmp;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			tmp= StringEscapeUtils.escapeJava(request.getParameter("admin"));
			admin = tmp!=null;
			if (usrn == null || pwd == null ||email == null || usrn.isEmpty() || pwd.isEmpty() || email.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			//response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			//return;
		}
		User user=null;
		try {
			// query db to register user
			user = usrService.registerUser(usrn,pwd,email,true,admin);
		} catch (CredentialsException | NonUniqueResultException e) {
			errorMsg= e.getMessage();
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		if (user == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", errorMsg);
			ctx.setVariable("usernameVal", usrn);
			ctx.setVariable("emailVal", email);
			ctx.setVariable("passwordVal", pwd);
			path = "/Register.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else{
			PrintWriter out = response.getWriter();
	        out.println("<script>");
	        out.println("alert('Registration ended correctly!!');");
	        out.println("window.location.replace('index.html');");
	        out.println("</script>");
		}

	}

	public void destroy() {
	}
}