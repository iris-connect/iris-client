<template>
  <component
    v-if="dataComponent && errors.length <= 0"
    v-bind="$attrs"
    v-on="$listeners"
    v-bind:is="dataComponent"
  />
  <error-message-alert v-else :errors="errors" />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { AsyncComponent, ComponentOptions, PropType } from "vue";
import { ErrorMessage } from "@/utils/axios";
import { IrisMessageDataDiscriminator } from "@/api";
import ErrorMessageAlert from "@/components/error-message-alert.vue";

export type IrisMessageDataComponentSource = {
  [key in IrisMessageDataDiscriminator]: {
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
