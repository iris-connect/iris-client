# Anleitung für das Beantragen und Einrichtung der Zertifikate

## Vorwort

Für die Anbindung an IRIS benötigt ein Gesundheitsamt (GA, Plural GÄ) vier Schlüsselpaare bzw. Zertifikate. Dieses
Dokument erklärt, wie ein Gesundheitsamt die Zertifikate beantragt und einrichtet.

Die Anleitung geht davon aus, dass IRIS über das jeweilige Bundesland gebündelt für alle Gesundheitsämter bezogen wird.
Für GÄ, die sich selbstständig, also unabhängig vom Land, an IRIS anschließen, unterscheiden sich die Schritte teils
deutlich.

Beim Rollout von IRIS werden die individuellen Bedürfnisse und Wünsche der Bundesländer berücksichtigt. Dadurch können
sich von Land zu Land einige Unterschiede beim Antragsprozess ergeben. Die Anleitung geht entsprechend darauf ein.

Sollte die Anleitung die Gegebenheiten in einem Bundesland nicht ausreichend berücksichtigen, bitten wir um kurzen
Hinweis

**TODO: Kontaktmöglichkeit oder Link auf contribution guideline**

## Inhaltsverzeichnis
* [Überblick]()
* [Welche Zertifikate gibt es?]()
* [Erforderliche Schritte seitens der Landesbehörde]()
    * [Antragsprozess bei der Bundesdruckerei anstoßen]()
    * [Ansprechpersonen in den Gesundheitsämtern erfassen]()
    * [Domains für die Gesundheitsämter bereitstellen]()
    * [Identitätsprüfung durchlaufen]()
* [Erforderliche Schritte seitens des GA]()
    * [Beantragen der Zertifikate Nr. 1 und Nr. 2 bei der Bundesdruckerei]()
        * [Ansprechperson benennen]()
        * [Zertifikate online beantragen]()
        * [Zertifikate herunterladen]()
        * [Zertifikate einrichten]()
    * [Erstellen der Zertifikate Nr. 3 und Nr. 4]()
        * [Zertifikate erstellen]()
        * [Zertifikate einrichten]()
    * [Zertifikate sicher verwahren]()


## Überblick
Für die Anbindung an IRIS auf Landesebene ist die Mitwirkung einer Landesbehörde nötig.
Diese tritt im Rahmen der Identitätsprüfung gegenüber der Bundesdruckerei stellvertretend für alle GÄ des Landes auf.
Die GÄ brauchen den Prüfungsprozess also nicht separat zu durchlaufen.

Hier der Ablauf, um die ersten beiden der vier Zertifikate zu bekommen:

1. Behörde stellt den GÄ ggf. eine (Sub-)Domain bereit, die im Kontext von IRIS genutzt werden kann.
2. Vertreter:in der Behörde nennt der BDr auf sicherem Weg eine Vertreter:in je GA.
3. BDr prüft Identität von Vertreter:in der Behörde und deren Vertretungsberechtigung.
4. BDr übermittelt Zugangsdaten zum Antragsportal je GA auf sicherem Wege an die Behörde, die sie an die Vertreter der GÄ weiterleitet.
5. GA erstellt kryptographische Schlüssel und stellt für diese Zertifikatsanfragen sicher im Antragsportal ein.
6. Nach Prüfung und Domain-Validierung der (Sub-)Domain des GA stellt die BDr die Zertifikate bereit.
7. GA kann die Zertifikate & Domain nun im IRIS-Client gemäß Anleitung konfigurieren.
8. GA erstellt weitere Zertifikate ohne Zutun der BDr gemäß Anleitung.


## Welche Zertifikate gibt es?

Für die Anbindung an IRIS benötigt ein GA vier Schlüsselpaare bzw. Zertifikate. Davon müssen zwei bei der
Bundesdruckerei (BDr) bzw. deren Vertrauensdiensteanbieter D-Trust beantragt werden.

