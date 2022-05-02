<template>
  <div v-if="errorMessages.length > 0" :class="{ 'mt-4': !dense }">
    <v-alert
      :class="{ 'mb-0': dense }"
      v-for="(errorMessage, index) in errorMessages"
      text
      type="error"
      :key="index"
      >{{ errorMessage }}
    </v-alert>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import _castArray from "lodash/castArray";

const ErrorMessageAlertProps = Vue.extend({
  props: {
    errors: {
      type: [Array, String],
      default: () => [],
    },
    dense: {
      type: Boolean,
      default: false,
    },
  },
});

@Component
export default class ErrorMessageAlert extends ErrorMessageAlertProps {
  get errorMessages(): Array<unknown> {
    return _castArray(this.errors).filter((e) => {
      return typeof e === "string" && e.length > 0;
    });
  }
}
</script>

<style scoped></style>
