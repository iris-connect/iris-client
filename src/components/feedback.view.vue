<template>
  <v-row justify="center">
    <v-dialog v-model="show" max-width="600px">
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="primary" dark v-bind="attrs" v-on="on">
          Open Dialog
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <span class="headline">FEEDBACK BOGEN</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-col class="justify-center" cols="12">
              <v-autocomplete
                class="justify-center"
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
                outlined
              ></v-autocomplete>

              <v-text-field
                class="justify-center"
                label="Titel*"
                required
                outlined
                v-model="description"
                :rules="rules"
                counter
                maxlength="100"
              ></v-text-field>

              <v-textarea
                class="justify-center"
                label="Ihr Platz für Feedback"
                outlined
                max-height="200px"
                v-model="description"
                :rules="rules"
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
                    label="Fist name*"
                    required
                    outlined
                    maxlength="25"
                  ></v-text-field>
                </v-col>

                <v-col cols="12" sm="6">
                  <v-text-field
                    class="justify-center"
                    label="Last name*"
                    required
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

              <div class="justify-center">
                <v-btn
                  color="blue darken-1"
                  image
                  label
                  hint="example of persistent helper text"
                  persistent-hint
                  @click="platzhalter"
                >
                  hinzufügen
                </v-btn>
              </div>
            </v-col>
          </v-container>
          <small>*indicates required field</small>
        </v-card-text>
        <v-row>
          <v-col class="d-flex justfiy-left">
            <v-btn
              class="justify-left"
              color="blue darken-1"
              text
              outlined
              @click="show = false"
            >
              Abbrechen
            </v-btn>
          </v-col>
          <v-col class="d-flex justfiy-center">
            <v-btn color="blue darken-1" outlined text @click="platzhalter">
              Absenden
            </v-btn>
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