1. Ein TLS-Zertifikat für das IRIS-Client-Backend des GA
    * Anwendungsfall: Identität des GA im Internet (TLS/HTTPS).
2. Ein Signaturzertifikat für Vertreter:in des GA
    * Anwendungsfall: Identität des GA im EPS-Netzwerk.

Die verbleibenden zwei Zertifikate können im Anschluss vom GA oder dessen IT-Dienstleister, je nachdem, wer den
IRIS-Client betreibt, selbstständig erstellt werden, also ohne Zutun der Bundesdruckerei.

3. Ein mTLS-Zertifikat für den EPS-Server des GA
    * Anwendungsfall: Absicherung der Kommunikation zwischen EPS-Servern.
4. Ein Ende-zu-Ende-Zertifikat für das IRIS-Client-Backend des GA
    * Anwendungsfall: Umsetzung der Datenschutzkonferenz-Anforderung an Betreiber von digitaler Kontaktdatenerfassung,
      wonach zusätzlich zur Transportverschlüsselung (TLS)
      eine zweite Verschlüsselungsschicht auf Anwendungsebene (Inhaltsverschlüsselung)
      umzusetzen ist.


## Erforderliche Schritte seitens der Landesbehörde

> Hinweis: Folgende Schritte beziehen sich auf Bundesländer, in denen eine Landesbehörde im Rhmen der einführung von IRIS jedem GA eine Domain bereitstellt.

### Antragsprozesses bei der Bundesdruckerei anstoßem

### Ansprechpersonen in den Gesundheitsämtern erfassen
Die Landesbehörde muss der Bundesdruckerei eine Liste der GÄ übermitteln, die Zertifikate erhalten sollen. Je GA muss
eine Kontaktperson vor Ort und deren Erreichbarkeit angegeben werden. Dazu gehören Anrede, Titel, Vorname, Nachname,
Funktion, Telefonnummer, E-Mail-Adresse.

Zusätzlich muss je GA auch ein Domain (Internetadresse) angegeben werden, unter der Bürger:innen das GA im Kontext von
IRIS später erreichen können (z.B. mit einem Webbrowser). Eine beispielhafte Domain wäre ```beispiel.de```.

Das sieht für das GA Bonn beispielsweise so aus:
<table>
<th rowspan="2">Kommune</th>
<th colspan="7">Ansprechperson im Gesundheitsamt</th>
<th rowspan="2">Domain des Gesundheitsamts für IRIS</th>
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
<td>alisha.riedel@gesundheitsamt-bonn.de</td>
<td>bonn.iris-conncet.nrw.de</td>
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

### Domains für die Gesundheitsämter bereitstellen
Bei den Domains gibt es zwei frei wählbare Gestaltungsmöglichkeiten:

**Möglichkeit 1: Die Behörde verwendet eine bereits bestehende Domain**

Im folgenden Beispiel gehen wir davon aus, dass die Behörde bereits eine Domain besitzt, hier ```www.nrw.de```.

In diesem Fall muss zunächst eine Subdomain angelegt werden, die im Kontext von IRIS angesprochen werden kann. Es gibt
keine feste Vorgabe für die Benennung dieser Subdomain, wir empfehlen aber ```iris-connct```. Damit ergibt
sich ```iris-connect.nrw.de```.

Als Nächstes muss für jedes GA eine weitere Subdomain unterhalb der gerade erzeugten angelegt werden. Für das GA Bonn
ergibt sich so ```bonn.iris-connect.nrw.de``` oder ```gesundheitsamt-bonn.iris-connect.nrw.de```. Auch hier gibt es
keine feste Vorgabe für die Benennung.

**Möglichkeit 2: Die Behörde verwendet eine neue Domain, die sie vorab erwirbt**

Im folgenden Beispiel gehen wir davon aus, dass eine Domain neue Domain verwendet werden soll, die es noch nicht gibt.
In diesem Fall muss eine Domain ausgesucht und erworben werden. Nennen wir sie ```iris-thueringen.de```. Es gibt keine
feste Vorgabe für die Benennung der Domain.

