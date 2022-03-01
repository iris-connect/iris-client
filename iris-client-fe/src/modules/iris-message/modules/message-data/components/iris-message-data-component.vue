<template>
  <div>
    <component
      v-bind="$attrs"
      v-on="$listeners"
      v-if="dataComponent"
      v-bind:is="dataComponent"
    />
    <error-message-alert :errors="errors" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { AsyncComponent, ComponentOptions, PropType } from "vue";
import { ErrorMessage } from "@/utils/axios";
import { IrisMessageDataDiscriminator } from "@/api";
import ErrorMessageAlert from "@/components/error-message-alert.vue";

export type IrisMessageDataComponentSource = {
  [key in IrisMessageDataDiscriminator]?: {
    component: ComponentOptions<Vue> | typeof Vue | AsyncComponent;
  };
};

const IrisMessageDataComponentProps = Vue.extend({
  inheritAttrs: false,
  props: {
    discriminator: {
      type: String as PropType<IrisMessageDataDiscriminator | null>,
      default: null,
    },
    source: {
      type: Object as PropType<IrisMessageDataComponentSource | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataComponent extends IrisMessageDataComponentProps {
  get dataComponent() {
    try {
      if (this.discriminator && this.source) {
        return this.source[this.discriminator]?.component;
      }
    } catch (e) {
      // ignored
    }
    return null;
  }
  get errors(): ErrorMessage[] {
    if (!this.discriminator || !this.source) return [];
    return [
      !this.dataComponent
        ? "Die angeforderte Komponente konnte nicht ermittelt werden."
        : null,
    ].filter((v) => v);
  }
}
</script>
