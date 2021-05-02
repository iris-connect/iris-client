<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <slot name="activator" v-bind="{ on, attrs }"></slot>
    </template>
    <v-card>
      <v-card-title> Ereignisort auswählen </v-card-title>
      <v-card-text>
        <v-row class="my-6">
          <v-col cols="12" sm="6">
            <v-text-field
              v-model="search"
              :disabled="disabled"
              append-icon="mdi-magnify"
              label="Suche nach Name/Adresse"
              single-line
              hide-details
              @keydown.enter="handleSearch(search)"
            />
          </v-col>
          <v-col cols="12" sm="6" class="d-flex align-end">
            <v-btn :disabled="disabled" @click="handleSearch(search)">
              Ereignisort suchen
            </v-btn>
          </v-col>
        </v-row>
        <v-data-table
          :loading="disabled"
          :headers="tableProps.headers"
          :items="locationRows"
          :items-per-page="5"
          class="twolineTable"
        >
          <template v-slot:item.actions="{ item }">
            <v-btn color="primary" @click="handleSelect(item)"> Wählen </v-btn>
          </template>
        </v-data-table>
        <v-alert v-if="error" text type="error">{{ error }}</v-alert>
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
import { LocationAddress, LocationInformation } from "@/api";
import { join } from "@/utils/misc";

const getFormattedAddress = (address: LocationAddress): string => {
  const { street, zip, city } = address;
  return join([street, zip, city], ", ");
};

type LocationInformationTableRow = {
  name: string;
  address: string;
  representative: string;
  email: string;
  phone: string;
  location: LocationInformation;
};

const EventTrackingFormLocationSelectProps = Vue.extend({
  props: {
    value: {
      type: Object as () => LocationInformation,
      default: null,
    },
    locations: {
      type: Array as () => LocationInformation[],
      default: () => [],
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    error: {
      type: String,
      default: null,
    },
  },
});

@Component
export default class EventTrackingFormLocationSelect extends EventTrackingFormLocationSelectProps {
  dialog = false;
  search = "";

  get model(): LocationInformation {
    return this.value;
  }
  set model(value: LocationInformation) {
    this.$emit("input", value);
  }

  get locationRows(): LocationInformationTableRow[] {
    return (this.locations || []).map((location) => {
      const { name, contact } = location;
      return {
        name: name || "-",
        address: getFormattedAddress(contact.address) || "-",
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

  handleSearch(searchText: string): void {
    this.$emit("search", searchText);
  }
}
</script>

<style scoped></style>
