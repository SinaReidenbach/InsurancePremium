#FORMATTIERUNGSREGELN:
-----------------------------------------------------------------------------------
# Dies ist eine große Überschrift

## Dies ist eine kleinere Überschrift

### Dies ist eine noch kleinere Überschrift

#### Noch kleiner

##### Und so weiter...

**Dies ist fetter Text**

*Dies ist kursiver Text*

***Dies ist fett und kursiv***

~~Durchgestrichener Text~~

`Dies ist ein Inline-Code`

```java
// Dies ist ein Codeblock
System.out.println("Hallo, Welt!");
```

![Diagramm](diagram.jpg)


-----------------------------------------------------------------------------------

#Versicherungsprämienberechnung
Übersicht

Die Anwendung berechnet eine Versicherungsprämie basierend auf drei Faktoren:

    Jährliche Kilometerleistung (annoKilometersFactor)
    Fahrzeugtyp (vehicleFactor)
    Region der Fahrzeugzulassung (regionFactor)

#Faktoren:

    annoKilometersFactor:
        0 bis 1.000 km: 0.5
        1.001 bis 10.000 km: 1.0
        10.001 bis 20.000 km: 1.5
        21.000 bis 99.999 km: 2.0

    vehicleFactor:
    Der Faktor für den Fahrzeugtyp ist frei definierbar. Für ein SUV beträgt der vehicleFactor 1.5.

    regionFactor:
    Der Faktor für die Region wird anhand des Bundeslandes berechnet. Für das Bundesland Baden-Württemberg beträgt der regionFactor 1.5.

#Berechnungsformel:

Die Versicherungsprämie wird durch die folgende Formel berechnet:

Prämie = annoKilometersFactor * vehicleFactor * regionFactor * Basiswert

Dabei wird der Basiswert über die application.properties angepasst. Dieser Basiswert wurde integriert, um sicherzustellen, dass die berechnete Prämie nicht unrealistisch niedrig ist.

Beispiel:

    annoKilometersFactor (für 10.000 km): 1.0
    vehicleFactor (für SUV): 1.5
    regionFactor (für Baden-Württemberg): 1.5
    Basiswert: 100 EUR

Berechnete Prämie:

Prämie = 1.0 * 1.5 * 1.5 * 100 EUR = 225 EUR

#Datenbank

Im Normalfall wäre eine MySQL-Datenbank verwendet worden, da sie eine weit verbreitete und skalierbare Lösung für relationale Daten ist. Sie bietet robuste Transaktionssicherheit, eine gute Performance bei größeren Datenmengen und ermöglicht einen effizienten Zugriff auf die Daten.

Da in diesem Fall jedoch kein Zugriff auf einen MySQL-Server besteht, wurde für dieses Beispiel eine H2-Datenbank verwendet. Diese ist einfach zu konfigurieren, leichtgewichtig und benötigt keine separate Serverinstallation, was sie für Entwicklungszwecke und kleinere Anwendungen ideal macht.

Die verwendeten Tabellen in der H2-Datenbank sind:

    Vehicle
    Region
    Postcode
    City
    Statistics
    Anno_Kilometers

Relevante Felder der CSV-Datei:

    Region1: Region
    Region4: City
    Postleitzahl: Postcode

#Services

Die Anwendung besteht aus den folgenden Services:

    DatabaseService:
    Dieser Service ist für den Aufbau der Datenbank und das Befüllen mit den erforderlichen Werten zuständig. Der DatabaseService liest die CSV-Datei, speichert die Daten und stellt sicher, dass die Datenbank initialisiert wird.

    CalculateService:
    Dieser Service übernimmt die Berechnung der Versicherungsprämie. Die Berechnung erfolgt auf Basis der Nutzereingaben, welche durch den FrontendController übergeben werden. Der berechnete Wert wird zurück an das Frontend geschickt, um die Prämie dem Nutzer anzuzeigen.

    StatisticsService:
    Dieser Service speichert die Eingaben des Nutzers (einschließlich IP-Adresse) in der Datenbank. In Zukunft wird eine zusätzliche Jahresarchiv-Tabelle erstellt, in der die IP-Adresse nach einem Jahr gelöscht wird.

Kommunikation zwischen den Services

    Der DatabaseService ist für das Initialisieren und Befüllen der H2-Datenbank zuständig.
    Der CalculateService verarbeitet die Eingaben des Nutzers und berechnet die Versicherungsprämie. Er kommuniziert mit dem FrontendController und dem ThirdPartyController, um die berechnete Prämie zurückzugeben.
    Der StatisticsService speichert alle Eingaben des Nutzers mit der zugehörigen IP-Adresse in der Datenbank und sorgt später dafür, dass die Daten jährlich archiviert werden.

#Anzeige der Berechnung

Um eine Berechnung durchführen zu können, müssen die Nutzer der Speicherung ihrer Daten zustimmen. Nur wenn diese Zustimmung gegeben wird, wird die berechnete Prämie angezeigt.