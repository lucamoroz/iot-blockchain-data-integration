import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  @Input() notifications: string[] = ['test', 'test2', 'test3'];
  constructor() { }

  ngOnInit(): void {
  }

}
