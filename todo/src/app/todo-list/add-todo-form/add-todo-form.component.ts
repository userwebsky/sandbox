import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-add-todo-form',
  templateUrl: './add-todo-form.component.html',
  styleUrls: ['./add-todo-form.component.css']
})
export class AddTodoFormComponent {
  @Output() addTodo = new EventEmitter<string>();
  todoName = '';

  // person = {name: 'test', surname: 'testowy', role: 'father'}

  addNewTodo() {
    // console.log(this.person)
    this.addTodo.emit(this.todoName);
  }
}
