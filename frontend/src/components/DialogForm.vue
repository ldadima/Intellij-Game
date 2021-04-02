<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600px" persistent>
      <template #activator="{ on, attrs }">
        <v-btn
            color="blue lighten-1"
            v-bind="attrs"
            v-on="on"
        >
          <v-card-text class="text-md-h6 white--text text-center">
            {{ openText }}
          </v-card-text>
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">{{ headline }}</span>
        </v-card-title>
        <v-card-text>
          <slot></slot>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="blue darken-1"
              text
              @click="dialog = false"
          >
            {{ closeText }}
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="submit"
          >
            {{ submitText }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: "DialogForm",
  props: ['openText', 'closeText', 'submitText', 'headline', 'submitAction'],
  data() {
    return {
      dialog: false
    }
  },
  methods: {
    submit() {
      this.dialog = false;
      this.submitAction();
    }
  }
}
</script>

<style scoped>

</style>