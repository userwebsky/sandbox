import { Component } from '@angular/core';
import { logout } from "../../../auth/store/auth.actions";
import { Store } from "@ngrx/store";
import { AppState } from "../../../../store/app.reducer";
import { selectAuthUser } from "../../../auth/store/auth.selectors";
import { Observable } from "rxjs";
import { User } from "../../models/auth.model";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  user$: Observable<User | null> = this.store.select(selectAuthUser);

  constructor(private store: Store<AppState>) {}

    logout() {
      this.store.dispatch(logout());
    }
}
