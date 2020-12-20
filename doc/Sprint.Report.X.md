# Sprint Report [1]

In diesem Sprint wurden die bis zum [Meilenstein 1](meilenstein-url) erledigten User Stories abgearbeitet.

## Verbesserte Dokumente [optional]

[Entwurf](Entwurf.md) wurde überarbeiter.  
- Aus dem System-context-diagram wurde die Datenverwalungs-API entfernt, da sie zu der Web App gehört.  
- Das UML Klassen Diagram wurde um die 
Domänen Assignment, TimeRegistration und Competences erweitert. Die Beziehungen wurden überbearbeitet
und die Projekte werden jetzt nur in Clients gespeichert und nicht auch noch umgekehrt.  
- Die View beschreibungen wurden etwas ausführlicher beschrieben.  
- Es wurde das package Controllers zu den Top-level packages hinzugefügt. 


## Beschreibung der wichtigsten implementierten Features

- **Kunde hinzufügen:** Es ist möglich einen Kunden in die WebApp hinzuzufügen. Dies geschieht über
    einen Butten in der **Client View**. Dieser öffnet ein Pop up Fenster, welches Eingabefelder
    hat um einen Kunden anlegen zu können. Dieser wird dann in der Datenbank gespeichert.  
- **Einsicht aller Kunden:** In der **Client View** werden automatisch alle Kunden angezeigt, die in 
    der Datenbank geespeichert sind. Mit einem Mausklick auf dem Namen des Kunden, erscheint noch 
    eine detailierte Ansicht des ausgewählten **Kunden**  
- **Löschen eines Kunden:** In der **Client View** gibt es neben jeden Kunden einen **delete** Button
    der es ermöglicht einen Kunden zu löschen. Dieser wird dann aus der Datenbank entfernt.  
- **Bearbeiten eines Kunden:** In der **Client View** gibt es neben jeden Kunden einen **edit** Button,
    der es ermöglicht die Kundendaten nachträglich zu ändern. Die Änderungen werden in der Datenbank gespeichert.
- **Einfügen eines Mitarbeiters:** In der **Employee View** ist es möglich einen Kunden mit seiner verfügbaren
    Arbeitszeit hinzuzufügen. Dies geschieht über einen Butten in der **Employee View**. Dieser öffnet ein Pop up Fenster,
    welches Eingabefelder
    hat um einen Mitarbeiter anlegen zu können. Dieser wird dann in der Datenbank gespeichert.  
- **Einsicht aller Mitarbeiter:** In der **Employee View** werden automatisch alle Mitarbeiter angezeigt. Mit einem Mausklick
    auf eine Mitarbeiternamen, scheint eine detailierte Ansicht des Mitarbeiters, sowie eine Ansicht um die Zuteilung der
    Projekte zu bearbeiten.
    siehe Feature **Mitarbeitern einem Projekt zuteilen**.  
- **Mitarbeiter bearbeiten:** In der **Employee View** ist es möglich einen Mitarbeiter im nachhinein zu bearbeiten. Dazu
    gibt es einen Button neben jeden Mitarbeiter, der ein Pop up Fenster aufruft mit Eingabefelder die man zum Bearbeiten
    braucht. Die Änderungen werden in der Datenbank gespeichert.
- **Mitarbeiter löschen:** In der **Employee View** ist es möglich einen Mitarbeiter zu löschen. Die geschieht über
    einen lösch Button der neben jeden Mitarbeiter positioniert ist. Der Mitarbeiter existiert danach in der Datenbank
    nicht mehr.  
- **Einsicht aller Projekte:*+ Die Projekte werden automatisch in der **Project View** angezeigt. Die Projekte müssen dafür 
    jedoch in der Datenbank gespeichert sei. Mit einem Mausklick auf den Projektnamen kann man Mitarbeiter dem Projekt zuweisen
    und/oder entfernen. Es wird sowie eine detailierte Ansicht des Projekts angezeigt mit allen Daten des Projekts.
- **Projekt hinzufügen:** In der **Project View** ist es möglich ein Projekt anzulegen. Dies geschieht über einen Button
    der ein Pop up Fenster öffnent, mit gegebenen Eingabefeldern die benötigt werden um die Attribute richtig in der Datenbank 
    zu speichern. Der Kunde wird dann in der Datenbank gespeichert.  
- **Projekt bearbeiten:** In der **Project View** ist es möglich Projekte im nachhinein zu bearbeiten. Dies geschieht über eine Edit Button.
    Dieser öffnet ein Pop up Fenstere, welches es ermöglich die Projektdaten zu ändern. Die Änderungen in der Datenbank abgespeichert.  
- **Zeiterfassung eines Mitarbeiters:** In deer **Time registration View** ist es möglich Arbeitszeiten eines Mitarbeiters,
    welcher schon in der Datenbank gespeichert ist zu erfassen. Die erfasse Zeit wird dann in der detailierten Ansicht des Mitarbeits
    angezeigt in Form von einer Chart, jedoch nur für die letzten 5 Tage.

## Tests/Testprotokolle/Nachweis der Testabdeckung

*TODO: Testprotokolle und Abdeckung hier einfügen/verlinken.*

*TODO: Begründungen, ob die fertigen Features für ihre Komplexität/Relevanz ausreichend getestet wurden (z.B. durch Unit Tests oder manuelle Tests)*