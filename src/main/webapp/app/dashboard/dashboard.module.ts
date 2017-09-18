import { NgModule } from '@angular/core';
import { JiraliteSharedModule } from "../shared/shared.module";
import { RouterModule } from "@angular/router";
import { DashboardRoute } from "./dashboard.route";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { IssueCreateComponent } from "./dashboard/issue-create/issue-create.component";

@NgModule({
  imports: [
      JiraliteSharedModule,
      RouterModule.forRoot(DashboardRoute, { useHash: true })
  ],
  declarations: [DashboardComponent, IssueCreateComponent]
})
export class DashboardModule { }
