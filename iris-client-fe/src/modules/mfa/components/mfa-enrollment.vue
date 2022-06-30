<template>
  <div>
    <v-card-text>
      <p>
        Scannen Sie den QR Code mit einer Authenticator App ein, um die
        2-Faktor-Authentifizierung für Ihr Konto zu aktivieren.
      </p>
      <div class="text-center">
        <img v-if="qrCode" :src="qrCode" alt="" />
        <p v-if="secret">
          <strong>{{ secret }}</strong>
        </p>
      </div>
    </v-card-text>
    <v-divider />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
const MfaEnrollmentProps = Vue.extend({
  inheritAttrs: false,
  props: {
    qrCode: {
      type: String,
      default: "",
    },
    secret: {
      type: String,
      default: "",
    },
  },
});
@Component
export default class MfaEnrollment extends MfaEnrollmentProps {}
</script>
