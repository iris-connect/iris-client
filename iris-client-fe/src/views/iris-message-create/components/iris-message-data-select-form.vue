<template>
  <v-form
    v-bind="$attrs"
    ref="form"
    v-model="form.valid"
    lazy-validation
    data-test="message-data.export-form"
  >
    <v-card>
      <v-card-title> Daten wählen </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <v-select
              label="Datentyp"
              :items="discriminatorOptions"
              v-model="form.model.discriminator"
              :rules="validationRules.defined"
              data-test="discriminator"
              :menu-props="{
                contentClass: 'select-menu-message-data-discriminator',
              }"
            />
          </v-col>
          <v-col cols="12">
            <v-text-field
              v-model="form.model.description"
              label="Kurzbeschreibung"
              :rules="validationRules.description"
              maxlength="255"
              data-test="description"
            ></v-text-field>
          </v-col>
          <v-col cols="12">
            <iris-message-data-component
              v-bind="dataComponentConfig"
              v-model="form.model.payload"
              :description.sync="form.model.description"
              :disabled="disabled"
            />
          </v-col>
        </v-row>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="secondary" text @click="cancel" data-test="cancel">
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="submit" data-test="submit">
          Daten übernehmen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-form>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import {
  IrisMessageDataDiscriminator,
  IrisMessageDataInsert,
  IrisMessageDataSelectionPayload,
} from "@/api";
import Discriminators from "@/constants/Discriminators";
import rules from "@/common/validation-rules";
import { parseData } from "@/utils/data";
import IrisMessageDataComponent, {
  IrisMessageDataComponentSource,
} from "@/modules/iris-message/modules/message-data/components/iris-message-data-component.vue";

type IrisMessageDataForm = {
  model: {
    discriminator: IrisMessageDataDiscriminator | null;
    payload: IrisMessageDataSelectionPayload | null;
    description: string;
  };
  valid: boolean;
};

type DiscriminatorOption = {
  text: string;
  value: IrisMessageDataDiscriminator;
};

const dataComponentSource: IrisMessageDataComponentSource = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-message-data.export" */ "../../../modules/event-tracking/modules/message-data/event-tracking-message-data.export.vue"
      ),
  },
};

const getDiscriminatorSelectOptions = (): DiscriminatorOption[] => {
  return Object.keys(IrisMessageDataDiscriminator).map((key) => {
    const k = key as keyof typeof IrisMessageDataDiscriminator;
    return {
      text: Discriminators.getLabel(IrisMessageDataDiscriminator[k]),
      value: IrisMessageDataDiscriminator[k],
    };
  });
};

const IrisMessageDataSelectFormProps = Vue.extend({
  props: {
    formValues: {
      type: Object as PropType<IrisMessageDataInsert | null>,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
});
@Component({
  components: {
    IrisMessageDataComponent,
  },
})
export default class IrisMessageDataSelectForm extends IrisMessageDataSelectFormProps {
  @Ref("form") readonly formRef!: HTMLFormElement;

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [rules.defined],
      description: [rules.defined, rules.sanitised, rules.maxLength(255)],
    };
  }

  discriminatorOptions: DiscriminatorOption[] = getDiscriminatorSelectOptions();

  form: IrisMessageDataForm = {
    valid: false,
    model: {
      discriminator:
        this.formValues?.discriminator ||
        (this.discriminatorOptions?.length === 1
          ? this.discriminatorOptions[0].value
          : null),
      payload: this.formValues?.payload || null,
      description: this.formValues?.description || "",
    },
  };

  get dataComponentConfig() {
    return {
      source: dataComponentSource,
      discriminator: this.form.model?.discriminator,
    };
  }

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
