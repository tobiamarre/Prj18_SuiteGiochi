<%@page import="sudoku.model.SudokuPuzzle"%>
<style><%@include file="/css/styleSudoku.css"%></style>

<div class="wrapper">
        <header>
            <h1><%= request.getAttribute("title") %></h1>
        </header>
        <main>
            <div class="wrapper">
                <h2>Big version</h2>
                <div class="cell">

			<% for (int i : ((SudokuPuzzle)request.getAttribute("puzzle")).getProblem().getMatrice()) { %>
					<%= i == 0 ? "<div>" : "<div class=\"cellaFissa\">" + Integer.toString(i) %></div>
			<% } %>
                    

                </div>

                
            </div>
        </main>
        <footer>
            <p>sudoku grid layout</p>
        </footer>

        <div class="note">Your browser doesn't support CSS Grid. You'll need
            <a href="http://gridbyexample.com/browsers/">a browser that does</a> to use this app.
        </div>
    </div>



<script type='text/javascript'>
var solution = [<% for (int digit : ((SudokuPuzzle)request.getAttribute("puzzle")).getSolution().getMatrice()) { %>
   
  				 <%="'" + digit + "',"%> 
   
				<%}%>
];
</script>


<script src="js/viewSudoku.js"></script>

