# Prozess für das Beantragen der Zertifikate der Bundesdruckerei (Produktivsystem) <br /> – Prozessuale Anleitung – 
## Vorwort
Für die Anbindung an IRIS benötigt ein Gesundheitsamt (GA, Plural GÄ) zwei Schlüsselpaare bzw. Zertifikate von der Bundesdruckerei. 

Dieses Dokument erklärt die organisatorischen Schritte, die Landesbehörden und Gesundheitsämter gemeinsam unternehmen müssen, um die Zertifikate zu beantragen und einzurichten. 

Erläuterungen zu technische Konfigurationsschritten, die ausschließlich für IT-Administratorinnen relevant sind, werden zur besseren Übersicht in der [technischen Installationsanleitung](Certificate-Process_Prod_technical.md) erläutert. 
Diese wird an den entsprechenden Stellen verlinkt. 

Dieses Dokument nimmt an, dass IRIS über das jeweilige Bundesland gebündelt für alle Gesundheitsämter bezogen wird.
Die Schritte für GÄ, die sich selbstständig, also unabhängig vom Land, an IRIS anschließen, unterscheiden sich stellenweise.

Beim Rollout von IRIS werden die individuellen Bedürfnisse und Wünsche der Bundesländer berücksichtigt. 
Dadurch können sich von Land zu Land einige Unterschiede im Antragsprozess ergeben, auf die hier eingegangen wird.

Sollten die Gegebenheiten eines Bundeslandes nicht ausreichend berücksichtigt sein, bitten wir um kurzen Hinweis.

