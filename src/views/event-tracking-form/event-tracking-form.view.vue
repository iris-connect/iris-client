<template>
<v-card>
  <v-card-title>Ereignis-Nachverfolgung starten</v-card-title>
  <v-card-subtitle>DISCLAIMER: DEMO FORMULAR - NICHT FINAL</v-card-subtitle>
  <v-card-text>
    <v-form ref="form" v-model="form.valid" lazy-validation>
      <v-row>
        <v-col
          cols="12"
          md="4"
        >
          <v-text-field
            v-model="form.model.externalId"
            :disabled="eventCreationOngoing"
            :rules="form.rules.defined"
            label="Externe ID"
            width="200px"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-text-field
          v-model="searchTable.search"
          :disabled="eventCreationOngoing"
          :rules="form.rules.defined"
          append-icon="mdi-magnify"
          label="Suche nach Name/Adresse"
          single-line
          hide-details
        ></v-text-field>
      <v-data-table
        :headers="searchTable.headers"
        :items="searchTable.results"
        :items-per-page="5"
        class="elevation-1"
        :search="searchTable.search"
      >
      
        <template v-slot:item.address.combined="{ item }">
          <v-list-item two-line>
            <v-list-item-content>
              <v-list-item-subtitle>{{item.address.street}}</v-list-item-subtitle>
              <v-list-item-content>{{item.address.zip}} {{item.address.city}}</v-list-item-content>
            </v-list-item-content>
          </v-list-item>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-btn
          color="primary"
          @click="selectItem(item)">
            Wählen
          </v-btn>
        </template>
      </v-data-table>
      <v-container>
      <v-row>
        <v-col>
          <v-menu
            v-model="form.showDatePicker"
            :close-on-content-click="false"
            :nudge-right="40"
            lazy
            transition="scale-transition"
            offset-y
            full-width
            max-width="290px"
            min-width="290px"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                label="Datum (Beginn)"
                readonly
                :value="form.model.date"
                v-on="on"
                :rules="form.rules.defined"
                required
                prepend-icon="mdi-calendar"
              ></v-text-field>
            </template>
            <v-date-picker
              locale="en-in"
              v-model="form.model.date"
              no-title
              @input="form.showDatePicker = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col>
          <v-text-field
            v-model="form.model.time.from"
            :disabled="eventCreationOngoing"
            :rules="form.rules.defined"
            label="Uhrzeit (Beginn)"
            prepend-icon="mdi-clock"
            required
          ></v-text-field>
        </v-col>
        <v-col>
          <v-menu
            v-model="form.showDatePicker"
            :close-on-content-click="false"
            :nudge-right="40"
            lazy
            transition="scale-transition"
            offset-y
            full-width
            max-width="290px"
            min-width="290px"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                label="Datum (Ende)"
                readonly
                :value="form.model.date"
                v-on="on"
                :rules="form.rules.defined"
                required
                prepend-icon="mdi-calendar"
              ></v-text-field>
            </template>
            <v-date-picker
              locale="en-in"
              v-model="form.model.date"
              no-title
              @input="form.showDatePicker = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col>
          <v-text-field
            v-model="form.model.time.till"
            :disabled="eventCreationOngoing"
            :rules="form.rules.defined"
            label="Uhrzeit (Ende)"
            required
            prepend-icon="mdi-clock"
          ></v-text-field>
        </v-col>
      </v-row>
      </v-container>
      <v-btn
        class="mt-4"
        color=""
        plain
      >
        Abbrechen
      </v-btn>
      <v-btn
        :disabled="!form.valid || eventCreationOngoing"
        class="mt-4"
        color="primary"
        @click="submit"
      >
        Anfrage senden
      </v-btn>
    </v-form>
  </v-card-text>
</v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "@/store/index";

@Component
export default class EventTrackingFormView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  get eventCreationOngoing(): boolean {
    return store.state.eventTrackingForm.eventCreationOngoing;
  }

  homeRoute = "/";

  form = {
    model: {
      externalId: "",
      date: "",
      name: "",
      time: {
        from: "",
        till: "",
      },
    },
    showDatePicker: false,
    rules: {
      defined: [(v: unknown): string | boolean => !!v || "Pfilichtfeld"],
    },
    valid: false,
  };
  searchTable = {
    search: "",
    headers: [
      {
        text: "Name",
        align: "start",
        sortable: true,
        value: "name",
      },
      { text: "Adresse", value: "address.combined" },
      { text: "Kategorie", value: "category" },
      { text: "Ansprechpartner", value: "contact" },
      { text: "Mail", value: "mail" },
      { text: "Telefon", value: "phone" },
      { text: "", value: "actions", sortable: false },
    ],
    
    results: [
      {
        name: "Bierbrunnen",
        address: {
          street: "Winterhider Allee 10",
          zip: "20249",
          city: "Hamburg",
          combined: "Winterhider Allee 10, 20249 Hamburg",
        },
        category: "Kneipe",
        contact: "Schick, Gerrit",
        mail: "Schick.G@mail.com",
      },
      {
        name: "Friseursalon Schick&Schön",
        address: {
          street: "Hochheimer Straße 1",
          zip: "12356",
          city: "Niederhausen",
          combined: "Hochheimer Straße 1, 12356 Niederhausen",
        },
        street: "Hochheimer Straße 1",
          zip: "12356",
          city: "Niederhausen",
        category: "Körperpflege",
        contact: "Zimmermann, Linda",
        mail: "Z.Lin@mail.com",
        phone: "06181/112241"
      },
    ],
  };
  selectItem(item: {name: string,}){
        alert(item.name)
        //TODO: Nur das ausgewählte Item anzeigen, mit Button "neue Suche"; erst wenn auswahl getroffen formular senden
      }
  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      await store.dispatch("eventTrackingForm/createEventTracking", {
        ...this.form.model,
      });
    }
  }
}
</script>
