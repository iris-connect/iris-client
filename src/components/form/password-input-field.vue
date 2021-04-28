<template>
  <v-text-field
    v-bind="$attrs"
    v-on="$listeners"
    :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
    :type="showPassword ? 'text' : 'password'"
    @click:append="showPassword = !showPassword"
  ></v-text-field>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
@Component
export default class PasswordInputField extends Vue {
  showPassword = false;
}
</script>
