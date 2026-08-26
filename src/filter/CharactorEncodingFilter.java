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
import javax.servlet.http.HttpServletResponseWrapper;

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

		chain.doFilter(new MyRequest(request), new CharResponseWrapper(response));
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	@Override
	public void destroy() {}
}

/**
 * 响应包装器：仅对文本类响应补充 charset=UTF-8，
 * 不影响 CSS/JS 等静态资源的 MIME 类型推断。
 */
class CharResponseWrapper extends HttpServletResponseWrapper {

	public CharResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void setContentType(String type) {
		if (type == null) {
			super.setContentType(null);
			return;
		}
		String lower = type.toLowerCase();
		if (lower.contains("charset")) {
			super.setContentType(type);
			return;
		}
		if (lower.startsWith("text/html") || lower.startsWith("text/plain")
				|| lower.startsWith("application/json")
				|| lower.startsWith("text/javascript")
				|| lower.startsWith("application/javascript")
				|| lower.startsWith("application/xml")) {
			super.setContentType(type + ";charset=UTF-8");
			return;
		}
		super.setContentType(type);
	}
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
		String value = super.getParameter(name);
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

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if(values == null) return null;
		if(!this.request.getMethod().equalsIgnoreCase("get")) return values;
		try {
			String[] converted = new String[values.length];
			for(int i = 0; i < values.length; i++) {
				converted[i] = new String(values[i].getBytes("ISO8859-1"), "UTF-8");
			}
			return converted;
		} catch (UnsupportedEncodingException e) {
			log.error("Encoding not supported", e);
			throw new RuntimeException(e);
		}
	}

}