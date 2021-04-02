<template>
  <dialog-form :submit-action="joinTeam"
               close-text="Закрыть"
               headline="Присоединение к команде"
               open-text="Присоединиться к команде"
               submit-text="Присоединиться"
  >
    <v-container>
      <v-row>
        <v-col>
          <v-select v-model="select" :items="items"
                    label="Выберите команду"
                    single-line
                    no-data-text="Нет доступных команд"
          ></v-select>
        </v-col>
      </v-row>
    </v-container>
  </dialog-form>
</template>

<script>
import DialogForm from './DialogForm.vue'

export default {
  name: "JoinTeamForm",
  props: ['teams', 'errorMessage'],
  components: {DialogForm},
  data() {
    return {
      select: null
    }
  },
  methods: {
    joinTeam() {
      if (this.select !== null) {
        const axios = require('axios')
        axios.put('/team/' + this.select).then(() =>{
          location.reload()
        }).catch(error => {
              if (error.response.status === 400) {
                this.errorMessage('Команды не существует');
              }
            }
        );
      }
    }
  },
  computed: {
    items() {
      return this.teams.map(item => {
        return {
          text: item.name,
          value: item.id
        }
      });
    }
  }
}
</script>

<style scoped>
disabled {
  pointer-events: none;
  color: #bfcbd9;
  cursor: not-allowed;
  background-image: none;
  background-color: #eef1f6;
  border-color: #d1dbe5;
}
</style>