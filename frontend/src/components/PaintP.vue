<template>
    <div class="body">
        <div class="container">
            <section class="tools-board">
                <div class="row">
                    <label class="title">Shapes</label>
                    <ul class="options">
                        <li class="option">
                            <a style="cursor: pointer;" @click="insert('Rectangle')">&#9645;</a>
                            <span @click="insert('Rectangle')">Rectangle</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="insert('Square')">&#x25FB;</a>
                            <span @click="insert('Square')">Square</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="insert('Circle')">&#9711;</a>
                            <span @click="insert('Circle')">Circle</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="insert('Triangle')">&#9722;</a>
                            <span @click="insert('Triangle')">Triangle</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="insert('Ellipse')">&#x2B2D;</a>
                            <span @click="insert('Ellipse')">Ellipse</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="insert('Line')">&#9586;</a>
                            <span @click="insert('Line')">Line</span>
                        </li>
                        <li class="option">
                            <input type="checkbox" id="fill-color" @change="changefill">
                            <label for="">Fill color</label>
                        </li>
                    </ul>
                </div>
                <div class="row">
                    <label class="title">Options</label>
                    <ul class="options">
                        <li class="option">
                            <a style="cursor: pointer;" @click="Undo">&#x21BA;</a>
                            <span @click="Undo">Undo</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="Redo">&#x21BB;</a>
                            <span @click="Redo">Redo</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="Delete_shape">&#128503;</a>
                            <span @click="Delete_shape">Delete selected shape</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="Copy_shape">&#128455;</a>
                            <span @click="Copy_shape">Copy shape</span>
                        </li>
                        <li class="option">
                            <a style="cursor: pointer;" @click="paste_shape">&#128427;</a>
                            <span @click="paste_shape">Paste shape</span>
                        </li>
                    </ul>
                </div>
                <div class="row colors">
                    <label class="colors">Colors</label>
                    <ul class="options">
                        <li class="option" @click="getcolor('white')"></li>
                        <li class="option" @click="getcolor('black')"></li>
                        <li class="option" @click="getcolor('red')"></li>
                        <li class="option" @click="getcolor('green')"></li>
                        <li class="option" @click="getcolor('blue')"></li>
                        <input type="color" id="color-picker" value="#4A98F7" @change="changecolor">
                    </ul>

                </div>
                <div class="row buttons">
                    <button class="clear-canvas" @click="clear">Clear all shapes</button>
                    <button class="save-img" @click="save();">Save As JSON</button>
                    <button class="save-img" @click="saveXML();">Save As XML</button>
                    <input type="file" id="inputfile" @change="loadJSON();loadXML()">
                </div>
            </section>
            <section class="drawing-board">
                <canvas id="canvas" ref="canvas" @mousemove="check_move" @mousedown="check_down"
                    @mouseup="check_up"></canvas>
            </section>
        </div>
    </div>
</template>

<script>

