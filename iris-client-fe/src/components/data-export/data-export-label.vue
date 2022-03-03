<script lang="ts">
/* eslint-disable   @typescript-eslint/no-explicit-any */
import { Component, Vue } from "vue-property-decorator";
import { getExportLabel } from "@/utils/data-export/common";
import { PropType } from "vue";

const DataExportLabelProps = Vue.extend({
  inheritAttrs: false,
  props: {
    selected: {
      type: Number,
      default: 0,
    },
    total: {
      type: Number,
      default: 0,
    },
    label: {
      type: Array as PropType<string[] | null>,
      default: null,
    },
  },
  render() {
    const defaultSlot = this.$scopedSlots.default;
    if (!defaultSlot) return;
    return defaultSlot({
      exportLabel: getExportLabel(this.selected, this.total, this.label),
    }) as any;
  },
});
@Component({
  name: "data-export-label",
})
export default class DataExportLabel extends DataExportLabelProps {}
</script>
