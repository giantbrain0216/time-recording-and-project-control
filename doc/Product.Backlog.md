# Product Backlog

Hier werden **alle** Anforderungen in Form von **User Stories** geordnet aufgelistet.

## Epic 1 *Datenverwaltungssystem*

> Als *Benutzer* möchte ich *Daten in das System eintragen und modifizieren*, um *diese besser verwalten zu können*.

Ausführliche Beschreibung: Der Benutzer soll in der Lage sein, Daten zu Projekten, Kunden & Mitarbeiter in das System einzutragen und nachträglich verändern zu können. Zudem sollte es möglich sein, Mitarbeiter zu Projekten vollständig oder teilweise zuzuordnen, sowie auch von Projekten zu entfernen. Außerdem sollte der Benutzer die Möglichkeit haben, geleistete Leistungen pro Mitarbeiter in das System einzutragen. Zudem sollte das System den Export der Aufwände eines Projekts ermöglichen. Die Web-App sollte dem Nutzer auch Optimierungsvorschläge basierend auf den benötigten Kompetenzen in Projekten machen. Das System kommuniziert zusätzlich mit einem Ticketsystem, um die geleistete Arbeit direkt zu erfassen.

### Feature 1.1 *Kunde hinzufügen*

> Als *Nutzer* möchte ich *Kunden in das System eintragen*, um *sie besser zu verwalten*.

- Aufwandsschätzung: [XS]
- Akzeptanztests:
  - Ein Kunde konnte angelegt werden.
  - Der Kunde ist in der Liste aller Kunden sichtbar.
  - Der Kunde kann nachträglich bearbeitet werden.

### Feature 1.2 *Projekt hinzufügen*

> Als *Nutzer* möchte ich *ein Projekt in das System anlegen*, um *die Mitarbeiter besser auf die Proejkte zu verteilen*.

- Aufwandsschätzung: [XS]
- Akzeptanztests:
  - Das Projekt konnte angelegt werden.
  - Das Projekt ist einem Kunden zugeordnet.
  - Der veranschlagte Aufwand ist erfasst.
  - Es wurde noch kein Aufwand geleistet.
  - Das Projekt ist in der Übersicht über alle Projekte vorhanden.

### Feature 1.3 *Mitarbeiter und ihre verfügbare Arbeitszeit hinzufügen*

> Als *Nutzer* möchte ich *einen Mitarbeiter und seine Arbeitszeit in das System anlegen*, um *ihn optimal einsetzen zu können*.

- Aufwandsschätzung: [XS]
- Akzeptanztests:
  - Ein Mitarbeiter konnte angelegt werden.
  - Der Mitarbeiter ist in der Liste aller Mitarbeiter sichtbar.
  - Die verfügbare Wochenarbeitszeit ist ersichtlich.

### Feature 1.4 *Mitarbeiter einem Projekt zuteilen oder entfernen*

> Als *Nutzer* möchte ich *einen Mitarbeiter einem Projekt zuordnen oder entfernen*, um *eine bessere Übersicht über die verschiedenen Projekten und Mitarbeiter zu haben.* .

- Aufwandsschätzung: [S]
- Akzeptanztests:
  - Ein weiterer Mitarbeiter ist dem Projekt zugeordnet.
  - Der Mitarbeiter kann komplett oder teilweise dem Projekt zugeordnet werden.
  - Ein Mitarbeiter kann aus einem Projekt entfernt werden.
  - Der Mitarbeiter wird in der Liste der Mitarbeiter, die dem Projekt zugewiesen sind, angezeigt.
  - Die Gesamtkapazität an verfügbarer Arbeitszeit ist um den zugewiesenen Umfang gestiegen.

### Feature 1.5 *Erfassung von geleisteten Aufwänden pro Mitarbeiter*

> Als *Mitarbeiter* möchte ich *meine geleistete Arbeit erfassen*, um *den Aufwand tracken zu können*.

- Aufwandsschätzung: [M]
- Akzeptanztests:
  - Alle relevanten Angaben zum Aufwand konnten erfasst werden:
    - Kurzbeschreibung der geleisteten Arbeit
    - Datum
    - Der geleistete Aufwand
    - Der entsprechende Mitarbeiter
  - Der insgesamt geleistete Aufwand ist entsprechend der erfassten Aufwände gestiegen.

### Feature 1.6 *Gesammelte Erfassung von geleisteten Aufwänden pro Mitarbeiter*

> Als *Nutzer* möchte ich *gesammelte geleistete Arbeit ins System importieren*, um *alte Erfassungen in das neue System hinzuzufügen*.

- Aufwandsschätzung: [M]
- Akzeptanztests:
  - Aufwände, die in einer CSV gesammelt wurden, konnten in die Anwendung übernommen werden.
  - Es wurden alle Informationen gespeichert, die auch bei der einzelnen Erfassung vorhanden sind.

### Additional Feature 1.7 *Export der Aufwände für eine Rechnung*

> Als *Mitarbeiter* möchte ich *eine Rechnung für ein Kunde oder das Controlling erstellen*, um *die Kosten des Projekts darzustellen*.

