import { NgModule } from '@angular/core';
import { JiraliteSharedModule } from "../shared/shared.module";
import { RouterModule } from "@angular/router";
import { DashboardRoute } from "./dashboard.route";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { IssueCreateComponent } from "./dashboard/issue-create/issue-create.component";
import { IssueViewComponent } from './dashboard/issue-view/issue-view.component';
import { IssueCommentsComponent } from './dashboard/issue-view/issue-comments/issue-comments.component';
import { CommentsCreateComponent } from "./dashboard/issue-view/comments-create/comments-create.component";

@NgModule({
  imports: [
      JiraliteSharedModule,
      RouterModule.forRoot(DashboardRoute, { useHash: true })
  ],
  declarations: [DashboardComponent, IssueCreateComponent, IssueViewComponent, IssueCommentsComponent, CommentsCreateComponent]
})
export class DashboardModule { }
