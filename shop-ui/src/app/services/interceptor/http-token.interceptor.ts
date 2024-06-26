import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from '../token.service';

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {
  constructor(private tokenService: TokenService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token: string = this.tokenService.token;
    if (token) {
      const authReq: HttpRequest<any> = req.clone({
        headers: new HttpHeaders({
          Authorization: 'Bearer ' + token,
        }),
      });
      return next.handle(authReq);
    }
    return next.handle(req);
  }
}
