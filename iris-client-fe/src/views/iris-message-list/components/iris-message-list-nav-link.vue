<template>
  <v-btn
    v-if="link.meta.menu"
    :key="link.name"
    :to="link.path"
    :exact="link.meta.menuExact"
    :disabled="link.meta.disabled"
    text
    :class="{ 'is-loading': unreadMessageCountLoading }"
    :data-test="`app-bar.nav.link.${link.name}`"
  >
    <v-badge
      color="blue"
      :content="unreadMessageCount"
      :value="unreadMessageCount > 0"
      class="badge"
      data-test="iris-messages.unread.count"
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
  pollingTimeout = -1;
  isMounted = false;
  get unreadMessageCount() {
    return this.$store.state.irisMessageList.unreadMessageCount || 0;
  }
  get unreadMessageCountLoading() {
    return this.$store.state.irisMessageList.unreadMessageCountLoading;
  }
  mounted() {
    this.isMounted = true;
    this.fetchMessageCount();
  }
  beforeDestroy() {
    this.isMounted = false;
    clearTimeout(this.pollingTimeout);
  }
  fetchMessageCount() {
    clearTimeout(this.pollingTimeout);
    if (this.isMounted) {
      this.$store
        .dispatch("irisMessageList/fetchUnreadMessageCount")
        .catch(() => {
          // ignored
        });
      this.pollingTimeout = setTimeout(this.fetchMessageCount, 30000);
    }
  }
}
</script>

<style lang="scss" scoped>
.badge {
  line-height: inherit;
}
</style>
