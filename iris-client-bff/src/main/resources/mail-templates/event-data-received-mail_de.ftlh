<!DOCTYPE html>
<html>
    <head>
      	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
		<p>Sehr geehrte Damen or Herren,</p>
		<p><br /></p>
		<p>Für das Event "${eventId}" sind neue Daten verfügbar. Diese können in Ihrem IRIS Portal begutachtet werden.</p>
		<p>Event:</p>
		<ul>
			<li>Event: ${eventId}</li>
			<li>Externe Id: ${externalId}</li>
			<li>Startzeit: ${startTime?datetime?string.long}</li>
			<li>Endzeit: ${endTime?datetime?string.long}</li>
		</ul>
		<p>
        	Mit den gegebenen Daten kann der entsprechende Datensatz identifiziert werden. Alternative der folgende Link führt direkt zum Datensatz: <a href="${eventUrl}">Event ${eventId}</a>
		</p>
		<p><br /></p>
		<p>
			Mit freundlichen Grüßen, <br />
			IRIS connect<br />
			Innovationsverbund Öffentliche Gesundheit (InÖG)
		</p>
		<p><br /></p>
		<p style="font-size: 11px;">Dies ist eine automatische erstellte Nachricht. Bitte versuchen Sie nicht auf diese Nachricht zu antworten.</p>
    </body>
</html>