<template>
  <div>
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
              <p class="subtitle-1">Daten anfügen</p>
              <div
                :key="index"
                v-for="(dataAttachment, index) in form.model.dataAttachments"
              >
                <v-row no-gutters class="py-3">
                  <v-col>
                    <v-row>
                      <v-col cols="auto">
                        <strong>
                          {{ dataAttachment.discriminator }}
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
                    <v-btn icon :disabled="true">
                      <v-icon> mdi-pencil </v-icon>
                    </v-btn>
                    <v-btn
                      icon
                      color="error"
                      @click="removeDataAttachment(index)"
                    >
                      <v-icon> mdi-delete </v-icon>
                    </v-btn>
                  </v-col>
                </v-row>
              </div>
            </v-col>
            <v-col cols="12">
              <!--
              <v-file-input
                label="Datei(en) anfügen"
                :value="form.model.fileAttachments"
                @change="addFileAttachments"
                @click:clear="clearFileAttachments"
                multiple
                data-test="fileAttachments"
                :accept="allowedFileTypes.join(',')"
              >
                <template v-slot:selection="{ index, text }">
                  <v-chip
                    :key="index"
                    dark
                    color="blue"
                    close
                    @click:close="removeFileAttachment(index)"
                    data-test="fileAttachments.remove"
                  >
                    {{ text }}
                  </v-chip>
                </template>
              </v-file-input>
              -->
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
import store from "@/store";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { IrisMessageInsert, IrisMessageHdContact } from "@/api";
import rules from "@/common/validation-rules";
// disabled file attachments
// import _unionBy from "lodash/unionBy";
import { ErrorMessage } from "@/utils/axios";
import _debounce from "lodash/debounce";

type IrisMessageCreateForm = {
  model: IrisMessageInsert;
  valid: boolean;
};

@Component({
  components: {
    ErrorMessageAlert,
  },
  // disabled file attachments
  /*
  beforeRouteEnter(to, from, next) {
    store.dispatch("irisMessageCreate/fetchAllowedFileTypes");
    next();
  },
   */
  beforeRouteLeave(to, from, next) {
    store.commit("irisMessageCreate/reset");
    next();
  },
})
export default class IrisMessageCreateView extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };
  form: IrisMessageCreateForm = {
    model: {
      subject: "",
      body: "",
      hdRecipient: "",
      dataAttachments:
        this.$store.state.irisMessageCreate.dataAttachments || [],
      // disabled file attachments
      // fileAttachments: [],
    },
    valid: false,
  };

  search: string | null = "";
  handleSearch = _debounce(async (value: string | null) => {
    this.search = value;
    await store.dispatch("irisMessageCreate/fetchRecipients", value);
  }, 500);

  get errors(): ErrorMessage[] {
    return [
      this.$store.state.irisMessageCreate.contactsLoadingError,
      this.$store.state.irisMessageCreate.messageCreationError,
    ];
  }
  // disabled file attachments
  /*
  addFileAttachments(files: File[]) {
    this.form.model.fileAttachments = _unionBy(
      this.form.model.fileAttachments,
      files,
      "name"
    );
  }
  removeFileAttachment(index: number) {
    if (this.form.model.fileAttachments) {
      this.form.model.fileAttachments.splice(index, 1);
    }
  }
  clearFileAttachments() {
    this.form.model.fileAttachments = [];
  }
  get allowedFileTypes(): string[] {
    return this.$store.state.irisMessageCreate.allowedFileTypes || [];
  }
   */

  removeDataAttachment(index: number) {
    if (this.form.model.dataAttachments) {
      this.form.model.dataAttachments.splice(index, 1);
    }
  }

  get recipients(): IrisMessageHdContact[] {
    return this.$store.state.irisMessageCreate.contacts || [];
  }

  get recipientsLoading(): boolean {
    return this.$store.state.irisMessageCreate.contactsLoading;
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [rules.defined],
      sanitised: [rules.sanitised],
      sanitisedAndDefined: [rules.sanitised, rules.defined],
      description: [rules.defined, rules.sanitised, rules.maxLength(255)],
    };
  }

  get disabled(): boolean {
    return this.$store.state.irisMessageCreate.messageCreationOngoing;
  }

  async submit(): Promise<void> {
    const valid = this.$refs.form.validate() as boolean;
    if (valid) {
      const payload: IrisMessageInsert = this.form.model;
      await store.dispatch("irisMessageCreate/createMessage", payload);
      await this.$router.replace({ name: "iris-message-list" });
    }
  }
}
</script>
