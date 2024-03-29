<template>
  <div>
    <entry-meta-data :entry="user" />
    <v-card class="my-3">
      <v-form
        ref="form"
        v-model="form.valid"
        lazy-validation
        :disabled="isBusy"
        :class="{ 'is-loading': userLoading }"
      >
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
                  data-test="firstName"
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
                  data-test="lastName"
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
                  data-test="userName"
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
                  data-test="role"
                  :menu-props="{ contentClass: 'select-menu-role' }"
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
                data-test="password"
              />
            </v-col>
            <v-col v-if="oldPasswordRequired" cols="12" md="6">
              <password-input-field
                v-model="form.model.oldPassword"
                label="Altes Passwort"
                :rules="validationRules.oldPassword"
                data-test="oldPassword"
              />
              <v-alert
                class="caption mt-3"
                icon="mdi-shield-lock-outline"
                text
                type="info"
              >
                Bitte geben Sie zur Bestätigung der Änderung Ihr altes Passwort
                ein.
              </v-alert>
            </v-col>
          </v-row>
          <conditional-field :config="fieldsConfig['locked']" v-slot="scope">
            <v-row>
              <v-col cols="12">
                <v-switch
                  v-bind="scope"
                  v-model="form.model.locked"
                  label="Benutzer sperren"
                ></v-switch>
              </v-col>
            </v-row>
          </conditional-field>
          <mfa-admin-user-fieldset
            v-model="form.model.useMfa"
            :user="user"
            @reset="fetchUser"
          />
          <v-row v-if="hasError">
            <v-col>
              <v-alert v-if="userLoadingError" text type="error">
                {{ userLoadingError }}
              </v-alert>
              <v-alert
                v-if="userSavingError"
                text
                type="error"
                data-test="error.edit"
              >
                {{ userSavingError }}
              </v-alert>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="secondary"
            :disabled="isBusy"
            plain
            :to="{ name: 'admin-user-list' }"
            replace
            data-test="cancel"
          >
            Abbrechen
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            :disabled="isBusy || !userId"
            color="primary"
            @click="editUser"
            data-test="submit"
          >
            Änderungen übernehmen
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </div>
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
import _defaults from "lodash/defaults";
import _isEmpty from "lodash/isEmpty";
import EntryMetaData from "@/components/entry-meta-data.vue";
import MfaAdminUserFieldset from "@/modules/mfa/components/mfa-admin-user-fieldset.vue";

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
    locked: {
      show: false,
    },
  },
};

@Component({
  components: {
    MfaAdminUserFieldset,
    EntryMetaData,
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

  fetchUser(): void {
    store.dispatch("adminUserEdit/fetchUser", this.userId);
  }

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
    return userRole
      ? {
          ...fieldsConfigByRole[userRole],
          locked: {
            show: !this.isCurrentUser,
          },
        }
      : {};
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      password: [rules.password, rules.sanitised],
      oldPassword: [rules.defined, rules.sanitised],
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
      password: undefined,
      oldPassword: undefined,
      role: undefined,
      locked: undefined,
      useMfa: undefined,
    },
    valid: false,
  };

  @Watch("user")
  onUserChanged(newValue: User | null): void {
    if (newValue) {
      const { id, ...restProps } = newValue;
      this.userId = id || "";
      this.form.model = _defaults({}, restProps, this.form.model);
    }
    this.$refs.form.resetValidation();
  }
  get user(): User | null {
    return store.state.adminUserEdit.user;
  }

  @Watch("form.model.password")
  onPasswordChanged(newValue: string): void {
    if (_isEmpty(newValue)) {
      this.form.model.oldPassword = undefined;
    }
  }

  get oldPasswordRequired(): boolean {
    if (_isEmpty(this.form.model.password)) return false;
    return this.$store.getters["userLogin/isUser"] || this.isCurrentUser;
  }

  get isCurrentUser(): boolean {
    return this.$store.getters["userLogin/isCurrentUser"](this.userId);
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
      this.$router
        .replace({
          name: this.$store.getters["userLogin/isAdmin"]
            ? "admin-user-list"
            : "dashboard",
        })
        .catch(() => {
          // ignored
        });
      // fetch the user profile after changing the current user to update the user`s display name in the main navigation bar
      // can be ignored if it fails because no business logic is affected if the user profile is not up to date as the user`s access token is invalidated by the Backend if anything of relevance (e.g. userName, password) changes
      if (this.isCurrentUser) {
        store.dispatch("userLogin/fetchAuthenticatedUser", true).catch(() => {
          // ignored
        });
      }
    }
  }
}

const ensureIntegrityOfUserUpsert = (
  data: UserUpdate,
  protectedFields: string[]
): UserUpdate => {
  return omitBy<UserUpdate>(data, (value, key) => {
    return protectedFields.indexOf(key) !== -1;
  });
};
</script>

<style scoped></style>
