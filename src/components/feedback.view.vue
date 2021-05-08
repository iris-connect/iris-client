<template>
  <div>
    <v-dialog v-model="show" max-width="40%">
      <v-card style="height: fit-content">
        <v-form v-model="isFormValid">
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
              label="Katergorie auswählen*"
              required
              :rules="kategoryRules"
            ></v-autocomplete>

            <v-text-field
              label="Titel*"
              required
              :rules="titelRules"
              maxlength="100"
            ></v-text-field>

            <v-textarea
              class="justify-center"
              label="Ihr Platz für Feedback*"
              no-resize
              outlined
              counter
              required
              :rules="textRules"
              maxlength="1000"
            ></v-textarea>

            <v-text-field
              class="justify-center"
              label="Organisation"
              required
              maxlength="50"
            ></v-text-field>

            <v-text-field
              class="justify-center"
              label="E-Mail"
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
                  :disabled="!isFormValid"
                  @click="showConfirmDialog = true"
                >
                  Absenden
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-card>
    </v-dialog>

    <v-dialog
      style="overflow-y: hidden"
      v-model="showCancelDialog"
      max-width="20%"
      persistent
    >
      <v-card>
        <v-card-title>
          <span>ACHTUNG!</span>
          <v-spacer></v-spacer>
          Wenn sie abbrechen, schliesen sie den Dialog.
        </v-card-title>
        <v-card-actions>
          <v-btn color="secondary" text @click="showCancelDialog = false">
            Zurück
          </v-btn>
          <v-btn color="primary" text @click="cancel()"> Bestätigen</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      style="overflow-y: hidden"
      v-model="showConfirmDialog"
      max-width="20%"
      persistent
    >
      <v-card style="height: fit-content">
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
            :disabled="!isFormValid"
            @click="showConfirmDialog = false"
          >
            Zurück
          </v-btn>
          <v-btn color="primary" text @click="confirm()" class="my-2">
            Absenden
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
      isFormValid: false,
      kategoryRules: [(content) => !!content || "Pflichtfeld"],
      titelRules: [
        (content) => !!content || "Pflichtfeld",
        (content) =>
          (content && content.length <= 100) ||
          "Der Titel darf nur 100 Charactäre überschreiten",
      ],
      textRules: [
        (content) => !!content || "Pflichtfeld",
        (content) =>
          (content && content.length <= 1000) ||
          "Der Text darf nur 1000 Charactäre überschreiten",
      ],
    };
  },
  methods: {
    export() {
      return {
        category: "",
        title: "",
        feedback: "",
        organisation: "",
        email: "",
      };
    },
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
