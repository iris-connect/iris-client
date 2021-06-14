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
              label="Suche (min. 4 Buchstaben)"
              single-line
              hide-details
              @keydown.enter="handleSearch(search)"
            />
          </v-col>
          <v-col cols="12" sm="6" class="d-flex align-end">
            <v-btn
              :disabled="disabled || search.length < 4"
              @click="handleSearch(search)"
            >
              Ereignisort suchen
            </v-btn>
          </v-col>
        </v-row>
        <v-data-table
          :loading="disabled"
          :page="dataTableModel.page"
          :server-items-length="dataTableModel.itemsLength"
          :headers="dataTableModel.headers"
          :items="dataTableModel.data"
          :items-per-page="dataTableModel.itemsPerPage"
          :footer-props="{ 'items-per-page-options': [10, 20, 30, 50] }"
          @update:options="updatePagination"
          class="twolineTable"
        >
          <template v-slot:[itemNameSlotName]="{ item }">
            <span class="text-pre-wrap"> {{ item.name }} </span>
          </template>
          <template v-slot:[itemActionsSlotName]="{ item }">
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
import { LocationAddress, LocationInformation, LocationList } from "@/api";
import { join } from "@/utils/misc";
import { DataQuery, getSortAttribute } from "@/api/common";
import { DataOptions } from "node_modules/vuetify/types";

const getFormattedAddress = (address: LocationAddress): string => {
  if (!address) {
    return "-";
  }

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
    locationList: {
      type: Object as () => LocationList,
      default: null,
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

  get itemNameSlotName(): string {
    return "item.name";
  }

  get itemActionsSlotName(): string {
    return "item.actions";
  }

  get locationRows(): LocationInformationTableRow[] {
    return (this.locationList?.locations || []).map((location) => {
      const { name, contact } = location;
      let combinedName = name;
      if (contact && contact.officialName) {
        combinedName += "\n(" + contact.officialName + ")";
      }

      return {
        name: combinedName || "-",
        address: getFormattedAddress(contact?.address) || "-",
        representative: contact?.representative || "-",
        email: contact?.email || "-",
        phone: contact?.phone || "-",
        location,
      };
    });
  }

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  get dataTableModel() {
    return {
      page: this.locationList.page + 1,
      itemsPerPage: this.locationList.size,
      itemsLength: this.locationList.totalElements,
      data: this.locationRows,
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
  }

  async updatePagination(pagination: DataOptions): Promise<void> {
    if (this.search.length < 4) {
      return;
    }
    let sort = getSortAttribute(pagination.sortBy[0]);
    if (sort) {
      pagination.sortDesc[0] ? (sort = sort + ",desc") : (sort = sort + ",asc");
    }

    const query: DataQuery = {
      size: pagination.itemsPerPage,
      page: pagination.page - 1,
      sort: sort,
      search: this.search,
    };
    this.$emit("search", query);
  }

  handleSelect(item: { location: LocationInformation }): void {
    this.model = item.location;
    this.dialog = false;
  }

  handleSearch(searchText: string): void {
    if (searchText.length < 4) {
      return;
    }
    const query: DataQuery = {
      size: this.dataTableModel.itemsPerPage,
      page: 0,
      search: searchText,
    };
    this.search = searchText;
    this.$emit("search", query);
  }
}
</script>

<style scoped></style>
