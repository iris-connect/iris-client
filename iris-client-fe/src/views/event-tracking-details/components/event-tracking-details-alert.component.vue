<template>
    <div class="alertOverlay">
        <v-alert
        :value="alert"
        color="blue"
        prominent
        dismissible
        elevation="4"
        type="success"
        transition="scale-transition"
        > Die Kontaktdaten zu diesem Ereignis wurden angefragt.
        </v-alert>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const EventTrackingDetailsAlertComponentProps = Vue.extend({
  props: {
  }
});

@Component
export default class EventTrackingDetailsAlertComponent extends EventTrackingDetailsAlertComponentProps {
  
  alert = false;

  created() {
    if(this.$route.params.is_created == 'true') {
      this.openAlert();
    }
  }

  openAlert(): void {    
    this.alert = true;
    setTimeout(()=>{
      this.alert = false;
    }, 2000);
  }
}
</script>
