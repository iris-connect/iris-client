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
          <v-autocomplete
            v-model="form.model.hdRecipient"
            label="Empfänger"
            :items="recipients"
            :rules="validationRules.defined"
            :menu-props="{ contentClass: 'select-menu-recipient' }"
            item-text="name"
            item-value="id"
            data-test="hdRecipient"
            :loading="recipientsLoading"
          ></v-autocomplete>
          <v-text-field
            v-model="form.model.subject"
            label="Betreff"
            :rules="validationRules.sanitisedAndDefined"
            maxlength="50"
            data-test="subject"
          ></v-text-field>
          <v-textarea
            v-model="form.model.body"
            label="Nachricht"
            auto-grow
            rows="5"
            value=""
            :rules="validationRules.sanitisedAndDefined"
            data-test="body"
          ></v-textarea>
          <v-file-input
            label="Datei(en) anfügen"
            :value="form.model.attachments"
            @change="addAttachments"
            @click:clear="clearAttachments"
            multiple
            data-test="attachments"
          >
            <template v-slot:selection="{ index, text }">
              <v-chip
                :key="index"
                dark
                color="blue"
                close
                @click:close="removeAttachment(index)"
                data-test="attachments.remove"
              >
                {{ text }}
              </v-chip>
            </template>
          </v-file-input>
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
import _unionBy from "lodash/unionBy";
import { ErrorMessage } from "@/utils/axios";

type IrisMessageCreateForm = {
  model: IrisMessageInsert;
  valid: boolean;
};

@Component({
  components: {
    ErrorMessageAlert,
  },
  beforeRouteEnter(to, from, next) {
    store.dispatch("irisMessageCreate/fetchRecipients");
    next();
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
  form: IrisMessageCreateForm = {
    model: {
      subject: "",
      body: "",
      hdRecipient: "",
      attachments: [],
    },
    valid: false,
  };

  get errors(): ErrorMessage[] {
    return [
      this.$store.state.irisMessageCreate.contactsLoadingError,
      this.$store.state.irisMessageCreate.messageCreationError,
    ];
  }
  addAttachments(files: File[]) {
    this.form.model.attachments = _unionBy(
      this.form.model.attachments,
      files,
      "name"
    );
  }
  removeAttachment(index: number) {
    if (this.form.model.attachments) {
      this.form.model.attachments.splice(index, 1);
    }
  }
  clearAttachments() {
    this.form.model.attachments = [];
  }

  get recipients(): IrisMessageHdContact[] {
    return this.$store.state.irisMessageCreate.contacts;
  }

  get recipientsLoading(): boolean {
    return this.$store.state.irisMessageCreate.contactsLoading;
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [rules.defined],
      sanitised: [rules.sanitised],
      sanitisedAndDefined: [rules.sanitised, rules.defined],
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
