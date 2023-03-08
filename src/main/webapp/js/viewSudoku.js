
var cells;
var selectedDigit;
var annotationsOn;

var columns = [[], [], [], [], [], [], [], [], [], ];
var rows = [[], [], [], [], [], [], [], [], [], ];
var blocks = [[], [], [], [], [], [], [], [], [], ];

var nullCell = {
	range() {return []},
	toggleAnnotation() {},
	showAnnotation() {},
	removeAnnotation() {},
	setValue() {},
	clearValue() {},
	getValue() {return 0},
	select() {selectedCell.deSelect(); selectedCell = this},
	deSelect() {},
	toggleSelect() {selectedCell.deSelect(); selectedCell = this},
	isHint() {return false},
	isFilled() {return false},
	
}
var selectedCell = nullCell;

function Cell(pos) {
	this.pos = pos;
	columns[this.pos % 9].push(this);
	rows[Math.floor(this.pos / 9)].push(this);
	blocks[Math.floor(this.pos / 3)%3 + Math.floor(this.pos / 27)*3].push(this);
	this.column = columns[this.pos % 9];
	this.row = rows[Math.floor(this.pos / 9)];
	this.block = blocks[Math.floor(this.pos / 3)%3 + Math.floor(this.pos / 27)*3];
	this.range = function() {
		return this.column.concat(this.row.concat(this.block));
	}

	this.background = document.createElement('div');
	this.background.classList.add('cell-background');
	document.querySelector('#backgrounds').appendChild(this.background);


	this.annotBox = document.createElement('div');
	this.annotBox.classList.add('annotations-box');
	for (let i = 0; i < 9; i++) {
		this.annotBox.appendChild(document.createElement('div'));
	};
	[...this.annotBox.children].forEach((div, n) => {
		div.classList.add('box', 'hidden');
		div.textContent = n+1;
	});
	this.toggleAnnotation = function(n) {
		this.annotBox.children[n-1].classList.toggle('hidden');
	};
	this.showAnnotation = function(n) {
		this.annotBox.children[n-1].classList.remove('hidden');
	};
	this.removeAnnotation = function(n) {
		this.annotBox.children[n-1].classList.add('hidden');
	};
	document.querySelector('#annotations').appendChild(this.annotBox);
	
	this.numberBox = document.createElement('div');
	this.numberBox.classList.add('box', 'hidden');
	this.numberBox.textContent = 0;
	document.querySelector('#numbers').appendChild(this.numberBox);
	
	this.setValue = function(n) {
		this.numberBox.textContent = n;
		if (n != 0) {
			this.numberBox.classList.remove('hidden');
			[1,2,3,4,5,6,7,8,9].forEach(n => this.removeAnnotation(n));
			this.background.classList.add('unavailableCell');
			this.range().forEach(cell => cell.removeAnnotation(n));
		}
		else {
			this.numberBox.classList.add('hidden');
			this.background.classList.remove('unavailableCell')

		}
		let dig = selectedDigit;
		highlightDigit(0);
		highlightDigit(dig);
	};
	this.clearValue = function() {
		this.numberBox.textContent = 0;
		this.numberBox.classList.add('hidden');
		this.background.classList.add('unavailableCell')

	};
	this.getValue = function() {
		return this.numberBox.textContent;
	}
	this.initValue = function(n) {
		this.numberBox.textContent = n;
		if (n == 0) {
			this.numberBox.classList.add('hidden')
		}
		else {
			this.background.classList.add('unavailableCell');
			this.numberBox.classList.add('hintCell');
			this.numberBox.classList.remove('hidden')
			this.toggleSelect = () => nullCell.select();
			this.select = () => nullCell.select();
			this.buttonBox.onclick = () => {highlightDigit(this.getValue())};
		}

	}
	this.select = function() {
		this.annotBox.classList.add('selectedCell');
		selectedCell.deSelect();
		selectedCell = this;
	}
	this.deSelect = function() {
		this.annotBox.classList.remove('selectedCell');
	}
	this.toggleSelect = function() {
		if (selectedCell == this) {
			nullCell.select();
			return;
		}
		this.select();
	}
	this.emphasize = function() {
		this.background.classList.add('emphasizedCell');
	}
	this.deEmphasize = function() {
		this.background.classList.remove('emphasizedCell');
	}
	this.toggleEmphasize = function() {
		this.background.classList.toggle('emphasizedCell');
	}
	this.setUnavailable = function() {
		this.background.classList.add('unavailableCell');
	}
	this.setAvailable = function() {
		this.background.classList.remove('unavailableCell');
	}
	this.toggleAvailable = function() {
		this.background.classList.toggle('unavailableCell');
	}
	this.highlightWrongCell = function() {
		this.background.classList.add('wrongCell');
	}
	this.deHighlightWrongCell = function() {
		this.background.classList.remove('wrongCell');
	}
	this.toggleHighlightWrongCell = function() {
		this.background.classList.toggle('wrongCell');
	}
	this.isHint = function() {
		return this.background.classList.contains('hintCell');
	}
	this.isFilled = function() {
		return this.numberBox.textContent != 0;
	}


	this.buttonBox = document.createElement('div');
	this.buttonBox.classList.add('box-button');
	this.buttonBox.onclick = 
	document.querySelector('#buttons').appendChild(this.buttonBox);

	this.buttonBox.onclick = () => {
		if (selectedCell == this) {
			nullCell.select();
			return;
		}
		if (annotationsOn && this.isFilled()) {
			highlightDigit(this.getValue());
			return;
		}
		this.select();

		if (this.isFilled()) {
			highlightDigit(this.getValue());
		}
	}
}

function highlightDigit(n) {

	cells.forEach(cell => {
		cell.deEmphasize();	
		if (!cell.isFilled()) {
			cell.setAvailable();
		}	
	});
	if (n == selectedDigit || n == 0) {
		selectedDigit = 0;
		return;
	}
	cells.filter(cell => cell.getValue() == n).forEach(cell => {
		cell.emphasize();
		cell.range().forEach(c => c.setUnavailable());
	});
	selectedDigit = n;
}

function keyHandler(event) {
	console.log(event);
	if (selectedCell == null) {
		return;
	}
	if (event.key.match(/^[1-9]$/)) {
		if (annotationsOn && !selectedCell.isFilled()) {
			selectedCell.toggleAnnotation(event.key);
			return;
		}
		if (!annotationsOn){
			selectedCell.setValue(event.key);
		}
		return;
	}
	if (event.key == 'Backspace') {
		if (!annotationsOn) {
			selectedCell.setValue(0);
		}
		return;
	}
	if (event.key == 'a') {
		annotationsOn = !annotationsOn;
		document.querySelector('.cell').classList.toggle('annot-on');
		if (annotationsOn && selectedCell.isFilled()) {
			nullCell.select();
		}
	}
}

document.addEventListener('keydown', keyHandler);


cells = [];
for (let i = 0; i < 81; i++) {
	cells.push(new Cell(i));
}


cells.forEach((cell, pos) => {
	cell.initValue(puzzle.problem[0][pos]);
});
