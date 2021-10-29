<template>
  <span class="d-flex align-center justify-end">
    <v-progress-circular
      v-if="loading"
      indeterminate
      color="primary"
    ></v-progress-circular>
    <span v-else>
      <v-icon v-if="status === appStatus.ERROR" color="error">
        mdi-alert-octagon
      </v-icon>
      <v-icon v-if="status === appStatus.WARNING" color="warning">
        mdi-alert
      </v-icon>
      <v-icon v-if="status === appStatus.OK" color="success">
        mdi-check-circle
      </v-icon>
      <v-icon v-if="status === appStatus.UNKNOWN"> mdi-help-circle </v-icon>
    </span>
  </span>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { CheckinAppStatus } from "@/api";

const CheckinAppStatusIndicatorProps = Vue.extend({
  inheritAttrs: false,
  props: {
    loading: {
      type: Boolean,
      default: false,
    },
    status: {
      type: String as () => CheckinAppStatus,
      default: CheckinAppStatus.UNKNOWN,
    },
  },
});

@Component
export default class CheckinAppStatusIndicator extends CheckinAppStatusIndicatorProps {
  appStatus = CheckinAppStatus;
}
</script>
