<template>
  <v-menu
    v-if="picker"
    ref="menu"
    v-model="active"
    :nudge-right="40"
    :close-on-content-click="false"
    transition="scale-transition"
    offset-y
  >
    <template v-slot:activator="{ on, attrs }">
      <div class="d-flex align-center">
        <div class="w-100" v-on="on" v-bind="attrs">
          <v-text-field
            v-model="model"
            prepend-icon="mdi-clock"
            v-bind="$attrs"
          ></v-text-field>
        </div>
        <v-icon v-if="model" @click="model = ''"> mdi-close </v-icon>
      </div>
    </template>
    <v-time-picker
      locale="de-de"
      v-model="model"
      ampm-in-title
      format="24hr"
      @change="active = false"
      @click:minute="$refs.menu.save(model)"
    ></v-time-picker>
  </v-menu>
  <v-text-field
    v-else
    v-model="model"
    prepend-icon="mdi-clock"
    v-bind="$attrs"
    v-on="$listeners"
  ></v-text-field>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const TimeInputFieldProps = Vue.extend({
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
export default class TimeInputField extends TimeInputFieldProps {
  active = false;

  get model(): string {
    return this.value;
  }

  set model(value: string) {
    if (this.isTimeFormat(value)) {
      this.$emit("input", value);
    }

    if (value == "") {
      this.$emit("input", value);
    }
  }

  isTimeFormat(value: string): boolean {
    if (typeof value === "string" && /^\d{2}:\d{2}$/.test(value)) {
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