export default ({
    data() {
        return {
            prevMouseX: 0,
            prevMouseY: 0,
            isDrawing: false,
            snapshot: null,
            ctx: null,
            currentShape: '',
            width: null,
            height: null,
            fillcolor: null,
            fill: false,
            picker: null,
            boxes: [],
            current_shape_index: null,
            is_dragging: false,
            selected_shape_from_menu: false,
            shape_found_in_canvas: false,
            shapes: [],
            copy: {},
            copied: false,
            changing_color: false,
            changing_fill: false,
            undo: [],
            redo: [],
            undo_actions: [],
            redo_actions: [],
            temp: {},
            first: true,
            undoClicked: false,
            selection: [],
            corner_index: null,
            Resize_clicked: false,
            moveclicked: false,
            StartX: 0,
            StartY: 0,
            idx: 0,
            starti: 0,
            s: '',
            jsonobj: null,
            
        }
    },

    methods: {

        check_down(e) {
            if (this.selected_shape_from_menu)
                this.startDraw(e);
            else {
                this.StartX = e.offsetX;
                this.StartY = e.offsetY;
                for (var index = 0; index < this.shapes.length; index++) {
                    if (this.is_mouse_in_shape(e.offsetX, e.offsetY, this.shapes[index])) {
                        this.current_shape_index = index;
                        this.shape_found_in_canvas = true;
                        this.display_selection();
                        this.moveclicked = true;
                        this.first = true;
                        return;
                    }
                }
                for (index = 0; index < this.selection.length; index++) {
                    if (Math.sqrt(Math.pow(e.offsetX - this.selection[index].x, 2) + Math.pow(e.offsetY - this.selection[index].y, 2)) <= this.selection[index].radius) {
                        this.moveclicked = false;
                        this.Resize_clicked = true;
                        this.first = true;
                        this.corner_index = index;
                        return;
                    }
                }
                this.shape_found_in_canvas = false;
                this.delete_selection();
                this.moveclicked = false;
                this.Resize_clicked = false;
            }
        },

        check_move(e) {
            if (this.isDrawing) {
                this.draw(e);
            }
            else if (this.moveclicked) {
                if (this.first) {
                    this.temp = this.put_shape(this.shapes[this.current_shape_index]);
                    this.first = false;
                }
                this.move_shape(e);
            }
            else if (this.Resize_clicked) {
                if (this.first) {
                    this.temp = this.put_shape(this.shapes[this.current_shape_index]);
                    this.first = false;
                }
                this.Resize_shape(e);
            }
        },

        check_up() {
            if (this.isDrawing) {
                this.selected_shape_from_menu = false;
                this.isDrawing = false;
                this.shapes.push(this.boxes[this.boxes.length - 1]);
                this.prepare_Undo('draw');
            }
            else if (this.is_dragging) {
                this.prepare_Undo('move');
                this.is_dragging = false;
                this.moveclicked = false;
                this.shape_found_in_canvas = false;
                this.delete_selection();
            }
            else if (this.Resize_clicked) {
                this.prepare_Undo('Resize');
                this.is_dragging = false;
                this.moveclicked = false;
                this.Resize_clicked = false;
                this.shape_found_in_canvas = false;
                this.delete_selection();
            }
            else if (!this.is_dragging)
                this.moveclicked = false;
        },

        insert(v) {
            this.currentShape = v;
            this.selected_shape_from_menu = true;
            if (this.shape_found_in_canvas)
                this.delete_selection();
            this.shape_found_in_canvas = false;
        },

        startDraw(e) {
            this.isDrawing = true;
            this.prevMouseX = e.offsetX;
            this.prevMouseY = e.offsetY;
            this.snapshot = this.ctx.getImageData(0, 0, this.width, this.height);

        },

        draw(e) {
            if (!this.isDrawing) return;
            this.ctx.putImageData(this.snapshot, 0, 0);
            if (this.currentShape === 'Rectangle')
                this.drawRect(e);
            else if (this.currentShape === 'Circle')
                this.drawcircle(e);
            else if (this.currentShape === 'Triangle')
                this.drawtriangle(e);
            else if (this.currentShape === 'Line')
                this.drawline(e);
            else if (this.currentShape === 'Ellipse')
                this.drawEllipse(e);
            else if (this.currentShape === 'Square')
                this.drawSquare(e);
        },

        drawRect(e) {
            this.ctx.strokeStyle = this.picker;
            this.ctx.fillStyle = this.picker;
            var box = {
                x: e.offsetX,
                y: e.offsetY,
                width: this.prevMouseX - e.offsetX,
                height: this.prevMouseY - e.offsetY,
                col: this.picker,
                id: this.idx++,
                name: this.currentShape,
                fill: this.fill.checked,
                dim: [e.offsetX,e.offsetY,this.prevMouseX - e.offsetX,this.prevMouseY - e.offsetY,],
            };
            this.boxes.push(box);
            if (!this.fill.checked)
                return this.ctx.strokeRect(e.offsetX, e.offsetY, this.prevMouseX - e.offsetX, this.prevMouseY - e.offsetY);
            return this.ctx.fillRect(e.offsetX, e.offsetY, this.prevMouseX - e.offsetX, this.prevMouseY - e.offsetY);
        },

        drawSquare(e) {
            this.ctx.strokeStyle = this.picker;
            this.ctx.fillStyle = this.picker;
            var box = {
                x: e.offsetX,
                y: e.offsetY,
                width: this.prevMouseX - e.offsetX,
                height: this.prevMouseX - e.offsetX,
                col: this.picker,
                id: this.idx++,
                name: this.currentShape,
                fill: this.fill.checked,
                dim: [e.offsetX,e.offsetY,this.prevMouseX - e.offsetX,this.prevMouseX - e.offsetX],
            };
            this.boxes.push(box);
            if (!this.fill.checked)
                return this.ctx.strokeRect(e.offsetX, e.offsetY, this.prevMouseX - e.offsetX, this.prevMouseX - e.offsetX);
            return this.ctx.fillRect(e.offsetX, e.offsetY, this.prevMouseX - e.offsetX, this.prevMouseX - e.offsetX);
        },

        drawcircle(e) {
            this.ctx.strokeStyle = this.picker;
            this.ctx.fillStyle = this.picker;
            this.ctx.linewidth = 1;
            this.ctx.beginPath();
            let radius = Math.sqrt(Math.pow((this.prevMouseX - e.offsetX), 2) + Math.pow((this.prevMouseY - e.offsetY), 2));
            this.ctx.arc(this.prevMouseX, this.prevMouseY, radius, 0, 2 * Math.PI);
            var box = {
                x: this.prevMouseX,
                y: this.prevMouseY,
                radius: radius,
                col: this.picker,
                name: this.currentShape,
                fill: this.fill.checked,
                id: this.idx++,
                dim: [this.prevMouseX,this.prevMouseY,radius],
            };
            this.boxes.push(box);
            this.fill.checked ? this.ctx.fill() : this.ctx.stroke();
        },

        drawEllipse(e) {
            this.ctx.strokeStyle = this.picker;
            this.ctx.fillStyle = this.picker;
            this.ctx.linewidth = 1;
            this.ctx.save();
            this.ctx.scale(1, 0.50);
            this.ctx.beginPath();
            let radius = Math.sqrt(Math.pow((this.prevMouseX - e.offsetX), 2) + Math.pow((this.prevMouseY - e.offsetY), 2));
            this.ctx.arc(this.prevMouseX, this.prevMouseY / .5, radius, 0, 2 * Math.PI);
            var box = {
                x: this.prevMouseX,
                y: this.prevMouseY,
                radius: radius,
                col: this.picker,
                name: this.currentShape,
                fill: this.fill.checked,
                id: this.idx++,
                dim: [this.prevMouseX,this.prevMouseY,radius],
            };
            this.boxes.push(box);
            this.fill.checked ? this.ctx.fill() : this.ctx.stroke();
            this.ctx.restore();
        },

        drawtriangle(e) {
            this.ctx.strokeStyle = this.picker;
            this.ctx.fillStyle = this.picker;
            var box = {
                x1: this.prevMouseX,
                y1: this.prevMouseY,
                x2: e.offsetX,
                y2: e.offsetY,
                x3: this.prevMouseX * 2 - e.offsetX,
                y3: e.offsetY,
                col: this.picker,
                id: this.idx++,
                name: this.currentShape,
                fill: this.fill.checked,
                dim: [this.prevMouseX,this.prevMouseY,e.offsetX,e.offsetY,this.prevMouseX * 2 - e.offsetX,e.offsetY,],
            };
            this.boxes.push(box);
            this.ctx.beginPath();
            this.ctx.moveTo(this.prevMouseX, this.prevMouseY);
            this.ctx.lineTo(e.offsetX, e.offsetY);
            this.ctx.lineTo(this.prevMouseX * 2 - e.offsetX, e.offsetY);
            this.ctx.closePath();
            this.fill.checked ? this.ctx.fill() : this.ctx.stroke();
        },

        drawline(e) {
            this.ctx.strokeStyle = this.picker;
            this.ctx.fillStyle = this.picker;
            var box = {
                x1: this.prevMouseX,
                y1: this.prevMouseY,
                x2: e.offsetX,
                y2: e.offsetY,
                length: Math.sqrt(Math.pow(e.offsetX - this.prevMouseX, 2) + Math.pow(e.offsetY - this.prevMouseY, 2)),
                col: this.picker,
                id: this.idx++,
                name: this.currentShape,
                fill: this.fill.checked,
                dim: [this.prevMouseX,this.prevMouseY,e.offsetX,e.offsetY],
            };
            this.boxes.push(box);
            this.ctx.beginPath();
            this.ctx.moveTo(this.prevMouseX, this.prevMouseY);
            this.ctx.lineTo(e.offsetX, e.offsetY);
            this.ctx.closePath();
            this.ctx.stroke();
        },

        async Redraw(shape) {
            this.ctx.strokeStyle = shape.col;
            this.ctx.fillStyle = shape.col;
            this.snapshot = this.ctx.getImageData(0, 0, this.width, this.height);

            if (shape.name === 'Rectangle' || shape.name === 'Square') {
                if (!shape.fill)
                    return this.ctx.strokeRect(shape.x, shape.y, shape.width, shape.height);
                return this.ctx.fillRect(shape.x, shape.y, shape.width, shape.height);
            }
            else if (shape.name === 'Circle') {
                this.ctx.linewidth = 1;
                this.ctx.beginPath();
                this.ctx.arc(shape.x, shape.y, shape.radius, 0, 2 * Math.PI);
                shape.fill ? this.ctx.fill() : this.ctx.stroke();
            }
            else if (shape.name === 'Triangle') {
                this.ctx.beginPath();
                this.ctx.moveTo(shape.x1, shape.y1);
                this.ctx.lineTo(shape.x2, shape.y2);
                this.ctx.lineTo(shape.x3, shape.y3);
                this.ctx.closePath();
                shape.fill ? this.ctx.fill() : this.ctx.stroke();
            }
            else if (shape.name === 'Line') {
                this.ctx.beginPath();
                this.ctx.moveTo(shape.x1, shape.y1);
                this.ctx.lineTo(shape.x2, shape.y2);
                this.ctx.closePath();
                this.ctx.stroke();
            }
            else if (shape.name === 'Ellipse') {
                this.ctx.linewidth = 1;
                this.ctx.save();
                this.ctx.scale(1, 0.50);
                this.ctx.beginPath();
                this.ctx.arc(shape.x, shape.y / .5, shape.radius, 0, 2 * Math.PI);
                shape.fill ? this.ctx.fill() : this.ctx.stroke();
                this.ctx.restore();
            }
        },

        is_mouse_in_shape(x, y, shape) {
            if (shape.name === 'Square' | shape.name === 'Rectangle') {
                var shape_left = shape.x;
                var shape_right = shape.x + shape.width;
                var shape_top = shape.y;
                var shape_bottom = shape.y + shape.height;
                if (x >= shape_left && x <= shape_right && y >= shape_top && y <= shape_bottom || x >= shape_right && x <= shape_left && y <= shape_bottom && y >= shape_top || x >= shape_left && x <= shape_right && y <= shape_top && y >= shape_bottom || x <= shape_left && x >= shape_right && y <= shape_top && y >= shape_bottom) {
                    return true;
                }
                return false;
            }
            else if (shape.name === 'Circle') {
                if (Math.sqrt(Math.pow(x - shape.x, 2) + Math.pow(y - shape.y, 2)) <= shape.radius)
                    return true;
                return false;
            }
            else if (shape.name === 'Line') {
                var distance = Math.sqrt(Math.pow((shape.x1 - x), 2) + Math.pow((shape.y1 - y), 2)) + Math.sqrt(Math.pow((shape.x2 - x), 2) + Math.pow((shape.y2 - y), 2));
                if (distance - shape.length <= .2 && distance - shape.length >= -.2)
                    return true;
                return false;
            }
            else if (shape.name === 'Ellipse') {
                if (Math.sqrt(Math.pow(x - shape.x, 2) + Math.pow(y / .5 - shape.y / .5, 2)) <= shape.radius)
                    return true;
                return false;
            }
            else if (shape.name === 'Triangle') {
                var x4 = shape.x2 - shape.x1;
                var y4 = shape.y2 - shape.y1;
                var x5 = shape.x3 - shape.x1;
                var y5 = shape.y3 - shape.y1;
                var a = ((x * y5 - y * x5) - (shape.x1 * y5 - shape.y1 * x5)) / (x4 * y5 - y4 * x5);
                var b = -1 * (((x * y4 - y * x4) - (shape.x1 * y4 - shape.y1 * x4)) / (x4 * y5 - y4 * x5));
                if (a > 0 && b > 0 && a + b < 1)
                    return true;
                return false;
            }
        },

        move_shape(e) {
            this.is_dragging = true;
            var dx = e.offsetX - this.StartX;
            var dy = e.offsetY - this.StartY;
            if (this.shapes[this.current_shape_index].name === 'Square' || this.shapes[this.current_shape_index].name === 'Rectangle' || this.shapes[this.current_shape_index].name === 'Circle' || this.shapes[this.current_shape_index].name === 'Ellipse') {
                this.shapes[this.current_shape_index].x += dx;
                this.shapes[this.current_shape_index].y += dy;
            }
            else if (this.shapes[this.current_shape_index].name === 'Line' || this.shapes[this.current_shape_index].name === 'Triangle') {
                this.shapes[this.current_shape_index].x1 += dx;
                this.shapes[this.current_shape_index].y1 += dy;
                this.shapes[this.current_shape_index].x2 += dx;
                this.shapes[this.current_shape_index].y2 += dy;

                if (this.shapes[this.current_shape_index].name === 'Triangle') {
                    this.shapes[this.current_shape_index].x3 += dx;
                    this.shapes[this.current_shape_index].y3 += dy;
                }
            }
            this.Delete_shape();
            this.Redraw(this.shapes[this.current_shape_index]);
            this.display_selection();
            this.StartX = e.offsetX;
            this.StartY = e.offsetY;
        },

        Resize_shape(e) {
            var dx = e.offsetX - this.StartX;
            var dy = e.offsetY - this.StartY;
            if (this.shapes[this.current_shape_index].name === 'Circle' || this.shapes[this.current_shape_index].name === 'Ellipse') {
                if (this.shapes[this.current_shape_index].radius + dy + dx >= 5) {
                    this.shapes[this.current_shape_index].radius = this.shapes[this.current_shape_index].radius + dy + dx;
                }
            }
            else if (this.shapes[this.current_shape_index].name === 'Rectangle') {
                this.shapes[this.current_shape_index].height += dy;
                this.shapes[this.current_shape_index].width += dx;
            }
            else if (this.shapes[this.current_shape_index].name === 'Square') {
                this.shapes[this.current_shape_index].width += dx;
                this.shapes[this.current_shape_index].height += dx;
            }
            else if (this.shapes[this.current_shape_index].name === 'Triangle') {
                if (this.selection[this.corner_index].x == this.shapes[this.current_shape_index].x1) {
                    this.shapes[this.current_shape_index].x1 += dx;
                    this.shapes[this.current_shape_index].y1 += dy;
                }
                else if (this.selection[this.corner_index].x == this.shapes[this.current_shape_index].x2) {
                    this.shapes[this.current_shape_index].x2 += dx;
                    this.shapes[this.current_shape_index].y2 += dy;
                }
                else if (this.selection[this.corner_index].x == this.shapes[this.current_shape_index].x3) {
                    this.shapes[this.current_shape_index].x3 += dx;
                    this.shapes[this.current_shape_index].y3 += dy;
                }
            }
            else if (this.shapes[this.current_shape_index].name === 'Line') {
                if (this.selection[this.corner_index].x == this.shapes[this.current_shape_index].x1) {
                    this.shapes[this.current_shape_index].x1 += dx;
                    this.shapes[this.current_shape_index].y1 += dy;
                }
                else if (this.selection[this.corner_index].x == this.shapes[this.current_shape_index].x2) {
                    this.shapes[this.current_shape_index].x2 += dx;
                    this.shapes[this.current_shape_index].y2 += dy;
                }
                this.shapes[this.current_shape_index].length = Math.sqrt(Math.pow(this.shapes[this.current_shape_index].x2 - this.shapes[this.current_shape_index].x1, 2) + Math.pow(this.shapes[this.current_shape_index].y1 - this.shapes[this.current_shape_index].y2, 2));
            }
            this.Delete_shape();
            this.Redraw(this.shapes[this.current_shape_index]);
            this.display_selection(this.shapes[this.current_shape_index]);
            this.StartX = e.offsetX;
            this.StartY = e.offsetY;
        },

        Delete_shape() {
            if (!this.is_dragging && !this.Resize_clicked && this.shape_found_in_canvas)
                this.prepare_Undo('delete');

            if (this.shape_found_in_canvas || this.undoClicked) {
                this.ctx.clearRect(0, 0, this.width, this.height);
                var boxes = [];
                for (var index = 0; index < this.shapes.length; index++) {
                    if (index != this.current_shape_index) {
                        this.Redraw(this.shapes[index]);
                        boxes.push(this.shapes[index]);
                    }
                }
                if (!this.is_dragging && !this.Resize_clicked) {
                    this.shapes = [];
                    for (index = 0; index < boxes.length; index++)
                        this.shapes.push(boxes[index]);
                    this.shape_found_in_canvas = false;
                }
                
            }
        },

        Copy_shape() {
            if (this.shape_found_in_canvas) {
                this.copied = true;
                this.copy.name = this.shapes[this.current_shape_index].name;
                this.copy.col = this.shapes[this.current_shape_index].col;
                this.copy.fill = this.shapes[this.current_shape_index].fill;

                if (this.copy.name === 'Rectangle' || this.copy.name === 'Square' || this.copy.name === 'Circle' || this.copy.name === 'Ellipse') {
                    this.copy.x = this.shapes[this.current_shape_index].x + 30;
                    this.copy.y = this.shapes[this.current_shape_index].y + 30;
                    this.copy.y = this.shapes[this.current_shape_index].id = this.idx++;
                    if (this.copy.name === 'Rectangle' || this.copy.name === 'Square') {
                        this.copy.width = this.shapes[this.current_shape_index].width;
                        this.copy.height = this.shapes[this.current_shape_index].height;
                    }
                    else
                        this.copy.radius = this.shapes[this.current_shape_index].radius;
                }
                else if (this.copy.name === 'Line') {
                    this.copy.x1 = this.shapes[this.current_shape_index].x1 + 10;
                    this.copy.y1 = this.shapes[this.current_shape_index].y1 + 10;
                    this.copy.x2 = this.shapes[this.current_shape_index].x2 + 10;
                    this.copy.y2 = this.shapes[this.current_shape_index].y2 + 10;
                    this.copy.length = this.shapes[this.current_shape_index].length;
                }

                else if (this.copy.name === 'Triangle') {
                    this.copy.x1 = this.shapes[this.current_shape_index].x1 + 50;
                    this.copy.y1 = this.shapes[this.current_shape_index].y1 + 30;
                    this.copy.x2 = this.shapes[this.current_shape_index].x2 + 30;
                    this.copy.y2 = this.shapes[this.current_shape_index].y2 + 30;
                    this.copy.x3 = this.shapes[this.current_shape_index].x3 + 50;
                    this.copy.y3 = this.shapes[this.current_shape_index].y3 + 30;
                }
                this.delete_selection();
                this.shape_found_in_canvas = false;
            }
        },

        paste_shape() {
            if (this.copied) {
                this.Redraw(this.copy);
                this.shapes.push(this.copy);
                this.prepare_Undo('draw');
                this.copied = false;
                this.copy = {};
                this.delete_selection();
            }
        },

        display_selection() {
            var x1;
            var x2;
            var x3;
            var x4;
            var y1;
            var y2;
            var y3;
            var y4;
            this.selection = [];

            if (this.shapes[this.current_shape_index].name == 'Circle' || this.shapes[this.current_shape_index].name == 'Ellipse') {

                x1 = this.shapes[this.current_shape_index].x + this.shapes[this.current_shape_index].radius;
                y1 = this.shapes[this.current_shape_index].y;
                x2 = this.shapes[this.current_shape_index].x - this.shapes[this.current_shape_index].radius;
                y2 = this.shapes[this.current_shape_index].y;
                x3 = this.shapes[this.current_shape_index].x;
                x4 = this.shapes[this.current_shape_index].x;

                var scale = 1;
                if (this.shapes[this.current_shape_index].name == 'Ellipse')
                    scale = .5;
                y3 = this.shapes[this.current_shape_index].y + scale * this.shapes[this.current_shape_index].radius;
                y4 = this.shapes[this.current_shape_index].y - scale * this.shapes[this.current_shape_index].radius;
            }

            else if (this.shapes[this.current_shape_index].name == 'Square' || this.shapes[this.current_shape_index].name == 'Rectangle') {
                x1 = this.shapes[this.current_shape_index].x;
                y1 = this.shapes[this.current_shape_index].y;
                x2 = this.shapes[this.current_shape_index].x + this.shapes[this.current_shape_index].width;
                y2 = this.shapes[this.current_shape_index].y;
                x3 = this.shapes[this.current_shape_index].x;
                y3 = this.shapes[this.current_shape_index].y + this.shapes[this.current_shape_index].height;
                x4 = this.shapes[this.current_shape_index].x + this.shapes[this.current_shape_index].width;
                y4 = this.shapes[this.current_shape_index].y + this.shapes[this.current_shape_index].height;
            }

            else if (this.shapes[this.current_shape_index].name == 'Triangle' || this.shapes[this.current_shape_index].name == 'Line') {
                x1 = this.shapes[this.current_shape_index].x1;
                y1 = this.shapes[this.current_shape_index].y1;
                x2 = this.shapes[this.current_shape_index].x2;
                y2 = this.shapes[this.current_shape_index].y2;
                if (this.shapes[this.current_shape_index].name == 'Triangle') {
                    x3 = this.shapes[this.current_shape_index].x3;
                    y3 = this.shapes[this.current_shape_index].y3;
                }
            }

            this.ctx.fillStyle = 'black';
            this.ctx.linewidth = 1;
            this.ctx.beginPath();

            this.ctx.arc(x1, y1, 4, 0, 2 * Math.PI);
            this.ctx.closePath();
            this.ctx.arc(x2, y2, 4, 0, 2 * Math.PI);
            this.ctx.closePath();
            var box = {
                x: x1,
                y: y1,
                radius: 4,
            };
            this.selection.push(box);
            box = {};
            box = {
                x: x2,
                y: y2,
                radius: 4,
            };
            this.selection.push(box);
            box = {};

            if (this.shapes[this.current_shape_index].name != 'Line') {
                this.ctx.arc(x3, y3, 4, 0, 2 * Math.PI);
                this.ctx.closePath();
                box = {
                    x: x3,
                    y: y3,
                    radius: 4,
                };
                this.selection.push(box);
                box = {};
            }

            if (this.shapes[this.current_shape_index].name != 'Line' && this.shapes[this.current_shape_index].name != 'Triangle') {
                this.ctx.arc(x4, y4, 4, 0, 2 * Math.PI);
                this.ctx.closePath();
                box = {
                    x: x4,
                    y: y4,
                    radius: 4,
                };
                this.selection.push(box);
            }
            this.ctx.fill();
        },

        delete_selection() {
            this.ctx.clearRect(0, 0, this.width, this.height);
            for (var index = 0; index < this.shapes.length; index++) {
                this.Redraw(this.shapes[index]);
            }
        },

        changecolor() {
            var colorPicker = document.getElementById("color-picker");
            this.picker = colorPicker.value;
            if (this.shape_found_in_canvas) {
                this.changing_color = true;
                this.change_painting();
            }
        },

        getcolor(v) {
            var colorPicker = document.getElementById("color-picker");
            if (v === 'black') {
                this.picker = '#000000';
                colorPicker.value = '#000000';
            }
            else if (v === 'white') {
                this.picker = '#FFFFFF';
                colorPicker.value = '#FFFFFF';
            }
            else if (v === 'red') {
                this.picker = '#FF0000';
                colorPicker.value = '#FF0000';
            }
            else if (v === 'green') {
                this.picker = '#008000';
                colorPicker.value = '#008000';
            }
            else if (v === 'blue') {
                this.picker = '#4A98F7';
                colorPicker.value = '#4A98F7';
            }
            if (this.shape_found_in_canvas) {
                this.changing_color = true;
                this.change_painting();
            }

        },

        changefill() {
            var fillcolor = document.getElementById("fill-color");
            this.fill = fillcolor;
            if (this.shape_found_in_canvas) {
                this.changing_fill = true;
                this.change_painting();
            }
        },

        change_painting() {
            this.temp = this.put_shape(this.shapes[this.current_shape_index]);
            if (this.changing_color) {
                this.shapes[this.current_shape_index].col = this.picker;
                this.changing_color = false;
                this.prepare_Undo('change_color');
            }
            else if (this.changing_fill) {
                this.shapes[this.current_shape_index].fill = this.fill.checked;
                this.changing_fill = false;
                this.prepare_Undo('change_fill');
            }
            this.ctx.clearRect(0, 0, this.width, this.height);
            for (var index = 0; index < this.shapes.length; index++)
                this.Redraw(this.shapes[index]);
            this.shape_found_in_canvas = false;
        },

        put_shape(shape) {
            var Box = {};
            Box.name = shape.name;
            Box.col = shape.col
            Box.fill = shape.fill;
            if (shape.name === 'Rectangle' || shape.name === 'Square' || shape.name === 'Circle' || shape.name === 'Ellipse') {
                Box.x = shape.x;
                Box.y = shape.y;
                if (shape.name === 'Rectangle' || shape.name === 'Square') {
                    Box.width = shape.width;
                    Box.height = shape.height;
                }
                else
                    Box.radius = shape.radius;
            }
            else if (shape.name === 'Line' || shape.name === 'Triangle') {
                Box.x1 = shape.x1;
                Box.y1 = shape.y1;
                Box.x2 = shape.x2;
                Box.y2 = shape.y2;
                if (shape.name === 'Line')
                    Box.length = shape.length;
                else {
                    Box.x3 = shape.x3;
                    Box.y3 = shape.y3;
                }
            }
            return Box;
        },

        prepare_Undo(x) {
            var action = {};
            var Box = {};
            if (x === 'draw') {
                action = {
                    name: 'draw',
                    index: (this.shapes.length - 1),
                };
                this.undo_actions.push(action);
                Box = this.put_shape(this.shapes[this.shapes.length - 1]);
                this.undo.push(Box);
            }

            else if (x === 'delete') {
                action = {
                    name: 'delete',
                    index: this.current_shape_index,
                };
                this.undo_actions.push(action);
                Box = this.put_shape(this.shapes[this.current_shape_index]);
                this.undo.push(Box);
            }
            else if (x === 'change_color') {
                Box = this.put_shape(this.shapes[this.current_shape_index]);
                action = {
                    name: 'change_color',
                    index: this.current_shape_index,
                    shape: this.temp,
                };
                this.undo_actions.push(action);
                this.undo.push(Box);
                this.temp = {};
            }
            else if (x === 'change_fill') {
                Box = this.put_shape(this.shapes[this.current_shape_index]);
                action = {
                    name: 'change_fill',
                    index: this.current_shape_index,
                    shape: this.temp,
                };
                this.undo_actions.push(action);
                this.undo.push(Box);
                this.temp = {};
            }
            else if (x === 'Resize') {
                Box = this.put_shape(this.shapes[this.current_shape_index]);
                action = {
                    name: 'Resize',
                    index: this.current_shape_index,
                    shape: this.temp,
                };
                this.undo_actions.push(action);
                this.undo.push(Box);
                this.temp = {};
            }
            else if (x === 'move') {
                Box = this.put_shape(this.shapes[this.current_shape_index]);
                action = {
                    name: 'move',
                    index: this.current_shape_index,
                    shape: this.temp,
                };
                this.undo_actions.push(action);
                this.undo.push(Box);
                this.temp = {};
            }
        },

        Undo() {
            var i;
            if (this.undo.length == 0)
                return;

            if (this.undo_actions[this.undo_actions.length - 1].name === 'draw') {
                this.undoClicked = true;
                this.current_shape_index = this.undo_actions[this.undo_actions.length - 1].index;
                this.Delete_shape();
                this.redo_actions.push(this.undo_actions.pop());
                this.redo.push(this.undo.pop());
                this.undoClicked = false;
            }
            else if (this.undo_actions[this.undo_actions.length - 1].name === 'delete') {
                this.ctx.clearRect(0, 0, this.width, this.height);
                var boxes = [];
                for (i = 0; i < this.undo_actions[this.undo_actions.length - 1].index; i++) {
                    this.Redraw(this.shapes[i]);
                    boxes.push(this.shapes[i]);
                }
                this.Redraw(this.undo[this.undo.length - 1]);
                boxes.push(this.undo[this.undo.length - 1]);
                for (; i < this.shapes.length; i++) {
                    this.Redraw(this.shapes[i]);
                    boxes.push(this.shapes[i]);
                }
                this.shapes = [];
                for (i = 0; i < boxes.length; i++)
                    this.shapes.push(boxes[i]);
                this.redo_actions.push(this.undo_actions.pop());
                this.redo.push(this.undo.pop());
            }
            else if (this.undo_actions[this.undo_actions.length - 1].name === 'change_color') {
                this.shapes[this.undo_actions[this.undo_actions.length - 1].index].col = this.undo_actions[this.undo_actions.length - 1].shape.col;
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.redo_actions.push(this.undo_actions.pop());
                this.redo.push(this.undo.pop());
            }
            else if (this.undo_actions[this.undo_actions.length - 1].name === 'change_fill') {
                this.shapes[this.undo_actions[this.undo_actions.length - 1].index].fill = this.undo_actions[this.undo_actions.length - 1].shape.fill;
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.redo_actions.push(this.undo_actions.pop());
                this.redo.push(this.undo.pop());
            }
            else if (this.undo_actions[this.undo_actions.length - 1].name === 'Resize') {
                this.shapes[this.undo_actions[this.undo_actions.length - 1].index] = this.undo_actions[this.undo_actions.length - 1].shape;
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.redo_actions.push(this.undo_actions.pop());
                this.redo.push(this.undo.pop());
            }
            else if (this.undo_actions[this.undo_actions.length - 1].name === 'move') {
                this.shapes[this.undo_actions[this.undo_actions.length - 1].index] = this.undo_actions[this.undo_actions.length - 1].shape;
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.redo_actions.push(this.undo_actions.pop());
                this.redo.push(this.undo.pop());
            }
        },

        Redo() {
            if (this.redo.length === 0)
                return;
            var i;
            if (this.redo_actions[this.redo_actions.length - 1].name === 'draw') {
                this.ctx.clearRect(0, 0, this.width, this.height);
                var boxes = [];
                for (i = 0; i < this.redo_actions[this.redo_actions.length - 1].index; i++) {
                    this.Redraw(this.shapes[i]);
                    boxes.push(this.shapes[i]);
                }
                this.Redraw(this.redo[this.redo.length - 1]);
                boxes.push(this.redo[this.redo.length - 1]);
                for (; i < this.shapes.length; i++) {
                    this.Redraw(this.shapes[i]);
                    boxes.push(this.shapes[i]);
                }
                this.shapes = [];
                for (i = 0; i < boxes.length; i++)
                    this.shapes.push(boxes[i]);
                this.undo_actions.push(this.redo_actions.pop());
                this.undo.push(this.redo.pop());
            }

            else if (this.redo_actions[this.redo_actions.length - 1].name === 'delete') {
                this.undoClicked = true;
                this.current_shape_index = this.redo_actions[this.redo_actions.length - 1].index;
                this.Delete_shape();
                this.undo_actions.push(this.redo_actions.pop());
                this.undo.push(this.redo.pop());
                this.undoClicked = false;
            }
            else if (this.redo_actions[this.redo_actions.length - 1].name === 'change_color') {
                this.shapes[this.redo_actions[this.redo_actions.length - 1].index] = this.put_shape(this.redo[this.redo.length - 1]);
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.undo_actions.push(this.redo_actions.pop());
                this.undo.push(this.redo.pop());
            }
            else if (this.redo_actions[this.redo_actions.length - 1].name === 'change_fill') {
                this.shapes[this.redo_actions[this.redo_actions.length - 1].index] = this.put_shape(this.redo[this.redo.length - 1]);
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.undo_actions.push(this.redo_actions.pop());
                this.undo.push(this.redo.pop());
            }
            else if (this.redo_actions[this.redo_actions.length - 1].name === 'Resize') {
                this.shapes[this.redo_actions[this.redo_actions.length - 1].index] = this.put_shape(this.redo[this.redo.length - 1]);
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.undo_actions.push(this.redo_actions.pop());
                this.undo.push(this.redo.pop());
            }
            else if (this.redo_actions[this.redo_actions.length - 1].name === 'move') {
                this.shapes[this.redo_actions[this.redo_actions.length - 1].index] = this.put_shape(this.redo[this.redo.length - 1]);
                this.ctx.clearRect(0, 0, this.width, this.height);
                for (i = 0; i < this.shapes.length; i++)
                    this.Redraw(this.shapes[i]);
                this.undo_actions.push(this.redo_actions.pop());
                this.undo.push(this.redo.pop());
            }
        },
        save() {
            for(var i=0;i<this.shapes.length;i++){
           if(this.shapes[i].name==='Rectangle'||this.shapes[i].name==='Square' )
             this.SendtoBack(this.shapes[i]);
            else if(this.shapes[i].name==='Circle'||this.shapes[i].name==='Ellipse')
            this.SendtoBack1(this.shapes[i]);
            else if(this.shapes[i].name==='Triangle')
            this.SendtoBack2(this.shapes[i]);
            else if(this.shapes[i].name==='Line')
            this.SendtoBack3(this.shapes[i]);
            console.log(this.shapes[i].radius);
            }
            this.saveJSON();
            
         },
         SendtoBack(shape){
            var col = shape.col.slice(1,-1);
          fetch("http://localhost:8085/"+shape.name+"/"+shape.id+"/"+col+
                       "/"+shape.fill+"/"+shape.x+"/"+shape.y+"/"+shape.width+
                       "/"+shape.height+"/"+"null"+"/"+"null",
                       {method: "get"})
                       .then(response => {
            return response.text();
          })
    
        },
        SendtoBack1(shape){
            console.log(shape.col);
            var col = shape.col.slice(1,-1);
          fetch("http://localhost:8085/"+shape.name+"/"+
          shape.id+"/"+col+"/"+shape.fill+"/"+shape.x+"/"+
                      shape.y+"/"+shape.radius+"/"+"null"+"/"+"null"+"/"+"null",
                      {method: "get"})
                      .then(response => {
            return response.text();
          })
        },
        SendtoBack2(shape){
            var col = shape.col.slice(1,-1);
          fetch("http://localhost:8085/"+shape.name+"/"+
          shape.id+"/"+col+"/"+shape.fill+"/"+shape.x1+"/"+
                      shape.y1+"/"+shape.x2+"/"+shape.y2+"/"+shape.x3+"/"+
                      shape.y3,{method: "get"})
                      .then(response => {
            return response.text();
          })

        },
        SendtoBack3(shape){
            var col = shape.col.slice(1,-1);
          fetch("http://localhost:8085/"+shape.name+"/"+
          shape.id+"/"+col+"/"+shape.fill+"/"+shape.x1+
                      "/"+shape.y1+"/"+shape.x2+"/"+shape.y2+
                      "/"+shape.length+"/"+"null",{method: "get"})
          .then(response => {
            return response.text();
          })
        },
        async saveJSON(){
            const jsonResponse = await fetch(`http://localhost:8085/saveJson`)
            let data = await jsonResponse.text()
             const blob = new Blob([data],{'json': 'text/plain'})
             const e = document.createEvent('MouseEvents'),
             a=document.createElement('a')
             a.download = 'file.json'
             a.href = window.URL.createObjectURL(blob);
             a.dataset.downloadurl = ['savedile/json',a.download,a.href].join(':')
             e.initEvent('click',true,false,window,0,0,0,0,0,false,false,false,false,0,null)
             a.dispatchEvent(e)
        
        },
        async loadJSON() {
            let file = document.getElementById('inputfile').files[0]
      let fR = new FileReader(file)
      fR.onload = async (fileLoadedEvent) => {
        const content = fileLoadedEvent.target.result
        if (file != null && (file.name.endsWith('.json'))){
            const response= await fetch(`http://localhost:8085/loadJson`,{method:'POST',body:content})
            let data = await response.json();
            this.shapes=data;
            console.log(this.shapes);
            for(var i=0;i<this.shapes.length;i++){
                console.log(this.shapes[i]);
                 this.Redraw(this.shapes[i]);
            }
        }
        }
      if (file != null && (file.name.endsWith('.json')))
      fR.readAsText(file, 'UTF-8')
        },
        async saveXML(){
            let data = JSON.stringify(this.shapes);
             const blob = new Blob([data],{'xml': 'text/plain'})
             const e = document.createEvent('MouseEvents'),
             a=document.createElement('a')
             a.download = 'file.xml'
             a.href = window.URL.createObjectURL(blob);
             a.dataset.downloadurl = ['savedile/xml',a.download,a.href].join(':')
             e.initEvent('click',true,false,window,0,0,0,0,0,false,false,false,false,0,null)
             a.dispatchEvent(e)
        
        },
        async loadXML() {
            let file = document.getElementById('inputfile').files[0]
      let fR = new FileReader(file)
      //fR.onload = async (fileLoadedEvent) => {
        //const content = fileLoadedEvent.target.result
        if (file != null && (file.name.endsWith('.xml'))){
            fetch(file.name)
           .then(response => console.log(response.json()))
            //.then(text => console.log(text))
            console.log(this.shapes);
            for(var i=0;i<this.shapes.length;i++){
                console.log(this.shapes[i]);
                 this.Redraw(this.shapes[i]);
            }
        }
       // }
      if (file != null && (file.name.endsWith('.xml')))
      fR.readAsText(file, 'UTF-8')
        },


          
        clear() {
            this.ctx.clearRect(0, 0, this.width, this.height);
            this.prevMouseX = 0;
            this.prevMouseY = 0;
            this.isDrawing = false;
            this.snapshot = null;
            this.currentShape = '';
            this.boxes = [];
            this.current_shape_index = null;
            this.is_dragging = false;
            this.selected_shape_from_menu = false;
            this.shape_found_in_canvas = false;
            this.shapes = [];
            this.copy = {};
            this.copied = false;
            this.changing_color = false;
            this.changing_fill = false;
            this.undo = [];
            this.redo = [];
            this.undo_actions = [];
            this.redo_actions = [];
            this.temp = {};
            this.first = true;
            this.undoClicked = false;
            this.selection = [];
            this.corner_index = null;
            this.Resize_clicked = false;
            this.moveclicked = false;
            this.StartX = 0;
            this.StartY = 0;
        },
    },

    mounted() {
        var canvas = document.getElementById("canvas");
        var c = canvas.getContext("2d");
        this.ctx = c;
        canvas.width = 1012.8;
        canvas.height = 754.6;
        this.width = canvas.width;
        this.height = canvas.height;
        this.ctx.fillStyle = "#fff";
        this.ctx.fillRect(0, 0, this.width, this.height);
        var colorPicker = document.getElementById("color-picker");
        this.picker = colorPicker.value;
        var fillcolor = document.getElementById("fill-color");
        this.fill = fillcolor;
    },

});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Poppings', sans-serif;
}

