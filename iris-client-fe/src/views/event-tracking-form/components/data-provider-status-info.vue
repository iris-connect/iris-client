<template>
  <v-progress-linear
    class="my-3"
    v-if="!!statusInfo.loading"
    indeterminate
  ></v-progress-linear>
  <div v-else>
    <v-alert v-if="alertType" text :type="alertType" class="text-body-2 my-3">
      <p>
        Eine Anfrage über den App Anbieter dieses Ereignisortes ist derzeit ggf.
        nicht möglich.
        <span class="d-block" v-if="statusInfo.message">
          Grund: {{ statusInfo.message }}
        </span>
      </p>
      Bitte versuchen Sie es zu einem späteren Zeitpunkt erneut oder wenden Sie
      sich an den Support, falls Ihre Anfrage fehl schlagen sollte.
    </v-alert>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { AppStatusInfo } from "@/views/checkin-app-status-list/checkin-app-status-list.store";
import { CheckinAppStatus } from "@/api";
import { PropType } from "vue";

const DataProviderStatusInfoProps = Vue.extend({
  inheritAttrs: false,
  props: {
    statusInfo: {
      type: Object as PropType<AppStatusInfo>,
      default: () => ({ status: CheckinAppStatus.UNKNOWN }),
    },
  },
});

@Component
export default class DataProviderStatusInfo extends DataProviderStatusInfoProps {
  appStatus = CheckinAppStatus;
  get alertType(): string {
    switch (this.statusInfo.status) {
      case CheckinAppStatus.ERROR:
        return "error";
      case CheckinAppStatus.WARNING:
        return "warning";
      default:
        return "";
    }
  }
}
</script>
