<template>
  <div>
    <iris-message-data-component
      v-bind="$attrs"
      v-on="$listeners"
      :source="source"
      :discriminator="discriminator"
      :payload="normalizedPayload"
    />
    <error-message-alert :errors="errors" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { ErrorMessage } from "@/utils/axios";
import { IrisMessageDataDiscriminator } from "@/api";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import IrisMessageDataComponent, {
  IrisMessageDataComponentSource,
} from "@/modules/iris-message/modules/message-data/components/iris-message-data-component.vue";
import { DataNormalizer } from "@/utils/data";

type IrisMessageDataNormalizePayloadSource<
  P extends { [Key in IrisMessageDataDiscriminator]: P[Key] }
> = {
  [Key in IrisMessageDataDiscriminator]: {
    normalize: DataNormalizer<P[Key]>;
  };
};

type PayloadSource = { [Key in IrisMessageDataDiscriminator]: unknown };

export type IrisMessageDataViewSource<
  P extends { [Key in IrisMessageDataDiscriminator]: P[Key] }
> = IrisMessageDataNormalizePayloadSource<P> & IrisMessageDataComponentSource;

export interface IrisMessageDataViewConfig {
  discriminator?: IrisMessageDataDiscriminator;
  source?: IrisMessageDataViewSource<PayloadSource>;
  payload?: unknown;
}

const IrisMessageDataViewProps = Vue.extend({
  inheritAttrs: false,
  props: {
    discriminator: {
      type: String as PropType<IrisMessageDataDiscriminator | null>,
      default: null,
    },
    payload: {
      type: [Object, Array],
      default: null,
    },
    source: {
      type: Object as PropType<IrisMessageDataViewSource<PayloadSource> | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    IrisMessageDataComponent,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataView extends IrisMessageDataViewProps {
  get normalizedPayload() {
    try {
      if (this.source && this.discriminator && this.payload) {
        return this.source[this.discriminator].normalize(this.payload);
      }
    } catch (e) {
      // ignored
    }
    return null;
  }
  get errors(): ErrorMessage[] {
    if (!this.discriminator || !this.source || !this.payload) return [];
    return [
      !this.normalizedPayload
        ? "Der Datensatz wurde nicht gefunden oder konnte nicht verarbeitet werden."
        : null,
    ].filter((v) => v);
  }
}
</script>
