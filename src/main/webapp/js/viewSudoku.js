/**
 * 
 */
//window.addEventListener('DOMContentLoaded', () => {

var celle = [...document.querySelectorAll(".wrapper>.cell>div")];

var selectedCell = null;
var selectedDigit = null;

function setSelectedCell(c) {
	
	if (c.textContent != '') {
		setSelectedDigit(c.textContent);
	}
	
	if (selectedCell != null) {
		selectedCell.classList.remove('selectedCell');
		c.classList.add("selectedCell");
	}

	selectedCell = c;
}

function setSelectedDigit(d) {
	selectedDigit = d;
	refreshCelle();
}
function refreshCelle() {
	celle.forEach(c => {
		c.classList.remove('selectedDigit');
		c.classList.remove('cellaPiena');
		c.classList.remove('mistakenCell');		
	});
	
	if (selectedDigit != '') {
		celle.filter(c => c.textContent == selectedDigit).forEach(c => c.classList.toggle('selectedDigit'));
	}
	
	celle.filter(c => c.textContent != '').forEach(c => c.classList.add('cellaPiena'))
	
	celle.
		filter((c, i) => c.textContent != '' && c.textContent != solution[i]).
		forEach(c => c.classList.add('mistakenCell'));
	
}

function checkSolution() {
	celle.forEach((c, i) => {
		if (c.textContent != '' && c.textContent != solution[i]) {
			c.classList.add('mistakenCell');
		}
		else (c.classList.remove('mistakenCell'));
	});
}
 
function cella(i, j){
	return celle[9*i + j];
}
 
function riga(i) {
	let out = [];
	for (let j = 0; j < 9; j++) {
		out.push(celle[9*i + j]);
	}
	return out;
}
 
function colonna(j) {
	let out = [];
	for (let i = 0; j < 9; i++) {
		out.push(celle[9*i + j]);
	}
	return out;
}
 
celle.forEach(cell => {
	
	cell.onclick = function(){

		setSelectedCell(this);
		if (this.textContent != '') {
			setSelectedDigit(this.textContent);
		}
		else if (selectedDigit == this.textContent) {
			setSelectedDigit(null);
		}
		
	} 
 });
 
 
document.addEventListener('keydown', (event) => {

    if (selectedCell != null && !selectedCell.classList.contains("cellaFissa")) {
		if (event.key.match(/^[1-9]$/)) {
			selectedCell.textContent = event.key;
			refreshCelle();
		}
		else if (event.key == 'Backspace') {
			selectedCell.textContent = '';
			refreshCelle();
		}		
	} 
});



//});
