<template>
  <div v-if="dataAttachments.length > 0">
    <alert-component
      v-if="dataImportAlert"
      data-test="message-data.import.success"
    >
      <template v-slot:message> Die Daten wurden importiert. </template>
    </alert-component>
    <v-divider class="my-4" />
    <p class="font-weight-bold">Daten</p>
    <div class="elevation-1 mt-4">
      <template v-for="(dataAttachment, index) in dataAttachments">
        <v-list-item
          dense
          :key="`item_${index}`"
          data-test="message-data.list-item"
          :class="dataAttachment.isImported ? 'is-imported' : ''"
        >
          <v-list-item-content>
            <v-list-item-title>
              {{ dataAttachment.description }}
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-action v-if="!readonly">
            <div>
              <v-btn
                icon
                @click="previewId = dataAttachment.id"
                :disabled="dataAttachmentLoading"
                data-test="message-data.preview"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>
              <iris-message-data-import-menu
                :disabled="!isImportEnabled(dataAttachment.id)"
                @import:update="
                  importDataAttachmentAndUpdate(dataAttachment.id)
                "
                @import:add="importDataAttachmentAndAdd(dataAttachment.id)"
              />
            </div>
          </v-list-item-action>
        </v-list-item>
        <v-divider
          :key="`divider_${index}`"
          v-if="index < dataAttachments.length - 1"
        />
      </template>
    </div>
    <iris-message-data-preview-dialog v-model="previewId">
      <template #dialog-actions>
        <iris-message-data-import-menu
          :disabled="!isImportEnabled(previewId)"
          @import:update="importDataAttachmentAndUpdate(previewId)"
          @import:add="importDataAttachmentAndAdd(previewId)"
        >
          <template #activator="{ attrs, on }">
            <v-btn v-on="on" v-bind="attrs" color="primary">
              Daten importieren
            </v-btn>
          </template>
        </iris-message-data-import-menu>
      </template>
    </iris-message-data-preview-dialog>
    <iris-message-data-import-dialog
      v-model="importModel"
      @import:done="handleImportDone"
    />
    <error-message-alert :errors="errors" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { IrisMessageDataAttachment } from "@/api";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import AlertComponent from "@/components/alerts/alert-component.vue";
import IrisMessageDataImportDialog, {
  IrisMessageImportData,
} from "@/views/iris-message-details/components/iris-message-data-import-dialog.vue";
import { bundleIrisMessageApi } from "@/modules/iris-message/services/api";
import IrisMessageDataImportMenu from "@/views/iris-message-details/components/iris-message-data-import-menu.vue";
import { getApiErrorMessages, getApiLoading } from "@/utils/api";
import IrisMessageDataPreviewDialog from "@/views/iris-message-details/components/iris-message-data-preview-dialog.vue";
import { ErrorMessage } from "@/utils/axios";

const IrisMessageDataAttachmentsProps = Vue.extend({
  inheritAttrs: false,
  props: {
    dataAttachments: {
      type: Array as PropType<IrisMessageDataAttachment[] | null>,
      default: null,
    },
    readonly: {
      type: Boolean,
      default: true,
    },
  },
});
@Component({
  components: {
    IrisMessageDataPreviewDialog,
    IrisMessageDataImportMenu,
    IrisMessageDataImportDialog,
    AlertComponent,
    ConfirmDialog,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataAttachments extends IrisMessageDataAttachmentsProps {
  previewId: string | null = null;
  dataImportAlert = false;
  showDataImportAlert(): void {
    this.dataImportAlert = true;
    setTimeout(() => {
      this.dataImportAlert = false;
    }, 2000);
  }

  messageDataApi = bundleIrisMessageApi(["importDataAttachmentAndAdd"]);

  get dataAttachmentLoading(): boolean {
    return getApiLoading(this.messageDataApi);
  }

  importModel: IrisMessageImportData | null = null;
  importDataAttachmentAndUpdate(value: string | null) {
    this.previewId = null;
    this.importModel =
      this.dataAttachments?.find((item) => item.id === value) || null;
  }

  async importDataAttachmentAndAdd(id: string) {
    this.previewId = null;
    await this.messageDataApi.importDataAttachmentAndAdd.execute(id);
    this.handleImportDone();
  }

  handleImportDone() {
    this.showDataImportAlert();
    this.$emit("import:done");
  }

  isImportEnabled(id?: string): boolean {
    if (this.dataAttachmentLoading || !id) return false;
    const attachment = this.dataAttachments?.find((a) => a.id === id);
    return !attachment?.isImported;
  }

  get errors(): ErrorMessage[] {
    return getApiErrorMessages(this.messageDataApi);
  }
}
</script>
