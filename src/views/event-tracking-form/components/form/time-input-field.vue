<template>
  <v-menu
    v-if="picker"
    ref="menu"
    :return-value.sync="model"
    v-model="active"
    :nudge-right="40"
    :close-on-content-click="false"
    transition="scale-transition"
    offset-y
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-model="model"
        prepend-icon="mdi-clock"
        readonly
        v-bind="{
          ...attrs,
          ...$attrs,
        }"
        v-on="on"
      ></v-text-field>
    </template>
    <v-time-picker
      locale="de-de"
      v-model="model"
      ampm-in-title
      @input="active = false"
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
import { Vue } from "vue-property-decorator";

const TimeInputFieldProps = Vue.extend({
  props: {
    value: {
      type: String,
      default: "",
    },
    picker: {
      type: Boolean,
      default: false,
    },
  },
});

export default class TimeInputField extends TimeInputFieldProps {
  active = false;

  get model(): string {
    return this.value;
  }

  set model(value: string) {
    this.$emit("input", value);
  }
}
</script>
