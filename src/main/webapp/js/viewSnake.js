  const width = 30;
        const height = 20;

        function Cell() {
            let box = document.createElement('div');
            this.box = box;
            this.isSnake = () => this.box.classList.contains('snake');
            this.isWall = () => this.box.classList.contains('wall');
            this.isFood = () => this.box.classList.contains('food');
            this.isEmpty = () => !this.isSnake() && !this.isWall() && !this.isFood();
        }

        cells = [];

        for (let i = 0; i < width * height; i++) {
            let cell = new Cell();
            document.querySelector('.arena').appendChild(cell.box);
            cells.push(cell);
        }

        cells.forEach((cell, pos) => {
            cell.right = cells[(pos+1) % (width * height)];
            cell.down = cells[(pos+width) % (width * height)];
        });

        cells.forEach((cell, pos) => {
            cell.right.left = cell;
            cell.down.up = cell;
        });

        cells.filter((cell, pos) => pos % width == 0).forEach(cell => {
            cell.left = cell.left.down;
            cell.left.right = cell;
        });

        var head = cells[Math.floor((width * height) / 2)];
        var tail = head.left;
        tail.next = head;

        head.box.classList.add('snake', 'left');
        tail.box.classList.add('snake', 'right');

        var directions = ['left', 'right', 'up', 'down'];
        var opposite = {right:'left', left:'right', down:'up', up:'down'};

        var direction = 'right';
        var nextDirection = null;
        var nextNextDirection = null;
        var gameLoopId;
        var score = 0;

        function advance() {
            if (nextDirection != null) {
                direction = nextDirection;
                nextDirection = nextNextDirection;
                nextNextDirection = null;
            }

            head.next = head[direction]
            head.box.classList.add(direction);
            head = head.next;
            if (head.isSnake() || head.isWall()) {
                gameOver();
                return;
            }
            head.box.classList.add('snake', opposite[direction]);
            
            if (tail.isFood()) {
                tail.box.classList.remove('food');
            }
            else {
                tail.box.classList.remove('snake');
                tail.next.box.classList.remove(opposite[tail.box.className]);
                tail.box.className = '';
                tail = tail.next;
            }

            if (head.isFood()) {
                score ++;
                newFood();
            }
        }

        function newFood() {
            let freeBoxes = [...document.querySelectorAll('.arena>:not(.snake, .food, .wall)')];
            freeBoxes[Math.floor(Math.random() * freeBoxes.length)].classList.add('food');
        }

        function gameOver() {
            clearInterval(gameLoopId);
            console.log('Game over. Score: ' + score);
        }

        function setDirection(newDirection) {
            if (nextDirection == null) {
                if (newDirection != opposite[direction]) {
                    nextDirection = newDirection;
                }
                return;
            }
            if (nextNextDirection == null) {
                if (newDirection != opposite[nextDirection]) {
                    nextNextDirection = newDirection;
                }
            }
        }
        
        
        function start(event) {
            gameLoopId = setInterval(advance, 150);
            document.addEventListener('keydown', captureControls);
        }
        
        function captureControls(event) {
            
            if (event.key.startsWith('Arrow')) {
                setDirection(event.key.slice(5).toLowerCase());
                return;
            }
            if (event.key == ' ') {
                clearInterval(gameLoopId);
                removeEventListener(document, captureControls);
                document.addEventListener('keydown', start, {once: true});
            }
        }
        
        newFood();
        document.addEventListener('keydown', start, {once: true});