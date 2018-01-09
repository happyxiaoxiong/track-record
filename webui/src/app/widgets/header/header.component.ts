import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() display_logout = true;
  username: string;
  constructor(private userSer: UserService) {
    this.username = userSer.getUsername();
  }

  ngOnInit() {
  }

  logout() {
    this.userSer.logout();
    return false;
  }
}
