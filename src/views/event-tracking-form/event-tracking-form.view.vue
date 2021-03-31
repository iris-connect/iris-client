<template>
  <v-card>
    <v-form ref="form" v-model="form.valid" lazy-validation>
      <v-card-title>Ereignis-Nachverfolgung starten</v-card-title>
      <v-card-text style="padding-bottom: 0px">
        <v-row>
          <v-col>
            <v-text-field
              v-model="form.model.externalId"
              :disabled="eventCreationOngoing"
              :rules="form.rules.defined"
              label="Externe ID"
              width="200px"
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              v-model="form.model.name"
              :disabled="eventCreationOngoing"
              :rules="form.rules.defined"
              label="Name"
              width="200px"
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col v-if="selectedLocation">
            <span>
              {{ selectedLocation }}
            </span>
          </v-col>
          <v-col>
            <v-dialog v-model="dialog">
              <template v-slot:activator="{ on, attrs }">
                <v-btn color="red lighten-2" dark v-bind="attrs" v-on="on">
                  <span v-if="!selectedLocation"> Addresse auswählen </span>
                  <span v-else> Addresse ändern </span>
                </v-btn>
              </template>

              <v-card>
                <v-card-title class="headline grey lighten-2">
                  Adresse auswählen
                </v-card-title>

                <v-card-text>
                  <v-row class="mt-6 mb-6">
                    <v-col>
                      <v-text-field
                        v-model="locationSearchFormSearchText"
                        :disabled="locationsLoading"
                        append-icon="mdi-magnify"
                        label="Suche nach Name/Adresse"
                        single-line
                        hide-details
                        @keydown.enter="
                          performSearch(locationSearchFormSearchText)
                        "
                      ></v-text-field>
                    </v-col>
                    <v-col>
                      <v-btn
                        :disabled="locationsLoading"
                        @click="performSearch(locationSearchFormSearchText)"
                      >
                        Lokation Suchen
                      </v-btn>
                    </v-col>
                  </v-row>
                  <v-data-table
                    :loading="locationsLoading"
                    :headers="searchTable.headers"
                    :items="locations"
                    :items-per-page="5"
                    class="elevation-1 twolineTable"
                  >
                    <template v-slot:[itemActionSlotName]="{ item }">
                      <v-btn color="primary" @click="selectItem(item)">
                        Wählen
                      </v-btn>
                    </template>
                  </v-data-table>
                </v-card-text>

                <v-divider></v-divider>

                <v-card-actions>
                  <v-btn color="secondary" text @click="dialog = false">
                    Abbrechen
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-col>
        </v-row>
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
              :rules="form.rules.time"
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
                  v-on="on"
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
              :rules="form.rules.time"
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
      </v-card-text>
      <v-card-actions>
        <v-btn class="mt-4" color="secondary" plain @click="$router.back()">
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          :disabled="!form.valid || eventCreationOngoing"
          class="mt-4"
          color="primary"
          @click="submit"
        >
          Anfrage senden
        </v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "@/store/index";
import {
  LocationAddress,
  DataRequestClient,
  LocationInformation,
  DataRequestDetails,
} from "@/api";
import router from "@/router";

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

function getDateWithTime(date: string, time: string): string {
  // TODO check if we need to consider timezones
  const hours = Number(time.split(":")[0]);
  const minutes = Number(time.split(":")[1]);
  const updated = new Date(date);
  updated.setHours(hours);
  updated.setMinutes(minutes);
  return updated.toISOString();
}

@Component
export default class EventTrackingFormView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  dialog = false;

  locationSearchFormSearchText = "";

  get eventCreationOngoing(): boolean {
    return store.state.eventTrackingForm.eventCreationOngoing;
  }

  get locationsLoading(): boolean {
    return store.state.eventTrackingForm.locationsLoading;
  }

  get selectedLocation(): string | null {
    const location = store.state.eventTrackingForm.selectedLocation;
    if (location) {
      return `${location.contact.officialName}, ${getFormattedAddress(
        location.contact.address
      )}`;
    }
    return null;
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
        location,
      };
    });
  }

  homeRoute = "/";

  form = {
    model: {
      externalId: `${Math.floor(Math.random() * 1e20)}`,
      date: new Date().toISOString().split("T")[0],
      dateEnd: new Date().toISOString().split("T")[0],
      name: "123",
      locationId: "123",
      providerId: "345",
      time: {
        from: "12:34",
        till: "23:45",
      },
    },
    showDatePicker: false,
    showDatePickerEnd: false,
    rules: {
      time: [
        (v: string): string | boolean => !!v || "Pflichtfeld",
        (v: string): string | boolean => /\d\d:\d\d/.test(v) || "Format HH:mm",
      ],
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
  // TODO create type for item
  selectItem(item: { location: LocationInformation }): void {
    console.log(item);
    store.commit("eventTrackingForm/setSelectedEventLocations", item.location);
    this.dialog = false;
  }
  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    const location = store.state.eventTrackingForm.selectedLocation;

    if (valid && location) {
      const payload: DataRequestClient = {
        // TODO validate start < end
        start: getDateWithTime(this.form.model.date, this.form.model.time.from),
        end: getDateWithTime(
          this.form.model.dateEnd,
          this.form.model.time.till
        ),
        name: this.form.model.name,
        locationId: location?.id,
        providerId: location?.providerId,
        externalRequestId: this.form.model.externalId,
      };
      const created: DataRequestDetails = await store.dispatch(
        "eventTrackingForm/createEventTracking",
        payload
      );
      router.push({
        name: `event-details`,
        params: {
          id: created.code || "",
        },
      });
    }
  }

  async performSearch(searchText: string): Promise<void> {
    await store.dispatch("eventTrackingForm/fetchEventLocations", searchText);
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
