# WebApp zur Aufwandserfassung und Projektsteuerung

> Die Webapp dient zur Aufwanderfassung der Mitarbeiter, um den geleisteten Aufwand besser tracken zu können. Zusätzlich dient die App zur Projektsteuerung, sodass Teamleiter ihre Mitarbeiter optimal einsetzen können.

## Installation und Start

**Run Application**
```
# Install server
- ./mvnw clean install
# Run server
- ./mvnw spring-boot:run
# install dependencies
- npm install
# if you have vulnerabilities
- npm audit fix --force
# then run
- npm run serve
```

## Verwendung der App

_Beschreibung der wichtigsten implementierten Anwendungsfälle_

### Wichtiger Anwendungsfall 1  
- **Kunde hinzufügen:** Es ist möglich einen Kunden in die WebApp hinzuzufügen. Dies geschieht über
  einen Butten in der **Client View**. Dieser öffnet ein Pop up Fenster, welches Eingabefelder
  hat um einen Kunden anlegen zu können. Dieser wird dann in der Datenbank gespeichert.  
    
### Wichtiger Anwendungsfall 2  
- **Löschen eines Kunden:** In der **Client View** gibt es neben jeden Kunden einen **delete** Button
  der es ermöglicht einen Kunden zu löschen. Dieser wird dann aus der Datenbank entfernt.  
  
### Wichtiger Anwendungsfall 3  
- **Bearbeiten eines Kunden:** In der **Client View** gibt es neben jeden Kunden einen **edit** Button,
  der es ermöglicht die Kundendaten nachträglich zu ändern. Die Änderungen werden in der Datenbank gespeichert.  
  
### Wichtiger Anwendungsfall 4  
- **Einfügen eines Mitarbeiters:** In der **Employee View** ist es möglich einen Kunden mit seiner verfügbaren
  Arbeitszeit hinzuzufügen. Dies geschieht über einen Butten in der **Employee View**. Dieser öffnet ein Pop up Fenster,
  welches Eingabefelder
  hat um einen Mitarbeiter anlegen zu können. Dieser wird dann in der Datenbank gespeichert.  
 
  
### Wichtiger Anwendungsfall 5  
- **Mitarbeiter bearbeiten:** In der **Employee View** ist es möglich einen Mitarbeiter im nachhinein zu bearbeiten. Dazu
  gibt es einen Button neben jeden Mitarbeiter, der ein Pop up Fenster aufruft mit Eingabefelder die man zum Bearbeiten
  braucht. Die Änderungen werden in der Datenbank gespeichert.  
  
### Wichtiger Anwendungsfall 6  
- **Mitarbeiter löschen:** In der **Employee View** ist es möglich einen Mitarbeiter zu löschen. Die geschieht über
  einen lösch Button der neben jeden Mitarbeiter positioniert ist. Der Mitarbeiter existiert danach in der Datenbank
  nicht mehr.  

### Wichtiger Anwendungsfall 7  
- **Projekt hinzufügen:** In der **Project View** ist es möglich ein Projekt anzulegen. Dies geschieht über einen Button
  der ein Pop up Fenster öffnent, mit gegebenen Eingabefeldern die benötigt werden um die Attribute richtig in der Datenbank
  zu speichern. Der Kunde wird dann in der Datenbank gespeichert.  
  
### Wichtiger Anwendungsfall 8
- **Projekt bearbeiten:** In der **Project View** ist es möglich Projekte im nachhinein zu bearbeiten. Dies geschieht über eine Edit Button.
  Dieser öffnet ein Pop up Fenstere, welches es ermöglich die Projektdaten zu ändern. Die Änderungen in der Datenbank abgespeichert. 

### Wichtiger Anwendungsfall 9  
- **Project löschen:** In der **Project View** ist es möglich ein Projekt zu löschen. Die geschieht über
  einen lösch Button der neben jedem Projekt zu finden ist. Das Projekt existiert danach in der Datenbank
  nicht mehr.  

### Wichtiger Anwendungsfall 10
- **Einsicht aller Projekte:** Die Projekte werden automatisch in der **Project View** angezeigt. Die Projekte müssen dafür
  jedoch in der Datenbank gespeichert sein. Mit einem Mausklick auf den Button ***Assign*** kann man Mitarbeiter dem Projekt zuweisen
  und/oder entfernen. Es wird sowie eine detailierte Ansicht des Projekts angezeigt mit allen Daten des Projekts. In dieser Ansicht werden die fehlenden Kompetenzen rot angezeigt, während die abgedeckten grün angezeigt werden!

### Wichtiger Anwendungsfall 11
- **Zuweisung von Mitarbeitern zu den Projekten** : In der **Project List View ** befindet sich neben jedem Projekt einen Button ***Assign***. Mit dem kann man Mitarbeiter dem Projekt zuweisen und/oder entfernen.

### Wichtiger Anwendungsfall 11  
- **Einsicht aller Kunden:** In der **Client View** werden automatisch alle Kunden angezeigt, die in
  der Datenbank geespeichert sind. Mit einem Mausklick auf dem Namen des Kunden, erscheint noch
  eine detailierte Ansicht des ausgewählten **Kunden**  


### Wichtiger Anwendungsfall 12
- **Rechnung für einen Kunden erstellen** : In der **Client View** wird vor jedem Kunden einen Button ***Invoice*** stehen. Wenn man auf den drückt, dann taucht ein Pop Up Fenster auf, wo man das Projekt eingeben kann (Searchbar mit autocomplete), den Zeitraum und den Preis/Stunde. Eine CSV Datei wird erst heruntergeladen, wenn man auf accept drückt. Die enthält alle Time Registrations und die Summe.
  
## Wichtiger Anwendungsfall 13
- **Einsicht aller Mitarbeiter:** In der **Employee View** werden automatisch alle Mitarbeiter angezeigt. Mit einem Mausklick
  auf eine Mitarbeiternamen, scheint eine detailierte Ansicht des Mitarbeiters, sowie eine Ansicht um die Zuteilung der
  Projekte zu bearbeiten.
  siehe Feature **Mitarbeitern einem Projekt zuteilen**.  
  
### Wichtiger Anwendungsfall 14
- **Zeiterfassung eines Mitarbeiters:** In der **Time registration View** ist es möglich Arbeitszeiten eines Mitarbeiters,
  welcher schon in der Datenbank gespeichert ist zu erfassen. Die erfasse Zeit wird dann in der detailierten Ansicht des Mitarbeits
  angezeigt in Form von einer Chart, jedoch nur für die letzten 5 Tage.

### Wichtiger Anwendungsfall 15 
- **Zeiterfassung aus CSV Datei** : In der ** Time registration view** gibt es einen Button mit der Beschriftung : *Import Time Registration Fron CSV File*. Wenn man darauf klickt, dann landet man in einer neuen View. Da steht eine Beispieltabelle, die dem anzunehmenden Format zeigt. Man kann die CSV Datei hochladen kann und dann werden entprechend die Time Registration in in einer Preview Tabelle angezeigt, wo man die löschen bzw. undo kann und nach dem Drücken auf Submit Button werden die jeweiligen Time Registrations in der Datenbank gespeichert.