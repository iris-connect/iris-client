<template>
  <v-card>
    <v-form
      ref="form"
      v-model="form.valid"
      lazy-validation
      :disabled="eventCreationOngoing"
    >
      <v-card-title>Ereignis-Nachverfolgung starten</v-card-title>
      <v-card-text style="padding-bottom: 0px">
        <v-row>
          <v-col>
            <v-text-field
              v-model="form.model.externalId"
              :rules="form.rules.defined"
              label="Externe ID"
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              v-model="form.model.name"
              :rules="form.rules.defined"
              label="Name"
            ></v-text-field>
          </v-col>
        </v-row>
        <location-select-dialog
          v-model="form.model.location"
          :locations="locations"
          :disabled="locationsLoading"
          @search="handleLocationSearch"
        >
          <template v-slot:activator="{ on, attrs, selectedFormattedLocation }">
            <v-row>
              <v-col v-if="selectedFormattedLocation">
                <span>
                  {{ selectedFormattedLocation }}
                </span>
              </v-col>
              <v-col>
                <v-input
                  v-model="form.model.location"
                  :rules="form.rules.location"
                >
                  <v-btn
                    color="red lighten-2"
                    dark
                    v-bind="attrs"
                    v-on="on"
                    :disabled="eventCreationOngoing"
                  >
                    {{
                      selectedFormattedLocation
                        ? "Lokation ändern"
                        : "Lokation auswählen"
                    }}
                  </v-btn>
                </v-input>
              </v-col>
            </v-row>
          </template>
        </location-select-dialog>
        <v-row>
          <v-col cols="12" sm="6" md="3">
            <date-input-field
              v-model="form.model.date"
              label="Datum (Beginn)"
              :rules="form.rules.defined"
              required
            />
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <time-input-field
              v-model="form.model.time.from"
              label="Uhrzeit (Beginn)"
              :rules="form.rules.time"
              required
            />
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <date-input-field
              v-model="form.model.dateEnd"
              label="Datum (Ende)"
              :rules="form.rules.defined"
              required
            />
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <time-input-field
              v-model="form.model.time.till"
              label="Uhrzeit (Ende)"
              :rules="form.rules.time"
              required
            />
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
  DataRequestClient,
  LocationInformation,
  DataRequestDetails,
} from "@/api";
import router from "@/router";
import TimeInputField from "@/views/event-tracking-form/components/form/time-input-field.vue";
import DateInputField from "@/views/event-tracking-form/components/form/date-input-field.vue";
import LocationSelectDialog from "@/views/event-tracking-form/components/location-select-dialog.vue";

function getDateWithTime(date: string, time: string): string {
  // TODO check if we need to consider timezones
  const hours = Number(time.split(":")[0]);
  const minutes = Number(time.split(":")[1]);
  const updated = new Date(date);
  updated.setHours(hours);
  updated.setMinutes(minutes);
  return updated.toISOString();
}

@Component({
  components: {
    LocationSelectDialog,
    DateInputField,
    TimeInputField,
    EventTrackingFormView: EventTrackingFormView,
  },
  beforeRouteLeave(to, from, next) {
    store.commit("eventTrackingForm/reset");
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

  get locations(): LocationInformation[] {
    return store.state.eventTrackingForm.locations;
  }

  async handleLocationSearch(searchText: string): Promise<void> {
    await store.dispatch("eventTrackingForm/fetchEventLocations", searchText);
  }

  homeRoute = "/";

  form = {
    model: {
      externalId: "",
      date: "",
      dateEnd: "",
      name: "",
      time: {
        from: "",
        till: "",
      },
      location: null,
    },
    rules: {
      time: [
        (v: string): string | boolean => !!v || "Pflichtfeld",
        (v: string): string | boolean => /\d\d:\d\d/.test(v) || "Format HH:mm",
      ],
      defined: [(v: unknown): string | boolean => !!v || "Pflichtfeld"],
      location: [
        (v: LocationInformation): string | boolean => {
          return !!v || "Bitte wählen Sie eine Lokation aus";
        },
      ],
    },
    valid: false,
  };

  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      const payload: DataRequestClient = {
        // TODO validate start < end
        start: getDateWithTime(this.form.model.date, this.form.model.time.from),
        end: getDateWithTime(
          this.form.model.dateEnd,
          this.form.model.time.till
        ),
        name: this.form.model.name,
        locationId: this.form.model.location?.id,
        providerId: this.form.model.location?.providerId,
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
}
</script>
