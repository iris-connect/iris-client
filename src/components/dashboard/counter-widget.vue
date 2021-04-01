<template lang="html">
  <section class="counter-widget">
    <v-card>
      <v-list-item three-line>
        <v-list-item-content>
          <v-list-item-title class="headline mb-1">{{
            count
          }}</v-list-item-title>
          <v-list-item-subtitle>
            {{ subtitle }}
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-img
          alt="Dashbord Illustration"
          class="shrink mr-4"
          contain
          :src="getPic(image)"
          transition="scale-transition"
          width="90"
        />
      </v-list-item>

      <v-card-actions>
        <v-btn :to="actionlink" outlined rounded text> {{ actionlabel }}</v-btn>
      </v-card-actions>
    </v-card>
  </section>
</template>

<script lang="js">

  export default  {
    name: 'counter-widget',
    props: ['subtitle','count','actionlabel','actionlink','image'],
    // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
    mounted () {
//
    },
    methods: {
      // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
      getPic(index) {
        return require(`@/assets/images/`+index)
      }
    },
    computed: {

    }
}
</script>

<style scoped lang="scss">
.counter-widget {
  .v-card {
    padding: 20px 10px;
  }
  .headline {
    color: #46ff9f;
    font-size: 2.2rem !important;
  }
  .v-card__actions {
    display: grid;
    .v-btn {
      font-size: 0.675rem;
    }
  }
}
</style>
