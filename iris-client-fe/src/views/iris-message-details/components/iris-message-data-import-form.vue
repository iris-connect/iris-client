<template>
  <v-form
    v-bind="$attrs"
    ref="form"
    v-model="form.valid"
    lazy-validation
    data-test="message-data.import-form"
  >
    <v-card>
      <v-card-title>Daten importieren</v-card-title>
      <v-card-text>
        <iris-message-data-view
          :disabled="disabled"
          v-bind="dataViewConfig"
          v-model="form.model"
          @update:target="$emit('update:target', $event)"
        />
      </v-card-text>
      <error-message-alert :dense="true" :errors="errors" />
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn
          color="secondary"
          text
          @click="cancel"
          :disabled="disabled"
          data-test="cancel"
        >
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <confirm-dialog
          title="Daten importieren?"
          text="Dieser Vorgang kann nicht rückgäng gemacht werden."
          @confirm="submit"
        >
          <template #activator="{ on, attrs }">
            <v-btn
              color="primary"
              v-on="on"
              v-bind="attrs"
              :disabled="disabled"
              data-test="submit"
            >
              Daten importieren
            </v-btn>
          </template>
        </confirm-dialog>
      </v-card-actions>
    </v-card>
  </v-form>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { IrisMessageDataSelectionPayload } from "@/api";
import { ErrorMessage } from "@/utils/axios";
import IrisMessageDataView, {
  IrisMessageDataViewConfig,
} from "@/modules/iris-message/modules/message-data/components/iris-message-data-view.vue";
import { parseData } from "@/utils/data";
import ConfirmDialog from "@/components/confirm-dialog.vue";

const IrisMessageDataImportFormProps = Vue.extend({
  props: {
    dataViewConfig: {
      type: Object as PropType<IrisMessageDataViewConfig | null>,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    errors: {
      type: Array as PropType<ErrorMessage[] | null>,
      default: null,
    },
  },
});

type MessageDataForm = {
  model: {
    target: string;
    selection: IrisMessageDataSelectionPayload;
  };
  valid: boolean;
};

@Component({
  components: {
    ConfirmDialog,
    IrisMessageDataView,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataImportForm extends IrisMessageDataImportFormProps {
  @Ref("form") readonly formRef!: HTMLFormElement;

  form: MessageDataForm = {
    valid: false,
    model: {
      target: "",
      selection: {},
    },
  };

  submit() {
    if (this.formRef.validate()) {
      this.$emit("submit", parseData(this.form.model));
    }
  }

  cancel() {
    this.$emit("cancel");
  }
}
</script>
