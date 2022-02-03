<template>
  <v-form v-bind="$attrs" ref="form" v-model="form.valid" lazy-validation>
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
            <v-input
              :rules="validationRules.defined"
              v-model="form.model.payload"
            >
              <component
                v-if="selectComponent"
                v-bind:is="selectComponent"
                v-model="form.model.payload"
                :description.sync="form.model.description"
              />
            </v-input>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn color="secondary" text @click="cancel"> Abbrechen </v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="submit"> Daten übernehmen </v-btn>
      </v-card-actions>
    </v-card>
  </v-form>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import { AsyncComponent, ComponentOptions, PropType } from "vue";
import {
  IrisMessageDataDiscriminator,
  IrisMessageDataInsert,
  IrisMessageDataInsertPayload,
} from "@/api";
import Discriminators from "@/constants/Discriminators";
import rules from "@/common/validation-rules";
import { parseData } from "@/utils/data";

type DataSelect = {
  [key in IrisMessageDataDiscriminator]: {
    component: ComponentOptions<Vue> | typeof Vue | AsyncComponent;
  };
};

type IrisMessageDataForm = {
  model: {
    discriminator: IrisMessageDataDiscriminator | null;
    payload: IrisMessageDataInsertPayload | null;
    description: string;
  };
  valid: boolean;
};

type DiscriminatorOption = {
  text: string;
  value: IrisMessageDataDiscriminator;
};

const dataSelect: DataSelect = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-select.view" */ "../../../views/event-tracking-select/event-tracking-select.view.vue"
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

@Component
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

  get selectComponent() {
    try {
      if (this.form.model?.discriminator) {
        return dataSelect[this.form.model?.discriminator].component;
      }
    } catch (e) {
      // ignored
    }
    return null;
  }

  submit() {
    console.log("submit", this.form.model);
    if (this.formRef.validate()) {
      this.$emit("submit", parseData(this.form.model));
    }
  }

  cancel() {
    this.$emit("cancel");
  }
}
</script>