## Inhaltsverzeichnis
* [Zusammenfassung](#zusammenfassung)
* [Welche Zertifikate müssen beantragt werden?](#welche-zertifikate-m-ssen-beantragt-werden-)
* [Erforderliche Schritte seitens des Landes](#erforderliche-schritte-seitens-des-landes)
    + [Antragsprozess bei der Bundesdruckerei anstoßem](#antragsprozess-bei-der-bundesdruckerei-ansto-em)
    + [Organisationsvalidierung durchlaufen](#organisationsvalidierung-durchlaufen)
    + [Zertifikatsverantwortliche Personen in den Gesundheitsämtern erfassen](#zertifikatsverantwortliche-personen-in-den-gesundheits-mtern-erfassen)
    + [Domains für die Gesundheitsämter bereitstellen](#domains-f-r-die-gesundheits-mter-bereitstellen)
* [Erforderliche Schritte seitens eines Gesundheitsamts](#erforderliche-schritte-seitens-eines-gesundheitsamts)
    + [Zertifikate Nr. 1 und Nr. 2 bei der Bundesdruckerei beantragen](#zertifikate-nr-1-und-nr-2-bei-der-bundesdruckerei-beantragen)
        - [Zertifikatsverantwortliche Person benennen](#zertifikatsverantwortliche-person-benennen)
        - [Zertifikate online beantragen](#zertifikate-online-beantragen)
        - [Zertifikate herunterladen](#zertifikate-herunterladen)
        - [Zertifikate einrichten](#zertifikate-einrichten)
    

## Zusammenfassung
Für die Anbindung an IRIS auf Landesebene ist die Mitwirkung einer Landesbehörde empfohlen. 
Diese tritt gegenüber der Bundesdruckerei im Rahmen der Identitätsprüfung stellvertretend für alle GÄ des Landes in Erscheinung.
Dadurch fällt nur eine Identitätsprüfung an, statt einer pro Kommune, was den Rollout beschleunigt und den Aufwand reduziert.
Die GÄ brauchen den Prüfungsprozess also nicht separat zu durchlaufen.

Der ganze Prozess kurz gefasst:

1. Behörde stellt den GÄ ggf. eine (Sub-)Domain bereit, die im Kontext von IRIS genutzt werden kann.
2. Behörde nennt der Bundesdruckerei (BDr) auf sicherem Weg eine zeichnungsberechtigte Person der Behörde und je GA eine zertifikatsverantwortliche Person.
3. BDr führt einmalig eine Organisationsvalidierung der Behörde und eine Prüfung der Vertretungsberechtigung durch.
4. BDr übermittelt Zugangsdaten zum Antragsportal auf sicherem Wege an die Zertifikatsverantwortlichen der GÄ.
5. Zertifikatsverantwortliche erstellen zwei kryptografische Schlüsselpaare und stellen für sie Zertifikatsanfragen online im Antragsportal ein.
6. Nach Prüfung und Domain-Validierung der (Sub-)Domain des GA stellt die BDr die Zertifikate zum Download im Antragsportal bereit.
7. Zertifikatsverantwortliche konfigurieren die Zertifikate und die Domain im Rahmen der Installation des IRIS-Clients gemäß Anleitung.

## Welche Zertifikate müssen beantragt werden?
Für die Anbindung an IRIS benötigt ein GA zwei Schlüsselpaare bzw. Zertifikate von der Bundesdruckerei (BDr) bzw. deren Vertrauensdiensteanbieter D-Trust: 

1. Ein TLS-Zertifikat für den IRIS-Client des GA  
Anwendungsfall: Identität des GA im Internet (TLS/HTTPS).


2. Ein Signaturzertifikat für Vertreter:in des GA  
Anwendungsfall: Identität des GA im EPS-Netzwerk.

## Erforderliche Schritte seitens des Landes

> Hinweis: Folgende Schritte beziehen sich auf Bundesländer, in denen eine Landesbehörde im Rahmen der Einführung von IRIS jedem GA eine (Sub-)Domain bereitstellt.

### Antragsprozesses bei der Bundesdruckerei anstoßem
Der Antragsprozess wird dadurch angestoßen, dass ein vertretungsberechtigtes Mitglied der Landesbehörde auf sicherem Weg Kontakt zur Bundesdruckerei aufnimmt.
Die Identität dieser Person, sowie ihre Vertretungsberechtigung werden von der BDr zu gegebenem Zeitpunkt geprüft.

Für das Beantragen und Ausgeben der Zertifikate gibt das Bundesamt für Sicherheit in der Informationstechnik (BSI) ein sogenanntes "substantielles Schutzniveau" vor (Technische Richtlinie [TR-03107-1](https://www.bsi.bund.de/SharedDocs/Downloads/DE/BSI/Publikationen/TechnischeRichtlinien/TR03107/TR-03107-1.html)).
Normale E-Mails als Kommunikationsmittel gelten in diesem Zusammenhang als unsicher und dürfen nur behilfsweise genutzt werden. 
Spätestens die Übergabe der fertig befüllten Liste der GÄ (siehe unten) muss hinreichend abgesichert erfolgen.

Vorgesehen ist hier eine Kombination aus von zwei Kommunikationswegen: 
1. Sichere E-Mails, die mit S/MIME digital signiert und verschlüsselt sind. Die BDr hat hierfür ein sicheres E-Mail-Postfach eingerichtet, an das verschlüsselt gemailt werden kann.
2. Ein Wiki-System (Confluence) der BDr, in dem Daten sicher übergeben werden können. Die BDr wird nach Kontaktaufnahme eine signierte E-Mail verschicken, die erklärt wie der Zugang erfolgt.

Bei der Kontaktaufnahme liefert die Behörde folgende Daten:
1. Gesetzmäßige Organisatinsdaten 
2. Kontaktdaten einer zeichnungsberechtigten Person
3. IRIS-Stammdomäne des Bundeslandes (s. unten, z.B. ```www.iris-connect.nrw.de```) 

Daraufhin übersendet die BDr der zeichnungsberechtigten Person ein Vollmacht-Schreiben, mit dem die BDr ermächtigt wird, im Auftrag der Behörde fachlich tätig zu werden und Zertifikate auszustellen. 
Ein Scan der ausgefüllten und unterschriebene Vollmacht kann dann einfach im Confluence der BDR hochgeladen werden. 

### Organisationsvalidierung durchlaufen
Im nächsten Schritt nimmt die Bundesdruckerei telefonisch Kontakt zur zeichnungsberechtigten Person auf, um die Behörde als Organisation zu validieren. 
Den genauen Ablauf dieses Vorgangs schildert die Bundesdruckerei dann im direkten Kontakt.

### Zertifikatsverantwortliche Personen in den Gesundheitsämtern erfassen
Die Landesbehörde muss der Bundesdruckerei eine Liste der GÄ übermitteln, die Zertifikate erhalten sollen. 

Je GA muss dafür eine zertifikatsverantwortliche Person und deren Erreichbarkeit angegeben werden. 
Dazu gehören Anrede, Titel, Vorname, Nachname, Funktion, Telefonnummer, E-Mail-Adresse. 
Die benannte Person sollte diejenige sein, die später auch die technisch-administrativen Schritte der Zertifikatseinrichtung durchführen wird. 
In der Regel sind das IT-Administrator:innen des GA bzw. dessen IT-Dienstleisters.

Zusätzlich muss je GA auch ein Domain (Internetadresse) angegeben werden, unter der Bürger:innen das GA im Kontext von
IRIS erreichen können (z.B. mit einem Webbrowser). Eine beispielhafte Domain wäre ```beispiel.de```. 

Das sieht für das GA Bonn beispielsweise so aus:
<table>
<th rowspan="2">Kommune</th>
<th colspan="7">Zertifikatsverantwortliche Person für das Gesundheitsamt</th>
<th rowspan="2">IRIS-Domain des Gesundheitsamts</th>
<tr>
<td>Anrede</td>
<td>Titel</td>
<td>Vorname</td>
<td>Nachname</td>
<td>Funktion</td>
<td>Telefonnummer</td>
<td>E-Mail-Adresse</td>
</tr>
<tr>
<td>Bonn</td>
<td>Frau</td>
<td>Dr.</td>
<td>Alisha</td>
<td>Riedel</td>
<td>IT-Administratorin für das Gesundheitsamt Bonn</td>
<td>+49 (0) 228 / 234 567 - 8</td>
<td>alisha.riedel@it.nrw.de</td>
<td>stadt-bonn.iris-conncet.nrw.de</td>
</tr>
<tr>
<td>Aachen</td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
</tr>
</table>

Als Sammelstelle für diese Informationen stellt die BDr eine Vorlage im eigenen Wiki-System (Confluence) bereit. 
Die Behörde kann die Datensätze dort nach Zulieferung zusammengetragen und iterativ vervollständigt.

### Domains für die Gesundheitsämter bereitstellen
Bei den Domains gibt es zwei frei wählbare Gestaltungsmöglichkeiten:

**Möglichkeit 1: Die Behörde verwendet eine bereits bestehende Domain**

Im folgenden Beispiel gehen wir davon aus, dass die Behörde bereits eine Domain besitzt, hier ```www.nrw.de```.

In diesem Fall muss zunächst eine Subdomain angelegt werden, die im Kontext von IRIS angesprochen werden kann (sog. IRIS-Stammdomain für das Bundesland). 
Es gibt keine feste Vorgabe für die Benennung dieser Subdomain, wir empfehlen aber ```iris-connct```. Damit ergibt
sich ```iris-connect.nrw.de```.

Als Nächstes muss für jedes GA eine weitere Subdomain unterhalb der gerade erzeugten angelegt werden. 
So ergibt sich für das GA Bonn ```stadt-bonn.iris-connect.nrw.de```. 
Auch hier gibt es keine feste Vorgabe für die Benennung. Wir empfehlen aber den offiziellen RKI-Namen des Gesundheitsamts zu verwenden. 
Wo der offizielle RKI-Name (für Bonn bswp. "Stadt Bonn") nachgeschaut werden kann und wie sich die normalisierte (domainfähige) Form daraus ableitet, wird in der [technischen Installationsanleitung](Certificate-Process_Prod_technical.md) beschrieben.

**Möglichkeit 2: Die Behörde verwendet eine neue Domain, die sie vorab erwirbt**

Im folgenden Beispiel gehen wir davon aus, dass eine neue Domain verwendet werden soll, die es noch nicht gibt.
Diese muss dann erst bei einem Domain-Anbieter käuflich erworben werden, die im Kontext von IRIS angesprochen werden kann (sog. IRIS-Stammdomain für das Bundesland). 
Nennen wir sie ```iris-thueringen.de```. 
Es gibt keine feste Vorgabe für die Benennung der Domain.

Als Nächstes muss für jedes GA eine Subdomain unterhalb dieser Domain angelegt werden. 
Für das GA Erfurt ergibt sich bspw. ```landeshauptstadt-erfurt.iris-thueringen.de```. 
Auch hier gibt es wieder keine feste Vorgabe für die Benennung. Wir empfehlen aber den offiziellen RKI-Namen des Gesundheitsamts zu verwenden.
Wo der offizielle RKI-Name (für Erfurt bswp. "Landeshauptstadt Erfurt") nachgeschaut werden kann und wie sich die normalisierte Form daraus ableitet, wird in der [technischen Installationsanleitung](Certificate-Process_Prod_technical.md) beschrieben.

--- 
Die technische Konfiguration der (Sub-)Domains und der zugehörigen Einträge im Domain Name System (DNS) ist in der [technischen Installationsanleitung](Certificate-Process_Prod_technical.md) erläutert. 

## Erforderliche Schritte seitens eines Gesundheitsamts
> Hinweis: Folgende Schritte beziehen sich auf Bundesländer, in denen eine Landesbehörde im Rahmen der Einführung von IRIS jedem GA eine Domain bereitstellt.

### Zertifikate Nr. 1 und Nr. 2 bei der Bundesdruckerei beantragen
#### Zertifikatsverantwortliche Person benennen
Der Antragsprozess bei der Bundesdruckerei wird von der jeweils zuständigen Landesbehörde angestoßen.
Diese wird zu gegebenem Zeitpunkt auf jedes GA zugehen und um die Benennung einer Ansprechperson bitten, welche später die technisch-administrativen Schritte der Zertifikatseinrichtung für das GA durchführen wird.
In der Regel sind das IT-Administrator:innen des GA bzw. dessen IT-Dienstleisters. 
Je nachdem, ob jemand vom medizinischen oder technisch-administrativen Fachpersonal benannt wird, muss die Kommunikation mit der Bundesdruckerei später ggf. an die IT weitergeleitet werden.

#### Zertifikate online beantragen
Die Bundesdruckerei lädt die jeweils zertifikatsverantwortliche Person per signierter E-Mail zum sog. Certificate Service Manager (CSM) ein, einem Online-Verwaltungsportal für Zertifikate. 
Darin können die Zertifikate anschließend mit wenigen Klicks beantragt werden.

Die Zertifikate werden nun von on der zertifikatsverantwortlichen Person des jeweiligen Gesundheitsamtes ohne weitere Beteiligung der Landesbehörde beantragt.
Dieser Teil-Prozess wird daher in der [technischen Installationsanleitung](Certificate-Process_Prod_technical.md)  der Zertifikate weiter beschrieben.

#### Zertifikate einrichten
Die Zertifikate werden von der zertifikatsverantwortlichen Person des jeweiligen Gesundheitsamtes eingerichtet.
Dieser Teil-Prozess wird daher in der [technischen Installationsanleitung](Certificate-Process_Prod_technical.md) der Zertifikate weiter beschrieben.