# Database Query Language - Proiect Scala

## Descriere
Acest proiect implementează un motor minimal de baze de date bazat pe principii de **programare funcțională** în Scala. Sistemul permite definirea tabelelor, gestionarea bazelor de date și executarea de interogări complexe printr-un DSL (*Domain Specific Language*) personalizat, utilizând structuri de date imuabile.

## Arhitectura Proiectului

### 1. Modelul de Date
Nucleul sistemului este definit în `Table.scala` și `Database.scala`:
* **Row**: Reprezentat ca un `Map[String, String]` unde cheile sunt numele coloanelor.
* **Tabular**: O listă de rânduri (`List[Row]`).
* **Table**: O clasă care încapsulează numele tabelului și datele efective. Oferă metode utilitare precum extragerea header-ului (`header`).
* **Database**: O colecție de obiecte de tip `Table`, oferind un context global pentru interogări.

### 2. Limbajul de Filtrare (FilterCond)
Sistemul suportă condiții de filtrare complexe prin intermediul unor structuri de date recursive definite în `FilterCond.scala`:
* **Field**: Filtrare bazată pe valoarea unei coloane specifice.
* **Not**: Inversarea unei condiții logice.
* **Any**: Operatorul logic **OR** (adevărat dacă oricare condiție din listă este îndeplinită).
* **All**: Operatorul logic **AND** (adevărat dacă toate condițiile sunt îndeplinite).
* **Compound**: Permite combinarea mai multor condiții logice.

### 3. Execuția Interogărilor (Queries)
Modulul `Queries.scala` conține logica de procesare a datelor. Acesta implementează operații standard de tip SQL precum:
* **Select**: Proiecția anumitor coloane.
* **Filter**: Selectarea rândurilor care respectă anumite `FilterCond`.
* **Join**: Combinarea tabelelor pe baza unor chei comune.

## Tehnologii Utilizate
* **Scala 3.3.1**: Utilizarea funcțiilor de ordin superior, imuabilității și pattern matching-ului.
* **SBT (Scala Build Tool)**: Pentru gestionarea dependențelor și automatizarea build-ului.
* **ScalaTest**: Suită completă de teste unitare pentru validarea corectitudinii tabelelor, filtrelor și a logicii de bază a bazei de date.

## Instalare și Rulare

### Precerințe
* Java SDK (versiunea 8 sau mai recentă).
* SBT instalat.

### Compilare și Testare
Pentru a compila proiectul:
```bash
sbt compile