Als Nächstes muss für jedes GA eine Subdomain angelegt werden. Für das GA Erfurt ergibt sich
so ```erfurt.iris-thueringen.de``` oder ```gesundheitsamt-erfurt.iris-thueringen.de```. Auch hier gibt es keine feste
Vorgabe für die Benennung.

### Durchlaufen einer Identitätsprüfung
Sobald die Liste der Gesundheitsämter vollständig vorliegt, führt die Bundesdruckerei eine formelle Identitätsprüfung der Person durch, die als Vertreter:in der Behörde in Erscheinung getreten ist.
Zusätzlich prüft sie auch die Vertretungsberechtigung. Den genauen Ablauf dieses Vorgangs schildert die Bundesdruckerei dann im direkten Kontakt.

**TODO: Zum genauen Ablauf brauchen wir noch keine Infos.**

## Erforderliche Schritte seitens eines Gesundheitsamts
> Hinweis: Folgende Schritte beziehen sich auf Bundesländer, in denen eine Landesbehörde im Rhmen der einführung von IRIS jedem GA eine Domain bereitstellt.

### Beantragen der Zertifikate Nr. 1 und Nr. 2 bei der Bundesdruckerei
#### Ansprechperson benennen
Der Antragsprozess bei der Bundesdruckerei wird von der jeweils zuständigen Landesbehörde angestoßen.
Diese wird zu gegebenem Zeitpunkt auf jedes GA zugehen und um die Benennung einer Ansprechperson vor Ort bitten.
Diese Ansprechperson wird später Anweisungen von der Bundesdruckerei erhalten.
Je nachdem, ob jemand vom medizinischen oder technisch-administrativen Fachpersonal benannt wird, müssen diese Informationen ggf. an die IT vor Ort weitergeleitet werden.


#### Zertifikate online beantragen
Bundesdruckerei sendet der Ansprechperson Zugangsdaten zum sog. Certificate Service Manager (CSM), einem Online-Verwaltungsportal für Zertifikate.
Darin können die Zertifikate mit wenigen Klicks beantragt werden.

**TODO: Video oder Screenshots der Antragsstrecke einfügen**

#### Zertifikate herunterladen
**TODO: Video oder Screenshots der Antragsstrecke einfügen**

#### Zertifikate einrichten
Die Zertifikate werden gemäß Anleitung in der Dokumentation des IRIS-Clients eingerichtet.

**TODO: Prozessbeschreibung für Prod hier einfügen oder im IRIS-Client-Repo auf Aktualität prüfen und hier verlinken.**

## Erstellen der Zertifikate Nr. 3 und Nr. 4
Sobald Zertifikat Nr. 2 vorliegt können die Zertifikate Nr. 3 und Nr. 4 vom GA, bzw. dessen IT-Dienstleister,
je nachdem, wer den IRIS-Client betreibt, selbstständig erstellt werden, also ohne Zutun der Bundesdruckerei.

### Zertifikate erstellen
Das Erstellen der Zertifikate Nr. 3 und Nr. 4 übernimmt ein Script, dass mit dem IRIS-Client zur Verfügung gestellt wird.

**TODO: Ein Script erstellen, das als Input das Signaturzertifikat und einige Parameter nimmt und Zertifikate Nr. 3 und 4 ausspuckt.**

**TODO: Eine Anleitung im IRIS-Client-Repo erstellen, die erklärt wie das Script zu nutzen ist. Dann zwecks Vollständigkeit copy-paste hierher.**

### Zertifikate einrichten
**TODO: Prozesse ggf. definieren und dokumentieren im IRIS-Client-Repo und copy-paste hierher.**

#### Zertifikat Nr. 3 einrichten

#### Zertifikat Nr. 4 einrichten

## Zertifikate sicher verwahren
**TODO: Beschreiben, wie mit den einzelnen Zertifikaten im Anschluss umgegangen werden soll. Z.B. Zertifikat Nr. 2 air-gaped verwahren.**
