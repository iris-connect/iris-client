<template>
  <v-btn
    v-if="link.meta.menu"
    :key="link.name"
    :to="link.path"
    :exact="link.meta.menuExact"
    :disabled="link.meta.disabled"
    text
    :data-test="`app-bar.nav.link.${link.name}`"
  >
    <v-badge
      color="blue"
      :content="unreadMessageCount"
      :value="unreadMessageCount > 0"
      class="badge"
    >
      {{ link.meta.menuName }}
    </v-badge>
  </v-btn>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { RouteConfig } from "vue-router";
import { PropType } from "vue";

const IrisMessageListNavLinkProps = Vue.extend({
  inheritAttrs: false,
  props: {
    link: {
      type: Object as PropType<RouteConfig>,
      default: () => ({}),
    },
  },
});

@Component
export default class IrisMessageListNavLink extends IrisMessageListNavLinkProps {
  mounted() {
    this.$store.dispatch("irisMessageList/fetchUnreadMessageCount");
  }
  get unreadMessageCount() {
    return this.$store.state.irisMessageList.unreadMessageCount || 0;
  }
}
</script>

<style lang="scss" scoped>
.badge {
  line-height: inherit;
}
</style>
