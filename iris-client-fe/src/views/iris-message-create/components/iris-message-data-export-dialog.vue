<template>
  <v-dialog v-model="dialog" max-width="500" scrollable>
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }">
        <v-btn
          color="primary"
          class="ml-2"
          v-on="on"
          v-bind="attrs"
          data-test="message-data-export-dialog.activator"
          :disabled="disabled"
        >
          {{ label || "Daten senden" }}
        </v-btn>
      </slot>
    </template>
    <v-form ref="form" v-model="form.valid" lazy-validation>
      <v-card data-test="message-data-export-dialog">
        <v-card-title> Daten senden </v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <v-text-field
                v-model="form.model.description"
                label="Kurzbeschreibung"
                :rules="validationRules.description"
                maxlength="255"
                data-test="description"
              ></v-text-field>
              <v-text-field
                disabled
                v-model="form.model.discriminator"
                label="Datentyp"
                :rules="validationRules.discriminator"
                maxlength="255"
                data-test="description"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-btn
            color="secondary"
            text
            @click="dialog = false"
            data-test="close"
          >
            Abbrechen
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="sendMessage" :disabled="disabled">
            Senden
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Ref, Vue, Watch } from "vue-property-decorator";
import { IrisMessageDataInsert, IrisMessageDataSelectionPayload } from "@/api";
import { PropType } from "vue";
import rules from "@/common/validation-rules";

type IrisMessageDataForm = {
  model: {
    discriminator: string;
    payload: IrisMessageDataSelectionPayload;
    description: string;
  };
  valid: boolean;
};

const IrisMessageDataExportDialogProps = Vue.extend({
  props: {
    data: {
      type: Object as PropType<IrisMessageDataInsert | null>,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    label: {
      type: String,
      default: null,
    },
  },
});

@Component({
  components: {},
})
export default class IrisMessageDataExportDialog extends IrisMessageDataExportDialogProps {
  @Ref("form") readonly formRef!: HTMLFormElement;

  get validationRules(): Record<string, Array<unknown>> {
    return {
      description: [rules.defined, rules.sanitised, rules.maxLength(255)],
    };
  }

  dialog = false;

  get form(): IrisMessageDataForm {
    return {
      valid: false,
      model: {
        discriminator: this.data?.discriminator || "",
        payload: this.data?.payload || {},
        description: this.data?.description || "",
      },
    };
  }

  @Watch("dialog")
  async onDialogChange(activated: boolean) {
    await this.$nextTick();
    if (activated && this.validateForm()) {
      this.sendMessage();
    }
  }

  validateForm(): boolean {
    if (!this.formRef) return false;
    return this.formRef.validate() as boolean;
  }

  sendMessage(): void {
    if (this.validateForm()) {
      this.dialog = false;
      this.$store.commit("irisMessageCreate/setDataAttachments", [
        this.form.model,
      ]);
      this.$router.push({ name: "iris-message-create" });
    }
  }
}
</script>

<style scoped></style>
