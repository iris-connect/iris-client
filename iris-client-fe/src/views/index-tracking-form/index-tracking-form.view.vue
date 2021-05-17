<template>
  <v-card class="my-3">
    <v-form
      ref="form"
      v-model="form.valid"
      lazy-validation
      :disabled="indexCreationOngoing"
    >
      <v-card-title>Indexfall Erfassung</v-card-title>
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
        <v-row>
          <v-col cols="12" sm="6">
            <v-textarea
              v-model="form.model.comment"
              label="Kommentar"
            ></v-textarea>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <date-time-input-field
              v-model="form.model.start"
              :date-props="{
                label: 'Datum (Beginn)',
              }"
              :time-props="{
                label: 'Uhrzeit (Beginn)',
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
              }"
              :time-props="{
                label: 'Uhrzeit (Ende)',
              }"
              :rules="validationRules.end"
            />
          </v-col>
        </v-row>
        <v-alert v-if="indexCreationError" text type="error">{{
          indexCreationError
        }}</v-alert>
      </v-card-text>
      <v-card-actions>
        <v-btn color="secondary" plain @click="$router.back()">
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn :disabled="indexCreationOngoing" color="primary" @click="submit">
          Daten senden
        </v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store/index";
import { DataRequestCaseClient, DataRequestCaseDetails } from "@/api";
import router from "@/router";
import dayjs from "@/utils/date";
import { ErrorMessage } from "@/utils/axios";
import DateTimeInputField from "@/components/form/date-time-input-field.vue";
import { get as _get, set as _set, has as _has } from "lodash";

type IndexTrackingForm = {
  model: IndexTrackingFormModel;
  valid: boolean;
};

type IndexTrackingFormModel = {
  externalId: string;
  start: string;
  end: string;
  name: string;
  comment: string;
};

type IndexTrackingFormQueryParameters = Partial<
  Omit<IndexTrackingFormModel, "location">
>;

@Component({
  components: {
    DateTimeInputField,
    IndexTrackingFormView: IndexTrackingFormView,
  },
  beforeRouteLeave(to, from, next) {
    store.commit("indexTrackingForm/reset");
    next();
  },
})
export default class IndexTrackingFormView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  get indexCreationOngoing(): boolean {
    return store.state.indexTrackingForm.indexCreationOngoing;
  }

  get indexCreationError(): ErrorMessage {
    return store.state.indexTrackingForm.indexCreationError;
  }

  homeRoute = "/";

  get validationRules(): Record<string, Array<unknown>> {
    return {
      start: [],
      end: [
        (v: string): string | boolean => {
          if (!this.form.model.start) return true;
          if (!this.form.model.end) return true;
          return (
            dayjs(v).isSameOrAfter(dayjs(this.form.model.start), "minute") ||
            "Bitte geben Sie einen Zeitpunkt an, der nach dem Beginn liegt"
          );
        },
      ],
      defined: [(v: unknown): string | boolean => !!v || "Pflichtfeld"],
    };
  }

  form: IndexTrackingForm = {
    model: {
      externalId: "",
      start: "",
      end: "",
      name: "",
      comment: "",
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
    this.validateField("end");
  }

  mounted(): void {
    const query: IndexTrackingFormQueryParameters = this.$route.query;
    Object.keys(this.form.model).forEach((key: string) => {
      const k = key as keyof IndexTrackingFormQueryParameters;
      this.form.model[k] = query[k] || this.form.model[k];
    });
  }

  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;

    if (valid) {
      const payload: DataRequestCaseClient = {
        start: this.form.model.start,
        end: this.form.model.end,
        name: this.form.model.name,
        externalCaseId: this.form.model.externalId,
        comment: this.form.model.comment,
      };
      const created: DataRequestCaseDetails = await store.dispatch(
        "indexTrackingForm/createIndexTracking",
        payload
      );
      router.push({
        name: `index-details`,
        params: {
          caseId: created.caseId || ""
        }, 
        query: {is_created: 'true'}
      });
    }
  }
}
</script>
