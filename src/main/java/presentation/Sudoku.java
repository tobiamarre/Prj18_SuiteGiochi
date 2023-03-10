package presentation;

import java.io.IOException;

import controller.SudokuCtrl2;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sudoku.controller.SudokuGenerator;
import sudoku.controller.SudokuGenerator2;

@WebServlet("/sudoku")
public class Sudoku extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("title", "Sudoku");
		req.getRequestDispatcher("header.jsp").include(req, resp);
		req.getRequestDispatcher("menu.jsp").include(req, resp);
		
		req.setAttribute("puzzle", new SudokuGenerator2().getPuzzle());
		req.getRequestDispatcher("viewSudoku.jsp").include(req, resp);
		
		req.getRequestDispatcher("footer.jsp").include(req, resp);
	}
}
