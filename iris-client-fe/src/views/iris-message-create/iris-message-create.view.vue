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
            hide-details="auto"
            :class="{
              'is-loading': recipientsLoading,
              'is-empty': recipients.length <= 0,
            }"
          />
          <p class="text-caption mt-2">
            Tippen Sie den Bezeichner des Gesundheitsamtes, die
            <strong>Postleitzahl</strong> oder den <strong>Ort</strong> ein, um
            nach dem Empfänger zu suchen.
          </p>
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
import { ErrorMessage } from "@/utils/axios";
import { debounce } from "lodash";

type IrisMessageCreateForm = {
  model: IrisMessageInsert;
  valid: boolean;
};

@Component({
  components: {
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
  form: IrisMessageCreateForm = {
    model: {
      subject: "",
      body: "",
      hdRecipient: "",
    },
    valid: false,
  };

  search: string | null = "";
  handleSearch = debounce(async (value: string | null) => {
    this.search = value;
    await store.dispatch("irisMessageCreate/fetchRecipients", value);
  }, 500);

  get errors(): ErrorMessage[] {
    return [
      this.$store.state.irisMessageCreate.contactsLoadingError,
      this.$store.state.irisMessageCreate.messageCreationError,
    ];
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