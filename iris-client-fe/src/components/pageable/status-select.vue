<template>
  <v-btn-toggle dense mandatory class="flex-wrap" v-model="model">
    <v-btn
      text
      v-for="status in selectable"
      :key="status"
      :data-test="getStatusTestLabel(status)"
    >
      {{ getStatusSelectLabel(status) }}
    </v-btn>
    <v-btn text :data-test="getStatusSelectLabel()">
      {{ getStatusSelectLabel() }}
    </v-btn>
  </v-btn-toggle>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { DataRequestStatus } from "@/api";
import StatusMessages from "@/constants/StatusMessages";
import StatusTestLabel from "@/constants/StatusTestLabel";
import { PropType } from "vue";
import { Dictionary } from "vue-router/types/router";

const StatusSelectProps = Vue.extend({
  inheritAttrs: false,
  props: {
    selectableStatus: {
      type: Object as PropType<Dictionary<DataRequestStatus>>,
      default: () => DataRequestStatus,
    },
    value: {
      type: String as PropType<DataRequestStatus>,
    },
  },
});

@Component
export default class StatusSelect extends StatusSelectProps {
  getStatusSelectLabel(status?: DataRequestStatus): string {
    if (!status) return "Alle";
    return StatusMessages.getMessage(status);
  }
  getStatusTestLabel(status?: DataRequestStatus): string {
    if (!status) return "status.select.all";
    return `status.select.${StatusTestLabel.getStatusTestLabel(status)}`;
  }
  get selectable() {
    const dict: Dictionary<DataRequestStatus> = this.selectableStatus;
    return Object.values(dict);
  }
  get model(): number {
    if (!this.value) {
      // return length + 1, assuming "Alle" button is displayed last
      return Object.keys(this.selectable).length;
    }
    return this.selectable.indexOf(this.value);
  }
  set model(value: number) {
    this.$emit("input", this.selectable[value]);
  }
}
</script>
