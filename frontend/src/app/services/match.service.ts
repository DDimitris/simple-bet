import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface MatchOdd {
  specifier: string;
  odd: number;
}

export interface Match {
  id?: number;
  description?: string;
  matchDate: string;
  matchTime: string;
  teamA: string;
  teamB: string;
  sport: string;
  odds: MatchOdd[];
}

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  private apiUrl = (window as any)["API_URL"] || "http://localhost:8081/api/matches";

  constructor(private http: HttpClient) { }

  getMatches(page = 0, size = 10, sortBy = 'id', direction = 'DESC'): Observable<any> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);
    return this.http.get<any>(this.apiUrl, { params });
  }

  createMatch(match: Match): Observable<Match> {
    return this.http.post<Match>(this.apiUrl, match);
  }

  updateMatch(id: number, match: Match): Observable<Match> {
    return this.http.put<Match>(`${this.apiUrl}/${id}`, match);
  }

  deleteMatch(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
