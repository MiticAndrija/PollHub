# PollHub

PollHub je studentska full-stack aplikacija za kreiranje anketa, glasanje i pregled rezultata. Korisnički interfejs je na srpskom jeziku.

## Tehnologije

- Backend: Java 21, Spring Boot 3.5.4, Spring Web, JPA, Security, JWT, PostgreSQL, Validation
- Frontend: React, Vite, React Router, Axios, react-qr-code
- Testovi: JUnit 5, Mockito, Spring Boot Test, MockMvc

## Funkcionalnosti i uloge

`USER` može da se registruje/prijavi, pregleda i pretražuje ankete, kreira ankete sa kategorijom i rokom, upravlja svojim anketama, glasa jednom i pregleda rezultate. `ADMIN` dodatno upravlja korisnicima, svim anketama i kategorijama i vidi statistiku sistema.

Pored CRUD funkcija aplikacija proverava pripadnost ankete, sprečava duplo glasanje i glasanje nakon isteka, računa rezultate i procente iz glasova, podržava filtriranje i sortiranje po popularnosti i prikazuje QR kod za deljenje.

## Struktura

- `backend` — Spring Boot REST API, domen, repozitorijumi, servisi, bezbednost i testovi
- `frontend` — React stranice, komponente, auth kontekst i API servisi

## PostgreSQL i pokretanje

Napraviti bazu `pollhub`. Podrazumevani parametri su `jdbc:postgresql://localhost:5432/pollhub`, korisnik `postgres` i lozinka `postgres`. Mogu se promeniti promenljivama `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` i `JWT_SECRET`. Hibernate kreira/ažurira potrebne tabele.

```powershell
cd backend
mvn spring-boot:run
```

```powershell
cd frontend
npm install
npm run dev
```

Frontend je na `http://localhost:5173`, a backend na `http://localhost:8080`. Druga backend adresa se podešava kroz `VITE_API_BASE_URL`.

## Važni REST endpointi

- `POST /api/auth/register`, `POST /login`
- `GET/POST /api/polls`, `GET/PUT/DELETE /api/polls/{id}`
- `GET /api/polls/mine`, `PATCH /api/polls/{id}/close`
- `POST /api/polls/{id}/votes`, `GET /api/polls/{id}/results`
- `GET /api/categories`; izmene kategorija su dostupne administratoru
- `/api/admin/users`, `/api/admin/polls`, `/api/admin/statistics`

Uspešna prijava vraća JWT. Frontend ga čuva lokalno i Axios ga šalje kao `Authorization: Bearer <token>`. API je stateless, lozinke su BCrypt heširane, a administratorske rute zahtevaju `ROLE_ADMIN`. Za dodelu administratorske uloge postojećem korisniku potrebno je dodati `ROLE_ADMIN` u tabelu `user_roles`.

## Testovi i build

```powershell
cd backend
mvn clean test

cd ../frontend
npm run build
```

Backend testovi koriste H2 bazu u memoriji, tako da PostgreSQL nije potreban za testiranje.
