package it.polimi.tiw.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
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
import it.polimi.db2.entities.Question;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.entities.User;
import it.polimi.db2.entities.UserExpertiseLevel;
import it.polimi.db2.entities.UserSex;
import it.polimi.db2.exceptions.DeletionAfterCurrentDateException;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.QuestionnaireService;
import it.polimi.db2.services.RecordService;

@WebServlet("/DeleteQuestionnaire")
public class GoToTheQuestionnaireDeletion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.services/ProductService")
	private ProductService pService;

	public GoToTheQuestionnaireDeletion() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null|| !((User)session.getAttribute("user")).isAdmin()) {
			response.sendRedirect(loginpath);
			return;
		}

		String dataString = StringEscapeUtils.escapeJava(request.getParameter("date"));
		Date date = null;
		if (dataString != null) {
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
			try {
				qService.eraseQuestionnaire(date);
			} catch (DeletionAfterCurrentDateException e) {
				String path = getServletContext().getContextPath() + "/HomeAdmin";
				PrintWriter out = response.getWriter();
		        out.println("<script>");
		        out.println("alert('"+e.getMessage()+"');");
		        out.println("window.location.replace('"+path+"');");
		        out.println("</script>");
		        return;
			}
			String path = getServletContext().getContextPath() + "/HomeAdmin";
			PrintWriter out = response.getWriter();
	        out.println("<script>");
	        out.println("alert('Deletion executed correctly!!');");
	        out.println("window.location.replace('"+path+"');");
	        out.println("</script>");
		} else {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process("/WEB-INF/AdminDeletionPage.html", ctx, response.getWriter());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}
}