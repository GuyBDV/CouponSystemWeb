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
import javax.ws.rs.core.MediaType;

@WebFilter
({"/api/customer/purchasecoupon","/api/customer/getallpurchasedcoupons",
	"/api/customer/getallpurchasedcouponsbytype/*","/api/customer/getallpurchasedcouponsbyprice/*",
	"/api/customer/getallsystemcoupons/*"})
public class CustomerFilter implements Filter {

	@Override
	public void init(FilterConfig fConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fChain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute("customer") != null) {
			fChain.doFilter(request, response);
		} else {
			res.setStatus(500);
			res.getWriter().println("{\"message\":\"You Must Login to Continue\"}");
			res.setContentType(MediaType.APPLICATION_JSON);
		}

	}

	@Override
	public void destroy() {

	}

}