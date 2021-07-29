# Testen der Installation

Unabhängig von der Installationsvariante können Sie testen, ob die Installation erfolgreich war. Erfolgreich bedeutet, dass Ihre IRIS Client Installation mit den zentralen IRIS Services als auch mit den dezentralen Kontaktdaten Anbieter Apps kommunizieren kann. Die Zertifikate wurden korrekt konfiguriert. 

1. Öffnen Sie einen Browser und navigieren Sie zum IRIS Client (`IRIS_CLIENT_DOMAIN` aus der Konfiguration).

1. Melden Sie sich mit den initialen Admin Zugangsdaten an (`SECURITY_AUTH_DB_ADMIN_USER_NAME` und `SECURITY_AUTH_DB_ADMIN_USER_PASSWORD` aus der Konfiguration).

   ![](images/smoketest/Login.png)

1. Navigieren Sie zu `Ereignisse` und klicken Sie dann auf `Neue Ereignisnachverfolgung starten`. 

   ![](images/smoketest/NewEvent.png)


1. Klicken Sie auf Ereignisort auswählen und suchen Sie nach `Da Michele (SmartMeeting GmbH)`. Klicken Sie auf `Wählen`.

   ![](images/smoketest/SearchResult.png)

1. Füllen Sie die restlichen Anfrage Parameter aus. Wählen Sie als Zeitraum den 03. Juni von 12:00 bis 23:59. Klick Sie auf `Anfrage senden`.

   ![](images/smoketest/EventRequest.png)

Bei `Da Michele (SmartMeeting GmbH)` handelt es sich um eine Test Lokation, die eigens für einen Smoketest zur Verfügung steht. Die Lokation liefert fiktionale Testdaten für den 03. Juni. 
