import { Routes } from '@angular/router';
import { MatchListComponent } from './components/match-list/match-list.component';
import { MatchFormComponent } from './components/match-form/match-form.component';

export const routes: Routes = [  
  { path: '', redirectTo: '/matches', pathMatch: 'full' },
  { path: 'matches', component: MatchListComponent },
  { path: 'matches/new', component: MatchFormComponent },
  { path: 'matches/:id/edit', component: MatchFormComponent },
  { path: '**', redirectTo: '/matches' } // fallback
];
