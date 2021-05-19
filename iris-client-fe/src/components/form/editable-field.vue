<template>
  <v-form v-model="isValid" class="flex-fill" :disabled="disabled">
    <v-hover v-if="!isEditing" v-slot="{ hover }">
      <div
        @click="isEditing = true"
        :class="['d-flex editable-button', hover ? 'hover' : '']"
      >
        <div>
          <slot v-bind="{ entry: model || '-' }" />
        </div>
        <v-spacer />
        <v-icon class="ml-3">mdi-pencil</v-icon>
      </div>
    </v-hover>
    <template v-else>
      <component
        v-bind="$attrs"
        :value="format(model)"
        @input="onValueChange"
        v-bind:is="component"
        :append-icon="isEdited() ? 'mdi-undo-variant' : ''"
        @click:append="reset"
        :append-outer-icon="isValid ? 'mdi-check' : ''"
        @click:append-outer="submit"
        hide-details="auto"
        :error-messages="error"
        outlined
      ></component>
    </template>
  </v-form>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { VTextField, VTextarea } from "vuetify/lib";
import { getErrorMessage } from "@/utils/axios";
import { AxiosError } from "axios";

const EditableFieldProps = Vue.extend({
  props: {
    component: {
      type: String,
      default: "v-text-field",
    },
    value: {
      type: String,
      default: "",
    },
    name: {
      type: String,
      default: "",
    },
    defaultValue: {
      type: String,
      default: "",
    },
  },
});

@Component({
  components: {
    VTextField,
    VTextarea,
  },
})
export default class EditableField extends EditableFieldProps {
  isEditing = false;

  isValid = false;

  disabled = false;

  error: string | null = "";

  model = "";

  isEdited(): boolean {
    return this.normalize(this.value) !== this.model;
  }

  // formats the model value to be displayed in the field input.
  format(value: string): string {
    return value === this.defaultValue ? "" : value;
  }

  // normalizes the value (coming from field input or attribute) before it is applied to the model value
  normalize(value: string): string {
    return value || this.defaultValue;
  }

  @Watch("value", { immediate: true })
  onValueChange(value: string): void {
    this.model = this.normalize(value);
  }

  reset(): void {
    this.model = this.normalize(this.value);
    this.error = "";
    this.disabled = false;
    this.isEditing = false;
  }

  submit(): void {
    if (!this.isEdited()) {
      return this.reset();
    }
    this.error = "";
    this.disabled = true;
    const payload: Record<string, string> = {
      [this.name]: this.model,
    };
    const resolve = () => {
      this.disabled = false;
      this.isEditing = false;
    };
    const reject = (error: AxiosError | string) => {
      this.disabled = false;
      this.error = getErrorMessage(error);
    };
    this.$emit("submit", payload, resolve, reject);
  }
}
</script>
<style scoped lang="scss">
.editable-button {
  cursor: pointer;
  border-bottom: 1px solid transparent;
  &.hover {
    border-bottom-color: rgba(0, 0, 0, 0.12);
  }
}
</style>
