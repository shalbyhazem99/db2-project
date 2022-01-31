package it.polimi.tiw.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.QuestionnaireService;
import it.polimi.db2.services.UserService;

@WebServlet("/AddQuestionnaire")
public class AddQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.services/ProductService")
	private ProductService pService;

	public AddQuestionnaire() {
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
		if (session.isNew() || session.getAttribute("user") == null
				|| !((User) session.getAttribute("user")).isAdmin()) {
			response.sendRedirect(loginpath);
			return;
		}
		// obtain and escape params
		Date date = null;
		int productId = -1;
		String[] questions = null;
		String tmp;
		try {
			String dataString = StringEscapeUtils.escapeJava(request.getParameter("date"));
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
			productId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("product")));
			questions = request.getParameterValues("question");
		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			
		}

		if (questions != null && productId != -1 && date != null) {
			Product product = pService.getProductById(productId);
			if (product != null) {
				if (qService.getQuestionnaireOfTheDay() == null) {
					qService.addQuestionnaire(date, product, questions);
				}else {
					String path = getServletContext().getContextPath() + "/HomeAdmin";
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('Invalid Questionnaire');");
					out.println("window.location.replace('" + path + "');");
					out.println("</script>");
					return;
				}
			}
		}
		String path = getServletContext().getContextPath() + "/HomeAdmin";
		response.sendRedirect(path);
	}

	public void destroy() {
	}
}