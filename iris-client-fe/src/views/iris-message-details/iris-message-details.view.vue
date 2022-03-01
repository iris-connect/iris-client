<template>
  <div>
    <v-card :loading="messageLoading" data-test="view.iris-message-details">
      <v-card-subtitle class="pb-0 text-right" data-test="message.createdAt">
        {{ message.createdAt }}
      </v-card-subtitle>
      <v-card-title data-test="message.subject">
        {{ message.subject }}
      </v-card-title>
      <v-card-text>
        <v-row no-gutters>
          <v-col cols="12" md="6" data-test="message.author">
            <span class="font-weight-bold">Von:</span>
            {{ message.author }}
          </v-col>
          <v-col cols="12" md="6" data-test="message.recipient">
            <span class="font-weight-bold">An:</span>
            {{ message.recipient }}
          </v-col>
        </v-row>
        <v-divider class="my-4" />
        <div class="body-1" data-test="message.body">
          {{ message.body }}
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn text @click="goBack"> Zurück </v-btn>
      </v-card-actions>
    </v-card>
    <error-message-alert :errors="errors" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { IrisMessageDetails } from "@/api";
import { ErrorMessage } from "@/utils/axios";
import { getFormattedDate } from "@/utils/date";

type MessageData = {
  author: string;
  recipient: string;
  createdAt: string;
  subject: string;
  body: string;
};

@Component({
  components: {
    ErrorMessageAlert,
  },
  beforeRouteEnter(to, from, next) {
    store.dispatch("irisMessageDetails/fetchMessage", to.params.messageId);
    next((vm) => {
      if (typeof from.name === "string") {
        (vm as IrisMessageDetailsView).prevLocation = from.name;
      }
    });
  },
  beforeRouteLeave(to, from, next) {
    store.commit("irisMessageDetails/reset");
    next();
  },
})
export default class IrisMessageDetailsView extends Vue {
  prevLocation: null | string = null;
  get message(): MessageData {
    const message: IrisMessageDetails | null =
      this.$store.state.irisMessageDetails.message;
    return {
      author: message?.hdAuthor?.name || "-",
      recipient: message?.hdRecipient?.name || "-",
      createdAt: message?.createdAt
        ? getFormattedDate(message?.createdAt)
        : "-",
      subject: message?.subject || "-",
      body: message?.body || "-",
    };
  }
  get messageLoading(): boolean {
    return (
      this.$store.state.irisMessageDetails.messageLoading ||
      this.$store.state.irisMessageDetails.messageSaving
    );
  }
  get errors(): ErrorMessage[] {
    return [
      this.$store.state.irisMessageDetails.messageLoadingError,
      this.$store.state.irisMessageDetails.messageSavingError,
    ];
  }
  get messageLoaded(): boolean {
    return (
      this.$store.state.irisMessageDetails.messageLoading !== true &&
      this.$store.state.irisMessageDetails.messageLoadingError === null &&
      this.$store.state.irisMessageDetails.message !== null
    );
  }
  @Watch("messageLoaded")
  async onMessageLoaded(newValue: boolean) {
    const message = store.state.irisMessageDetails.message;
    if (newValue && !message?.isRead) {
      await this.$store.dispatch(
        "irisMessageDetails/markAsRead",
        this.$route.params.messageId
      );
      await this.$store.dispatch("irisMessageList/fetchUnreadMessageCount");
    }
  }
  goBack() {
    if (this.prevLocation === "iris-message-list") {
      return this.$router.back();
    }
    this.$router.replace({ name: "iris-message-list" });
  }
}
</script>