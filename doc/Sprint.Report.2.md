# Sprint Report [2]

In diesem Sprint wurden die bis zum [Meilenstein 3](sopra-ws20/team12%"M3") erledigten User Stories abgearbeitet.



## Beschreibung der wichtigsten implementierten Features

#### 1. Die Diagramme auf der Dashboard wurden verändert

##### 	- Anteil der fertigen Projekte und der Projekt Progress beziehen sich jetzt nur auf das jetztige Jahr, damit sie nicht mehr immer hohe Werte haben

##### 	- Es gibt ein Diagramm mit den Einnahmen der Firma dieses Jahr und im vergangenen Jahr. Diese werden anhand der TimeRegistrations und dem Preis für das jeweilige Projekt errechnet. 

#### 2. Fehlende Kompetenzen in der Firma werden angezeigt

#####  -  Auf der Dashboard gibt es eine Liste mit fehlenden Kompetenzen im Unternehmen. Wenn nur ein Projekt eine gewisse Kompetenz braucht, wird das als Low Priority gewertet, für 2 oder mehr wird die Kompetenz als High Priority gewertet.

#### 3. Auf der Dashboard gibt es einen News Feed

##### - Nachrichten werden dynamisch von  "The New York Times" über die öffentliche API gefetchet. Die Nachrichten kommen aus vier Kategorien. Über dem Knopf gelangt man zum Artikel

#### 4. Wetter Anzeige

##### - Rechts in der Navbar gibt es eine kleine Wetteranzeige. Beim draufklicken wird eine ausführliche Wettervorhersage angezeigt. Der Standort wird anhand der IP erkannt und die Wetterdaten werden von der Komponente dynamisch gefetcht. Das ganze ist auch animiert.

#### 5. Adresse des Kundens auf einer Karte angezeigt

##### - Wenn man auf einen Kunden klickt, wird in den Details eine Karte von der Adresse angezeigt. Die Lat,Long bekommen wir über eine API von OpenStreetMap, damit schaffen wir es die Addresse vom Kunden recht zuverlässig auf der Karte anzeigen zu können.

#### 6. Searchbar in der Navbar hinzugefügt

##### - Über die Searchbar kann man Kunden, Projekte, Mitarbeiter und TimeRegistrations suchen. Die Suche kann man auch nach mehreren Entitäten filtern. Wenn man nach dem Namen eines Mitarbeiters sucht, werden auch die zugehörigen TimeRegistrations angezeigt. Ansonsten kann man auch nach der Beschreibung einer TimeRegistration suchen. 

#### 7. Eine Rechnung für ein Kunde erstellen

##### - Man kann für einen Kunden eine Rechnung als .csv  Datei exportieren. Dafür muss man ein Projekt des Kundens und eine Zeitspanne für die Rechnung auswählen. Der Preis pro Stunde wird als Attrbut vom Projekt festgehalten. Es wird eine CSV Datei mit allen passenden Timeregistrations und den Preisen exportiert. 

#### 8. CSV Import Aufwände aus eigener Tabelle oder aus einem Ticketsystem

##### - Man kann auf der Seite Time Registrations beide Arten vom Import tätigen. Bei dem Import aus einer eigenen CSV Datei kann man ein Beispiel Template für das benutzte Format runterladen. Nachdem aufwählen der Datei sieht man ein Preview aller Aufwände. Hier kann man sehen welche Timeregistrations valide sind und welche nicht, plus man kann aussuchen welche man importieren will und welche nicht. Das gleiche gilt auch für den Import aus einem Ticketsystem. Hier muss die Datei mit den Issues von Gitlab hochgeladen werden. Dabei muss man eine Spalte mit dem Namen "project id" hinzufügen, wo man die ID des Projekts noch angibt.

#### 9. Kompetenzen als Entität

##### - Die Kompetenzen sind jetzt als Entität definiert. Für die Verknüpfungen zwischen Kunden & Kompetenzen und Projekte & Kompetenzen gibt es extra Entitäten und Rest Schnittstellen. Die Kompetenzen kann man in der GUI über eine Searchbar aussuchen, diese werden dann als Checkboxes angezeigt. Man kann keine neuen Kompetenzen aus der GUI erstellen, die Kompetenzen müssen per Schnittstelle hinzugefügt werden.

#### 10. Für Projekte wird die Kompetenzabdeckung angezeigt

##### - In der Detailansicht von Projekten sieht man eine Liste mit benötigten Kompetenzen. Wenn diese rot sind, wurde kein Mitarbeiter mit dieser Kompetenz zum Projekt zugeteilt. Wenn die grün ist, dann ist die bereits für das Projekt abgedeckt.  

#### 11. Status von Projekten erweitert

##### - Es gibt jetzt 4 verschiedene Stutus(running, finished, overdue, unbegun). Für finished muss die Deadline erreicht worden sein und progress muss größer 80% sein. Wenn das Projekt nicht gestartet ist, dann ist das unbegun. Wenn man die Deadline überschritten hat, aber Progress ist kleiner 80%, dann ist es overdue. Ansonsten ist es running.

#### 12. Alle Dropdowns mit Searchbars ersetzt

##### - Da dropdowns sehr groß sein können wenn es viele Entitäten gibt, haben wir alle Dropdowns durch Searchbars ersetzt. Wenn man anfängt zu schreiben werden Empfehlungen gemacht(für alle Searchbars außer von Kompetenzen und Invoice wird für ein Buchstabe mit dem Anfang des Namens vergliechen, ab 2 Buchstaben wird auch innerhalb eines Names gesucht. Für Michael Skinder kann man auch nach sk oder ae suchen)

#### 13. Pagination für alle Listen und TimeRegistrations hinzugefügt

##### -  Da für viele Entitäten die Seite sehr lang werden kann, haben wir Pagination hinzugefügt. Diese zeigt immer nur 7 Elemente maximal. Beim Löschen oder Hinzufügen von Elementen werden die Seiten automatisch angepasst.

#### 14. Delete Prompt bei Kunden angepasst

##### - Bevor man einen Kunden löscht, wird man gefragt ob man auch alle seine Projekte löschen will. Sollte das der Fall sein, kann man dann den Kunden löschen, damit wird alles mit ihm verbundenes gelöscht.

#### 15. Logische Verknüpfungen zwischen Entitäten verbessert.

##### - In dem Issue "List of Issues to be Solved" stehen alle logischen Relationen die implementiert wurden(Zum Beispiel wenn man ein Projekt löscht, muss man alle benötigten Kompetenzen des Projekts auch löschen)

#### 16. Event Log für Veränderungen von Kunden, Projekten und Mitarbeitern

##### - Im event log werden Veränderungen von Entitäten festgehalten. Dies bezieht sich auf löschen, bearbeiten und erstellen. Die Events werden für eine Browser Session gespeichert, da es sich nicht lohnt eine extra Tabelle und Rest Schnittstelle dafür zu implementieren. 

#### 17. Swagger Dokumentation für die API

##### - Es wurde eine swagger Dokumentation für die Schnittstellen erstellt





### Noch vohandene Probleme bis zur Präsentation:

#### 1. Unit Tests für das Frontend schreiben

#### 2. Ergänzen der Tests im Backend





## Tests/Testprotokolle/Nachweis der Testabdeckung

Die neuen Methoden der API wurden getestet, leider funktioniert die Testabdeckung nicht. Wenn man alles Tests auf einmal aufführt, dann macht die Datenbank nicht mehr mit. Wenn man die 2 Ordner separat aufführt, funktionieren alle Tests. Die Frontend Tests werden bis zur Präsentation implementiert. Das Frontend wurde ausführlich manuell getestet