<template>
  <div class="pt-16">
    <v-dialog v-model="feedbackDialog" max-width="500">
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          v-bind="{ ...$attrs, ...attrs }"
          fab
          dark
          fixed
          bottom
          right
          v-on="on"
        >
          <v-icon :size="30"> mdi-chat-alert-outline</v-icon>
          <slot />
        </v-btn>
      </template>
      <v-form
        ref="form"
        lazy-validation
        v-model="form.valid"
        :disabled="feedbackSubmissionOngoing"
      >
        <v-card>
          <v-card-title> Feedback abgeben </v-card-title>
          <v-card-subtitle> Wir freuen uns über Ihr Feedback! </v-card-subtitle>
          <v-card-text>
            <v-autocomplete
              class="mb-3"
              v-model="form.model.category"
              :items="['Verbesserungsvorschlag', 'Problem']"
              label="Kategorie"
              required
              :rules="validationRules.sanitisedAndDefined"
            ></v-autocomplete>
            <v-text-field
              class="mb-3"
              v-model="form.model.title"
              label="Titel"
              required
              :rules="validationRules.title"
              maxlength="100"
            ></v-text-field>
            <v-textarea
              class="mb-3"
              v-model="form.model.comment"
              label="Ihr Platz für Feedback"
              no-resize
              outlined
              counter
              required
              :rules="validationRules.text"
              maxlength="1000"
            ></v-textarea>
            <v-text-field
              class="mb-3"
              v-model="form.model.name"
              label="Name"
              :rules="validationRules.sanitisedAndDefined"
            ></v-text-field>
            <v-text-field
              class="mb-3"
              v-model="form.model.organisation"
              label="Organisation"
              required
              :rules="validationRules.sanitisedAndDefined"
            ></v-text-field>
            <v-text-field
              v-model="form.model.email"
              label="E-Mail"
              :rules="validationRules.email"
              required
            ></v-text-field>
            <v-alert v-if="feedbackSubmissionError" text type="error">{{
              feedbackSubmissionError
            }}</v-alert>
          </v-card-text>
          <v-divider />
          <v-card-actions>
            <confirm-dialog @confirm="feedbackDialog = false">
              <template #activator="{ on, attrs }">
                <v-btn
                  @click="
                    isPristine() ? (feedbackDialog = false) : on.click($event)
                  "
                  v-bind="attrs"
                  color="secondary"
                  text
                  :disabled="feedbackSubmissionOngoing"
                >
                  Abbrechen
                </v-btn>
              </template>
              <template #title> Feedback verwerfen </template>
              <template #text>
                Klicken Sie auf "Bestätigen", um Ihr Feedback zu vewerfern. Ihre
                Eingaben werden vollständig gelöscht.
              </template>
            </confirm-dialog>
            <v-spacer></v-spacer>
            <confirm-dialog @confirm="submit">
              <template #activator="{ on, attrs }">
                <v-btn
                  @click="isValid() ? on.click($event) : noop"
                  v-bind="attrs"
                  color="primary"
                  text
                  :disabled="feedbackSubmissionOngoing"
                >
                  Absenden
                </v-btn>
              </template>
              <template #title> Achtung! </template>
              <template #text>
                <p>
                  Mit Klick auf "Bestätigen" wird auf Basis Ihres Feedbacks
                  automatisch ein Ticket auf GitHub angelegt und veröffentlicht.
                </p>
                <p>
                  Bitte fahren Sie nur dann fort, wenn Sie damit einverstanden
                  sind.
                </p>
                Dieser Schritt kann
                <span class="text-decoration-underline">nicht</span>
                rückgängig gemacht werden.
              </template>
            </confirm-dialog>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-dialog>
    <v-dialog v-model="submitFeedback" max-width="500">
      <v-card>
        <v-card-title> Vielen Dank! </v-card-title>
        <v-card-text>
          Ihr Feedback wurde erfolgreich entgegen genommen.
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="submitFeedback = false">
            Ok
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { FeedbackInsert } from "@/api";
import rules from "@/common/validation-rules";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import _noop from "lodash/noop";
import _isEqual from "lodash/isEqual";

type FeedbackForm = {
  model: FeedbackInsert;
  valid: boolean;
};

/**
 * The Feedback-Dialog is a view, only visible if the connected button is pressed. It follows the rules of the dialog and has form rules for being able to be send.
 * The fields category, title and text has to be filled and within letter limit.
 */
@Component({
  components: {
    ConfirmDialog,
  },
})
export default class FeedbackDialog extends Vue {
  $refs!: {
    form: HTMLFormElement;
  };

  get initialValues(): FeedbackInsert {
    return {
      category: "",
      title: "",
      comment: "",
      organisation: "",
      email: "",
      name: this.$store.getters["userLogin/userDisplayName"],
    };
  }

  form: FeedbackForm = {
    model: { ...this.initialValues },
    valid: false,
  };

  noop = _noop;

  feedbackDialog = false;
  submitFeedback = false;

  get validationRules(): Record<string, Array<unknown>> {
    return {
      title: [rules.defined, rules.sanitised, rules.maxLength(100)],
      text: [rules.defined, rules.sanitised, rules.maxLength(1000)],
      email: [rules.sanitised, rules.email],
      sanitisedAndDefined: [rules.sanitised, rules.defined],
    };
  }

  get feedbackSubmissionOngoing(): boolean {
    return store.state.feedback.feedbackSubmissionOngoing;
  }

  get feedbackSubmissionError(): ErrorMessage {
    return store.state.feedback.feedbackSubmissionError;
  }

  isPristine(): boolean {
    return _isEqual(this.initialValues, this.form.model);
  }

  isValid(): boolean {
    return this.$refs.form?.validate() as boolean;
  }

  // show submit feedback after successful submission
  async submit(): Promise<void> {
    if (this.isValid()) {
      await store.dispatch("feedback/submitFeedback", this.form.model);
      this.submitFeedback = true;
    }
  }

  // if submit feedback is shown: close dialog - this will trigger the reset functionality
  @Watch("submitFeedback")
  onSubmitFeedbackChanged(newValue: boolean): void {
    if (newValue) {
      this.feedbackDialog = false;
    }
  }

  // reset everything if dialog is opened or closed to get a clean state
  @Watch("feedbackDialog")
  onFeedbackDialogChanged(): void {
    this.form = {
      model: { ...this.initialValues },
      valid: false,
    };
    this.$refs.form?.resetValidation();
    store.commit("feedback/reset");
  }
}
</script>
