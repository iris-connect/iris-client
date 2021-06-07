<template>
  <v-menu
    v-if="picker"
    v-model="active"
    :nudge-right="40"
    transition="scale-transition"
    offset-y
  >
    <template v-slot:activator="{ on, attrs }">
      <div class="d-flex align-center">
        <div class="w-100" v-on="on" v-bind="attrs">
          <v-text-field
            slot="activator"
            v-model="dateFormatted"
            prepend-icon="mdi-calendar"
            v-bind="$attrs"
            @blur="date = parseDate(dateFormatted)"
          ></v-text-field>
        </div>
        <v-icon v-if="model" @click="model = ''"> mdi-close </v-icon>
      </div>
    </template>
    <v-date-picker
      locale="de-de"
      v-model="model"
      no-title
      @input="active = false"
    ></v-date-picker>
  </v-menu>
  <v-text-field
    v-else
    v-model="model"
    prepend-icon="mdi-calendar"
    v-bind="$attrs"
    v-on="$listeners"
  ></v-text-field>
</template>

<script lang="ts">
import dayjs from "@/utils/date";
import { Component, Vue } from "vue-property-decorator";

const DateInputFieldProps = Vue.extend({
  props: {
    value: {
      type: String,
      default: "",
    },
    picker: {
      type: Boolean,
      default: true,
    },
  },
});

@Component
export default class DateInputField extends DateInputFieldProps {
  active = false;

  formatDate(date: string): string {
    if (!date) return date;

    return dayjs(date).format("DD.MM.YYYY");
  }

  parseDate(date: string): string | null {
    if (!date) return null;

    const [day, month, year] = date.split(".");
    return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
  }

  get dateFormatted(): string {
    if (this.isGermanFormat(this.value)) {
      return this.value;
    } else {
      if (this.isValidDate(this.value)) {
        return this.formatDate(this.value);
      }
    }
    return this.value;
  }

  set dateFormatted(value: string) {
    if (this.isGermanFormat(value)) {
      this.$emit("input", this.parseDate(value));
    }
  }

  get model(): string {
    return this.value;
  }

  set model(value: string) {
    this.$emit("input", value);
  }

  isGermanFormat(value: string): boolean {
    if (typeof value === "string" && /^\d{2}.\d{2}.\d{4}$/.test(value)) {
      return true;
    } else {
      return false;
    }
  }

  isValidDate(value: string): boolean {
    if (typeof value === "string" && dayjs(value).isValid) {
      return true;
    } else {
      return false;
    }
  }
}
</script>

<style scoped>
.picker-input-field {
  pointer-events: none;
}
.w-100 {
  width: 100%;
}
</style>
