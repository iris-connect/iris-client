<template>
  <v-layout justify-center>
    <v-card class="my-3">
      <v-form
        ref="form"
        v-model="formIsValid"
        lazy-validation
        :disabled="isDisabled"
      >
        <v-card-title>Anmelden</v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <v-text-field
                v-model="formModel.userName"
                :rules="validationRules.defined"
                label="Benutzername"
              ></v-text-field>
            </v-col>
            <v-col cols="12">
              <v-text-field
                v-model="formModel.password"
                :rules="validationRules.password"
                label="Passwort"
                type="password"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-alert v-if="authenticationError" text type="error">
            {{ authenticationError }}
          </v-alert>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn :disabled="isDisabled" color="primary" @click="submit">
            Anmelden
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-layout>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import { Credentials } from "@/api";
import rules from "@/common/validation-rules";

@Component({
  beforeRouteLeave(to, from, next) {
    store.commit("userLogin/reset");
    next();
  },
})
export default class UserLoginView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  formIsValid = true;

  formModel: Credentials = {
    userName: "",
    password: "",
  };

  get authenticationError(): ErrorMessage {
    return this.$store.state.userLogin.authenticationError;
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [rules.defined],
      password: [rules.defined, rules.minLength(8)],
    };
  }

  get isDisabled(): boolean {
    return this.$store.state.userLogin.authenticating;
  }

  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      await store.dispatch("userLogin/authenticate", this.formModel);
      await this.$router.push(store.state.userLogin.interceptedRoute);
    }
  }
}
</script>
