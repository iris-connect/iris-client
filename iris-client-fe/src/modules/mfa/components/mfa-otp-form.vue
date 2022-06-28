<template>
  <v-form
    ref="form"
    v-model="formIsValid"
    lazy-validation
    :disabled="disabled"
    @submit.prevent
  >
    <v-card-text>
      <p>
        Bitte bestätigen Sie Ihre Identität über den Pin-Code aus der
        Authenticator App.
      </p>
      <v-row>
        <v-col cols="12">
          <v-text-field
            :disabled="disabled || error"
            autofocus
            v-model="formModel.otp"
            :rules="validationRules.sanitisedAndDefined"
            label="Bestätigungscode"
            @keyup.native.enter="submit"
            data-test="mfaCode"
          ></v-text-field>
        </v-col>
      </v-row>
      <user-login-error :error="error" />
    </v-card-text>
    <v-card-actions>
      <v-btn
        :disabled="disabled"
        color="secondary"
        plain
        @click="$emit('cancel')"
        data-test="cancel"
      >
        {{ error ? "Erneut anmelden" : "Abbrechen" }}
      </v-btn>
      <v-spacer></v-spacer>
      <v-btn
        v-if="!error"
        :disabled="disabled"
        color="primary"
        @click="submit"
        data-test="submit"
      >
        Bestätigen
      </v-btn>
    </v-card-actions>
  </v-form>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { PropType } from "vue";
import { ErrorMessage } from "@/utils/axios";
import rules from "@/common/validation-rules";
import UserLoginError from "@/views/user-login/components/user-login-error.vue";
import { MfaVerification } from "@/api";
const MfaOtpFormProps = Vue.extend({
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
export default class MfaOtpForm extends MfaOtpFormProps {
  $refs!: {
    form: HTMLFormElement;
  };
  formIsValid = true;
  formModel: MfaVerification = {
    otp: "",
  };
  get validationRules(): Record<string, Array<unknown>> {
    return {
      sanitisedAndDefined: [rules.defined, rules.sanitised],
    };
  }
  @Watch("formModel.otp")
  onFormModelChange(newValue: string) {
    if (newValue.length >= 6) {
      this.submit();
    }
  }
  submit(): void {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      this.$emit("submit", this.formModel);
    }
  }
}
</script>
