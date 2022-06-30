<template>
  <v-dialog v-model="dialog" scrollable content-class="fill-height">
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <iris-message-data-import-form
      v-if="dialog"
      :import-data="value"
      :disabled="loading"
      :errors="errors"
      :data-view-config="dataViewConfig"
      @update:target="onUpdateTarget"
      @submit="onSubmit"
      @cancel="dialog = false"
    />
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import {
  IrisMessageDataDiscriminator,
  IrisMessageDataSelectionPayload,
} from "@/api";
import { bundleIrisMessageApi } from "@/modules/iris-message/services/api";
import { ErrorMessage } from "@/utils/axios";
import { getApiErrorMessages, getApiLoading } from "@/utils/api";
import { IrisMessageDataViewSource } from "@/modules/iris-message/modules/message-data/components/iris-message-data-view.vue";
import {
  EventTrackingMessageDataImportSelection,
  normalizeEventTrackingMessageDataImportSelection,
} from "@/modules/event-tracking/modules/message-data/services/normalizer";
import IrisMessageDataImportForm from "@/views/iris-message-details/components/iris-message-data-import-form.vue";
import {
  normalizeVaccinationReportMessageDataImportSelection,
  VaccinationReportMessageDataImportSelection,
} from "@/modules/vaccination-report/modules/message-data/services/normalizer";

/**
 * Please provide a user-interface for selecting which data should be updated on an existing entry in the database.
 *
 * For implementation, follow these steps:
 * 1. Create a typed normalizer for the MessageDataImportSelection
 * 2. Add the type to the IrisMessageDataViewPayload type
 * 3. Create a new vue component for the user to select the data (s)he wants to import
 * 4. Add a new entry to the DataViewSource containing the normalizer and the vue component
 *
 * The normalizer usually contains a combination of selectables and duplicates.
 * It is used to cleanup the ImportSelectionViewData, provided by the API.
 *
 * The vue component has to provide a validated user-interface for selecting the ID of the import target Object.
 * After an import target is selected, the user has to be able to define which elements of the source Object s(he) wants to import.
 * The processed value has to be an Object, containing ...
 * ... a "target" property, representing the ID of the target Object
 * ... a "selection" property for the elements of the source Object, the user wants to import. Please use the "messageDataSelectId" to identify collection items of the selection.
 * If the ID of the target Object is changed, make sure to reset the "selection" and emit the new ID, using "@update:target".
 * "@update:target" will trigger a fetch of the ImportSelectionViewData, usually containing a list of selectables and duplicates (if present).
 * As soon as the user has selected all required information, mark the value as valid. This will enable the import Button.
 */

type IrisMessageDataViewPayload = {
  [IrisMessageDataDiscriminator.EventTracking]: EventTrackingMessageDataImportSelection;
  [IrisMessageDataDiscriminator.VaccinationReport]: VaccinationReportMessageDataImportSelection;
};

export type DataViewSource =
  IrisMessageDataViewSource<IrisMessageDataViewPayload>;

const dataViewSource: DataViewSource = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    normalize: normalizeEventTrackingMessageDataImportSelection,
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-message-data.import" */ "../../../modules/event-tracking/modules/message-data/event-tracking-message-data.import.vue"
      ),
  },
  [IrisMessageDataDiscriminator.VaccinationReport]: {
    normalize: normalizeVaccinationReportMessageDataImportSelection,
    component: () =>
      import(
        /* webpackChunkName: "vaccination-report-message-data.import" */ "../../../modules/vaccination-report/modules/message-data/vaccination-report-message-data.import.vue"
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
    IrisMessageDataImportForm,
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

  onUpdateTarget(value: string | null) {
    if (this.value?.id && value) {
      this.messageDataApi.getMessageDataImportSelectionViewData.execute(
        this.value?.id,
        value
      );
    }
  }

  get dataViewConfig() {
    const viewData =
      this.messageDataApi.getMessageDataImportSelectionViewData.state.result;
    return {
      discriminator: this.value?.discriminator,
      source: dataViewSource,
      payload: viewData?.payload,
    };
  }

  get loading(): boolean {
    return getApiLoading(this.messageDataApi);
  }
  get errors(): ErrorMessage[] {
    return getApiErrorMessages(this.messageDataApi);
  }

  async onSubmit(messageData: {
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
