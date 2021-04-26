<template>
  <v-row>
    <v-dialog style="overflow-y: hidden" v-model="show" max-width="800px">
      <v-card>
        <v-card-title class="justify-center">
          <span class="headline">FEEDBACK BOGEN</span>
        </v-card-title>

        <v-card-subtitle style="text-align: center">
          <span class="headline">Wir freuen uns über ihr Feedback.</span>
        </v-card-subtitle>

        <v-container>
          <v-col class="justify-content:center" cols="auto">
            <v-autocomplete
              :items="[
                'Skiing',
                'Ice hockey',
                'Soccer',
                'Basketball',
                'Hockey',
                'Reading',
                'Writing',
                'Coding',
                'Basejump',
              ]"
              label="Katergorie auswählen"
              multiple
            ></v-autocomplete>

            <v-text-field
              label="Titel*"
              required
              counter
              maxlength="100"
            ></v-text-field>

            <v-textarea
              class="justify-center"
              label="Ihr Platz für Feedback"
              outlined
              max-height="200px"
              no-resize
              counter
              maxlength="1000"
            ></v-textarea>

            <v-text-field
              class="justify-center"
              label="Organisation*"
              required
              outlined
              maxlength="50"
            ></v-text-field>

            <v-row>
              <v-col cols="12" sm="6">
                <v-text-field
                  class="justify-center"
                  label="First name*"
                  required
                  outlined
                  maxlength="25"
                ></v-text-field>
              </v-col>

              <v-col cols="12" sm="6">
                <v-text-field
                  class="justify-center"
                  label="Last name*"
                  outlined
                  maxlength="25"
                ></v-text-field>
              </v-col>
            </v-row>

            <v-text-field
              class="justify-center"
              label="E-Mail*"
              required
              outlined
              maxlength="80"
            ></v-text-field>

            <v-file-input outlined accept="image/*" label="File input">
            </v-file-input>
          </v-col>
        </v-container>

        <v-row>
          <v-col class="d-flex justify-center">
            <v-btn color="blue darken-1" text outlined @click="show = false">
              Abbrechen
            </v-btn>
          </v-col>
          <v-col class="d-flex justify-center">
            <v-btn color="green accent-2" outlined text> Absenden </v-btn>
          </v-col>
        </v-row>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  props: {
    value: Boolean,
  },
  computed: {
    show: {
      get() {
        return this.value;
      },
      set(value) {
        this.$emit("input", value);
      },
    },
  },
};
</script>
