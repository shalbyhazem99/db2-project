package it.polimi.tiw.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.entities.Record;
import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.QuestionnaireService;
import it.polimi.db2.services.UserService;

@WebServlet("/InspectQuestionnaire")
public class InspectQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;

	public InspectQuestionnaire() {
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
		// If the user is not logged in (not present in session) redirect to the login
				String loginpath = getServletContext().getContextPath() + "/index.html";
				HttpSession session = request.getSession();
				if (session.isNew() || session.getAttribute("user") == null|| !((User)session.getAttribute("user")).isAdmin()) {
					response.sendRedirect(loginpath);
					return;
				}
		// obtain and escape params
		String dataString = StringEscapeUtils.escapeJava(request.getParameter("date"));
		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(dataString);
		} catch (Exception e) {
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dataString);
			} catch (ParseException e1) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Values incorrect");
				return;
			}
		}
		List<Record> records = null;
		try {
			records = qService.getQuestionnaireByDate(date).getRecords();
		} catch (Exception e) {

		}
		String path = "/WEB-INF/AdminQuestionnaireDetails.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("records", records);
		ctx.setVariable("date", dataString);
		templateEngine.process(path, ctx, response.getWriter());

	}

	public void destroy() {
	}
}