<template>
  <div v-if="isActive">
    <slot
      v-bind="{
        disabled: isDisabled,
        rules: isDisabled ? [] : rules,
      }"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { FieldConfig } from "@/views/admin-user-edit/admin-user-edit.view.vue";

const ConditionalFieldProps = Vue.extend({
  props: {
    config: {
      type: Object as () => FieldConfig,
    },
    rules: {
      type: Array,
      default: () => [],
    },
  },
});

@Component
export default class ConditionalField extends ConditionalFieldProps {
  get fieldConfig(): FieldConfig {
    return {
      show: this.config?.show !== false,
      edit: this.config?.edit !== false,
    };
  }
  get isActive(): boolean {
    return this.config?.show !== false;
  }
  get isDisabled(): boolean {
    return this.config?.edit === false;
  }
}
</script>

<style scoped></style>
