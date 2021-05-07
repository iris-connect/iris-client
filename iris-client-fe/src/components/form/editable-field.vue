<template>
  <v-form v-model="isValid" class="flex-fill" :disabled="disabled">
    <v-hover v-if="!isEditing" v-slot="{ hover }">
      <div
        @click="isEditing = true"
        :class="['d-flex editable-button', hover ? 'hover' : '']"
      >
        <div>
          <slot v-bind="{ entry: value || '-' }" />
        </div>
        <v-spacer />
        <v-icon class="ml-3">mdi-pencil</v-icon>
      </div>
    </v-hover>
    <template v-else>
      <component
        v-model="model"
        v-bind:is="component"
        :append-icon="value !== model ? 'mdi-undo-variant' : ''"
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

const EditableFieldProps = Vue.extend({
  props: {
    component: {
      type: String,
      default: "v-text-field",
    },
    errors: {
      type: Array,
      default: () => [],
    },
    value: {
      type: String,
      default: "",
    },
    name: {
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

  model = "";

  disabled = false;

  // @todo: find better solution for duplicate error handling: Vuex store & local state
  error: string | undefined = "";

  @Watch("value")
  onValueChange(value: string): void {
    this.model = value;
  }

  reset(): void {
    this.model = this.value;
    this.error = "";
    this.disabled = false;
    this.isEditing = false;
  }

  submit(): void {
    if (this.model === this.value) {
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
    const reject = (error: string | undefined) => {
      this.disabled = false;
      this.error = error;
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
    border-bottom-color: #000;
  }
}
</style>
