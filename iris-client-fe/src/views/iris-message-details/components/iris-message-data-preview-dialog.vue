<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <v-card>
      <iris-message-data-view v-bind="dataComponentConfig" />
      <error-message-alert :dense="true" :errors="errors" />
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="secondary" text @click="dialog = false" data-test="close">
          Schließen
        </v-btn>
        <v-spacer></v-spacer>
        <slot name="data-import" v-if="errors.length <= 0" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { bundleIrisMessageApi } from "@/modules/iris-message/api";
import { ErrorMessage } from "@/utils/axios";
import { getApiErrorMessages } from "@/utils/api";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { DataRequestDetails, IrisMessageDataDiscriminator } from "@/api";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";
import IrisMessageDataView, {
  IrisMessageDataViewSource,
} from "@/modules/iris-message/components/iris-message-data-view.vue";

type IrisMessageDataViewPayload = {
  [IrisMessageDataDiscriminator.EventTracking]: DataRequestDetails;
};

type DataViewSource = IrisMessageDataViewSource<IrisMessageDataViewPayload>;

const dataViewSource: DataViewSource = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    normalize: normalizeDataRequestDetails,
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-details.preview" */ "../../event-tracking-message-data/event-tracking-details.preview.vue"
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
  get dataComponentConfig() {
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
