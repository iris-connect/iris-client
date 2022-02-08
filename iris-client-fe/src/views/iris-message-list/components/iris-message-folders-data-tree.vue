<template>
  <v-row data-test="message-folders">
    <v-col :cols="loading ? '12' : 'auto'" class="mt-3">
      <v-skeleton-loader :loading="loading" type="sentences">
        <data-tree
          data-test="message-folders-data-tree"
          data-test-key="context"
          :item="{ items: folders }"
          v-model="model"
        />
      </v-skeleton-loader>
    </v-col>
    <v-col v-if="!loading">
      <slot
        name="data-table"
        v-bind="{ context: folder && folder.context }"
      ></slot>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { PropType } from "vue";
import { IrisMessageFolder } from "@/api";
import DataTree from "@/components/data-tree/data-tree.vue";

const extractFolder = (
  folders: IrisMessageFolder[],
  match: [keyof IrisMessageFolder, unknown]
): IrisMessageFolder | undefined => {
  return folders.find((folder: IrisMessageFolder) => {
    const matches = folder[match[0]] === match[1];
    if (!matches && Array.isArray(folder.items) && folder.items.length > 0) {
      return extractFolder(folder.items, match);
    }
    return matches;
  });
};

const IrisMessageFoldersDataTreeProps = Vue.extend({
  inheritAttrs: false,
  props: {
    value: {
      type: String,
      default: "",
    },
    folders: {
      type: Array as PropType<IrisMessageFolder[] | null>,
      default: null,
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
});
@Component({
  components: {
    DataTree,
  },
})
export default class IrisMessageFoldersDataTree extends IrisMessageFoldersDataTreeProps {
  get folder(): IrisMessageFolder | undefined {
    if (!this.value) return undefined;
    return extractFolder(this.folders || [], ["id", this.value]);
  }
  get defaultFolder(): IrisMessageFolder | undefined {
    return extractFolder(this.folders || [], ["isDefault", true]);
  }
  get model() {
    return this.value;
  }
  set model(value: string) {
    this.$emit("input", value);
  }
  @Watch("defaultFolder", { immediate: true })
  onDefaultFolderChange(newValue: IrisMessageFolder | undefined) {
    if (!this.model && newValue?.id) {
      this.model = newValue.id;
    }
  }
}
</script>
