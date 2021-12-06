<template>
  <div>
    <v-card :loading="messageLoading">
      <v-card-subtitle class="pb-0 text-right">
        {{ message.createdAt }}
      </v-card-subtitle>
      <v-card-title>
        {{ message.subject }}
      </v-card-title>
      <v-card-text>
        <v-row no-gutters>
          <v-col cols="12" md="6">
            <span class="font-weight-bold">Von:</span>
            {{ message.author }}
          </v-col>
          <v-col cols="12" md="6">
            <span class="font-weight-bold">An:</span>
            {{ message.recipient }}
          </v-col>
        </v-row>
        <v-divider class="my-4" />
        <div class="body-1">
          {{ message.body }}
        </div>
        <div v-if="message.attachments.length > 0">
          <v-divider class="my-4" />
          <p class="font-weight-bold">Anhang</p>
          <div class="elevation-1 mt-4">
            <template v-for="(attachment, index) in message.attachments">
              <v-list-item
                dense
                :key="`item_${index}`"
                @click="openAttachment(attachment.id)"
              >
                <v-list-item-icon>
                  <v-icon>mdi-download</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>
                    {{ attachment.name }}
                  </v-list-item-title>
                  <v-list-item-subtitle>
                    {{ attachment.type }}
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-divider
                inset
                :key="`divider_${index}`"
                v-if="index < message.attachments.length - 1"
              />
            </template>
          </div>
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
import { IrisMessageAttachment, IrisMessageDetails } from "@/api";
import { ErrorMessage } from "@/utils/axios";
import { getFormattedDate } from "@/utils/date";

type MessageData = {
  author: string;
  recipient: string;
  createdAt: string;
  subject: string;
  body: string;
  attachments: IrisMessageAttachment[];
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
      attachments: message?.attachments || [],
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
  onMessageLoaded(newValue: boolean) {
    if (newValue) {
      this.$store.dispatch(
        "irisMessageDetails/markAsRead",
        this.$route.params.messageId
      );
    }
  }
  openAttachment(id: string) {
    this.$store.dispatch("irisMessageDetails/downloadAttachment", id);
  }
  goBack() {
    if (this.prevLocation === "iris-message-list") {
      return this.$router.back();
    }
    this.$router.replace({ name: "iris-message-list" });
  }
}
</script>
