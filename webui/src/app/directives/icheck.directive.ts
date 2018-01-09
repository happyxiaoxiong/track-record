import {AfterViewInit, Directive, ElementRef, Renderer2} from '@angular/core';

@Directive({
  selector: '[appIcheck]'
})
export class IcheckDirective implements AfterViewInit {
  constructor(private el: ElementRef, private ren2: Renderer2) { }

  ngAfterViewInit() {
    $(this.el.nativeElement).iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
    this.ren2.addClass(this.el.nativeElement, 'icheck');
  }
}
