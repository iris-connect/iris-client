<template>
  <div>
    <p>
      <strong>{{ locationContact.officialName }}</strong>
    </p>
    <v-row>
      <v-col>
        {{ locationAddress.street }}<br />
        {{ locationAddress.zip }}<br />
        {{ locationAddress.city }}
      </v-col>
      <v-col>
        {{ locationContact.representative }}<br />
        {{ locationContact.email }}<br />
        {{ locationContact.phone }}
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
  get locationContact(): LocationContact {
    return this.location?.contact;
  }
  get locationAddress(): LocationAddress {
    return this.locationContact?.address;
  }
}
</script>
