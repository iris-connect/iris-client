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

const AlertComponentProps = Vue.extend({
  props: {
  }
});

@Component
export default class AlertComponent extends AlertComponentProps {
  
  alert = false;

  created() {
    if(this.$route.query.is_created == 'false') {
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
