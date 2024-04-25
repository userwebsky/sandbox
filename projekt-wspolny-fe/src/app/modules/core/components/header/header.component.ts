import { Component } from '@angular/core';
import { logout } from "../../../auth/store/auth.actions";
import { Store } from "@ngrx/store";
import { AppState } from "../../../../store/app.reducer";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {


  constructor(private store: Store<AppState>) {}

    logout() {
      this.store.dispatch(logout());
    }
}
