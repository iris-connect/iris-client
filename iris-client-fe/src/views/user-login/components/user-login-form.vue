<template>
  <v-form
    ref="form"
    v-model="formIsValid"
    lazy-validation
    :disabled="disabled"
    @submit.prevent
  >
    <v-card-title>Anmelden</v-card-title>
    <v-card-text>
      <v-row>
        <v-col cols="12">
          <v-text-field
            v-model="formModel.userName"
            :rules="validationRules.sanitisedAndDefined"
            label="Anmeldename"
            data-test="userName"
          ></v-text-field>
        </v-col>
        <v-col cols="12">
          <v-text-field
            v-model="formModel.password"
            :rules="validationRules.password"
            label="Passwort"
            type="password"
            @keyup.native.enter="submit"
            data-test="password"
          ></v-text-field>
        </v-col>
      </v-row>
      <user-login-error :error="error" />
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn
        :disabled="disabled"
        color="primary"
        @click="submit"
        data-test="submit"
      >
        Anmelden
      </v-btn>
    </v-card-actions>
  </v-form>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { Credentials } from "@/api";
import { PropType } from "vue";
import { ErrorMessage } from "@/utils/axios";
import UserLoginError from "@/views/user-login/components/user-login-error.vue";
import rules from "@/common/validation-rules";

const UserLoginFormProps = Vue.extend({
  inheritAttrs: false,
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
    error: {
      type: String as PropType<ErrorMessage>,
      default: "",
    },
  },
});
@Component({
  components: {
    UserLoginError,
  },
})
export default class UserLoginForm extends UserLoginFormProps {
  $refs!: {
    form: HTMLFormElement;
  };
  formIsValid = true;
  formModel: Credentials = {
    userName: "",
    password: "",
  };
  get validationRules(): Record<string, Array<unknown>> {
    return {
      sanitisedAndDefined: [rules.defined, rules.sanitised],
      password: [rules.defined, rules.sanitised],
    };
  }
  submit(): void {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      this.$emit("submit", this.formModel);
    }
  }
}
</script>
