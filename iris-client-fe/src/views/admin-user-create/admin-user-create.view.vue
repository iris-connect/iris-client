<template>
  <v-card class="my-3">
    <v-form
      ref="form"
      v-model="form.valid"
      lazy-validation
      :disabled="userCreationOngoing"
    >
      <v-card-title>Konto anlegen</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.firstName"
              label="Vorname"
              :rules="validationRules.names"
              maxlength="50"
              data-test="firstName"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.lastName"
              label="Nachname"
              :rules="validationRules.names"
              maxlength="50"
              data-test="lastName"
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.userName"
              label="Anmeldename"
              :rules="validationRules.userName"
              maxlength="50"
              data-test="userName"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-select
              v-model="form.model.role"
              label="Rolle"
              :items="roleSelectOptions"
              :rules="validationRules.defined"
              data-test="role"
              :menu-props="{ contentClass: 'select-menu-role' }"
            ></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <password-input-field
              v-model="form.model.password"
              label="Passwort"
              :rules="validationRules.password"
              data-test="password"
            />
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-switch
              v-model="form.model.locked"
              label="Benutzer sperren"
            ></v-switch>
          </v-col>
        </v-row>
        <mfa-admin-user-fieldset
          v-model="form.model.useMfa"
          :apply-config="true"
        />
        <v-alert v-if="userCreationError" text type="error">
          {{ userCreationError }}
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-btn
          color="secondary"
          :disabled="userCreationOngoing"
          plain
          :to="{ name: 'admin-user-list' }"
          replace
          data-test="cancel"
        >
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          :disabled="userCreationOngoing"
          color="primary"
          @click="createUser"
          data-test="submit"
        >
          Konto anlegen
        </v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import { UserInsert, UserRole } from "@/api";
import PasswordInputField from "@/components/form/password-input-field.vue";
import rules from "@/common/validation-rules";
import MfaAdminUserFieldset from "@/modules/mfa/components/mfa-admin-user-fieldset.vue";

type AdminUserCreateForm = {
  model: UserInsert;
  valid: boolean;
};

@Component({
  components: {
    MfaAdminUserFieldset,
    PasswordInputField,
  },
  beforeRouteLeave(to, from, next) {
    store.commit("adminUserCreate/reset");
    next();
  },
})
export default class AdminUserCreateView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  get userCreationOngoing(): boolean {
    return store.state.adminUserCreate.userCreationOngoing;
  }

  get userCreationError(): ErrorMessage {
    return store.state.adminUserCreate.userCreationError;
  }

  get roleSelectOptions(): Array<Record<string, unknown>> {
    return [
      {
        text: "Nutzung",
        value: UserRole.User,
      },
      {
        text: "Administration",
        value: UserRole.Admin,
      },
    ];
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [rules.defined],
      password: [rules.defined, rules.password, rules.sanitised],
      userName: [rules.defined, rules.sanitised, rules.maxLength(50)],
      sanitised: [rules.sanitised],
      names: [rules.sanitised, rules.nameConventions, rules.maxLength(50)],
    };
  }

  form: AdminUserCreateForm = {
    model: {
      firstName: "",
      lastName: "",
      userName: "",
      password: "",
      role: UserRole.User,
      locked: false,
      useMfa: false,
    },
    valid: false,
  };

  async createUser(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      const payload: UserInsert = this.form.model;
      await store.dispatch("adminUserCreate/createUser", payload);
      this.$router.replace({ name: "admin-user-list" });
    }
  }
}
</script>

<style scoped></style>
