<template>
  <div
    v-if="
      currentUserRole === userRoles.Admin &&
      user &&
      user.mfaSecretEnrolled === true
    "
  >
    <confirm-dialog title="Sicherheitsschlüssel zurücksetzen?" @confirm="reset">
      <template #text>
        <p>
          Möchten Sie den Sicherheitsschlüssel der 2-Faktor Authentifizierung
          zurücksetzen?
        </p>
        <p class="mb-0">
          Die Verbindung zum hinterlegten Konto in der Authenticator App wird
          dadurch getrennt und die 2-Faktor Authentifizierung muss bei der
          nächsten Anmeldung erneut eingerichtet werden.
        </p>
      </template>
      <template #activator="{ attrs, on }">
        <v-btn
          v-bind="attrs"
          v-on="on"
          color="red lighten-2 white--text"
          :disabled="resetMfaSecret.state.loading"
        >
          Sicherheitsschlüssel zurücksetzen
        </v-btn>
      </template>
    </confirm-dialog>
    <error-message-alert :errors="resetMfaSecret.state.error" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { User, UserRole } from "@/api";
import store from "@/store";
import { mfaApi } from "@/modules/mfa/services/api";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import ConfirmDialog from "@/components/confirm-dialog.vue";

const MfaAdminUserSecretResetProps = Vue.extend({
  inheritAttrs: false,
  props: {
    user: {
      type: Object as PropType<User | undefined>,
      default: undefined,
    },
  },
});
@Component({
  components: {
    ConfirmDialog,
    ErrorMessageAlert,
  },
})
export default class MfaAdminUserSecretReset extends MfaAdminUserSecretResetProps {
  userRoles = UserRole;
  get currentUserRole(): UserRole | undefined {
    return store.state.userLogin.user?.role;
  }
  resetMfaSecret = mfaApi.resetUsersMfaSecret();
  async reset(): Promise<void> {
    if (this.user?.id) {
      await this.resetMfaSecret.execute(this.user.id);
      this.$emit("reset");
    }
  }
}
</script>
