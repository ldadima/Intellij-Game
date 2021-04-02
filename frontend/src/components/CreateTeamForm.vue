<template>
  <dialog-form :submit-action="createTeam"
               close-text="Закрыть"
               headline="Создание команды"
               open-text="Создать команду"
               submit-text="Создать"
  >
    <v-container>
    <v-row>
        <v-col>
          <v-text-field
              v-model="team"
              label="Название команды"
              required
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>
  </dialog-form>
</template>

<script>
import DialogForm from './DialogForm.vue'

export default {
  name: "CreateTeamForm",
  components: {DialogForm},
  props: ['errorMessage'],
  data() {
    return {
      team: null
    }
  },
  methods: {
    createTeam() {
      const axios = require('axios');
      axios.post('/team', {'name': this.team}).then(() =>{
        location.reload()
      }).catch(error => {
        if (error.response.status === 400) {
          if(error.response.data.message.includes('занято'))
            this.errorMessage('Название команды занято');
          else
            this.errorMessage('Введите название команды')
        }
      });

    }
  }
}
</script>

<style scoped>

</style>