package web.client;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.User;
import filter.CsrfFilter;
import service.impl.LoginService;

/**
 * 用户登录 Servlet（/Login）。
 * 参数：username、password、captcha（服务端验证码）。
 * 安全控制见 {@link LoginService}（验证码/锁定/密码哈希/会话固定防护）。
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String captcha = request.getParameter("captcha");

		// 无登录凭据：渲染登录页（携带 CSRF Token 供表单提交）
		if (username == null) {
			String token = (String) request.getSession().getAttribute(CsrfFilter.TOKEN_SESSION_KEY);
			if (token == null) {
				token = utils.PasswordUtils.randomToken();
				request.getSession().setAttribute(CsrfFilter.TOKEN_SESSION_KEY, token);
			}
			request.setAttribute("csrfToken", token);
			RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
			rd.forward(request, response);
			return;
		}

		LoginService service = new LoginService();
		User user = service.login(request, username, password, captcha);
		if (user == null) {
			// 登录失败：重新渲染登录页并保留错误提示与 Token
			String token = (String) request.getSession().getAttribute(CsrfFilter.TOKEN_SESSION_KEY);
			request.setAttribute("csrfToken", token);
			request.setAttribute("error", service.getLastError());
			RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
			rd.forward(request, response);
			return;
		}
		response.sendRedirect(request.getContextPath() + "/pages/addExample.html");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
