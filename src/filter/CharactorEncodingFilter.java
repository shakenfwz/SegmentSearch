package filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CharactorEncodingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// Tomcat 9 的 DefaultServlet 对 HTML 默认使用 ISO-8859-1，
		// 需显式设置 Content-Type 为 UTF-8，否则中文显示为乱码
		String path = request.getRequestURI();
		if (path != null && (path.endsWith(".html") || path.endsWith(".htm") || path.endsWith(".jsp"))) {
			response.setContentType("text/html;charset=UTF-8");
		}

		chain.doFilter(new MyRequest(request), response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	@Override
	public void destroy() {}
}

class MyRequest extends HttpServletRequestWrapper{

	private static final Logger log = LogManager.getLogger(MyRequest.class);
	private HttpServletRequest request;
	public MyRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	@Override
	public String getParameter(String name) {
		String value = request.getParameter(name);
		if(value == null) return null;
		if(!this.request.getMethod().equalsIgnoreCase("get")) return value;
		try {
			value = new String(value.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Encoding not supported", e);
			throw new RuntimeException(e);
		}
		return value;
	}
	
}