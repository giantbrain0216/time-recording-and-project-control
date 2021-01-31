# WebApp zur Aufwandserfassung und Projektsteuerung

> Die Webapp dient zur Aufwanderfassung der Mitarbeiter, um den geleisteten Aufwand besser tracken zu können. Zusätzlich dient die App zur Projektsteuerung, sodass Teamleiter ihre Mitarbeiter optimal einsetzen können.

## Installation und Start

**Run de.unistuttgart.sopra.Application**
```
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

**TODO:** Beschreibung der wichtigsten implementierten Anwendungsfälle

### Wichtiger Anwendungsfall 1  
- **Kunde hinzufügen:** Es ist möglich einen Kunden in die WebApp hinzuzufügen. Dies geschieht über
  einen Butten in der **Client View**. Dieser öffnet ein Pop up Fenster, welches Eingabefelder
  hat um einen Kunden anlegen zu können. Dieser wird dann in der Datenbank gespeichert.  
  
### Wichtiger Anwendungsfall 2  
- **Einsicht aller Kunden:** In der **Client View** werden automatisch alle Kunden angezeigt, die in
  der Datenbank geespeichert sind. Mit einem Mausklick auf dem Namen des Kunden, erscheint noch
  eine detailierte Ansicht des ausgewählten **Kunden**  
  
### Wichtiger Anwendungsfall 3  
- **Löschen eines Kunden:** In der **Client View** gibt es neben jeden Kunden einen **delete** Button
  der es ermöglicht einen Kunden zu löschen. Dieser wird dann aus der Datenbank entfernt.  
  
## Wichtiger Anwendungsfall 3  
- **Bearbeiten eines Kunden:** In der **Client View** gibt es neben jeden Kunden einen **edit** Button,
  der es ermöglicht die Kundendaten nachträglich zu ändern. Die Änderungen werden in der Datenbank gespeichert.  
  
## Wichtiger Anwendungsfall 4  
- **Einfügen eines Mitarbeiters:** In der **Employee View** ist es möglich einen Kunden mit seiner verfügbaren
  Arbeitszeit hinzuzufügen. Dies geschieht über einen Butten in der **Employee View**. Dieser öffnet ein Pop up Fenster,
  welches Eingabefelder
  hat um einen Mitarbeiter anlegen zu können. Dieser wird dann in der Datenbank gespeichert.  
  
## Wichtiger Anwendungsfall 5  
- **Einsicht aller Mitarbeiter:** In der **Employee View** werden automatisch alle Mitarbeiter angezeigt. Mit einem Mausklick
  auf eine Mitarbeiternamen, scheint eine detailierte Ansicht des Mitarbeiters, sowie eine Ansicht um die Zuteilung der
  Projekte zu bearbeiten.
  siehe Feature **Mitarbeitern einem Projekt zuteilen**.  
  
## Wichtiger Anwendungsfall 6  
- **Mitarbeiter bearbeiten:** In der **Employee View** ist es möglich einen Mitarbeiter im nachhinein zu bearbeiten. Dazu
  gibt es einen Button neben jeden Mitarbeiter, der ein Pop up Fenster aufruft mit Eingabefelder die man zum Bearbeiten
  braucht. Die Änderungen werden in der Datenbank gespeichert.  
  
## Wichtiger Anwendungsfall 7  
- **Mitarbeiter löschen:** In der **Employee View** ist es möglich einen Mitarbeiter zu löschen. Die geschieht über
  einen lösch Button der neben jeden Mitarbeiter positioniert ist. Der Mitarbeiter existiert danach in der Datenbank
  nicht mehr.  
  
## Wichtiger Anwendungsfall 8
- **Einsicht aller Projekte:** Die Projekte werden automatisch in der **Project View** angezeigt. Die Projekte müssen dafür
  jedoch in der Datenbank gespeichert sei. Mit einem Mausklick auf den Projektnamen kann man Mitarbeiter dem Projekt zuweisen
  und/oder entfernen. Es wird sowie eine detailierte Ansicht des Projekts angezeigt mit allen Daten des Projekts.  
  
## Wichtiger Anwendungsfall 9  
- **Projekt hinzufügen:** In der **Project View** ist es möglich ein Projekt anzulegen. Dies geschieht über einen Button
  der ein Pop up Fenster öffnent, mit gegebenen Eingabefeldern die benötigt werden um die Attribute richtig in der Datenbank
  zu speichern. Der Kunde wird dann in der Datenbank gespeichert.  
  
## Wichtiger Anwendungsfall 10
- **Projekt bearbeiten:** In der **Project View** ist es möglich Projekte im nachhinein zu bearbeiten. Dies geschieht über eine Edit Button.
  Dieser öffnet ein Pop up Fenstere, welches es ermöglich die Projektdaten zu ändern. Die Änderungen in der Datenbank abgespeichert.  
  
## Wichtiger Anwendungsfall 11
- **Zeiterfassung eines Mitarbeiters:** In deer **Time registration View** ist es möglich Arbeitszeiten eines Mitarbeiters,
  welcher schon in der Datenbank gespeichert ist zu erfassen. Die erfasse Zeit wird dann in der detailierten Ansicht des Mitarbeits
  angezeigt in Form von einer Chart, jedoch nur für die letzten 5 Tage.