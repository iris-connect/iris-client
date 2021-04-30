<template>
  <v-row>
    <v-dialog style="overflow-y: hidden" v-model="show" max-width="40%">
      <v-card>
        <v-card-title class="justify-center">
          <span class="headline">FEEDBACK BOGEN</span>
        </v-card-title>

        <v-card-subtitle style="text-align: center">
          <span class="headline">Wir freuen uns über ihr Feedback.</span>
        </v-card-subtitle>

        <v-container>
          <v-col class="justify-content:center" cols="auto">
            <v-autocomplete
              :items="[
                'Skiing',
                'Ice hockey',
                'Soccer',
                'Basketball',
                'Hockey',
                'Reading',
                'Writing',
                'Coding',
                'Basejump',
              ]"
              label="Katergorie auswählen"
              multiple
              outlined
            ></v-autocomplete>

            <v-text-field
              label="Titel*"
              outlined
              required
              counter
              maxlength="100"
            ></v-text-field>

            <v-textarea
              class="justify-center"
              label="Ihr Platz für Feedback"
              outlined
              max-height="200px"
              no-resize
              counter
              maxlength="1000"
            ></v-textarea>

            <v-text-field
              class="justify-center"
              label="Organisation*"
              required
              outlined
              maxlength="50"
            ></v-text-field>

            <v-row>
              <v-col cols="12" sm="6">
                <v-text-field
                  class="justify-center"
                  label="First name*"
                  required
                  outlined
                  maxlength="25"
                ></v-text-field>
              </v-col>

              <v-col cols="12" sm="6">
                <v-text-field
                  class="justify-center"
                  label="Last name*"
                  outlined
                  maxlength="25"
                ></v-text-field>
              </v-col>
            </v-row>

            <v-text-field
              class="justify-center"
              label="E-Mail*"
              required
              outlined
              maxlength="80"
            ></v-text-field>

            <v-file-input outlined accept="image/*" label="File input">
            </v-file-input>
          </v-col>
        </v-container>

        <v-row>
          <v-col class="d-flex justify-center">
            <v-btn
              color="blue darken-1"
              text
              outlined
              @click="showCancelDialog = true"
            >
              Abbrechen
            </v-btn>
          </v-col>
          <v-col class="d-flex justify-center">
            <v-btn
              color="green accent-2"
              outlined
              text
              @click="showConfirmDialog = true"
            >
              Absenden
            </v-btn>
          </v-col>
        </v-row>
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
          <v-btn color="primary" text @click="cancel()"> Schliesen </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      style="overflow-y: hidden"
      v-model="showConfirmDialog"
      max-width="20%"
    >
      <v-card>
        <v-card-title>
          <span>Bestätigen</span>
          <v-spacer></v-spacer>
          Sind sie sich Sicher, dass sie den Feedback Bogen abschicken
          wollen.Die Daten werden in einem GitHub Issue gespeichert und es wird
          ihnen ein Link zum Issue zu ihrer E-Mail Addresse gesendet.
        </v-card-title>
        <v-card-actions>
          <v-btn color="primary" text @click="showConfirmDialog = false">
            Abbrechen
          </v-btn>
          <v-btn color="primary" text @click="confirm()"> Abschicken </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
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
