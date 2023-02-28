package presentation;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/giochi")
public class GiochiMVC extends HttpServlet {
	private int counterVisite = 0;
	
	public GiochiMVC() {
		System.out.println("creato oggetto GiochiMVC");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// header
		req.setAttribute("titolo", "Giochi");
		req.getRequestDispatcher("header.jsp").include(req, resp);

		// menu
		req.getRequestDispatcher("menu.jsp").include(req, resp);
		
		// content
		if (req.getParameter("gioco") != null) {
			switch (req.getParameter("gioco")) {
			case "1":
				req.getRequestDispatcher("gioco1.jsp").include(req, resp);
				break;
				
			case "3":
				req.getRequestDispatcher("gioco3.jsp").include(req, resp);
				break;
				
			default:
				resp.getWriter().append("<h2>Gioco non disponibile</h2>");
				break;
			}
			req.getRequestDispatcher("content.jsp").include(req, resp);
		}
		
//		footer
		req.getRequestDispatcher("footer.jsp").include(req, resp);
	}
	
	
}
