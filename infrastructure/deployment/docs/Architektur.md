## IRIS Connect Architektur

IRIS connect baut auf einer dezentralen Architektur auf. Dabei findet der Austausch von persönlichen Daten (wie z.B. Gästelisten oder Kontakttagebüchern) direkt zwischen den App Anbietern und dem zuständigen Gesundheitsamt statt.

IRIS connect besteht aus
* Einem zentralen Gateway, bei dem alle teilnehmenden digitalen Lösungen "registriert" sind. Dieses Gateway stellt den
  angeschlossenen Gesundheitsämtern und Anbietern zentrale Dienste bereit, z.B. das Vermitteln von Anfragen aus den
  Gesundheitsämtern an die angeschlossenen Lösungen und das Rück-Vermitteln der angefragten Daten an die
  Gesundheitsämter.
* Einer Benutzerschnittstelle in den Gesundheitsämtern (Client), über die Mitarbeitende die erfassten Daten der
  Bürger:innen und Einrichtungen anfragen und abrufen können.
* Einem verteilten Netzwerk von Endpoint-Servern (EPS), das den verschiedenen Akteuren bei IRIS eine sichere Kommunikation miteinander ermöglicht.

IRIS connect tritt als Vermittler zwischen den Gesundheitsämtern und den verschiedenen digitalen Lösungen auf.
Es erfolgt insbesondere keine zentrale Speicherung der übermittelten Daten.

Das Verschlüsseln von personenbezogenen Daten findet direkt in der jeweiligen an IRIS angebundenen Anwendung statt.
Das Entschlüsseln erfolgt im IRIS-Client-Backend, also quasi im Gesundheitsamt.
Hinzu kommt Transportverschlüsselung mittels TLS/HTTPS und ggf. eine zusätzliche Inhaltsverschlüsselung gemäß Anforderung der Datenschutzkonferenz.
Erst im IRIS-Client liegen die Daten dann unverschlüsselt vor.
Vor der Datenabfrage durch ein Gesundheitsamt liegen diese ausschließlich bei der angebundenen Anwendung und nicht im IRIS-System.

Das folgende Schaubild visualisiert die Architektur und erklärt die zentralen Bestandteile.

*Schaubild der Akteure, Komponenten und typischen Use Cases von IRIS*
![IRIS C2 Architektur](IRIS-C2-with-EPS.jpg)

| Nummer | Bezeichnung | Erklärung |
| --- | --- | --- |
|1| IRIS connect | IRIS connect unterteilt sich in *IRIS Central Services* und den *IRIS Client*. Erstere werden zentral im Rechenzentrum der [AKDB](https://www.akdb.de/) gehostet und vom [IRIS connect Team](https://github.com/iris-connect) verwaltet. Der IRIS Client wird mitsamt einer Dokumentation zum Download für die IT-Teams der Gesundheitsämter bereitgestellt. |
|2| Kontakdatenerfassungs-Apps| Ein weiterer wichtiger Teilnehmer in IRIS connect sind die Kontaktdatenerfassungs-Apps. Diese stammen zum größten Teil aus der Initiative [Wir für Digitaliserung](https://www.wirfuerdigitalisierung.de/). |
|3| Suchregister <br /> (Locations Service) | Damit Einrichtungen, bei denen eine digitale Kontaktdatenerfassung im Einsatz ist vom Gesundheitsamt gefunden werden können, stellt IRIS connect ein zentrales Suchregister zur Verfügung. Die Daten im Suchregister werden von den Kontaktdatenerfassungs-Lösungen bereitgestellt. |
|4| Service Directory | Das vom IRIS connect Team verwaltete Service Directory enthält Einträge für alle teilnehmenden Akteure um IRIS connect. Zudem werden die Berechtigungen der Kommunikationsbeziehungen hier hinterlegt. |
|5| IRIS Proxy Service | Der IRIS Proxy Service ermöglicht es Bürger:innen und Lösungsanbieter:innen, Daten aktiv in ein Gesundheitsamt zu schicken. Dafür stellt der Proxy eine autorisierte Verbindung zwischen einer App bzw. einem Browser und einer Proxy-Komponente im Gesundheitsamt her. Der IRIS Client im Gesundheitsamt muss dafür keine eingehenden Verbindungen zulassen. |
|6| Endpunktserver (EPS)| Herzstück der Punkt-zu-Punkt Kommunikation ist der IRIS [Endpunktserver (EPS)](https://github.com/iris-connect/eps/blob/master/README.md). Dabei handelt es sich um eine Komponente, die dezentral bei allen Akteure des IRIS connect Systems installiert wird. Die Kommunikation erfolgt gesichert über mTLS. |
