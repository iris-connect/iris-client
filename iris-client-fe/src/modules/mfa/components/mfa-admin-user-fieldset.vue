<template>
  <div v-if="mfaOption !== undefined && mfaOption !== mfaOptions.DISABLED">
    <v-divider class="my-4" />
    <v-row>
      <v-col cols="12">
        <p class="subtitle-1 mb-0">2-Faktor Authentifizierung</p>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" sm="6">
        <v-switch
          class="mt-0"
          v-model="model"
          label="2FA aktivieren"
          :disabled="
            mfaOption === mfaOptions.ALWAYS || mfaOption === mfaOptions.DISABLED
          "
        />
      </v-col>
      <v-col cols="12" sm="6">
        <p v-if="setupRequired">
          Die Einrichtung der 2-Faktor Authentifizierung über eine Authenticator
          App erfolgt beim nächsten Login.
        </p>
        <mfa-admin-user-secret-reset :user="user" @reset="$emit('reset')" />
      </v-col>
    </v-row>
    <v-divider class="my-4" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { mfaApi } from "@/modules/mfa/services/api";
import { MfaOption, User, UserRole } from "@/api";
import { PropType } from "vue";
import store from "@/store";
import MfaAdminUserSecretReset from "@/modules/mfa/components/mfa-admin-user-secret-reset.vue";

const MfaAdminUserFieldsetProps = Vue.extend({
  inheritAttrs: false,
  props: {
    applyConfig: {
      type: Boolean,
      default: false,
    },
    value: {
      type: Boolean,
      default: false,
    },
    user: {
      type: Object as PropType<User | undefined>,
      default: undefined,
    },
  },
});
@Component({
  components: {
    MfaAdminUserSecretReset,
  },
})
export default class MfaAdminUserFieldset extends MfaAdminUserFieldsetProps {
  fetchMfaConfig = mfaApi.fetchMfaConfig();
  mounted() {
    this.fetchMfaConfig.execute();
  }
  userRoles = UserRole;
  get userRole(): UserRole | undefined {
    return store.state.userLogin.user?.role;
  }

  mfaOptions = MfaOption;
  get mfaOption(): MfaOption | undefined {
    return this.fetchMfaConfig.state.result?.mfaOption;
  }

  get setupRequired(): boolean {
    if (this.user) {
      return this.model && this.user?.mfaSecretEnrolled === false;
    }
    return this.model;
  }

  @Watch("mfaOption", { immediate: true })
  onMfaOptionChange(newValue: MfaOption) {
    const option = this.processMfaConfigOption(newValue);
    if (option !== undefined) {
      this.model = option;
    }
  }
  processMfaConfigOption(option: MfaOption): boolean | undefined {
    if (option === MfaOption.ALWAYS) {
      return true;
    }
    if (option === MfaOption.DISABLED) {
      return false;
    }
    if (this.applyConfig) {
      if (option === MfaOption.OPTIONAL_DEFAULT_TRUE) {
        return true;
      }
      if (option === MfaOption.OPTIONAL_DEFAULT_FALSE) {
        return false;
      }
    }
  }

  get model(): boolean {
    return this.value;
  }
  set model(value: boolean) {
    this.$emit("input", value);
  }
}
</script>
