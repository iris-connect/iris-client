<template>
  <v-card class="my-3">
    <v-form ref="form" v-model="form.valid" lazy-validation :disabled="isBusy">
      <v-card-title>Konto bearbeiten</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.firstName"
              label="Vorname"
              :rules="validationRules.names"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.lastName"
              label="Nachname"
              :rules="validationRules.names"
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.userName"
              label="Anmeldename"
              :rules="validationRules.sanitisedAndDefined"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-select
              v-model="form.model.role"
              label="Rolle"
              :items="roleSelectOptions"
            ></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <password-input-field
              v-model="form.model.password"
              label="Passwort"
              :rules="validationRules.password"
            />
          </v-col>
        </v-row>
        <v-row v-if="hasError">
          <v-col>
            <v-alert v-if="userLoadingError" text type="error">{{
              userLoadingError
            }}</v-alert>
            <v-alert v-if="userSavingError" text type="error">{{
              userSavingError
            }}</v-alert>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn
          color="secondary"
          :disabled="isBusy"
          plain
          @click="$router.back()"
        >
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn :disabled="isBusy" color="primary" @click="editUser">
          Änderungen übernehmen
        </v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import { User, UserRole, UserUpdate } from "@/api";
import PasswordInputField from "@/components/form/password-input-field.vue";
import rules from "@/common/validation-rules";
import { omitBy } from "lodash";

type AdminUserEditForm = {
  model: UserUpdate;
  valid: boolean;
};

@Component({
  components: {
    PasswordInputField,
  },
  async beforeRouteEnter(to, from, next) {
    next();
    await store.dispatch("adminUserEdit/fetchUser", to.params.id);
  },
  beforeRouteLeave(to, from, next) {
    store.commit("adminUserEdit/reset");
    next();
  },
})
export default class AdminUserEditView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  get userLoading(): boolean {
    return store.state.adminUserEdit.userLoading;
  }

  get userLoadingError(): ErrorMessage {
    return store.state.adminUserEdit.userLoadingError;
  }

  get userSavingOngoing(): boolean {
    return store.state.adminUserEdit.userSavingOngoing;
  }

  get userSavingError(): ErrorMessage {
    return store.state.adminUserEdit.userSavingError;
  }

  get isBusy(): boolean {
    return this.userLoading || this.userSavingOngoing;
  }

  get hasError(): boolean {
    return !!this.userLoadingError || !!this.userSavingError;
  }

  get roleSelectOptions(): Array<Record<string, unknown>> {
    return [
      {
        text: "Nutzer",
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
      password: [rules.password, rules.sanitised],
      sanitisedAndDefined: [rules.defined, rules.sanitised],
      sanitised: [rules.sanitised],
      names: [rules.sanitised, rules.nameConventions],
    };
  }

  userId = "";

  form: AdminUserEditForm = {
    model: {
      firstName: "",
      lastName: "",
      userName: "",
      password: "",
      role: undefined,
    },
    valid: false,
  };

  @Watch("user")
  onUserChanged(newValue: User | null): void {
    const { id, ...restProps } = newValue || {};
    this.userId = id || "";
    this.form.model = {
      ...this.form.model,
      ...restProps,
    };
    this.$refs.form.resetValidation();
  }
  get user(): User | null {
    return store.state.adminUserEdit.user;
  }

  async editUser(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      const payload = {
        id: this.userId,
        data: ensureIntegrityOfUserUpsert(this.form.model),
      };
      await store.dispatch("adminUserEdit/editUser", payload);
      this.$router.back();
    }
  }
}

const ensureIntegrityOfUserUpsert = (data: UserUpdate): UserUpdate => {
  const mandatoryFields = ["userName", "password", "role"];
  return omitBy(data, (value, key) => {
    if (!value) {
      return mandatoryFields.indexOf(key) !== -1;
    }
    return false;
  });
};
</script>

<style scoped></style>
