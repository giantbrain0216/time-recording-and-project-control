# Sprint Report 1

In diesem Sprint wurden die bis zum [Meilenstein 2](SoPra WS20 / team12) erledigten User Stories abgearbeitet.

## Verbesserte Dokumente

#### 	1. Verbesserung des Entwurfs:

##### 		- Grundlegender Aufbau der App beschrieben

##### 		- Anpassung des System Context Diagramms

##### 		- Verbesserung des UML Diagramms

##### 		- API Package hinzugefügt

##### 		- Beschreibung der Funktionalität der Views hinzugefügt

#### 	2. README

##### 		- Installation und Ausführung der APP wurden beschrieben



## Beschreibung der wichtigsten implementierten Features

#### 1. Erfassung von Kunden

##### 	- Über einen Pop-Up Fenster kann man alle Daten zu einem Kunde eingeben. Gleich nachdem hinzufügen ist der Kunde in der Kundenliste sichtbar. Neben dem Kunde erscheint ein Button zum bearbeiten. Hiermit kann man Daten nachträglich ändern

#### 2. Einsicht aller Kunden

##### 	- Alle Kunden sind in einer Liste sichtbar. Wenn man auf dem Namen der Kunden clickt, erscheinen neben der Liste die Details vom Kunde

#### 3. Erfassung von Projekten

##### 	- Über einen Pop-Up Fenster kann man alle Daten zu einem Projekt eingeben. Man kann über einem Dropdown das Projekt einem Kunden zuteilen. Gleich nachdem hinzufügen ist das Projekt in der Projektenliste sichtbar. Neben dem Projekt erscheint ein Button zum bearbeiten. Hiermit kann man Daten nachträglich ändern.

#### 4. Einsicht aller Projekte

##### 	- Alle Projekte sind in einer Liste sichtbar. Wenn man auf dem Namen des Projekts clickt, erscheinen neben der Liste die Details vom Projekt. Der insgesamt geplante Aufwand im Verhältniss zum getätigten Aufwand wird auf der Home Seite in Prozent ausgedrückt. Die Liste der Projekte kann nach einem Kunden gefiltert werden.

#### 5. Erfassung von Mitarbeitern und deren verfügbaren Arbeitszeit.

##### 	- Über einen Pop-Up Fenster kann man alle Daten zu einem Mitarbeiter eingeben. Gleich nachdem hinzufügen ist der Mitarbeiter in der Mitarbeiterliste sichtbar. In der Liste wird auch die verfügbare Wochenarbetiszeit angezeigt.

#### 6. Einsicht aller Mitarbeiter.

##### 	- Alle Mitarbeiter sind in einer Liste sichtbar. Wenn man auf dem Namen des Mitarbeiters clickt, erscheinen neben der Liste die Details vom Mitarbeiter. Hier kann man die verfügbare, sowie die geplante Arbeitszeit sehen. Zusätzlich werden hier die Anzahl der geleisteten Studen in der letzten Woche in einem Diagramm angezeigt.

#### 7. Zuordnung von Mitarbeitern zu Projekten.

##### 	- Mitarbeiter kann man zu Projekten zuordnen auf der Projektenliste. Zu jedem Projekt gibt es einen Knopf Assign, womit man neue Mitarbeiter hinzufügen kann oder bereits hinzugefügte entfernen.

#### 8. Einzelne Erfassung von geleisteten Aufwänden pro Mitarbeiter.

##### 	- Auf der Seite "Time Registration" kann ein Mitarbeiter seine Zeit erfassen. Wenn man einen Mitarbeiter aussucht, wird eine Liste mit den Erfassungen von dem Mitarbeiter angezeigt. Nach der Zeiterfassung wird der geleistete Aufwand für das jeweilige Projekt steigen. Beim löschen einer Aufwanderfassung wird der Aufwand für das Projekt kleiner.

#### 9. Aufsetzen der Java Umgebung

##### 	- Die Rest-API ist für alle bis jetzt vohandenen Entitäten implementiert worden. Für jede Entität werden mindestnens die 5 grundlegenen CRUD Operationen angeboten

#### 10.  Aufsetzen der Datenbank

##### 	- Die Datenbank besitzt für alle bis jetzt vorhandenen Entitäten eine Tabelle, in der man die Attribute der Entitäten speichern kann

