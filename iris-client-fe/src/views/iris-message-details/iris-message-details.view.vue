<template>
  <div>
    <alert-component v-if="dataImportAlert">
      <template v-slot:message> Die Daten wurden importiert. </template>
    </alert-component>
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
        <div v-if="message.dataAttachments.length > 0">
          <v-divider class="my-4" />
          <p class="font-weight-bold">Daten</p>
          <div class="elevation-1 mt-4">
            <template
              v-for="(dataAttachment, index) in message.dataAttachments"
            >
              <v-list-item dense :key="`item_${index}`">
                <v-list-item-content>
                  <v-list-item-title>
                    {{ dataAttachment.description }}
                  </v-list-item-title>
                </v-list-item-content>
                <v-list-item-action v-if="isInbox">
                  <div>
                    <v-btn
                      icon
                      @click="viewDataAttachment(dataAttachment.id)"
                      :disabled="dataAttachmentLoading"
                    >
                      <v-icon>mdi-eye</v-icon>
                    </v-btn>
                    <iris-message-data-import-confirm-dialog
                      @confirm="importDataAttachment(dataAttachment.id)"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                          v-on="on"
                          v-bind="attrs"
                          icon
                          :disabled="!isImportEnabled(dataAttachment.id)"
                        >
                          <v-icon>mdi-download</v-icon>
                        </v-btn>
                      </template>
                    </iris-message-data-import-confirm-dialog>
                  </div>
                </v-list-item-action>
              </v-list-item>
              <v-divider
                inset
                :key="`divider_${index}`"
                v-if="index < message.dataAttachments.length - 1"
              />
            </template>
          </div>
        </div>
        <!--
        <div v-if="message.fileAttachments.length > 0">
          <v-divider class="my-4" />
          <p class="font-weight-bold">Anhang</p>
          <div class="elevation-1 mt-4">
            <template
              v-for="(fileAttachment, index) in message.fileAttachments"
            >
              <v-list-item
                dense
                :key="`item_${index}`"
                @click="openFileAttachment(fileAttachment.id)"
                :disabled="fileAttachmentLoading"
              >
                <v-list-item-icon>
                  <v-icon>mdi-download</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>
                    {{ fileAttachment.name }}
                  </v-list-item-title>
                  <v-list-item-subtitle v-if="fileAttachment.type">
                    {{ fileAttachment.type }}
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-divider
                inset
                :key="`divider_${index}`"
                v-if="index < message.fileAttachments.length - 1"
              />
            </template>
          </div>
        </div>
        -->
        <iris-message-data-preview-dialog
          v-model="dataPreview"
          @import="importDataAttachment"
          :import-enabled="isImportEnabled(dataPreviewId)"
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
import store from "@/store";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import {
  IrisMessageContext,
  IrisMessageDataAttachment,
  IrisMessageDetails,
  IrisMessageFileAttachment,
  IrisMessageViewData,
} from "@/api";
import { ErrorMessage } from "@/utils/axios";
import { getFormattedDate } from "@/utils/date";
import IrisMessageDataPreviewDialog from "@/views/iris-message-details/components/iris-message-data-preview-dialog.vue";
import AlertComponent from "@/components/alerts/alert-component.vue";
import IrisMessageDataImportConfirmDialog from "@/views/iris-message-details/components/iris-message-data-import-confirm-dialog.vue";

type MessageData = {
  author: string;
  recipient: string;
  createdAt: string;
  subject: string;
  body: string;
  dataAttachments: IrisMessageDataAttachment[];
  fileAttachments: IrisMessageFileAttachment[];
};

@Component({
  components: {
    IrisMessageDataImportConfirmDialog,
    AlertComponent,
    IrisMessageDataPreviewDialog,
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
  dataImportAlert = false;
  showDataImportAlert(): void {
    this.dataImportAlert = true;
    setTimeout(() => {
      this.dataImportAlert = false;
    }, 2000);
  }
  dataPreview: IrisMessageViewData | null = null;
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
      dataAttachments: message?.dataAttachments || [],
      fileAttachments: message?.fileAttachments || [],
    };
  }
  get isInbox(): boolean {
    const message: IrisMessageDetails | null =
      this.$store.state.irisMessageDetails.message;
    return message?.context === IrisMessageContext.Inbox;
  }
  get messageLoading(): boolean {
    return (
      this.$store.state.irisMessageDetails.messageLoading ||
      this.$store.state.irisMessageDetails.messageSaving
    );
  }
  get dataAttachmentLoading(): boolean {
    return this.$store.state.irisMessageDetails.dataAttachmentLoading;
  }
  get fileAttachmentLoading(): boolean {
    return this.$store.state.irisMessageDetails.fileAttachmentLoading;
  }
  get errors(): ErrorMessage[] {
    return [
      this.$store.state.irisMessageDetails.messageLoadingError,
      this.$store.state.irisMessageDetails.messageSavingError,
      this.$store.state.irisMessageDetails.dataAttachmentLoadingError,
      this.$store.state.irisMessageDetails.fileAttachmentLoadingError,
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
  async importDataAttachment(id: string) {
    await this.$store.dispatch("irisMessageDetails/importDataAttachment", id);
    const message: IrisMessageDetails | null =
      this.$store.state.irisMessageDetails.message;
    this.$store.dispatch("irisMessageDetails/fetchMessage", message?.id);
    this.showDataImportAlert();
  }
  async viewDataAttachment(id: string) {
    this.dataPreview = await this.$store.dispatch(
      "irisMessageDetails/viewDataAttachment",
      id
    );
  }
  get dataPreviewId() {
    return this.dataPreview?.id;
  }
  isImportEnabled(id?: string): boolean {
    if (this.dataAttachmentLoading) return false;
    const attachment = this.message.dataAttachments.find((a) => a.id === id);
    return !attachment?.isImported;
  }
  // disabled file attachments
  /*
  openFileAttachment(id: string) {
    this.$store.dispatch("irisMessageDetails/downloadFileAttachment", id);
  }
   */
  goBack() {
    if (this.prevLocation === "iris-message-list") {
      return this.$router.back();
    }
    this.$router.replace({ name: "iris-message-list" });
  }
}
</script>
