<template>
  <v-file-input
    v-bind="$attrs"
    :value="model"
    @change="handleChange"
    @click:clear="clear"
    multiple
  >
    <template v-slot:selection="{ index, text }">
      <v-chip
        :key="index"
        dark
        color="blue"
        close
        @click:close="removeFile(index)"
      >
        {{ text }}
      </v-chip>
    </template>
  </v-file-input>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { PropType } from "vue";
import fileUtil from "@/utils/fileUtil";
import _unionBy from "lodash/unionBy";
import { Base64File } from "@/api";

const FileInputFieldProps = Vue.extend({
  inheritAttrs: false,
  props: {
    value: {
      type: Array as PropType<Base64File[] | null>,
      default: null,
    },
  },
});

@Component
export default class FileInputField extends FileInputFieldProps {
  model: File[] = [];
  @Watch("value")
  onValueChange(newValue: Base64File[] | null) {
    this.model = (newValue || []).map((item) => {
      return fileUtil.base64ToFile(item.content, item.name || "");
    });
  }
  async handleChange(files: Array<File>) {
    const base64Files = await Promise.all(
      files.map(async (file) => {
        return {
          name: file.name,
          content: await fileUtil.fileToBase64(file),
        };
      })
    );
    this.$emit("input", _unionBy(this.value, base64Files, "name"));
  }
  removeFile(index: number) {
    const base64Files = this.value ? [...this.value] : [];
    base64Files.splice(index);
    this.$emit("input", base64Files);
  }
  clear() {
    this.$emit("input", []);
  }
}
</script>
