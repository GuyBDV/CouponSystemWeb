package facadeservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import couponexception.Message;
import couponsystem.CouponSystem;
import couponsystem.CouponSystemException;
import facade.ClientType;
import facade.CompanyFacade;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.CouponType;

@Path("/company")
public class CompanyService {

	public CompanyService() {
		
	}
	
	private CompanyFacade getFacadeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		CompanyFacade companyfacade = (CompanyFacade) session.getAttribute("company");
		return companyfacade;
	}
	
	@Path("/login/{username}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message login(@PathParam("username") String username, 
						 @PathParam("password") String password, 
						 @Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = (CompanyFacade) CouponSystem.getInstance().login(username, password, ClientType.COMPANY);
		if (username != null && !username.trim().isEmpty()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("company", companyfacade);
			return new Message("Session ID: " + session.getId());
		}
		return new Message("Login Failed");
	}
	
	@Path("/logout")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message logout(@Context HttpServletRequest request) throws CouponSystemException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			return new Message("Session Invalidated Successfully");
		}
		return new Message("Session Invalidation Failed");
	}
	
	@Path("/coupons")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon createCoupon(Coupon coupon, @Context HttpServletRequest request) throws CouponSystemException {
		if (coupon.getId() != null) {
			throw new CouponSystemException("Coupon must not have an ID");
		}
		CompanyFacade companyfacade = getFacadeFromSession(request);
		return companyfacade.createCoupon(coupon);
	}
	
	@Path("/coupons/{id}")
	@DELETE
	public void removeCoupon(@PathParam("id") Long id, @Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		Coupon coupon = companyfacade.getCoupon(id);
		companyfacade.removeCoupon(coupon);
	}
	
	@Path("/coupons/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCoupon(@PathParam("id") Long id, Coupon coupon, @Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		if (!id.equals(coupon.getId())) {
			throw new CouponSystemException("ID's do not match");
		}
		companyfacade.updateCoupon(coupon);
	}
	
	@Path("/coupons")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		return companyfacade.getCompany();
	}
	
	@Path("/coupons/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getCoupon(@PathParam("id") Long id, @Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		return companyfacade.getCoupon(id);
	}
	
	@Path("/allcoupons")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllCoupons(@Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		return companyfacade.getAllCoupon().toArray(new Coupon[0]);
	}
	
	@Path("/getcouponbytype/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getCouponByType(@PathParam("type") CouponType type, @Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		return companyfacade.getCompanyCouponByType(type).toArray(new Coupon[0]);
	}
	
	@Path("/getcouponbyprice/{price}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getCouponByPrice(@PathParam("price") double price, @Context HttpServletRequest request) throws CouponSystemException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		return companyfacade.getCouponByPrice(price).toArray(new Coupon[0]);
	}
	
	@Path("/getcouponbydate/{endDate}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getCouponByDate(@PathParam("endDate") String date, @Context HttpServletRequest request) throws CouponSystemException, ParseException {
		CompanyFacade companyfacade = getFacadeFromSession(request);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = format.parse(date);
		return companyfacade.getCouponByDate(newDate).toArray(new Coupon[0]);
	}
	
}
