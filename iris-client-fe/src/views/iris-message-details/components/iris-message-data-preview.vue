<template>
  <div>
    <iris-message-data-component
      v-bind="dataComponentConfig"
      :data="previewPayload"
    />
    <slot v-bind="{ errors }" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { AsyncComponent, ComponentOptions, PropType } from "vue";
import {
  IrisMessageDataDiscriminator,
  IrisMessageDataViewPayload,
  IrisMessageViewData,
} from "@/api";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";
import { ErrorMessage } from "@/utils/axios";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import IrisMessageDataComponent from "@/modules/iris-message/components/iris-message-data-component.vue";

type DataPreview = {
  [key in IrisMessageDataDiscriminator]: {
    normalize: (
      source?: IrisMessageDataViewPayload[key],
      parse?: boolean
    ) => IrisMessageDataViewPayload[key];
    component: ComponentOptions<Vue> | typeof Vue | AsyncComponent;
  };
};

const dataPreviewSource: DataPreview = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    normalize: normalizeDataRequestDetails,
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-details.preview" */ "../../event-tracking-message-data/event-tracking-details.preview.vue"
      ),
  },
};

const IrisMessageDataPreviewProps = Vue.extend({
  props: {
    viewData: {
      type: Object as PropType<IrisMessageViewData | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    IrisMessageDataComponent,
    ConfirmDialog,
  },
})
export default class IrisMessageDataPreview extends IrisMessageDataPreviewProps {
  get previewId() {
    return this.viewData?.id;
  }
  get previewPayload() {
    try {
      if (this.viewData?.discriminator) {
        return dataPreviewSource[this.viewData?.discriminator].normalize(
          this.viewData.payload
        );
      }
    } catch (e) {
      // ignored
    }
    return null;
  }
  get dataComponentConfig() {
    return {
      source: dataPreviewSource,
      discriminator: this.viewData?.discriminator,
    };
  }
  get errors(): ErrorMessage[] {
    if (!this.viewData) return [];
    return [
      this.previewPayload === null
        ? "Der Datensatz wurde nicht gefunden oder konnte nicht verarbeitet werden."
        : null,
    ].filter((v) => v);
  }
}
</script>
