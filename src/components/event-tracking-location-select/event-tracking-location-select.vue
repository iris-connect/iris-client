<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        color="red lighten-2"
        dark
        v-bind="attrs"
        v-on="on"
        :disabled="isDisabled"
      >
        {{ model ? "Lokation ändern" : "Lokation auswählen" }}
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="headline grey lighten-2">
        Lokation auswählen
      </v-card-title>
      <v-card-text>
        <v-row class="mt-6 mb-6">
          <v-col>
            <v-text-field
              v-model="search"
              :disabled="isDisabled"
              append-icon="mdi-magnify"
              label="Suche nach Name/Adresse"
              single-line
              hide-details
              @keydown.enter="handleSearch(search)"
            />
          </v-col>
          <v-col>
            <v-btn :disabled="isDisabled" @click="handleSearch(search)">
              Lokation Suchen
            </v-btn>
          </v-col>
        </v-row>
        <v-data-table
          :loading="isDisabled"
          :headers="tableProps.headers"
          :items="locationRows"
          :items-per-page="5"
          class="elevation-1 twolineTable"
        >
          <template v-slot:item.actions="{ item }">
            <v-btn color="primary" @click="handleSelect(item)"> Wählen </v-btn>
          </template>
        </v-data-table>
      </v-card-text>
      <v-divider />
      <v-card-actions>
        <v-btn color="secondary" text @click="dialog = false">
          Abbrechen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import {
  IrisClientFrontendApiFactory,
  LocationAddress,
  LocationInformation,
} from "@/api";
import { clientConfig } from "@/main";
import { join } from "@/utils/misc";

type LocationInformationTableRow = {
  address: string;
  // category: string;
  representative: string;
  mail: string;
  phone: string;
  location: LocationInformation;
};

const EventTrackingLocationSelectProps = Vue.extend({
  props: {
    value: {
      type: Object as () => LocationInformation,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
});

@Component
export default class EventTrackingLocationSelect extends EventTrackingLocationSelectProps {
  client = IrisClientFrontendApiFactory(clientConfig);
  dialog = false;
  locations: LocationInformation[] = [];
  loading = false;
  search = "";
  get isDisabled(): boolean {
    return this.loading || this.disabled;
  }
  get locationRows(): LocationInformationTableRow[] {
    const getFormattedAddress = (address: LocationAddress) => {
      const { street, zip, city } = address;
      return join([street, zip, city], ", ");
    };
    return this.locations.map((location) => {
      const { name, contact } = location;
      return {
        name: name || "-",
        address: getFormattedAddress(contact.address) || "-",
        // category: location.contact.representative || "-",
        representative: contact.representative || "-",
        email: contact.email || "-",
        phone: contact.phone || "-",
        location,
      };
    });
  }
  tableProps = {
    search: "rep",
    headers: [
      {
        text: "Name",
        align: "start",
        sortable: true,
        value: "name",
      },
      { text: "Adresse", value: "address" },
      // { text: "Kategorie", value: "category" },
      { text: "Ansprechpartner", value: "representative" },
      { text: "Mail", value: "email" },
      { text: "Telefon", value: "phone" },
      { text: "", value: "actions", sortable: false },
    ],
  };
  handleSelect(item: { location: LocationInformation }): void {
    this.model = item.location;
    this.dialog = false;
  }
  async handleSearch(searchText: string): Promise<void> {
    this.loading = true;
    try {
      this.locations = (
        await this.client.searchSearchKeywordGet(searchText)
      ).data.locations;
    } catch (error) {
      console.log(error, error);
    }
    this.loading = false;
  }

  get model(): LocationInformation {
    return this.value;
  }
  set model(value: LocationInformation) {
    this.$emit("input", value);
  }
}
</script>

<style scoped></style>
