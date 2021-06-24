<template>
  <div>
    <v-dialog v-model="show" max-width="40%">
      <template v-slot:activator="{ on }">
        <slot name="activator" v-bind="{ on }"></slot>
      </template>
      <v-card style="height: fit-content">
        <v-form v-model="isFormValid">
          <div>
            <v-card-title class="justify-center">
              <span class="headline">FEEDBACK BOGEN</span>
            </v-card-title>

            <v-card-subtitle style="text-align: center">
              <span class="subtitle-1">Wir freuen uns über Ihr Feedback.</span>
            </v-card-subtitle>
            <v-divider class="theme--light primary" />
          </div>
          <v-container class="mt-1 px-sm-15">
            <v-autocomplete 
              v-model="category"
              :items="['Verbesserungsvorschlag', 'Problem']"
              label="Katergorie auswählen*"
              required
              :rules="kategoryRules"
            ></v-autocomplete>

            <v-text-field
              v-model="title"
              label="Titel*"
              required
              :rules="titelRules"
              maxlength="100"
            ></v-text-field>

            <v-textarea
              v-model="feedback"
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
              v-model="organisation"
              class="justify-center"
              label="Organisation"
              required
              maxlength="50"
            ></v-text-field>

            <v-text-field
              v-model="email"
              class="justify-center"
              label="E-Mail"
              required
              maxlength="80"
            ></v-text-field>
          </v-container>
          <v-divider class="theme--light primary" />

          <v-container>
            <v-row>
              <v-btn
                class="ma-4"
                color="secondary"
                plain
                @click="showCancelDialog = true"
              >
                Abbrechen
              </v-btn>
              <v-spacer></v-spacer>

              <v-btn
                class="ma-4"
                color="primary"
                :disabled="!isFormValid"
                @click="showConfirmDialog = true"
              >
                Absenden
              </v-btn>
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
      <v-card style="height: fit-content">
        <v-card-title class="justify-center">
          <span>ACHTUNG!</span>
        </v-card-title>
        <v-divider class="theme--light primary" />
        <v-card-text class="mt-3">
          Wenn Sie auf "Bestätigen" klicken, schließen Sie den Feedback-Bogen.
        </v-card-text>
        <v-card-actions>
          <v-btn
            class="mt-4"
            color="secondary"
            plain
            text
            @click="showCancelDialog = false"
          >
            Zurück
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn class="mt-4" color="primary" @click="cancel()">
            Bestätigen</v-btn
          >
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
        <v-divider class="theme--light primary" />
        <v-card-text class="mt-3">
          Wenn Sie Ihr Feedback abschicken möchten, dann klicken Sie auf
          "Absenden".
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            class="mt-4"
            color="secondary"
            plain
            text
            :disabled="!isFormValid"
            @click="showConfirmDialog = false"
          >
            Zurück
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="confirm()" class="mt-4">
            Absenden
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
//TODO useage of class

import { Component, Vue } from "vue-property-decorator";

@Component
export default class FeedbackDialog extends Vue {
  category = "";
  title = "";
  feedback = "";
  organisation = "";
  email = "";
  name = "userLogin/userDisplayName";
  show = false;
  showCancelDialog = false;
  showConfirmDialog = false;
  isFormValid = false;
  kategoryRules = [(content) => !!content || "Pflichtfeld"];
  titelRules = [
    (content) => !!content || "Pflichtfeld",
    (content) =>
      (content && content.length <= 100) ||
      "Der Titel darf nicht mehr als 100 Zeichen beinhalten.",
  ];
  textRules = [
    (content) => !!content || "Pflichtfeld",
    (content) =>
      (content && content.length <= 1000) ||
      "Das Feedback darf nicht mehr als 1000 Zeichen beinhalten.",
  ];

  cancel() {
    this.showCancelDialog = false;
    this.show = false;
  }

  confirm() {
    this.showConfirmDialog = false;
    this.show = false;
  }

  exportData() {
    return {"category":this.category, 
                    "title":this.title, 
                    "comment":this.feedback,
                    "name":"nutzername", 
                    "email":this.email, 
                    "organisation":this.organisation
    } 
  }

}
</script>
