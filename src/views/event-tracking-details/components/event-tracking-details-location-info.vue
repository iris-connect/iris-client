<template>
  <v-row>
    <v-col
      cols="12"
      sm="6"
      v-for="([title, info], index) in locationInfo"
      :key="index"
    >
      <p class="mb-0">
        <strong> {{ title }}: </strong>
      </p>
      <template v-if="info.length > 0">
        <span
          class="d-block"
          v-for="(item, itemIndex) in info"
          :key="itemIndex"
        >
          {{ item }}
        </span>
      </template>
      <span v-else class="d-block"> - </span>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { LocationAddress, LocationContact, LocationInformation } from "@/api";

const EventTrackingDetailsLocationInfoProps = Vue.extend({
  props: {
    location: {
      type: Object as () => LocationInformation,
      default: null,
    },
  },
});

@Component
export default class EventTrackingDetailsLocationInfo extends EventTrackingDetailsLocationInfoProps {
  get locationContact(): Array<string> {
    const contact: LocationContact = this.location?.contact;
    return [
      contact?.representative ?? "",
      contact?.email ?? "",
      contact?.phone ?? "",
    ].filter((v) => !!v);
  }
  get locationAddress(): Array<string> {
    const address: LocationAddress = this.location?.contact?.address;
    return [
      this.location?.contact?.officialName ?? "",
      address?.street ?? "",
      [address?.zip ?? "", address?.city ?? ""].join(" ").trim(),
    ].filter((v) => v);
  }
  get locationInfo(): Array<Array<string | Array<string>>> {
    return [
      ["Ort", this.locationAddress],
      ["Ansprechpartner", this.locationContact],
    ];
  }
}
</script>
