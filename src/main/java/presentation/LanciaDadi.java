package presentation;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dadi")
public class LanciaDadi extends HttpServlet {

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("header.jsp").include(req, resp);
		req.getRequestDispatcher("menu.jsp").include(req, resp);
		
		req.getRequestDispatcher("viewDadi.jsp").include(req, resp);
		
		
		
		
		
		
		
		
		req.getRequestDispatcher("footer.jsp").include(req, resp);
	}
}
