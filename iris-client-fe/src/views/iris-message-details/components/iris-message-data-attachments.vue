<template>
  <div v-if="dataAttachments.length > 0">
    <v-divider class="my-4" />
    <p class="font-weight-bold">Daten</p>
    <div class="elevation-1 mt-4">
      <template v-for="(dataAttachment, index) in dataAttachments">
        <v-list-item dense :key="`item_${index}`">
          <v-list-item-content>
            <v-list-item-title>
              {{ dataAttachment.description }}
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-action v-if="!readonly">
            <div>
              <v-btn
                icon
                @click="viewDataAttachment(dataAttachment.id)"
                :disabled="dataAttachmentLoading"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>
              <iris-message-data-import-menu
                :disabled="!isImportEnabled(dataAttachment.id)"
                @import:update="importDataAttachmentAndUpdate(dataAttachment)"
                @import:create="
                  importDataAttachmentAndCreate(dataAttachment.id)
                "
              />
            </div>
          </v-list-item-action>
        </v-list-item>
        <v-divider
          inset
          :key="`divider_${index}`"
          v-if="index < dataAttachments.length - 1"
        />
      </template>
    </div>
    <iris-message-data-preview-dialog v-model="dataPreview">
      <template #data-import>
        <iris-message-data-import-menu
          :disabled="!isImportEnabled(dataPreview && dataPreview.id)"
          @import:update="importDataAttachmentAndUpdate(dataPreview)"
          @import:create="importDataAttachmentAndCreate(dataPreview.id)"
        >
          <template #activator="{ attrs, on, disabled }">
            <v-btn
              v-on="on"
              v-bind="attrs"
              color="primary"
              data-test="import-data"
              :disabled="disabled"
            >
              Daten importieren
            </v-btn>
          </template>
        </iris-message-data-import-menu>
      </template>
    </iris-message-data-preview-dialog>
    <iris-message-data-import-dialog v-model="dataImport" />
    <error-message-alert :errors="errors" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { IrisMessageDataAttachment, IrisMessageViewData } from "@/api";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import AlertComponent from "@/components/alerts/alert-component.vue";
import IrisMessageDataImportDialog, {
  IrisMessageImportData,
} from "@/views/iris-message-details/components/iris-message-data-import-dialog.vue";
import { bundleIrisMessageApi } from "@/modules/iris-message/api";
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
  dataImportAlert = false;
  showDataImportAlert(): void {
    this.dataImportAlert = true;
    setTimeout(() => {
      this.dataImportAlert = false;
    }, 2000);
  }

  messageDataApi = bundleIrisMessageApi([
    "importDataAttachment",
    "viewDataAttachment",
  ]);

  get dataAttachmentLoading(): boolean {
    return getApiLoading(this.messageDataApi);
  }

  dataPreview: IrisMessageViewData | null = null;
  async viewDataAttachment(id: string) {
    this.dataPreview = await this.messageDataApi.viewDataAttachment.execute(id);
  }

  dataImport: IrisMessageImportData | null = null;
  importDataAttachmentAndUpdate(value: IrisMessageImportData | null) {
    this.dataPreview = null;
    this.dataImport = value;
  }

  async importDataAttachmentAndCreate(id: string) {
    this.dataPreview = null;
    await this.messageDataApi.importDataAttachment.execute(id);
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
