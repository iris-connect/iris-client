<template>
  <v-input
    :value="value"
    :rules="rules"
    v-bind="$attrs"
    @update:error="handleError"
  >
    <v-row class="date-time">
      <v-col cols="12" sm="6">
        <date-input-field
          v-model="date"
          @input="setDateTime"
          :rules="dateTimeRules.date"
          :error="error"
          v-bind="dateProps"
        />
      </v-col>
      <v-col cols="12" sm="6">
        <time-input-field
          v-model="time"
          @input="setDateTime"
          :rules="dateTimeRules.time"
          :error="error"
          v-bind="timeProps"
        />
      </v-col>
    </v-row>
  </v-input>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import DateInputField from "@/components/form/date-input-field.vue";
import TimeInputField from "@/components/form/time-input-field.vue";
import dayjs from "@/utils/date";

const DateTimeInputFieldProps = Vue.extend({
  props: {
    value: {
      type: String,
      default: "",
    },
    dateProps: {
      type: Object,
      default: () => ({}),
    },
    timeProps: {
      type: Object,
      default: () => ({}),
    },
    rules: {
      type: Array,
      default: () => [],
    },
    required: {
      type: Boolean,
      default: false,
    },
  },
});
@Component({
  components: { TimeInputField, DateInputField },
})
export default class DateTimeInputField extends DateTimeInputFieldProps {
  dateTimeRules = {
    date: [
      this.required
        ? (v: unknown): string | boolean =>
            !!v || "Bitte geben Sie ein Datum an"
        : "",
      (v: unknown): string | boolean =>
        !v ||
        (typeof v === "string" && /\d{4}-\d{2}-\d{2}/.test(v)) ||
        "Bitte geben Sie ein Datum im Format YYYY-MM-DD an",
    ].filter((v) => v),
    time: [
      this.required
        ? (v: unknown): string | boolean =>
            !!v || "Bitte geben Sie eine Uhrzeit an"
        : "",
      (v: unknown): string | boolean =>
        !v ||
        (typeof v === "string" && /\d\d:\d\d/.test(v)) ||
        "Bitte geben Sie eine Uhrzeit im Format HH:mm an",
    ].filter((v) => v),
  };

  date = "";
  time = "";
  error = false;

  @Watch("value")
  onChange(newValue: string): void {
    const date = dayjs(newValue);
    if (newValue && date.isValid()) {
      this.date = date.format("YYYY-MM-DD");
      this.time = date.format("HH:mm");
    }
  }

  setDateTime(): void {
    const value = dayjs(this.date + " " + this.time, "YYYY-MM-DD HH:mm");
    const dateString = value.isValid() ? value.toISOString() : "";
    this.$emit("input", dateString);
  }

  handleError(error: boolean): void {
    this.error = error;
  }
}
</script>
