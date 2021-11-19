<template>
  <v-text-field
    v-model="model"
    append-icon="mdi-magnify"
    :label="`Suchbegriff (min. ${minLength} Buchstaben)`"
    single-line
    hide-details
  ></v-text-field>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import _debounce from "lodash/debounce";
import { DebouncedFunc } from "lodash";

const SearchFieldProps = Vue.extend({
  inheritAttrs: false,
  props: {
    value: {
      type: String,
      default: "",
    },
    debounce: {
      type: Number,
      default: 1000,
    },
    minLength: {
      type: Number,
      default: 2,
    },
  },
});

@Component
export default class SearchField extends SearchFieldProps {
  debounced: DebouncedFunc<() => void> | null = null;
  get model() {
    return this.value;
  }
  set model(value: string) {
    if (this.debounced) {
      this.debounced.cancel();
      this.debounced = null;
    }
    this.debounced = _debounce(() => {
      let search = value?.trim();
      if (!search || search.length >= this.minLength) {
        this.$emit("input", search || undefined);
      }
    }, this.debounce);
    this.debounced();
  }
}
</script>
