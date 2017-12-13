import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {AlertService} from '../../services/alert.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  model: any = {};
  loading = false;
  returnUrl: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private alertService: AlertService) { }

  ngOnInit() {
    document.body.className = 'login-page';
  }

  login() {
    this.loading = true;
    this.userService.login(this.model.account, this.model.password)
      .subscribe(
        data => {},
        error => {
          this.alertService.error('账号或者密码不正确');
          this.loading = false;
        });
  }
}
