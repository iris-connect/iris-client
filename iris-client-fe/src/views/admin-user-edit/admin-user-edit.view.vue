<template>
  <v-card class="my-3">
    <v-form ref="form" v-model="form.valid" lazy-validation :disabled="isBusy">
      <v-card-title>Konto bearbeiten</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <conditional-field
              :config="fieldsConfig['firstName']"
              :rules="validationRules.names"
              v-slot="scope"
            >
              <v-text-field
                v-bind="scope"
                v-model="form.model.firstName"
                label="Vorname"
                maxlength="50"
              ></v-text-field>
            </conditional-field>
          </v-col>
          <v-col cols="12" md="6">
            <conditional-field
              :config="fieldsConfig['lastName']"
              :rules="validationRules.names"
              v-slot="scope"
            >
              <v-text-field
                v-bind="scope"
                v-model="form.model.lastName"
                label="Nachname"
                maxlength="50"
              ></v-text-field>
            </conditional-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <conditional-field
              :config="fieldsConfig['userName']"
              :rules="validationRules.userName"
              v-slot="scope"
            >
              <v-text-field
                v-bind="scope"
                v-model="form.model.userName"
                label="Anmeldename"
                maxlength="50"
              ></v-text-field>
            </conditional-field>
          </v-col>
          <v-col cols="12" md="6">
            <conditional-field :config="fieldsConfig['role']" v-slot="scope">
              <v-select
                v-bind="scope"
                v-model="form.model.role"
                label="Rolle"
                :items="roleSelectOptions"
              ></v-select>
            </conditional-field>
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
import ConditionalField from "@/views/admin-user-edit/components/conditional-field.vue";

type AdminUserEditForm = {
  model: UserUpdate;
  valid: boolean;
};

export type FieldConfig = {
  show?: boolean;
  edit?: boolean;
};

type FieldsConfig = {
  [P in keyof UserUpdate]?: FieldConfig;
};

/**
 * You can define a field config per role for every form field except "password".
 * "password" is excluded because a valid password has to be submitted with every change.
 * roles:
 * You can define different field configurations for each role.
 * fields:
 * You can use all or some of the keys of UserUpdate.
 * If a field is not explicitly set, it defaults to "undefined":
 * fieldConfig:
 * - show:
 *   - if "true" or undefined:
 *     - the field is rendered / shown to the user
 *   - if "false":
 *     - the edit parameter is ignored
 *     - the field is not rendered / shown to the user
 *     - the field validation rules are ignored
 *     - the field value is not submitted
 * - edit:
 *   - if "true" or undefined:
 *     - the field is editable
 *   - if "false":
 *     - the field is not editable
 *     - the field validation rules are ignored
 *     - the field value is not submitted
 */
const fieldsConfigByRole: Record<UserRole, FieldsConfig> = {
  [UserRole.Admin]: {},
  [UserRole.User]: {
    userName: {
      edit: false,
    },
    role: {
      edit: false,
    },
  },
};

@Component({
  components: {
    ConditionalField,
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
        text: "Nutzung",
        value: UserRole.User,
      },
      {
        text: "Administration",
        value: UserRole.Admin,
      },
    ];
  }

  get fieldsConfig(): FieldsConfig {
    const userRole = store.state.userLogin.user?.role;
    return userRole ? fieldsConfigByRole[userRole] : {};
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      password: [rules.password, rules.sanitised],
      userName: [rules.sanitised, rules.maxLength(50)],
      names: [rules.sanitised, rules.nameConventions, rules.maxLength(50)],
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
      const protectedFields = Object.keys(this.fieldsConfig).filter((key) => {
        const config = this.fieldsConfig[key as keyof UserUpdate];
        return config?.show === false || config?.edit === false;
      });
      const payload = {
        id: this.userId,
        data: ensureIntegrityOfUserUpsert(this.form.model, protectedFields),
      };
      await store.dispatch("adminUserEdit/editUser", payload);
      this.$router.back();
    }
  }
}

const ensureIntegrityOfUserUpsert = (
  data: UserUpdate,
  protectedFields: string[]
): UserUpdate => {
  const mandatoryFields = ["userName", "password", "role"];
  return omitBy<UserUpdate>(data, (value, key) => {
    if (protectedFields.indexOf(key) !== -1) return true;
    if (!value) {
      return mandatoryFields.indexOf(key) !== -1;
    }
    return false;
  });
};
</script>

<style scoped></style>
