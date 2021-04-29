<template>
  <div>
    <p>
      <strong>{{ locationContact.officialName }}</strong>
    </p>
    <v-row>
      <v-col>
        <span class="d-block" v-for="key in addressKeys" :key="key">
          {{ locationAddress[key] }}
        </span>
      </v-col>
      <v-col>
        <span class="d-block" v-for="key in contactKeys" :key="key">
          {{ locationContact[key] }}
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
  get locationContact(): LocationContact | Record<string, unknown> {
    return this.location?.contact ?? {};
  }
  get locationAddress(): LocationAddress | Record<string, unknown> {
    return this.location?.contact?.address ?? {};
  }
  addressKeys = ["street", "zip", "city"];
  contactKeys = ["representative", "email", "phone"];
}
</script>
