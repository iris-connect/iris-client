<template>
  <v-card>
    <v-card-title>Ereignis-Nachverfolgung starten</v-card-title>
    <v-card-subtitle>DISCLAIMER: DEMO FORMULAR - NICHT FINAL</v-card-subtitle>
    <v-card-text>
      <v-form ref="form" v-model="form.valid" lazy-validation>
        <v-row>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="form.model.externalId"
              :disabled="eventCreationOngoing"
              :rules="form.rules.defined"
              label="Externe ID"
              width="200px"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="form.model.name"
              :disabled="eventCreationOngoing"
              :rules="form.rules.defined"
              label="Name"
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
          :items="locations"
          :items-per-page="5"
          class="elevation-1"
          :search="searchTable.search"
        >
          <template v-slot:[itemActionSlotName]="{ item }">
            <v-btn color="primary" @click="selectItem(item)"> Wählen </v-btn>
          </template>
        </v-data-table>
        <v-container>
          <v-row>
            <v-col>
              <v-menu
                v-model="form.showDatePicker"
                :close-on-content-click="false"
                :nudge-right="40"
                transition="scale-transition"
                offset-y
                max-width="290px"
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    label="Datum (Beginn)"
                    readonly
                    :value="form.model.date"
                    @click="on"
                    :rules="form.rules.defined"
                    required
                    prepend-icon="mdi-clock"
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
                v-model="form.showDatePickerEnd"
                :close-on-content-click="false"
                :nudge-right="40"
                transition="scale-transition"
                offset-y
                max-width="290px"
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    label="Datum (Ende)"
                    readonly
                    :value="form.model.dateEnd"
                    @click="on"
                    :rules="form.rules.defined"
                    required
                    prepend-icon="mdi-calendar"
                  ></v-text-field>
                </template>
                <v-date-picker
                  locale="en-in"
                  v-model="form.model.dateEnd"
                  no-title
                  @input="form.showDatePickerEnd = false"
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
          <v-row>
            <v-col>
              <v-textarea
                name="requestComment"
                label="Anfragendetails für den Betrieb"
                auto-grow
                rows="1"
                value=""
                hint="Datenschutz-Hinweis: Die Anfragendetails werden an den Betrieb übermittelt!"
              ></v-textarea>
            </v-col>
          </v-row>
        </v-container>
        <v-btn class="mt-4" color="" plain> Abbrechen </v-btn>
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
import { LocationAddress } from "@/api";

type LocationInformationTableRow = {
  address: string;
  category: string;
  contactPerson: string;
  mail: string;
  phone: string;
};

function getFormattedAddress(address: LocationAddress) {
  return `${address.street}, ${address.zip} ${address.city}`;
}

@Component({
  beforeRouteEnter: async (_from, _to, next) => {
    await store.dispatch("eventTrackingForm/fetchEventLocations", "");
    next();
  },
})
export default class EventTrackingFormView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  get eventCreationOngoing(): boolean {
    return store.state.eventTrackingForm.eventCreationOngoing;
  }

  get locationsLoading(): boolean {
    return store.state.eventTrackingForm.locationsLoading;
  }

  get locations(): LocationInformationTableRow[] {
    return (store.state.eventTrackingForm.locations || []).map((location) => {
      return {
        name: location.contact.officialName,
        address: getFormattedAddress(location.contact.address) || "-",
        category: location.contact.officialName || "-",
        contactPerson: location.contact.representative || "-",
        mail: location.contact.email || location.contact.ownerEmail || "-",
        phone: location.contact.email || location.contact.ownerEmail || "-",
      };
    });
  }

  homeRoute = "/";

  form = {
    model: {
      externalId: Math.floor(Math.random() * 1e20),
      date: new Date().toDateString(),
      dateEnd: new Date().toDateString(),
      name: "123",
      time: {
        from: "12:34",
        till: "23:45",
      },
    },
    showDatePicker: false,
    showDatePickerEnd: false,
    rules: {
      defined: [(v: unknown): string | boolean => !!v || "Pflichtfeld"],
    },
    valid: false,
  };
  searchTable = {
    search: "rep",
    headers: [
      {
        text: "Name",
        align: "start",
        sortable: true,
        value: "name",
      },
      { text: "Adresse", value: "address" },
      { text: "Kategorie", value: "category" },
      { text: "Ansprechpartner", value: "contactPerson" },
      { text: "Mail", value: "mail" },
      { text: "Telefon", value: "phone" },
      { text: "", value: "actions", sortable: false },
    ],
  };
  selectItem(item: { name: string }): void {
    alert(item.name);
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

  on(): void {
    console.log("NOT IMPLEMENTED");
  }

  // TODO improve this - we need it to circumvent v-slot eslint errors
  // https://stackoverflow.com/questions/61344980/v-slot-directive-doesnt-support-any-modifier
  get itemActionSlotName(): string {
    return "item.actions";
  }
}
</script>
