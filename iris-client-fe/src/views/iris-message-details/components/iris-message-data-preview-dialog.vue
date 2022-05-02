<template>
  <v-dialog v-model="dialog" scrollable>
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <v-card data-test="message-data.preview.dialog">
      <v-card-text class="pa-0">
        <iris-message-data-view v-bind="dataViewConfig" />
        <error-message-alert :dense="true" :errors="errors" />
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="secondary" text @click="dialog = false" data-test="close">
          Schließen
        </v-btn>
        <v-spacer></v-spacer>
        <slot name="dialog-actions" v-if="errors.length <= 0" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { bundleIrisMessageApi } from "@/modules/iris-message/services/api";
import { ErrorMessage } from "@/utils/axios";
import { getApiErrorMessages } from "@/utils/api";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import {
  DataRequestDetails,
  IrisMessageDataDiscriminator,
  VaccinationReportDetails,
} from "@/api";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";
import IrisMessageDataView, {
  IrisMessageDataViewSource,
} from "@/modules/iris-message/modules/message-data/components/iris-message-data-view.vue";
import { normalizeVaccinationReportDetails } from "@/modules/vaccination-report/services/normalizer";

type IrisMessageDataViewPayload = {
  [IrisMessageDataDiscriminator.EventTracking]: DataRequestDetails;
  [IrisMessageDataDiscriminator.VaccinationReport]: VaccinationReportDetails;
};

type DataViewSource = IrisMessageDataViewSource<IrisMessageDataViewPayload>;

const dataViewSource: DataViewSource = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    normalize: normalizeDataRequestDetails,
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-message-data.preview" */ "../../../modules/event-tracking/modules/message-data/event-tracking-message-data.preview.vue"
      ),
  },
  [IrisMessageDataDiscriminator.VaccinationReport]: {
    normalize: normalizeVaccinationReportDetails,
    component: () =>
      import(
        /* webpackChunkName: "vaccination-report-message-data.preview" */ "../../../modules/vaccination-report/modules/message-data/vaccination-report-message-data.preview.vue"
      ),
  },
};

const IrisMessageDataPreviewDialogProps = Vue.extend({
  props: {
    value: {
      type: String,
      default: null,
    },
  },
});

@Component({
  components: {
    IrisMessageDataView,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataPreviewDialog extends IrisMessageDataPreviewDialogProps {
  messageDataApi = bundleIrisMessageApi(["getMessageDataViewData"]);
  get dialog() {
    return !!this.value;
  }
  set dialog(value) {
    this.$emit("input", null);
  }
  get dataViewConfig() {
    const viewData = this.messageDataApi.getMessageDataViewData.state.result;
    return {
      discriminator: viewData?.discriminator,
      source: dataViewSource,
      payload: viewData?.payload,
    };
  }
  @Watch("value")
  onValueChange(newValue: string) {
    if (newValue) {
      this.messageDataApi.getMessageDataViewData.execute(newValue);
    } else {
      this.messageDataApi.getMessageDataViewData.reset();
    }
  }
  get errors(): ErrorMessage[] {
    return getApiErrorMessages(this.messageDataApi);
  }
}
</script>
