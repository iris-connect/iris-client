<template>
  <div v-if="workInProgress" class="work-in-progress">
    <v-row>
      <v-col>
        <v-card>
          <v-card-title>Indexfallverfolgung</v-card-title>
          <v-card-text>
            Hier erhalten Sie in Kürze eine Übersicht der Indexfälle sowie die
            Möglichkeit, eine neue Indexfallverfolgung zu starten.
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
  <div v-else>
    <v-row>
      <v-col cols="8">
        <div class="mb-6">
          Status:
          <v-btn class="ml-2 mr-2" color="white" @click="on">Angefragt </v-btn>
          <v-btn class="mr-2" color="white" @click="on">Update </v-btn>
          <v-btn class="mr-2" color="white" @click="on">Geschlossen </v-btn>
        </div>
      </v-col>
      <v-col cols="4">
        <div class="mb-6">
          <v-dialog
            transition="dialog-bottom-transition"
            max-width="98%"
            width="1600px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                class="float-right"
                color="primary"
                v-bind="attrs"
                @click="on"
                >Neue Indexfallverfolgung starten
              </v-btn>
            </template>
          </v-dialog>
        </div>
      </v-col>
    </v-row>

    <v-card>
      <v-card-title>Indexfallverfolgung</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-data-table
          :headers="tableData.headers"
          :items="tableData.eventList"
          :items-per-page="5"
          class="elevation-1 mt-5 twolineTable"
          :search="tableData.search"
        >
          <template v-slot:item.status="{ item }">
            <v-chip :color="getStatusColor(item.status)" dark>
              {{ item.status }}
            </v-chip>
          </template>
          <template v-slot:item.actions="{ item }">
            <v-btn
              color="primary"
              :to="'details/' + item.tan"
              @click="selectItem(item)"
            >
              Details
            </v-btn>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";

@Component({
  components: {
    EventTrackingFormView: EventTrackingFormView,
  },
})
export default class EventTrackingListView extends Vue {
  workInProgress = true;
  tableData = {
    search: "",
    headers: [
      {
        text: "Ext.ID",
        align: "start",
        sortable: true,
        value: "extID",
      },
      { text: "Name", value: "name" },
      { text: "Geburtsdatum", value: "birthdate" },
      { text: "Datum (Start)", value: "startTime" },
      { text: "Generiert", value: "generatedTime" },
      { text: "TAN", value: "tan" },
      { text: "Status", value: "status" },
      { text: "Letzte Ändrung", value: "lastChange" },
      { text: "", value: "actions" },
    ],
    eventList: [
      {
        extID: "ZEUCTEAL",
        name: "Hans Müller",
        birthdate: "14.03.1968",
        startTime: "20.03.2021",
        generatedTime: "31.03.2021 09:44",
        tan: "ASUE-DU3Z9-KTB8U",
        status: "Angefragt",
        lastChange: "31.03.2021 09:44",
      },
      {
        extID: "JAUTGREO",
        name: "Lisa Maier",
        birthdate: "05.10.1988",
        startTime: "23.03.2021",
        generatedTime: "26.03.2021 13:15",
        tan: "AS3H-65TYJ-9RHM",
        status: "UPDATE",
        lastChange: "26.03.2021 13:19",
      },
    ],
  };

  on(): void {
    console.log("NOT IMPLEMENTED");
  }

  getStatusColor(status: string): string {
    if (status == "Angefragt") return "blue";
    else if (status == "UPDATE") return "red";
    else if (status == "Abgeschlossen") return "green";
    else return "";
  }
}
</script>

<style lang="scss" scoped>
.work-in-progress {
  & > * {
    margin-bottom: 1em;
    margin-top: 1em;
  }
}
</style>
