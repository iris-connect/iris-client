<template>
  <transition name="modal">
    <div class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <v-card>
            <v-card-title class="modal-header">
              <h3>Daten exportieren</h3>
            </v-card-title>
            <v-divider />
            <v-card-text class="modal-body">
              <v-row align="center">
                <v-col cols="8">
                  <span>Standard-csv</span>
                </v-col>
                <v-col cols="4">
                  <v-btn color="primary" @click="handleStandardCsvExport"
                    >Download</v-btn
                  >
                </v-col>
              </v-row>
              <v-row align="center">
                <v-col cols="8">
                  <span>Alternative Standard-csv</span>
                </v-col>
                <v-col cols="4">
                  <v-btn
                    color="primary"
                    @click="handleAlternativeStandardCsvExport"
                    >Download</v-btn
                  >
                </v-col>
              </v-row>
              <v-row align="center">
                <v-col cols="8">
                  <span>SORMAS-csv (Ereignisteilnehmer-Format)</span>
                </v-col>
                <v-col cols="4">
                  <v-btn
                    color="primary"
                    @click="handleSormasCsvEventParticipantsExport"
                    >Download</v-btn
                  >
                </v-col>
              </v-row>
              <v-row align="center">
                <v-col cols="8">
                  <span>SORMAS-csv (Kontaktpersonen-Format)</span>
                </v-col>
                <v-col cols="4">
                  <v-btn
                    color="primary"
                    @click="handleSormasCsvContactPersonExport"
                    >Download</v-btn
                  >
                </v-col>
              </v-row>
            </v-card-text>
            <v-divider />
            <v-card-actions>
              <v-row no-gutters justify="end">
                <v-col cols="4">
                  <v-btn text @click="closeExportModal">Schließen</v-btn>
                </v-col>
              </v-row>
            </v-card-actions>
          </v-card>
        </div>
      </div>
    </div>
  </transition>
</template>
<style>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-container {
  width: 500px;
  margin: 0px auto;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
  font-family: Helvetica, Arial, sans-serif;
}

.modal-header h3 {
  margin-top: 0;
}

.modal-body {
  margin: 20px 0;
}

.modal-default-button {
  float: right;
}

/*
  * The following styles are auto-applied to elements with
  * transition="modal" when their visibility is toggled
  * by Vue.js.
  *
  * You can easily play with the modal transition by editing
  * these styles.
  */

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
}
</style>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const EventTrackingDetailsExportProps = Vue.extend({
  props: {},
});

@Component
export default class EventTrackingDetailsExport extends EventTrackingDetailsExportProps {
  closeExportModal(): void {
    this.$emit("close-export-modal");
  }

  handleStandardCsvExport(): void {
    this.$emit("handle-standard-csv-export");
  }

  handleAlternativeStandardCsvExport(): void {
    this.$emit("handle-alternative-standard-csv-export");
  }

  handleSormasCsvEventParticipantsExport(): void {
    this.$emit("handle-sormas-csv-event-participants-export");
  }

  handleSormasCsvContactPersonExport(): void {
    this.$emit("handle-sormas-csv-contact-person-export");
  }
}
</script>
