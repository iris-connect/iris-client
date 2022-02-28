<template>
  <div class="my-3">
    <v-form
      ref="form"
      v-model="form.valid"
      lazy-validation
      :disabled="disabled"
    >
      <v-card>
        <v-card-title>Nachricht schreiben</v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <v-autocomplete
                v-model="form.model.hdRecipient"
                :search-input="search"
                @update:search-input="handleSearch"
                :no-filter="true"
                label="Empfänger"
                :items="recipients"
                :rules="validationRules.defined"
                :menu-props="{ contentClass: 'select-menu-recipient' }"
                item-text="name"
                item-value="id"
                data-test="hdRecipient"
                :loading="recipientsLoading"
                :class="{
                  'is-loading': recipientsLoading,
                  'is-empty': recipients.length <= 0,
                }"
              ></v-autocomplete>
            </v-col>
            <v-col cols="12">
              <v-text-field
                v-model="form.model.subject"
                label="Betreff"
                :rules="validationRules.sanitisedAndDefined"
                maxlength="50"
                data-test="subject"
              ></v-text-field>
            </v-col>
            <v-col cols="12">
              <v-textarea
                v-model="form.model.body"
                label="Nachricht"
                auto-grow
                rows="5"
                value=""
                :rules="validationRules.sanitisedAndDefined"
                data-test="body"
              ></v-textarea>
            </v-col>
            <v-col cols="12">
              <p class="subtitle-1">Daten</p>
              <div
                :key="index"
                v-for="(dataAttachment, index) in form.model.dataAttachments"
              >
                <v-row no-gutters>
                  <v-col>
                    <v-row>
                      <v-col cols="auto">
                        <strong>
                          {{ getDataLabel(dataAttachment.discriminator) }}
                        </strong>
                      </v-col>
                      <v-col>
                        <v-text-field
                          v-model="dataAttachment.description"
                          label="Kurzbeschreibung"
                          :rules="validationRules.description"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-col>
                  <v-col cols="auto" class="mt-3">
                    <iris-message-data-select-dialog
                      :value="dataAttachment"
                      @input="updateDataAttachment($event, index)"
                    >
                      <template #activator="{ attrs, on }">
                        <v-btn icon v-bind="attrs" v-on="on">
                          <v-icon> mdi-pencil </v-icon>
                        </v-btn>
                      </template>
                    </iris-message-data-select-dialog>
                    <confirm-dialog
                      title="Anhang entfernen?"
                      text="Möchten Sie den gewählten Anhang dieser Nachricht entfernen?"
                      @confirm="removeDataAttachment(index)"
                    >
                      <template #activator="{ attrs, on }">
                        <v-btn v-bind="attrs" v-on="on" icon color="error">
                          <v-icon> mdi-delete </v-icon>
                        </v-btn>
                      </template>
                    </confirm-dialog>
                  </v-col>
                </v-row>
                <v-divider class="mb-5" />
              </div>
              <iris-message-data-select-dialog @input="addDataAttachment" />
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="secondary"
            :disabled="disabled"
            plain
            :to="{ name: 'iris-message-list' }"
            replace
            data-test="cancel"
          >
            Abbrechen
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            :disabled="disabled"
            color="primary"
            @click="submit"
            data-test="submit"
          >
            Senden
          </v-btn>
        </v-card-actions>
      </v-card>
      <error-message-alert :errors="errors" />
    </v-form>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import {
  IrisMessageInsert,
  IrisMessageHdContact,
  IrisMessageDataInsert,
  IrisMessageDataDiscriminator,
} from "@/api";
import rules from "@/common/validation-rules";
import { ErrorMessage } from "@/utils/axios";
import _debounce from "lodash/debounce";
import IrisMessageDataSelectDialog from "@/views/iris-message-create/components/iris-message-data-select-dialog.vue";
import Discriminators from "@/constants/Discriminators";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import { getApiErrorMessages } from "@/utils/api";
import { bundleIrisMessageApi } from "@/modules/iris-message/api";
import store from "@/store";

type IrisMessageCreateForm = {
  model: IrisMessageInsert;
  valid: boolean;
};

@Component({
  components: {
    ConfirmDialog,
    IrisMessageDataSelectDialog,
    ErrorMessageAlert,
  },
  beforeRouteLeave(to, from, next) {
    store.commit("irisMessageCreate/reset");
    next();
  },
})
export default class IrisMessageCreateView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };
  messageApi = bundleIrisMessageApi(["createMessage", "fetchRecipients"]);
  form: IrisMessageCreateForm = {
    model: {
      subject: "",
      body: "",
      hdRecipient: "",
      dataAttachments:
        this.$store.state.irisMessageCreate.dataAttachments || [],
    },
    valid: false,
  };

  search: string | null = "";
  handleSearch = _debounce(async (value: string | null) => {
    this.search = value;
    await this.messageApi.fetchRecipients.execute(value);
  }, 500);

  get errors(): ErrorMessage[] {
    return getApiErrorMessages(this.messageApi);
  }

  addDataAttachment(messageData: IrisMessageDataInsert) {
    if (!this.form.model.dataAttachments) {
      this.form.model.dataAttachments = [];
    }
    this.form.model.dataAttachments.push(messageData);
  }

  updateDataAttachment(messageData: IrisMessageDataInsert, index: number) {
    if (this.form.model.dataAttachments) {
      this.form.model.dataAttachments[index] = messageData;
    }
  }

  removeDataAttachment(index: number) {
    if (this.form.model.dataAttachments) {
      this.form.model.dataAttachments.splice(index, 1);
    }
  }

  get recipients(): IrisMessageHdContact[] {
    return this.messageApi.fetchRecipients.state.result || [];
  }

  get recipientsLoading(): boolean {
    return this.messageApi.fetchRecipients.state.loading;
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [rules.defined],
      sanitised: [rules.sanitised],
      sanitisedAndDefined: [rules.sanitised, rules.defined],
      description: [rules.defined, rules.sanitised, rules.maxLength(255)],
    };
  }

  getDataLabel(discriminator: IrisMessageDataDiscriminator): string {
    return Discriminators.getLabel(discriminator);
  }

  get disabled(): boolean {
    return this.messageApi.createMessage.state.loading;
  }

  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      const payload: IrisMessageInsert = this.form.model;
      await this.messageApi.createMessage.execute(payload);
      await this.$router.replace({ name: "iris-message-list" });
    }
  }
}
</script>
