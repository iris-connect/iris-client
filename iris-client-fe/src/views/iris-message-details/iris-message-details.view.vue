<template>
  <div class="my-3">
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
        <iris-message-data-attachments
          :data-attachments="message.dataAttachments"
          :readonly="!isInbox"
          @import:done="handleImportDone"
        />
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
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import {
  IrisMessageContext,
  IrisMessageDataAttachment,
  IrisMessageDetails,
} from "@/api";
import { ErrorMessage } from "@/utils/axios";
import { getFormattedDate } from "@/utils/date";
import {
  bundleIrisMessageApi,
  fetchUnreadMessageCountApi,
} from "@/modules/iris-message/api";
import { getApiErrorMessages, getApiLoading } from "@/utils/api";
import IrisMessageDataAttachments from "@/views/iris-message-details/components/iris-message-data-attachments.vue";

type MessageData = {
  author: string;
  recipient: string;
  createdAt: string;
  subject: string;
  body: string;
  dataAttachments: IrisMessageDataAttachment[];
};

@Component({
  components: {
    IrisMessageDataAttachments,
    ErrorMessageAlert,
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (typeof from.name === "string") {
        (vm as IrisMessageDetailsView).prevLocation = from.name;
      }
    });
  },
})
export default class IrisMessageDetailsView extends Vue {
  messageApi = bundleIrisMessageApi(["fetchMessage", "markAsRead"]);

  mounted() {
    this.messageApi.fetchMessage.execute(this.$route.params.messageId);
  }

  get messageDetails(): IrisMessageDetails | null {
    return this.messageApi.fetchMessage.state.result;
  }

  prevLocation: null | string = null;

  get message(): MessageData {
    const message = this.messageDetails;
    return {
      author: message?.hdAuthor?.name || "-",
      recipient: message?.hdRecipient?.name || "-",
      createdAt: message?.createdAt
        ? getFormattedDate(message?.createdAt)
        : "-",
      subject: message?.subject || "-",
      body: message?.body || "-",
      dataAttachments: message?.dataAttachments || [],
    };
  }
  get isInbox(): boolean {
    return this.messageDetails?.context === IrisMessageContext.Inbox;
  }
  get messageLoading(): boolean {
    return getApiLoading(this.messageApi);
  }
  get errors(): ErrorMessage[] {
    return getApiErrorMessages(this.messageApi);
  }
  get messageLoaded(): boolean {
    const messageState = this.messageApi.fetchMessage.state;
    return (
      !messageState.loading &&
      messageState.error === null &&
      messageState.result !== null
    );
  }
  @Watch("messageLoaded")
  async onMessageLoaded(newValue: boolean) {
    if (newValue && !this.messageDetails?.isRead) {
      await this.messageApi.markAsRead.execute(this.$route.params.messageId);
      await fetchUnreadMessageCountApi.execute();
    }
  }

  handleImportDone() {
    this.messageApi.fetchMessage.execute(this.$route.params.messageId);
  }

  goBack() {
    if (this.prevLocation === "iris-message-list") {
      return this.$router.back();
    }
    this.$router.replace({ name: "iris-message-list" });
  }
}
</script>
