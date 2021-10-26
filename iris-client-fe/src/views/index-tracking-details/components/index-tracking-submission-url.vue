<template>
  <div class="d-inline-block index-tracking-submission-url">
    <v-row class="align-center">
      <v-col v-on:dblclick="toggleUrl" class="toggle-url overflow-hidden py-0">
        <v-tooltip bottom>
          <span v-if="expanded" data-test="submission-url.collapse.tooltip">
            URL minimieren per Doppelklick
          </span>
          <span v-else data-test="submission-url.expand.tooltip">
            Vollständige URL anzeigen per Doppelklick
          </span>
          <template v-slot:activator="{ on, attrs }">
            <span
              v-on="on"
              v-bind="attrs"
              :class="['d-block', expanded ? '' : 'text-truncate']"
              data-test="submission-url.url"
            >
              {{ url }}
            </span>
          </template>
        </v-tooltip>
      </v-col>
      <v-col class="d-flex flex-nowrap flex-grow-0 flex-shrink-0 py-0">
        <v-tooltip bottom>
          <span data-test="submission-url.copy.tooltip">
            URL in Zwischenablage kopieren
          </span>
          <template v-slot:activator="{ on, attrs }">
            <index-tracking-submission-url-copy
              v-on="on"
              v-bind="attrs"
              :url="url"
              data-test="submission-url.copy"
            />
          </template>
        </v-tooltip>
        <v-tooltip bottom>
          <span data-test="submission-url.mail.tooltip">
            URL per E-Mail versenden
          </span>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-on="on"
              v-bind="attrs"
              class="text-decoration-none"
              icon
              :href="`mailto:?subject=${subject}&body=${body}${url}`"
              data-test="submission-url.mail"
            >
              <v-icon>mdi-email-edit-outline</v-icon>
            </v-btn>
          </template>
        </v-tooltip>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import IndexTrackingSubmissionUrlCopy from "@/views/index-tracking-details/components/index-tracking-submission-url-copy.vue";

const IndexTrackingSubmissionUrlProps = Vue.extend({
  props: {
    subject: {
      type: String,
      default: "Übermittlungs-URL",
    },
    body: {
      type: String,
      default: "",
    },
    url: {
      type: String,
      default: "",
    },
  },
});

@Component({
  components: {
    IndexTrackingSubmissionUrlCopy,
  },
})
export default class IndexTrackingSubmissionUrl extends IndexTrackingSubmissionUrlProps {
  expanded = false;
  toggleUrl(): void {
    this.expanded = !this.expanded;
  }
}
</script>

<style lang="scss" scoped>
.index-tracking-submission-url {
  max-width: 100%;
}
.toggle-url {
  cursor: pointer;
  max-width: 400px;
}
</style>
