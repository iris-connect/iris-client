<template>
  <v-card class="my-3">
    <v-form
      ref="form"
      v-model="form.valid"
      lazy-validation
      :disabled="eventCreationOngoing"
    >
      <v-card-title>Ereignis-Nachverfolgung starten</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" sm="6">
            <v-text-field
              v-model="form.model.externalId"
              :rules="validationRules.defined"
              label="Externe ID"
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="6">
            <v-text-field v-model="form.model.name" label="Name"></v-text-field>
          </v-col>
        </v-row>
        <location-select-dialog
          v-model="form.model.location"
          :locationList="locationList"
          :disabled="locationsLoading"
          :error="locationsError"
          @search="handleLocationSearch"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-row>
              <v-col v-if="form.model.location">
                <event-tracking-form-location-info
                  :location="form.model.location"
                />
              </v-col>
              <v-col>
                <v-input
                  v-model="form.model.location"
                  :rules="validationRules.location"
                >
                  <v-btn
                    color="red lighten-2"
                    dark
                    v-bind="attrs"
                    v-on="on"
                    :disabled="eventCreationOngoing"
                  >
                    {{
                      form.model.location
                        ? "Ereignisort ändern"
                        : "Ereignisort auswählen"
                    }}
                  </v-btn>
                </v-input>
              </v-col>
            </v-row>
          </template>
        </location-select-dialog>
        <v-row>
          <v-col cols="12" md="6">
            <date-time-input-field
              v-model="form.model.start"
              :date-props="{
                label: 'Datum (Beginn)',
                max: maxStartDate,
              }"
              :time-props="{
                label: 'Uhrzeit (Beginn)',
                max: maxStartTime,
              }"
              :rules="validationRules.start"
              required
            />
          </v-col>
          <v-col cols="12" md="6">
            <date-time-input-field
              v-model="form.model.end"
              :date-props="{
                label: 'Datum (Ende)',
                min: minEndDate,
              }"
              :time-props="{
                label: 'Uhrzeit (Ende)',
              }"
              :rules="validationRules.end"
              required
            />
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-textarea
              v-model="form.model.requestDetails"
              name="requestComment"
              label="Anfragendetails für den Betrieb"
              auto-grow
              rows="1"
              value=""
              hint="Datenschutz-Hinweis: Die Anfragendetails werden an den Betrieb übermittelt!"
            ></v-textarea>
          </v-col>
        </v-row>
        <v-alert v-if="eventCreationError" text type="error">{{
          eventCreationError
        }}</v-alert>
      </v-card-text>
      <v-card-actions>
        <v-btn color="secondary" plain @click="$router.back()">
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn :disabled="eventCreationOngoing" color="primary" @click="submit">
          Anfrage senden
        </v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store/index";
import {
  DataRequestClient,
  LocationInformation,
  DataRequestDetails,
  LocationList,
} from "@/api";
import router from "@/router";
import LocationSelectDialog from "@/views/event-tracking-form/components/location-select-dialog.vue";
import dayjs from "@/utils/date";
import { ErrorMessage } from "@/utils/axios";
import DateTimeInputField from "@/components/form/date-time-input-field.vue";
import { get as _get, set as _set, has as _has } from "lodash";
import EventTrackingFormLocationInfo from "@/views/event-tracking-form/components/event-tracking-form-location-info.vue";
import rules from "@/common/validation-rules";
import { DataQuery } from "@/api/common";

type EventTrackingForm = {
  model: EventTrackingFormModel;
  valid: boolean;
};

type EventTrackingFormModel = {
  externalId: string;
  start: string;
  end: string;
  name: string;
  location: LocationInformation | null;
  requestDetails: string;
};

type EventTrackingFormQueryParameters = Partial<
  Omit<EventTrackingFormModel, "location">
>;

@Component({
  components: {
    EventTrackingFormLocationInfo,
    DateTimeInputField,
    LocationSelectDialog,
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

  minEndDate = "";

  get maxStartDate(): string {
    return dayjs().format("YYYY-MM-DD");
  }

  get maxStartTime(): string {
    return this.form.model.start &&
      dayjs(this.form.model.start).isSame(dayjs(), "day")
      ? dayjs().format("HH:mm")
      : "";
  }

  get eventCreationOngoing(): boolean {
    return store.state.eventTrackingForm.eventCreationOngoing;
  }

  get eventCreationError(): ErrorMessage {
    return store.state.eventTrackingForm.eventCreationError;
  }

  get locationsLoading(): boolean {
    return store.state.eventTrackingForm.locationsLoading;
  }

  get locationsError(): ErrorMessage {
    return store.state.eventTrackingForm.locationsError;
  }

  get locationList(): LocationList | null {
    return store.state.eventTrackingForm.locationList;
  }

  async handleLocationSearch(query: DataQuery): Promise<void> {
    await store.dispatch("eventTrackingForm/fetchEventLocations", query);
  }

  homeRoute = "/";

  get validationRules(): Record<string, Array<unknown>> {
    return {
      start: [],
      end: [
        (v: string): string | boolean => {
          if (!this.form.model.start) return true;
          return (
            dayjs(v).isSameOrAfter(dayjs(this.form.model.start), "minute") ||
            "Bitte geben Sie einen Zeitpunkt an, der nach dem Beginn liegt"
          );
        },
      ],
      defined: [rules.defined],
      location: [
        (v: LocationInformation): string | boolean => {
          return !!v || "Bitte wählen Sie einen Ereignisort aus";
        },
      ],
    };
  }

  form: EventTrackingForm = {
    model: {
      externalId: "",
      start: "",
      end: "",
      name: "",
      location: null,
      requestDetails: "",
    },
    valid: false,
  };

  /**
   * super ugly hack but vuetify doesn't support field level validation
   * we should consider using something like VeeValidate
   * @param field
   */
  validateField(field: string): void {
    if (_has(this.form.model, field)) {
      const val = _get(this.form.model, field);
      _set(this.form.model, field, "");
      this.$nextTick(() => {
        _set(this.form.model, field, val);
      });
    }
  }

  @Watch("form.model.start")
  onDateChanged(): void {
    this.form.model.end = dayjs(this.form.model.start).endOf("day").toString();
    this.minEndDate = dayjs(this.form.model.start).format("YYYY-MM-DD");
    this.validateField("end");
  }

  mounted(): void {
    const query: EventTrackingFormQueryParameters = this.$route.query;
    Object.keys(this.form.model).forEach((key: string) => {
      const k = key as keyof EventTrackingFormQueryParameters;
      this.form.model[k] = query[k] || this.form.model[k];
    });
  }

  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    const location = this.form.model.location;
    if (valid && location) {
      const payload: DataRequestClient = {
        start: this.form.model.start,
        end: this.form.model.end,
        name: this.form.model.name,
        locationId: location.id,
        providerId: location.providerId,
        externalRequestId: this.form.model.externalId,
        requestDetails: this.form.model.requestDetails,
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
        query: { is_created: "true" },
      });
    }
  }
}
</script>
