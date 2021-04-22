<template>
  <v-card class="my-3">
    <v-form ref="form" v-model="form.valid" lazy-validation :disabled="isBusy">
      <v-card-title>Benutzer bearbeiten</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.firstName"
              label="Vorname"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.lastName"
              label="Nachname"
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.model.userName"
              label="Benutzername"
              :rules="validationRules.defined"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-select
              v-model="form.model.role"
              label="Rolle"
              :items="roleSelectOptions"
              :rules="validationRules.defined"
            ></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <password-input-field
              v-model="form.model.password"
              label="Passwort"
              :rules="validationRules.defined"
            />
          </v-col>
        </v-row>
        <v-row v-if="hasError">
          <v-col>
            <v-alert v-if="userLoadingError" text type="error">{{
              userLoadingError
            }}</v-alert>
            <v-alert v-if="userEditError" text type="error">{{
              userEditError
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
import { User, UserRole, UserUpsert } from "@/api";
import PasswordInputField from "@/components/form/password-input-field.vue";

type AdminUserEditForm = {
  model: UserUpsert;
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

  get userEditOngoing(): boolean {
    return store.state.adminUserEdit.userEditOngoing;
  }

  get userEditError(): ErrorMessage {
    return store.state.adminUserEdit.userEditError;
  }

  get isBusy(): boolean {
    return this.userLoading || this.userEditOngoing;
  }

  get hasError(): boolean {
    return !!this.userLoadingError || !!this.userEditError;
  }

  get roleSelectOptions(): Array<Record<string, unknown>> {
    return [
      // {
      //   text: "Bitte wählen",
      // },
      {
        text: "Administrator",
        value: UserRole.Admin,
      },
      {
        text: "Nutzer",
        value: UserRole.User,
      },
    ];
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [(v: unknown): string | boolean => !!v || "Pflichtfeld"],
    };
  }

  userId: string | undefined = "";

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
  onUserChanged(newValue: User | undefined): void {
    const { id, ...restProps } = newValue || {};
    this.userId = id;
    this.form.model = {
      ...restProps,
    };
    this.$refs.form.resetValidation();
  }
  get user(): User | null {
    return store.state.adminUserEdit.user;
  }

  editUser(): void {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      const payload = {
        id: this.userId,
        data: this.form.model,
      };
      store
        .dispatch("adminUserEdit/editUser", payload)
        .then(() => {
          this.$router.back();
        })
        .catch(() => {
          // ignored
        });
    }
  }
}
</script>

<style scoped></style>
