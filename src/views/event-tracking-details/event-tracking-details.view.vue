<template>
  <div>
    <h1>Details für Ereignis {{ eventId }}</h1>
  </div>
</template>

<script lang="ts">
import router from "@/router";
import { Component, Vue } from "vue-property-decorator";

@Component
export default class EventTrackingDetailsView extends Vue {
  eventId = router.currentRoute.params.id;
}
</script>
