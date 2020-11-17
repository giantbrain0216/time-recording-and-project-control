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

### Additional Feature 1.8 *Ergänzung der vorhandenen Kompetenzen für Mitarbeiter und Projekte*

> Als *Mitarbeiter* möchte ich *zu einem Projekt oder Mitarbeiter Kompetenzen hinzufügen*, um *zu sehen welche Kompetenzen für Projekte fehlen*.

- Aufwandsschätzung: [S]
- Akzeptanztests(intern):
  - Kompetenzen können von Mitarbeiter/Projekt hinzugefügt oder entfernt werden
  - Es kann überprüft werden, ob alle Kompetenzen für ein Projekt abgedeckt werden
  - Kompetenzen können zum System hinzugefügt werden

### Additional Feature 1.9 *Auflistung benötigter Mitarbeiter im Unternehmen an Hand der benötigten Kompetenzen*

> Als *Mitarbeiter* möchte ich *auf sehen können, welche Mitarbeiter in der Firma wie sehr gebraucht werden*, um *alle Projekte bewältigen zu können*.

- Aufwandsschätzung: [M]
- Akzeptanztests(intern):
  - Es werden die zusätzlich gebrauchten Mitarbeiter mit einem Bedarf Index angezeigt
  - Benötigen viele Projekte mit geringem Aufwand oder einzelne Projekte mit hohem Aufwand eine Kompetenz, ist der Bedarf hieran hoch.

### Additional Feature 1.10 *Auswertung von Projektdaten*

> Als *Nutzer* möchte ich *sehen, wie ich die Mitarbeiter am besten einsetzen kann*, um *Kosten zu sparen und alle Projekte zu bewältigen*.

- Aufwandsschätzung: [XL]
- Akzeptanztests(intern):
  - Es wird angezeigt ob alle Projekte bewältigt werden und wie man am besten die Mitarbeiter einsetzen kann
  - Projektkosten je nachdem wie man Mitarbeiter einsetzt werden angezeigt
  - Es wird angezeigt ob es zu Überstunden kommt
  - Ausgelastete bzw. nicht ausgelastete Mitarbeiter werden angezeigt und ein Verbesserungsvorschlag wird gemacht

### Additional Feature 1.11 *Erfassung der Aufwände aus einem Ticketsystem*

> Als *Mitarbeiter* möchte ich *meine bearbeiteten Tickets ins System importieren*, um *sie direkt in meinem Aufwand zu erfassen*.

- Aufwandsschätzung: [L]
- Akzeptanztests(intern):
  - Tickets können aus einem Ticketsystem importiert werden
  - Der Aufwand für das Ticket wird automatisch in das System erfasst





## Epic 2 *User Interface*

> Als *Benutzer* möchte ich *eine Liste der Mitarbeiter, Projekte, Charts etc. sehen*, um *mir einen besseren Überblick zu erschaffen*.

Ausführliche Beschreibung: Der Benutzer soll in der Lage sein, Daten zu Projekten, Kunden & Mitarbeiter einsehen zu können. Diese werden in einer Liste untereinander angezeigt, mit der Möglichkeit diese nach einem Attribut sortieren oder filtern zu können. Zu jedem Kunden, Projekt und Mitarbeiter sollte es ein Vorschaufenster geben, wo man alle Informationen auf einem Blick sehen kann. Zusätzlich gibt es Charts, die verschiedene Informationen visualisieren.

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

### Additional Feature 2.4 *Visualisierung der Projektauslastung in Form von Charts*

> Als *Mitarbeiter* möchte ich *die Projektauslastungen visualisiert bekommen*, um *besser zu verstehen wo Resourcen noch gebraucht werden und wo nicht*.

- Aufwandsschätzung: [S]
- Akzeptanztests(intern):
  - Die dargestellten Informationen entsprechen genau den Daten aus der Datenbank.
  - Die Berechnungen für die verschiedenen Charts wurden richtig berechnet.