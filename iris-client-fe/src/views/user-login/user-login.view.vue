<template>
  <v-layout justify-center>
    <v-card class="my-3 user-login-container">
      <user-login-form
        v-if="!mfaRequired"
        :error="authenticationError"
        :disabled="isDisabled"
        @submit="login"
      />
      <template v-if="mfaRequired">
        <v-card-title>Identität bestätigen</v-card-title>
        <mfa-enrollment
          v-if="mfaEnrollmentRequired"
          :qr-code="mfaAuthentication.qrCodeImageUri"
          :secret="mfaAuthentication.mfaSecret"
        />
        <mfa-otp-form
          :error="authenticationError"
          :disabled="isDisabled"
          @submit="verifyOtp"
          @cancel="cancelMfa"
        />
      </template>
    </v-card>
  </v-layout>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import {
  AuthenticationStatus,
  Credentials,
  MfaAuthentication,
  MfaVerification,
} from "@/api";
import UserLoginError from "@/views/user-login/components/user-login-error.vue";
import MfaEnrollment from "@/modules/mfa/components/mfa-enrollment.vue";
import UserLoginForm from "@/views/user-login/components/user-login-form.vue";
import MfaOtpForm from "@/modules/mfa/components/mfa-otp-form.vue";

@Component({
  components: {
    MfaOtpForm,
    UserLoginForm,
    MfaEnrollment,
    UserLoginError,
  },
  beforeRouteLeave(to, from, next) {
    store.commit("userLogin/resetLogin");
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

  get authenticated(): boolean {
    return this.$store.getters["userLogin/isAuthenticated"];
  }

  get authenticationError(): ErrorMessage {
    return this.$store.state.userLogin.authenticationError;
  }

  get isDisabled(): boolean {
    return this.$store.state.userLogin.authenticating;
  }

  get mfaAuthentication(): MfaAuthentication | undefined {
    return this.$store.state.userLogin.mfa;
  }

  get mfaRequired(): boolean {
    const status = this.mfaAuthentication?.authenticationStatus;
    return (
      status === AuthenticationStatus.PRE_AUTHENTICATED_ENROLLMENT_REQUIRED ||
      status === AuthenticationStatus.PRE_AUTHENTICATED_MFA_REQUIRED
    );
  }

  get mfaEnrollmentRequired(): boolean {
    const status = this.mfaAuthentication?.authenticationStatus;
    return (
      status === AuthenticationStatus.PRE_AUTHENTICATED_ENROLLMENT_REQUIRED
    );
  }

  login(credentials: Credentials): void {
    store.dispatch("userLogin/login", credentials);
  }

  verifyOtp(mfaVerification: MfaVerification): void {
    store.dispatch("userLogin/verifyMfaOtp", mfaVerification.otp);
  }

  cancelMfa(): void {
    store.commit("userLogin/resetLogin");
  }

  @Watch("authenticated", { immediate: true })
  onAuthenticatedChange(newValue: boolean) {
    if (newValue) {
      this.$router.push(store.state.userLogin.interceptedRoute);
    }
  }
}
</script>

<style scoped>
.user-login-container {
  width: 420px;
  max-width: 100%;
}
</style>
