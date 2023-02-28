package presentation;

import java.io.IOException;

import controller.SudokuCtrl2;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tris")
public class Tris extends HttpServlet {

	SudokuCtrl2 ctrl = new SudokuCtrl2();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("header.jsp").include(req, resp);
		req.getRequestDispatcher("menu.jsp").include(req, resp);
		
		req.setAttribute("title", "Tris");
		req.setAttribute("listaNumeri", ctrl.stampaSequenza());
		
		req.getRequestDispatcher("viewTris.jsp").include(req, resp);
		
		
		
		
		
		
		
		
		req.getRequestDispatcher("footer.jsp").include(req, resp);
	}
}
