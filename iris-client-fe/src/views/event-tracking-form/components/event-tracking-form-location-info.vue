<template>
  <div>
    <p>
      <strong>{{ locationName }}</strong>
    </p>
    <v-row>
      <v-col>
        <span
          class="d-block"
          v-for="(item, index) in locationAddress"
          :key="index"
        >
          {{ item }}
        </span>
      </v-col>
      <v-col>
        <span
          class="d-block"
          v-for="(item, index) in locationContact"
          :key="index"
        >
          {{ item }}
        </span>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { LocationAddress, LocationContact, LocationInformation } from "@/api";

const EventTrackingFormLocationInfoProps = Vue.extend({
  props: {
    location: {
      type: Object as () => LocationInformation,
      default: null,
    },
  },
});

@Component
export default class EventTrackingFormLocationInfo extends EventTrackingFormLocationInfoProps {
  get locationName(): string {
    return this.location?.contact?.officialName ?? "";
  }
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
      address?.street ?? "",
      address?.zip ?? "",
      address?.city ?? "",
    ].filter((v) => v);
  }
}
</script>