#### 11. Vollständige GUI mit Menü und Searchbar

##### 	- Die GUI ist fast vollständig bis auf einem Search-Input Feld

#### 12. Additional Feature: Visualisierung der Projektauslastung in Form von Charts

##### 	- Visualisierungen von der Auslastung von Projekten und Mitarbeitern gibts sowohl in der Projektenliste, als auch in der Home View

##### 	- Berechnung Projekte die mehr Mitarbeiter brauchen: Es wird die noch zu verrichtende Arbeit durch die Zeit bis zu Deadline geteilt, damit hat man die Anzahl der Stunden pro Woche. Wenn zusammen addiert alle Zuteilungen kleiner sind als die benötigte Arbeitskraft pro Woche, wird das zu den Projekten gezählt, bei denen Mitarbeiter fehlen.

##### 	- Berechnung überlastete Mitarbeiter: Es werden die Stunden gezählt, die der Mitarbeiter in den letzten 30 Tagen gearbeitet hat. Sind diese mehr wie 110% von seiner monatlichen geplanten Arbeitszeit, zählt dieser als überlastet

##### 	- Berechnung nicht belastete Mitarbeiter: Es werden die Stunden gezählt, die der Mitarbeiter in den letzten 30 Tagen gearbeitet hat. Sind diese weniger als 80% von seiner monatlichen geplanten Arbeitszeit, zählt dieser als unterlastet



### Noch vohandene Probleme bei den Features:

#### 1. Competences und Beziehung Kunde-Projekt müssen als extra Tabelle erstellt werden. Die Implementierung als String ist nicht optimal.

#### 2. Logische Verknüpfungen zwischen den Klassen nicht immer vorhanden

##### 	- Wenn man ein Projekt löscht, müsste man erstmal überprüfen ob das Projekt noch läuft und falls nicht, beim Löschen muss man die Zuteilungen von Mitarbeiteirn mitlöschen.

##### 	- Wenn man ein Mitarbeiter löscht, müsste er von allen Projekten entfernt werden.

##### 	- Mitarbeiter kann mehrmals einem Projekt zugeteilt werden

#### 3. Kunde soll eine Anschrift besitzen

#### 4.  Projekt soll ein Attribut "status" besitzen, bis jetzt wird der Status anhand der Deadline dynamisch berechnet

#### 5. Searchbar um etwas zu suchen fehlt



## Tests/Testprotokolle/Nachweis der Testabdeckung

**Die Testabdeckung für das Backend:**  
- finden Sie [hier](coverage/index.html)  
All Klassen sind abgedeckt und 93% der methoden sind abgedeckt. Somit kann man behaupten,
dass die Test alle nicht-trivialen Methoden abdecken und diese auch funktionieren.




*Testabdeckung für das Frontend*  
- Im Frontend wurden alle Methoden manuell getesten. Es wurde für jedes implementierte Feature
  mehrere Anwendungen getesten.  
  - Objekte(Kunde, Mitarbeiter, Zeiterfassung, Projekte) hinzufügen:  
    - Falsche Eingaben: Werden dank der Validierungen nicht akzeptiert und sind somit nicht möglich.  
    - Richtige Eingaben: Objekt wird in der Datenbank gespeichert und im Frontend ausgegeben.
  - Objekte bearbeiten(Mitarbeiter, Kunden, Projekte):  
      - Falsche Eingaben: Werden dank der Validierungen nicht akzeptiert und sind somit nicht möglich.
      - Richtige Eingaben: Änderung des Objekts wird in der Datenbank gespeichert und im Frontend ausgegeben.  
  - Detailierte Ansicht der Objekte(Mitarbeiter, Kunden, Projekte):  
      - Wird immer richtig Angezeigt wenn man auf den Namen des Objekts in der Objektliste klickt.  
  - Mitarbeiter einem Projekt zuweiseen:  
    - Die verfügbare Arbeitszeit des Mitarbeiters verringert sich und die Projektauslastung wird auch angepasst.  
  - Zeiterfassung eines Mitarbeiteers:  
    - Die aufgewandte Zeit des Mitarbeiters wird in seiner detailierten Ansicht angezeigt in Form einer Chart.