- Aufwandsschätzung: [S]
- Akzeptanztests:
  - Eine CSV-Datei für den gewünschten Kunden und Zeitraum liegt vor.
  - Die CSV-Datei enthält:
   - Kurzbeschreibung der geleisteten Arbeit
   - Datum
   - Der geleistete Aufwand
   - Der entsprechende Mitarbeiter



## Epic 2 *User Interface*

> Als *Benutzer* möchte ich *eine Liste der Mitarbeiter, Projekte etc. sehen*, um *mir einen besseren Überblick zu erschaffen*.

Ausführliche Beschreibung: Der Benutzer soll in der Lage sein, Daten zu Projekten, Kunden & Mitarbeiter einsehen zu können. Diese werden in einer Liste untereinander angezeigt, mit der Möglichkeit diese nach einem Attribut sortieren oder filtern zu können. Zu jedem Kunden, Projekt und Mitarbeiter sollte es ein Vorschaufenster geben, wo man alle Informationen auf einem Blick sehen kann.

### Feature 2.1 *Einsicht aller Kunden*

> Als *Nutzer* möchte ich *alle Kunden einsehen*, um *mir einen besseren Überblick zu verschaffen*.

- Aufwandsschätzung: [S]
- Akzeptanztests:
  - Alle erfassten Kunden können auf einen Blick eingesehen werden.
  - Die Detailansicht zu einem Kunden kann geöffnet werden. Es sind alle Kundendaten und die zugewiesenen Projekte einsehbar.

### Feature 2.2 *Einsicht aller Projekte.*

> Als *Nutzer* möchte ich *alle Projekte einsehen*, um *mir einen besseren Überblick zu verschaffen und um einschätzen zu können, ob Resourcen noch benötigt werden*.

- Aufwandsschätzung: [S]
- Akzeptanztests:
  - Alle erfassten Projekte können auf einen Blick eingesehen werden.
  - Der geplante Aufwand sowie der geleistete Aufwand werden pro Projekt angezeigt.
  - Der Status des Projekts wird angezeigt.
  - Der insgesamt geplante Aufwand über alle Projekte wird angezeigt.
  - Die Projektliste kann speziell für einen Kunden angezeigt werden.

### Feature 2.3 *Einsicht aller Mitarbeiter.*

> Als *Nutzer* möchte ich *alle Mitarbeiter einsehen*, um *sie optimal einsetzen zu können*.

- Aufwandsschätzung: [S]
- Akzeptanztests:
  - Alle Mitarbeiter können auf einen Blick eingesehen werden.
  - Sowohl die verfügbare Arbeitszeit, als auch die auf Projekte geplante Arbeitszeit ist ersichtlich.
  - Die insgesamt verfügbare Arbeitszeit über alle Mitarbeiter wird angezeigt.






[XS, S, M, L, XL]
#### Implementable Story 1.1.1 *Titel*

> Als *Rolle* möchte ich *Ziel/Wunsch*, um *Nutzen*.

- Aufwandsschätzung: [0-100] Story Points
- Akzeptanztests:
  - *TODO (Beschreibung von Testfällen die das erwartete Verhalten der gesamten Implementable Story überprüfen.)*
  - *TODO*
  - *TODO*

##### Task 1.1.1.1 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.1.1.2 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.1.1.3 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

#### Implementable Story 1.1.2 *Titel*

> Als *Rolle* möchte ich *Ziel/Wunsch*, um *Nutzen*.

- Aufwandsschätzung: [0-100] Story Points
- Akzeptanztests:
  - *TODO (Beschreibung von Testfällen die das erwartete Verhalten der gesamten Implementable Story überprüfen.)*
  - *TODO*
  - *TODO*

##### Task 1.1.2.1 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.1.2.2 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.1.2.3 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

### Feature 1.2 *Titel*

> Als *Rolle* möchte ich *Ziel/Wunsch*, um *Nutzen*.

- Aufwandsschätzung: [XS, S, M, L, XL]
- Akzeptanztests:
  - *TODO (Beschreibung von Testfällen die das erwartete Verhalten des gesamten Features überprüfen.)*
  - *TODO*
  - *TODO*

#### Implementable Story 1.2.1 *Titel*

> Als *Rolle* möchte ich *Ziel/Wunsch*, um *Nutzen*.

- Aufwandsschätzung: [0-100] Story Points
- Akzeptanztests:
  - *TODO (Beschreibung von Testfällen die das erwartete Verhalten der gesamten Implementable Story überprüfen.)*
  - *TODO*
  - *TODO*

##### Task 1.2.1.1 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.2.1.2 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.2.1.3 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

#### Implementable Story 1.2.2 *Titel*

> Als *Rolle* möchte ich *Ziel/Wunsch*, um *Nutzen*.

- Aufwandsschätzung: [0-100] Story Points
- Akzeptanztests:
  - *TODO (Beschreibung von Testfällen die das erwartete Verhalten der gesamten Implementable Story überprüfen.)*
  - *TODO*
  - *TODO*

##### Task 1.2.2.1 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.2.2.2 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

##### Task 1.2.2.3 *Titel/Beschreibung*

- Aufwandsschätzung: [0-24] Stunden

## Epic 2 *Titel*

> Als *Rolle* möchte ich *Ziel/Wunsch*, um *Nutzen*.

*Wie bei Epic 1.*