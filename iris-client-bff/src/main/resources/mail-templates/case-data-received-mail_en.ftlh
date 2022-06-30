<!DOCTYPE html>
<html>
    <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
		<p>Dear Sir or Madam,</p>
		<p><br /></p>
		<p>New data is available for the Index Case "${caseId}" in your IRIS Portal.</p>
		<p>Index Case:</p>
		<ul>
			<li>Index Case: ${caseId}</li>
			<li>External Id: ${externalId}</li>
			<li>Start: ${startTime?datetime?string.long}</li>
			<li>End: ${endTime?datetime?string.long}</li>
		</ul>
		<p>
        	With the provided data the case can be identified online. Alternatively the following link will redirect to it directly: <a href="${caseUrl}">Index Case ${caseId}</a>
		</p>
		<p><br /></p>
		<p>
			With kind regards, <br />
			IRIS connect<br />
			Innovationsverbund Öffentliche Gesundheit (InÖG)
		</p>
		<p><br /></p>
		<p style="font-size: 11px;">This is an automated message. Please do not reply.</p>
    </body>
</html>