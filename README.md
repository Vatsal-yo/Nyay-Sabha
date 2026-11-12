# Nyay Sabha — Case Library & Judge Upgrade

Two-player, same-device Indian courtroom simulation. This upgrade implements UI polish, expanded case library, detailed case files, and a more structured AI judge. **Verified Indian statutory-law integration and JDBC are intentionally deferred to the December evaluation.**

## Stack
- Frontend: React + Vite
- Backend: Core Java 17+ with `HttpServer` and built-in `HttpClient`
- AI: Groq API through Java backend only
- No Spring Boot and no third-party Java backend dependencies

## Run backend (PowerShell)
```powershell
cd backend
$env:GROQ_API_KEY="YOUR_GROQ_API_KEY"
$env:GROQ_MODEL="openai/gpt-oss-120b"
javac --add-modules jdk.httpserver -d out (Get-ChildItem src/nyaysabha/*.java).FullName
java --add-modules jdk.httpserver -cp out nyaysabha.Main
```
The API runs at `http://localhost:8080`. Without a key, the server uses demo mode.

## Run frontend in a second terminal
```powershell
cd frontend
npm install
npm run dev
```
Open `http://localhost:5173`.

## Included in this update
### Phase 1 — Judge UI polish
- Highlighted objection ruling chips (Sustained green / Overruled orange)
- Color emphasis for verdict and important judge terms
- Preserved judge line breaks and clean review formatting
- Better objection display in court record

### Phase 2 — Case library
- Categories: Criminal, Cybercrime, Forensic, Family, Civil, Constitutional, Environmental, Corporate, Motor Vehicle, Custom
- Difficulty labels
- Fictional cases and historical replay entries
- Case cards with category/type/difficulty and evidence/witness counts
- Custom case creation

### Phase 3 — Detailed case files
- Timeline
- Exhibit/evidence inventory
- Witness/source notes
- Illustrative scene images
- Historical case outcome notes and source links for real-case replays
- Custom case fields for timeline, exhibits and witness notes

### Phase 4 — Smarter AI judge
- Structured round evaluations and final verdict
- Evidence reliability, corroboration, contradiction, relevance and unsupported-claim checks
- Objection rulings with a concise reason
- Criminal-case burden guidance: presumption of innocence; acquit if the record fails to prove guilt beyond reasonable doubt
- Civil/family cases are instructed not to use the criminal standard
- Historical replays are instructed not to invent facts or exhibits and to evaluate the simulation independently

## Historical replay caution
Historical case entries are condensed educational summaries, not complete court records. Source links are provided. Do not treat the case-file summaries as exhaustive evidence. Historical case replay is not legal advice and cannot change real judgments.

## Deferred to December
- Verified BNS/BSA/BNSS and other statute library, section retrieval/citations
- JDBC and MySQL persistence

## Limitations
- Trials remain in-memory; restarting the backend clears active trials.
- Groq requires internet, a valid key, an available model and quota.
- Demo mode is not AI and does not make a real decision.
- The frontend production build could not be verified in this environment because npm package installation timed out; run `npm install` then `npm run build` locally.
