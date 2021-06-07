<template>
  <div class="about">
    <h1 class="mb-6">Über IRIS</h1>

    <v-card class="mb-6">
      <v-card-title>Installation</v-card-title>

      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Version</v-list-item-title>
          <v-list-item-subtitle>{{ version }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Build</v-list-item-title>
          <v-list-item-subtitle>{{ build }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>GitHub</v-list-item-title>
          <v-list-item-subtitle
            ><a href="https://github.com/iris-connect"
              >https://github.com/iris-connect</a
            ></v-list-item-subtitle
          >
        </v-list-item-content>
      </v-list-item>
    </v-card>

    <v-card class="mb-6">
      <v-card-title>Support</v-card-title>

      <v-card-text>
        <p>
          IRIS connect wurde unter dem Dach des Innovationsverbunds Öffentliche
          Gesundheit (InÖG) initiiert und mit der Open-Source Community
          entwickelt. Hauptsponsor und Partner hinter IRIS connect ist die Björn
          Steiger Stiftung, die auch für den verlässlichen Verfahrensbetrieb
          verantwortlich ist.
        </p>
        <p>
          Bei Fragen und Problemen kontaktieren Sie bitte Ihre IT-koordinierende
          Stelle, um technische Probleme an den Support des Betreibers
          weiterzuleiten. Vielen Dank!
        </p>
      </v-card-text>

      <v-list-item two-line v-if="contact.length">
        <v-list-item-content>
          <v-list-item-title>Lokaler Ansprechpartner</v-list-item-title>
          <v-list-item-subtitle v-for="(item, i) in contact" :key="i">
            {{ item }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-row justify="center" align="center">
        <v-col>
          <v-row justify="center" align="center">
            <v-img
              alt="IRIS Logo"
              class="shrink mt-3 mr-4"
              contain
              src="@/assets/logo-inoeg.svg"
              transition="scale-transition"
              height="150"
              width="300"
              max-width="600"
            />
          </v-row>
        </v-col>
        <v-col>
          <v-row justify="center" align="center">
            <v-img
              alt="IRIS Logo"
              class="shrink mt-3 mr-4"
              contain
              src="@/assets/logo-bss.svg"
              transition="scale-transition"
              height="150"
              width="300"
              max-width="600"
            />
          </v-row>
        </v-col>
      </v-row>
    </v-card>

    <v-card>
      <v-card-title>Lizenz</v-card-title>
      <v-card-text> Affero GPL 3.0 </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import config from "@/config";

@Component
export default class AboutView extends Vue {
  version = config.appVersionId || "";
  build = config.appBuildId || "";
  contact = [
    config.localContactPerson.name,
    config.localContactPerson.phone,
    config.localContactPerson.mail,
  ].filter((v) => !!v);
}
</script>
