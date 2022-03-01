<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <v-sheet>
      <iris-message-data-view
        :disabled="loading"
        v-bind="dataComponentConfig"
        :payload="importSelectionViewPayload"
        @update:target="onSelectTarget"
        @submit="handleSubmit"
        @cancel="dialog = false"
      />
      <error-message-alert class="mb-n4" :errors="errors" />
    </v-sheet>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import {
  Guest,
  IrisMessageDataDiscriminator,
  IrisMessageDataSelectionPayload,
} from "@/api";
import { bundleIrisMessageApi } from "@/modules/iris-message/services/api";
import { ErrorMessage } from "@/utils/axios";
import { getApiErrorMessages, getApiLoading } from "@/utils/api";
import IrisMessageDataView, {
  IrisMessageDataViewSource,
} from "@/modules/iris-message/modules/message-data/components/iris-message-data-view.vue";
import { normalizeGuests } from "@/views/event-tracking-details/event-tracking-details.data";

type IrisMessageDataViewPayload = {
  [IrisMessageDataDiscriminator.EventTracking]: Guest[];
};

type DataViewSource = IrisMessageDataViewSource<IrisMessageDataViewPayload>;

const dataViewSource: DataViewSource = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    normalize: normalizeGuests,
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-message-data.import" */ "../../../modules/event-tracking/modules/message-data/event-tracking-message-data.import.vue"
      ),
  },
};

export interface IrisMessageImportData {
  id: string;
  discriminator?: IrisMessageDataDiscriminator;
}

const IrisMessageDataImportDialogProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<IrisMessageImportData | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    IrisMessageDataView,
    ConfirmDialog,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataImportDialog extends IrisMessageDataImportDialogProps {
  messageDataApi = bundleIrisMessageApi([
    "getMessageDataImportSelectionViewData",
    "importDataAttachmentAndUpdate",
  ]);

  get dialog() {
    return !!this.value;
  }
  set dialog(value) {
    this.$emit("input", null);
  }
  get importSelectionViewPayload() {
    const viewData =
      this.messageDataApi.getMessageDataImportSelectionViewData.state.result;
    return viewData?.payload;
  }
  onSelectTarget(value: string | null) {
    if (this.value?.id && value) {
      this.messageDataApi.getMessageDataImportSelectionViewData.execute(
        this.value?.id,
        value
      );
    }
  }
  get dataComponentConfig() {
    return {
      discriminator: this.value?.discriminator,
      source: dataViewSource,
    };
  }
  get loading(): boolean {
    return getApiLoading(this.messageDataApi);
  }
  get errors(): ErrorMessage[] {
    return getApiErrorMessages(this.messageDataApi);
  }
  async handleSubmit(messageData: {
    target: string;
    selection: IrisMessageDataSelectionPayload;
  }) {
    if (this.value?.id && messageData.target) {
      await this.messageDataApi.importDataAttachmentAndUpdate.execute(
        this.value.id,
        messageData.target,
        messageData.selection
      );
      this.dialog = false;
      this.$emit("import:done");
    }
  }
}
</script>
