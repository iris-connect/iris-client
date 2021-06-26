<template v-slot:activator="{ on }">
        <v-btn
          class="mr-0 mb-0"
          large
          fab
          dark
          fixed
          bottom
          right
          :max-height="48"
          :max-width="48"
          v-on="on"
        >
          <v-icon :size="30"> mdi-chat-alert-outline </v-icon>
        </v-btn>
</template>

  <script lang="ts">
  import Vue from 'vue'
  export default class FeedbackButton extends Vue {
  }
  </script>

<!-- TODO Make the integration of the feedback button component work>