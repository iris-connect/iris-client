<template>
  <div v-if="errorMessages.length > 0" class="mt-4">
    <v-alert
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

const ErrorMessageAlertProps = Vue.extend({
  props: {
    errors: {
      type: Array,
      default: () => [],
    },
  },
});

@Component
export default class ErrorMessageAlert extends ErrorMessageAlertProps {
  get errorMessages(): Array<unknown> {
    return this.errors.filter((e) => {
      return typeof e === "string" && e.length > 0;
    });
  }
}
</script>

<style scoped></style>
