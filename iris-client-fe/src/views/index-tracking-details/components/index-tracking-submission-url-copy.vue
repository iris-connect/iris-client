<template>
  <div v-bind="$attrs" v-on="$listeners">
    <v-btn icon @click="copyToClipboard">
      <v-icon>mdi-content-copy</v-icon>
    </v-btn>
    <v-snackbar v-model="copyFeedback">
      {{ copyFeedbackMessage }}
    </v-snackbar>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

function copyTextToClipboardFallback(text: string): Promise<unknown> {
  const textArea = document.createElement("textarea");
  textArea.value = text;
  textArea.style.top = "0";
  textArea.style.left = "0";
  textArea.style.position = "fixed";
  document.body.appendChild(textArea);
  textArea.focus();
  textArea.select();
  try {
    const successful = document.execCommand("copy");
    if (successful) return Promise.resolve();
    return Promise.reject(new Error("copy failed"));
  } catch (err) {
    return Promise.reject(err);
  } finally {
    document.body.removeChild(textArea);
  }
}
function copyTextToClipboard(text: string): Promise<unknown> {
  if (!navigator.clipboard) {
    return copyTextToClipboardFallback(text);
  }
  return navigator.clipboard.writeText(text);
}

const IndexTrackingSubmissionUrlCopyProps = Vue.extend({
  props: {
    url: {
      type: String,
      default: "",
    },
  },
});

@Component
export default class IndexTrackingSubmissionUrlCopy extends IndexTrackingSubmissionUrlCopyProps {
  copyFeedbackMessage = "";
  copyToClipboard(): void {
    this.copyFeedbackMessage = "";
    copyTextToClipboard(this.url)
      .then(() => {
        this.copyFeedbackMessage = "URL erfolgreich kopiert";
      })
      .catch(() => {
        this.copyFeedbackMessage = "URL kopieren fehlgeschlagen";
      });
  }
  get copyFeedback(): boolean {
    return !!this.copyFeedbackMessage;
  }
  set copyFeedback(val: boolean) {
    if (!val) {
      this.copyFeedbackMessage = "";
    }
  }
}
</script>
