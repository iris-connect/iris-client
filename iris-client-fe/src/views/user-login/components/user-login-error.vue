<template>
  <div>
    <v-alert v-if="errorMessage" text type="error" data-test="user-login-error">
      {{ errorMessage }}
    </v-alert>
    <v-progress-linear
      :value="100 - blockedRemainingPercent"
      :active="blockedRemainingPercent > 0"
      color="error"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import dayjs from "@/utils/date";
import { ErrorMessage } from "@/utils/axios";

const unauthorizedRegExp = /Unauthorized/i;
const blockedRegExp = /User blocked! \((.*)\)/i;

const UserLoginErrorProps = Vue.extend({
  props: {
    error: {
      type: String,
      default: "",
    },
  },
});
@Component
export default class UserLoginError extends UserLoginErrorProps {
  blockedUntilMs = 0;
  blockedRemainingPercent = 0;
  countDownTimeout = 0;

  // extracts dateString from error message based on blockedRegExp. returns empty string if error message doesn't match the RegExp.
  get blockedUntil(): string {
    if (this.error) {
      if (blockedRegExp.test(this.error)) {
        return this.error.match(blockedRegExp)?.[1] ?? "";
      }
    }
    return "";
  }

  // parses error message based on error type and block status
  get errorMessage(): ErrorMessage {
    // checks if this is a blocking error -> true if dateString of error message is valid
    if (dayjs(this.blockedUntil).isValid()) {
      const milliseconds =
        (this.blockedUntilMs * this.blockedRemainingPercent) / 100;
      const seconds = Math.round(milliseconds / 1000);
      // checks if user has to wait until blockedUntil date is passed
      if (seconds > 0) {
        // shows error message with remaining block time
        return this.error.replace(
          blockedRegExp,
          `Diese Anmeldedaten sind leider nicht gültig, bitte versuchen Sie es in ${seconds} ${
            seconds === 1 ? "Sekunde" : "Sekunden"
          } noch einmal.`
        );
      }
      // shows generic auth error message if blockedUntil date is passed
      return this.error.replace(
        blockedRegExp,
        "Diese Anmeldedaten sind leider nicht gültig."
      );
    }
    if (this.error && unauthorizedRegExp.test(this.error)) {
      return this.error.replace(
        unauthorizedRegExp,
        "Diese Anmeldedaten sind leider nicht gültig."
      );
    }
    return this.error;
  }

  // handles blockUntil dateString changes.
  // resets count down.
  // starts a new count down if blockUntil is a valid dateString
  @Watch("blockedUntil")
  watchBlockedUntil(newValue: string): void {
    this.reset();
    if (dayjs(newValue).isValid()) {
      this.blockedUntilMs = Math.max(
        0,
        dayjs(newValue).diff(dayjs(), "milliseconds")
      );
      this.countDown();
    }
  }

  // helper function to reset count down
  reset(): void {
    clearTimeout(this.countDownTimeout);
    this.blockedUntilMs = 0;
    this.blockedRemainingPercent = 0;
  }

  // resets count down before component is unmounted
  beforeDestroy(): void {
    this.reset();
  }

  // self calling update function to calculate the remaining block time
  countDown(): void {
    clearTimeout(this.countDownTimeout);
    if (!dayjs(this.blockedUntil).isValid()) {
      return;
    }
    const remainingMs = dayjs(this.blockedUntil).diff(dayjs(), "milliseconds");
    this.blockedRemainingPercent =
      this.blockedUntilMs > 0 ? (remainingMs * 100) / this.blockedUntilMs : 0;
    this.countDownTimeout = setTimeout(() => {
      if (this.blockedRemainingPercent > 0) {
        this.countDown();
      }
    }, 200);
  }
}
</script>
