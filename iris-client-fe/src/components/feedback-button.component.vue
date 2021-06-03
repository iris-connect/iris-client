
<template>
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
    @click="showFeedbackDialog = true"
  >
    <v-icon :size="30"> mdi-chat-alert-outline </v-icon>
  </v-btn>
</template>

<!-- TODO Make the feedback button its own component and import the component in the main views (home etc)>