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

                    <div class="cell-grid" id="backgrounds"></div>
                    <div class="cell-grid" id="annotations"></div>
                    <div class="cell-grid" id="numbers"></div>
                    <div class="cell-grid" id="buttons"></div>

                    <div class="cell-borders-vert"></div>
                    <div class="cell-borders-vert-0"></div>
                    <div class="cell-borders-vert-1"></div>
                    <div class="cell-borders-vert-2"></div>
                    
                    <div class="cell-borders-hori"></div>
                    <div class="cell-borders-hori-0"></div>
                    <div class="cell-borders-hori-1"></div>
                    <div class="cell-borders-hori-2"></div>
                    
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
var puzzle = <%=((SudokuPuzzle)request.getAttribute("puzzle")).getJsonString()%>;
</script>
<script src="js/viewSudoku.js"></script>

