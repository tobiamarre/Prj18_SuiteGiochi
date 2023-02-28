/**
 * 
 */

var celle = [...document.querySelectorAll(".wrapper>.cell>div")];

var cellaSelected = null;

function setCellaSelected(c) {
	celle.forEach(oc => oc.style.backGroundColor = '');
	cellaSelected=c;
	
	
	celle.filter(oc=>oc.textContent==cellaSelected.textContent).forEach(oc=>oc.style.backGroundColor = 'rgb(0,200,0)');
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
 
celle.forEach((cell, i) => {
	
	cell.onclick = function(){
		setCellaSelected(this); 
		
	} 
 });
 
 
document.addEventListener('keydown', (event) => {
    if (cellaSelected != null && event.key.match(/[1-9]$/)) {
		cellaSelected.textContent = event.key;
	}
});
