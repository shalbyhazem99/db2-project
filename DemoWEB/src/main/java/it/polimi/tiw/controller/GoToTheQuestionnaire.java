package it.polimi.tiw.controller;

import java.io.IOException;
import java.util.Comparator;
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
import it.polimi.db2.exceptions.NonActiveUserException;
import it.polimi.db2.exceptions.NotValidEntryException;
import it.polimi.db2.exceptions.OffensiveLanguageException;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.QuestionnaireService;
import it.polimi.db2.services.RecordService;
import it.polimi.db2.services.UserService;

@WebServlet("/Questionnaire")
public class GoToTheQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;

	@EJB(name = "it.polimi.db2.services/RecordService")
	private RecordService rService;
	@EJB(name = "it.polimi.db2.services/UserService")
	private UserService uService;

	String firstPage = "/WEB-INF/user/Questionnaire.html";
	String endPage = "/WEB-INF/user/QuestionnaireEndPage.html";

	public GoToTheQuestionnaire() {
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
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}

		User user = (User) session.getAttribute("user");
		
		if(!user.isActive()) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		
		List<Question> questions = (List<Question>) session.getAttribute("questions");
		String path = firstPage;
		String message = "";

		String action = request.getParameter("action");
		Questionnaire questionnaire = qService.getQuestionnaireOfTheDay();
		// TODO: check if the questionnaire if null for some strange reason

		if (!rService.isThereAnyRecord(user, questionnaire)) {

			if (action == null) {
				try {
					questions = questionnaire.getQuestions();
					request.getSession().setAttribute("questions", questions);
				} catch (Exception e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get data");
					return;
				}
			} else if (action.compareToIgnoreCase("submit") == 0) {
				String[] answers = request.getParameterValues("answer");
				int age = -1;
				if (!request.getParameter("age").isBlank()) {
					age = Integer.parseInt(request.getParameter("age"));
				}
				UserSex sex = null;
				if (request.getParameter("sex") != null)
					sex = UserSex.getUserSexFromInt(Integer.parseInt(request.getParameter("sex")));
				UserExpertiseLevel expertise_level = null;
				if (request.getParameter("expertise_level") != null)
					expertise_level = UserExpertiseLevel
							.getUserExpertiseLevelFromInt(Integer.parseInt(request.getParameter("expertise_level")));

				try {
					rService.addNewRecord(answers, age, sex, expertise_level, user, questionnaire,questions);
					message = "You submit the Questionnaire correctly";
				} catch (NotValidEntryException | NonActiveUserException e) {
					message = "Operation aborted because: " + e.getMessage();
				}
				catch(OffensiveLanguageException e) {
					uService.banUser(user);
					message = "Operation aborted because: " + e.getMessage();
				}

				path = endPage;
			} else if (action.compareToIgnoreCase("cancel") == 0) {
				rService.cancelQuestionnaire(user, questionnaire);
				message = "You cancel the Questionnaire correctly";
				path = endPage;
			}
		} else {
			message = "You Already submit/cancel the questionnaire!!";
			path = endPage;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questions", questions);
		ctx.setVariable("message", message);
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}
}