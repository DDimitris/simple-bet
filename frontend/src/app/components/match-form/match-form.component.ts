import { Component, OnInit } from '@angular/core';
import { MatchService, Match } from '../../services/match.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-match-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './match-form.component.html',
})
export class MatchFormComponent implements OnInit {
  match: Match = {
    teamA: '',
    teamB: '',
    matchDate: '',
    matchTime: '',
    sport: 'FOOTBALL',
    odds: [
      { specifier: '1', odd: 1 },
      { specifier: 'X', odd: 1 },
      { specifier: '2', odd: 1 }
    ]
  };

  constructor(
    private matchService: MatchService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.matchService.getMatches(0, 5, 'id', 'ASC').subscribe(res => {
        const match = res.content.find((m: Match) => m.id == +id);
        if (match) this.match = match;
      });
    }
  }

  submit(): void {
    if (this.match.id) {
      this.matchService.updateMatch(this.match.id, this.match).subscribe(() => {
        this.router.navigate(['/matches']);
      });
    } else {
      this.matchService.createMatch(this.match).subscribe(() => {
        this.router.navigate(['/matches']);
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/matches']);
  }
}
