# Bestimmung vom IRIS Identifier Ihres Gesundheitsamtes

Der `IRIS Identifier` an einigen Stellen benutzt, um ein Gesundheitsamt eindeutig zu identifizieren. 

## 1) Bestimmung vom offiziellen RKI Namen

Bestimmen Sie zunächst den offiziellen RKI Namen Ihres Gesundheitsamtes. Gehen Sie dafür auf https://tools.rki.de/PLZTool und suchen Sie nach Ihrem GA. Der offizielle Name ist die obere Zeit im Adressfeld ([Beispiel Bonn](./Beispiel-Bonn.png)).

## 2) Bestimmung vom IRIS Identifier Ihres Gesundheitsamtes

   ```
   Der allgemeine Aufbau vom IRIS Identifier ist: ga-${sanitized_name(Offizieller RKI Name)}

   sanitized_name bedeutet folgendes: Ersetzung aller Umlaute (z.B. ö -> oe). Sonderzeichen und Leerzeichen (auch aufeinanderfolgend) werden durch ein '-' ersetzt. Alles Lower Case.

   # Beispiel Bonn
   Offizieller RKI Name:        Stadt Bonn
   sanitized_name:              stadt-bonn
   IRIS Identifier:             ga-stadt-bonn
   ```