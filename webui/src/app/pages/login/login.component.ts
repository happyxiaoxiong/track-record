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
  redirectUrl: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userSer: UserService,
    private alertSer: AlertService) { }

  ngOnInit() {
    document.body.className = 'login-page';
    this.redirectUrl = this.route.snapshot.queryParams['redirectUrl'] || '/home';
  }

  login() {
    this.loading = true;
    this.userSer.login(this.model.account, this.model.password, () => {
      this.router.navigate([this.redirectUrl]);
    }, () => {
      this.alertSer.error('账号或者密码不正确');
      this.loading = false;
    });
  }
}
