<template>
  <div :data-test="dataTest ? dataTest : false">
    <div v-if="!root" class="d-flex align-center">
      <v-icon
        :disabled="!hasItems"
        @click="open = !open"
        data-test="toggle.children"
      >
        {{ showItems ? "mdi-folder-open" : "mdi-folder" }}
      </v-icon>
      <v-btn
        class="ml-2"
        text
        :input-value="value === item.id"
        @click="handleSelect(item.id)"
        :data-test="`select.${item[dataTestKey] || 'item'}`"
      >
        {{ item.name }}
      </v-btn>
    </div>
    <div v-show="root || showItems" :class="{ 'ml-3': !root }">
      <data-tree
        v-for="(child, index) in item.items"
        :key="index"
        :item="child"
        :value="value"
        @input="handleSelect"
        :root="false"
        :data-test-key="dataTestKey"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";

export interface DataTreeItem {
  id: string;
  name?: string;
  items?: DataTreeItem[];
}

const DataTreeProps = Vue.extend({
  inheritAttrs: false,
  props: {
    value: {
      type: String,
      default: "",
    },
    item: {
      type: Object as PropType<DataTreeItem | null>,
      default: null,
    },
    root: {
      type: Boolean,
      default: true,
    },
    dataTest: {
      type: String,
      default: "",
    },
    dataTestKey: {
      type: String,
      default: "",
    },
  },
});

@Component({
  name: "data-tree",
  components: {
    DataTree,
  },
})
export default class DataTree extends DataTreeProps {
  open = false;
  get hasItems() {
    return (
      Array.isArray(this.item?.items) && (this.item?.items || []).length > 0
    );
  }
  get showItems() {
    return this.open && this.hasItems;
  }
  handleSelect(value: string) {
    this.$emit("input", value);
  }
}
</script>
