<template>
  <v-badge
    color="blue"
    :content="unreadMessageCount"
    :value="unreadMessageCount > 0"
    :class="{ badge: true, 'is-loading': unreadMessageCountLoading }"
    data-test="iris-messages.unread.count"
  >
    <slot name="default" />
  </v-badge>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const IrisMessageListNavLinkProps = Vue.extend({
  inheritAttrs: false,
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
