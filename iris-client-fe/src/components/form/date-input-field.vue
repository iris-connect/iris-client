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
            class="picker-input-field"
            v-model="model"
            prepend-icon="mdi-calendar"
            readonly
            v-bind="$attrs"
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

  get model(): string {
    return this.value;
  }

  set model(value: string) {
    this.$emit("input", value);
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
