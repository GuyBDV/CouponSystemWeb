package facadeservice;

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
import facade.AdminFacade;
import facade.ClientType;
import javabeans.Company;
import javabeans.Customer;

@Path("/admin")
public class AdminService {
	
	
	public AdminService() {
	}
	
	private AdminFacade getFacadeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		AdminFacade adminfacade = (AdminFacade) session.getAttribute("admin");
		return adminfacade;
	}

	@Path("/login/{username}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message login(@PathParam("username") String username, 
						 @PathParam("password") String password, 
						 @Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = (AdminFacade) CouponSystem.getInstance().login(username, password, ClientType.ADMIN);
		if (username != null && !username.trim().isEmpty()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("admin", adminfacade);
			return new Message("Session ID: " + session.getId());
		}
		return new Message ("Login Failed");
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
	
	@Path("/companies")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Company createCompany(Company company, @Context HttpServletRequest request) throws CouponSystemException {
		if (company.getId() != null) {
			throw new CouponSystemException("Company must not have an ID");
		}
		AdminFacade adminfacade = getFacadeFromSession(request);
		return adminfacade.createCompany(company);
	}
	
	@Path("/companies/{id}")
	@DELETE
	public void removeCompany(@PathParam("id") Long id, @Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		Company company = adminfacade.getCompany(id);
		adminfacade.removeCompany(company);
	}
	
	@Path("/companies/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCompany(@PathParam("id") Long id, Company company, @Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		if (!id.equals(company.getId())) {
			throw new CouponSystemException("ID's do not match");
		}
		adminfacade.updateCompany(company);
	}
	
	@Path("/companies/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@PathParam("id") Long id, @Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		return adminfacade.getCompany(id);
	}
	
	@Path("/companies")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company[] getAllCompanies (@Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		return adminfacade.getAllCompanies().toArray(new Company[0]);
	}
	
	@Path("/customers")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer createCustomer(Customer customer, @Context HttpServletRequest request) throws CouponSystemException {
		if (customer.getId() != null) {
			throw new CouponSystemException("Customer must not have an ID");
		}
		AdminFacade adminfacade = getFacadeFromSession(request);
		return adminfacade.createCustomer(customer);
	}
	
	@Path("customers/{id}")
	@DELETE
	public void removeCustomer(@PathParam("id") Long id, @Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		Customer customer = adminfacade.getCustomer(id);
		adminfacade.removeCustomer(customer);
	}
	
	@Path("customers/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCustomer(@PathParam("id") Long id, Customer customer, @Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		if (!id.equals(customer.getId())) {
			throw new CouponSystemException("ID's do not match");
		}
		adminfacade.updateCustomer(customer);
	}
	
	@Path("customers/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("id") Long id, @Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		return adminfacade.getCustomer(id);
	}
	
	@Path("/customers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Customer[] getAllCustomers (@Context HttpServletRequest request) throws CouponSystemException {
		AdminFacade adminfacade = getFacadeFromSession(request);
		return adminfacade.getAllCustomer().toArray(new Customer[0]);
	}
}
