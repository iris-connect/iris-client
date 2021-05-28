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

      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>E-Mail</v-list-item-title>
          <v-list-item-subtitle
            ><a :href="mailTo">{{ mail }}</a></v-list-item-subtitle
          >
        </v-list-item-content>
      </v-list-item>

      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Telefon-Hotline</v-list-item-title>
          <v-list-item-subtitle>{{ phone }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-list-item two-line v-if="localContact">
        <v-list-item-content>
          <v-list-item-title>Lokaler Ansprechpartner</v-list-item-title>
          <v-list-item-subtitle>{{ localContact }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
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
  version = config.appVersionId;
  build = config.appBuildId;
  localContact = config.localContactPerson;

  // TODO verify mail + phone
  mail = "iris@steiger-stiftung.de";
  mailTo = `mailTo${this.mail}`;
  phone = "0800 123 123 123";
}
</script>