.body {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    background: pink;
}

.container {
    display: flex;
    width: 100%;
    max-width: 1300px;
    padding: 10px;
    gap: 10px;

}

section {
    background-color: #fff;
    border-radius: 7px;
}

.drawing-board {
    flex: 1;

}

.tools-board {
    width: 210px;
    padding: 15px 22px 0;
}

.tools-board .row {
    margin-bottom: 20px;
}

.row .options {
    list-style: none;
    margin: 10px 0 0 5px;
}

.row .options .option {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    cursor: pointer;

}

.option :where(span, label) {
    color: #5A6168;
    padding-left: 10PX;
}

.option:hover {
    color: pink;
}

#fill-color:checked~label {
    color: pink;
}

.option #size-slider {
    width: 100%;
    height: 5px;
    margin-top: 10px;
}

.colors .option {
    height: 20px;
    width: 20px;
    background: red;
    border-radius: 50%;
    margin-top: 3px;
    position: relative;
}

.colors .options {
    display: flex;
    justify-content: space-between;

}

.colors .option:selected::before {
    position: absolute;
    content: "";
    height: 12px;
    width: 12px;
    background: inherit;
    border-radius: inherit;
    border: 2px solid #fff;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

.colors .option:nth-child(1) {
    background: #fff;
    border: 1px solid #000
}

.colors .option:nth-child(2) {
    background: #000;
}

.colors .option:nth-child(3) {
    background: red;
}

.colors .option:nth-child(4) {
    background: green;
}

.colors .option:nth-child(5) {
    background: #4A98F7;
}

.colors .option:first-child:hover::before {
    border-color: #ccc;

}

.buttons button {
    width: 100%;
    color: #fff;
    border: none;
    outline: none;
    padding: 11px 0;
    margin-bottom: 13px;
    background: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.9rem;
}

.buttons .clear-canvas {
    color: #6C757D;
    border: 1px solid #6C757D;
    transition: all 0.3 ease;
}

.buttons .save-img {
    background: pink;
    border: 1px solid pink;
}

.clear-canvas:hover {
    color: #fff;
    background: #6C757D;
}

.colors.opion:first-child.selected::before {
    border-color: #ccc;
}

.option #color-picker {
    opacity: 0;
    cursor: pointer;

}

.drawing-board canvas {
    cursor: pointer;
}

</style>
