<template>
  <div>
    <v-card :loading="messageLoading">
      <v-card-subtitle class="pb-0 text-right">
        {{ getFormattedDate(message.createdAt) }}
      </v-card-subtitle>
      <v-card-title>
        {{ message.subject }}
      </v-card-title>
      <v-card-text>
        <v-row no-gutters>
          <v-col cols="12" md="6">
            <span class="font-weight-bold">Von:</span>
            {{ message.author || "-" }}
          </v-col>
          <v-col cols="12" md="6">
            <span class="font-weight-bold">An:</span>
            {{ message.recipient || "-" }}
          </v-col>
        </v-row>
        <v-divider class="my-4" />
        <div class="body-1">
          {{ message.body }}
        </div>
        <div v-if="message.attachments && message.attachments.length > 0">
          <v-divider class="my-4" />
          <p class="font-weight-bold">Anhang</p>
          <div class="elevation-1 mt-4">
            <template v-for="(attachment, index) in message.attachments">
              <v-list-item
                dense
                :key="`item_${index}`"
                @click="openAttachment(attachment.link)"
              >
                <v-list-item-icon>
                  <v-icon>mdi-download</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>
                    {{ attachment.name }}
                  </v-list-item-title>
                  <v-list-item-subtitle>
                    {{ attachment.link }} ({{ attachment.type }})
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
        <v-btn text :to="{ name: 'iris-message-list' }" replace> Zurück </v-btn>
      </v-card-actions>
    </v-card>
    <error-message-alert :errors="errors" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "@/store";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { IrisMessageDetails } from "@/api";
import { ErrorMessage } from "@/utils/axios";
import { getFormattedDate } from "@/utils/date";

@Component({
  components: {
    ErrorMessageAlert,
  },
  beforeRouteEnter(to, from, next) {
    store.dispatch("irisMessageDetails/fetchMessage", to.params.messageId);
    next();
  },
  beforeRouteLeave(to, from, next) {
    store.commit("irisMessageDetails/reset");
    next();
  },
})
export default class IrisMessageDetailsView extends Vue {
  getFormattedDate = getFormattedDate;
  get message(): IrisMessageDetails {
    return this.$store.state.irisMessageDetails.message || {};
  }
  get messageLoading(): boolean {
    return this.$store.state.irisMessageDetails.messageLoading;
  }
  get errors(): ErrorMessage[] {
    return [this.$store.state.irisMessageDetails.messageLoadingError];
  }
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  openAttachment(link: string) {
    // @todo: handle link opening
  }
}
</script>
