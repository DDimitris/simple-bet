import { Component, OnInit } from '@angular/core';
import { Match, MatchService } from '../../services/match.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-match-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './match-list.component.html',
  styleUrls: ['./match-list.component.scss']
})

export class MatchListComponent implements OnInit {
  matches: Match[] = [];
  page = 0;
  size = 5;
  sortBy = 'id';
  direction: 'ASC' | 'DESC' = 'ASC';
  availableSports = ['FOOTBALL', 'BASKETBALL'];

  filters = {
    teamA: '',
    teamB: '',
    sport: ''
  };
  
  constructor(private matchService: MatchService, private router: Router) {}

  ngOnInit(): void {
    this.loadMatches();
  }

  loadMatches(): void {
    this.matchService
      .getMatches(this.page, this.size, this.sortBy, this.direction,
        this.filters.teamA, this.filters.teamB, this.filters.sport)
      .subscribe((res) => {
        this.matches = res.content;
      });
  }

  applyFilters(): void {
    this.page = 0;
    this.loadMatches();
  }

  clearFilters(): void {
    this.filters = { teamA: '', teamB: '', sport: '' };
    this.page = 0;
    this.loadMatches();
  }

  deleteMatch(id: number): void {
    this.matchService.deleteMatch(id).subscribe(() => this.loadMatches());
  }

  nextPage(): void {
    this.page++;
    this.loadMatches();
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadMatches();
    }
  }

  sort(field: string): void {
    this.sortBy = field;
    this.direction = this.direction === 'ASC' ? 'DESC' : 'ASC';
    this.loadMatches();
  }

  goToCreate(): void {
    this.router.navigate(['/matches/new']);
  }

  goToEdit(id?: number): void {
    if (id) this.router.navigate(['/matches', id, 'edit'],
      { state: { match: this.matches } }
    );
  }
}
