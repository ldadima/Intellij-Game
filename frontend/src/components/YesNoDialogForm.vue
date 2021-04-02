<template>
  <v-dialog
      v-model="dialog"
      width="600px"
      persistent>
    <v-card>
      <v-card-title class="text-md-h4 black--text headline grey lighten-1 justify-center">
        {{ title }}
      </v-card-title>
      <v-card-text class="text-md-h5 black--text justify-center"><br>{{ question }}</v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            color="primary"
            text
            @click="yesAction"
            class="text-md-h6"
        >
          Да
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
            color="primary"
            text
            @click="noAction"
            class="text-md-h6"
        >
          Отмена
        </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "YesNoDialogForm",
  props: ['yesAction', 'noAction', 'dialog', 'title', 'question']
}
</script>

<style scoped>

</style>