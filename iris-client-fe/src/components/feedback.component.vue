<template>
  <div>
    <v-dialog v-model="show" max-width="40%">
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          v-bind="{ ...$attrs, ...attrs }"
          class="mr-0 mb-0"
          large
          fab
          dark
          fixed
          bottom
          right
          elevation="10"
          :max-height="48"
          :max-width="48"
          v-on="on"
          @click="form.model.name = userDisplayName()"
        >
          <v-icon :size="30"> mdi-chat-alert-outline</v-icon>
          <slot />
        </v-btn>
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
              v-model="form.model.category"
              :items="['Verbesserungsvorschlag', 'Problem']"
              label="Kategorie auswählen*"
              required
              :rules="kategoryRules"
            ></v-autocomplete>

            <v-text-field
              v-model="form.model.title"
              label="Titel*"
              required
              :rules="titelRules"
              maxlength="100"
            ></v-text-field>

            <v-textarea
              v-model="form.model.comment"
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
              v-model="form.model.name"
              class="justify-center"
              label="Name"
              maxlength="100"
            ></v-text-field>

            <v-text-field
              v-model="form.model.organisation"
              class="justify-center"
              label="Organisation"
              required
              maxlength="50"
            ></v-text-field>

            <v-text-field
              v-model="form.model.email"
              class="justify-center"
              label="E-Mail"
              :rules="emailPatternRules"
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
            Bestätigen
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="showFinishedDialog" max-width="20%">
      <v-card style="height: fit-content">
        <v-card-title class="justify-center">
          <span>Danke!</span>
        </v-card-title>
        <v-divider class="theme--light primary" />
        <v-card-text class="mt-3" style="text-align: center">
          Ihr Feedback wurde erfolgreich versendet.
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="showFinishedDialog = false">
            OK
          </v-btn>
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
    <v-dialog v-model="showFinishedDialog" max-width="20%">
      <v-card style="height: fit-content">
        <v-card-title class="justify-center">
          <span>Danke!</span>
        </v-card-title>
        <v-divider class="theme--light primary" />
        <v-card-text class="mt-3" style="text-align: center">
          Ihr Feedback wurde erfolgreich versendet.
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="clearEntries()"> OK </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import {
  Configuration,
  IrisClientFrontendApiFactory,
  FeedbackInsert,
} from "@/api";
import axios, { AxiosResponse } from "axios";
import config from "@/config";

/**
 * The data transfer object
 */
type FeedbackForm = {
  model: FeedbackInsert;
};

/**
 * The Feedbackdialog is a view, only visible if the connected button is pressed. It follows the rules of the dialog and has form rules for being able to be send.
 * The fields categorie, titel and text has to be filled and within letter limit.
 */
@Component
export default class FeedbackDialog extends Vue {
  form: FeedbackForm = {
    model: {
      category: "",
      title: "",
      comment: "",
      organisation: "",
      email: "",
      name: "",
    },
  };
  show = false;
  showCancelDialog = false;
  showConfirmDialog = false;
  showFinishedDialog = false;
  isFormValid = false;
  kategoryRules = [(content: any) => !!content || "Pflichtfeld"];
  titelRules = [
    (content: any) => !!content || "Pflichtfeld",
    (content: string | any[]) =>
      (content && content.length <= 100) ||
      "Der Titel darf nicht mehr als 100 Zeichen beinhalten.",
  ];
  textRules = [
    (content: any) => !!content || "Pflichtfeld",
    (content: string | any[]) =>
      (content && content.length <= 1000) ||
      "Das Feedback darf nicht mehr als 1000 Zeichen beinhalten.",
  ];
  emailPatternRules = [
    (content: string) =>
      !content ||
      /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(content) ||
      "E-mail muss gültig oder leer sein.",
  ];

  /**
   * privately used function to close the feedback dialog
   */
  cancel() {
    this.showCancelDialog = false;
    this.show = false;
  }

  /**
   * privately used function to get the user name to display in the name text field.
   */
  userDisplayName(): string {
    return this.$store.getters["userLogin/userDisplayName"];
  }

  /**
   * privately used function to close the feedback dialog and call the sendFeedback function
   */
  confirm() {
    this.showConfirmDialog = false;
    this.show = false;
    this.sendFeedback();
    this.showFinishedDialog = true;
  }

  /**
   * makes a instance of the api class and attempts to send the feedback to the Server.
   */
  sendFeedback() {
    const authAxiosInstance = axios.create();
    const { apiBaseURL } = config;
    const clientConfig = new Configuration({
      basePath: apiBaseURL,
    });

    const feedbackClient = IrisClientFrontendApiFactory(
      clientConfig,
      undefined,
      authAxiosInstance
    );
    try {
      console.log(this.form.model);
      feedbackClient.feedbackPost(this.form.model);
    } catch (e) {
      console.log("Fehler beim Post");
      throw e;
    }
  }

  /**
   * this method clears all entrys
   */
  clearEntries() {
    this.showFinishedDialog = false;
    this.form.model.category = "";
    this.form.model.title = "";
    this.form.model.comment = "";
    this.form.model.email = "";
    this.form.model.organisation = "";
    this.form.model.name = "";
  }
}
</script>
