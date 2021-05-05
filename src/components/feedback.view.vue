<template>
  <div>
    <v-dialog v-model="show" max-width="40%">
      <v-card style="height: fit-content">
        <div>
          <v-card-title class="justify-center">
            <span class="headline">FEEDBACK BOGEN</span>
          </v-card-title>

          <v-card-subtitle style="text-align: center">
            <span class="subtitle-1">Wir freuen uns über ihr Feedback.</span>
          </v-card-subtitle>
          <v-divider class="theme--light primary" />
        </div>
        <v-container class="mt-1 px-sm-15">
          <v-autocomplete
            :items="['Verbesserungsvorschlag', 'Probleme']"
            label="Katergorie auswählen"
          ></v-autocomplete>

          <v-text-field label="Titel*" required maxlength="100"></v-text-field>

          <v-textarea
            class="justify-center"
            label="Ihr Platz für Feedback"
            no-resize
            outlined
            counter
            maxlength="1000"
          ></v-textarea>

          <v-text-field
            class="justify-center"
            label="Organisation*"
            required
            maxlength="50"
          ></v-text-field>

          <v-text-field
            class="justify-center"
            label="E-Mail*"
            required
            maxlength="80"
          ></v-text-field>
        </v-container>
        <v-divider class="theme--light primary" />

        <v-container>
          <v-row>
            <v-col class="d-flex justify-center">
              <v-btn
                class="my-2"
                color="secondary"
                plain
                @click="showCancelDialog = true"
              >
                Abbrechen
              </v-btn>
            </v-col>
            <v-col class="d-flex justify-center">
              <v-btn
                class="my-2"
                color="primary"
                @click="showConfirmDialog = true"
              >
                Absenden
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-dialog>

    <v-dialog
      style="overflow-y: hidden"
      v-model="showCancelDialog"
      max-width="20%"
    >
      <v-card>
        <v-card-title>
          <span>Abbrechen</span>
          <v-spacer></v-spacer>
          Sind sie sich Sicher, dass sie den Feedback Bogen schliesen wollen.\n
          Das Schliesen über den Abbrechen Knopf löscht alle geschriebenen
          Daten.
        </v-card-title>
        <v-card-actions>
          <v-btn color="primary" text @click="showCancelDialog = false">
            Abbrechen
          </v-btn>
          <v-btn color="primary" text @click="cancel()"> Schliesen</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      style="overflow-y: hidden"
      v-model="showConfirmDialog"
      max-width="20%"
    >
      <v-card>
        <v-card-title class="justify-center">
          <span>Bestätigen</span>
        </v-card-title>
        <hr />
        <v-card-text class="mt-3">
          Möchten sie ihr Feedback abschicken? Die Daten werden in einem GitHub
          Issue gespeichert und es wird ihnen ein Link zum Issue zu ihrer E-Mail
          Addresse gesendet.
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            class="my-2"
            color="secondary"
            plain
            text
            @click="showConfirmDialog = false"
          >
            Zurück
          </v-btn>
          <v-btn color="primary" text @click="confirm()" class="my-2">
            Abschicken
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showCancelDialog: false,
      showConfirmDialog: false,
    };
  },
  methods: {
    cancel() {
      this.showCancelDialog = false;
      this.show = false;
    },
    confirm() {
      this.showConfirmDialog = false;
      this.show = false;
    },
  },
  props: {
    value: Boolean,
  },
  computed: {
    show: {
      get() {
        return this.value;
      },
      set(value) {
        this.$emit("input", value);
      },
    },
  },
};
</script>
