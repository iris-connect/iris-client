<template>
  <div>
    <h1 class="text-h3 mb-6">Nachverfolgung starten</h1>
    <p>DISCLAIMER: DEMO FORMULAR - NICHT FINAL</p>
    <v-form ref="form" v-model="form.valid" lazy-validation>
      <v-text-field
        v-model="form.model.externalId"
        :disabled="eventCreationOngoing"
        :rules="form.rules.defined"
        label="Externe ID"
        required
      ></v-text-field>

      <v-text-field
        v-model="form.model.name"
        :disabled="eventCreationOngoing"
        :rules="form.rules.defined"
        label="Name/Titel"
        required
      ></v-text-field>

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
            label="Datum"
            readonly
            :value="form.model.date"
            v-on="on"
            :rules="form.rules.defined"
            required
          ></v-text-field>
        </template>
        <v-date-picker
          locale="en-in"
          v-model="form.model.date"
          no-title
          @input="form.showDatePicker = false"
        ></v-date-picker>
      </v-menu>

      <v-text-field
        v-model="form.model.time.from"
        :disabled="eventCreationOngoing"
        :rules="form.rules.defined"
        label="Uhrzeit Von"
        required
      ></v-text-field>

      <v-text-field
        v-model="form.model.time.till"
        :disabled="eventCreationOngoing"
        :rules="form.rules.defined"
        label="Uhrzeit Bis"
        required
      ></v-text-field>

      <v-btn
        :disabled="!form.valid || eventCreationOngoing"
        class="mt-4"
        @click="submit"
      >
        Abschicken
      </v-btn>
    </v-form>
  </div>
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
