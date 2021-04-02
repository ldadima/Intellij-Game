<template>
  <v-layout column>
    <v-data-table
        no-data-text="Нет предстоящих игр"
        :headers="allHeaders"
        :items="games"
        sort-by="id"
        class="elevation-1"
        @update:page="switchPage"
        :items-per-page="10"
        :server-items-length="totalItems"
        :footer-props="{
          showFirstLastPage: true,
          showCurrentPage: true,
          itemsPerPageOptions: [10],
          pageText: ''
        }"
        :hide-default-footer="hideDefault"
    >
      <template v-slot:item.actions="{ item }">
        <v-row v-if="profile.role === 'Admin'">
          <v-col>
            <v-btn small>Просмотр ответов</v-btn>
          </v-col>
          <v-col>
            <v-btn small @click="updateGame(item.id)">
              Редактировать
            </v-btn>
          </v-col>
          <v-col>
            <v-btn small @click="showDeleteDialog(item)">
              Удалить
            </v-btn>
          </v-col>
        </v-row>
        <v-row v-if="profile.role === 'Captain'">
          <v-col v-if="!item.participation">
            <v-btn small @click="participateInGame(item)">Участвовать</v-btn>
          </v-col>
          <v-col v-if="item.participation">
            <v-btn small @click="cancelParticipation">Отменить</v-btn>
          </v-col>
        </v-row>
      </template>
    </v-data-table>

    <yes-no-dialog-form :dialog="dialog"
                        :yes-action="deleteGame"
                        :no-action="cancelDelete"
                        title="Удалить игру"
                        :question="'Вы действительно хотите удалить игру ' + selectedGame.name + '?'"
    ></yes-no-dialog-form>
  </v-layout>
</template>

<script>
import YesNoDialogForm from './YesNoDialogForm.vue'

export default {
  name: "GameList",
  components: {YesNoDialogForm},
  props: ['profile'],
  data() {
    return {
      pageNo: 1,
      totalPages: 1,
      totalItems: 0,
      headers: [
        {text: 'Название', value: 'name', sortable: false},
        {text: 'Дата и время проведения', value: 'dateString', sortable: false},
        {text: 'Время обсуждения вопроса (сек.)', value: 'secondsPerQuestion', sortable: false},
        {text: 'Количество раундов', value: 'numberOfLaps', sortable: false},
        {text: 'Время между раундами (мин.)', value: 'minutesBetweenLaps', sortable: false},
        {text: 'Количество вопросов в раунде', value: 'countQuestionsInLap', sortable: false},
      ],
      games: [],
      editedIndex: -1,
      editedItem: {
        id: null,
        name: '',
        dateStart: '',
        dateString: '',
        secondsPerQuestion: 0,
        numberOfLaps: 0,
        minutesBetweenLaps: 0,
        countQuestionsInLap: 0,
        participation: false,
        participationString: ''
      },
      defaultItem: {
        id: null,
        name: '',
        dateStart: '',
        dateString: '',
        secondsPerQuestion: 0,
        numberOfLaps: 0,
        minutesBetweenLaps: 0,
        countQuestionsInLap: 0,
        participation: false,
        participationString: ''
      },
      dialog: false,
      selectedGame: {}
    }
  },

  computed: {
    hideDefault(){
      return this.games.length === 0;
    },
    allHeaders() {
      if (this.profile.role === 'Player') {
        return this.headers.concat([
          {text: 'Участие', value: 'participationString', sortable: false}
        ])
      } else if (this.profile.role === 'Admin') {
        return this.headers.concat([
          {text: 'Заявки', value: 'applications', sortable: false},
          {text: '', value: 'actions', sortable: false}
        ])
      } else {
        return this.headers.concat([
          {text: '', value: 'actions', sortable: false}
        ])
      }
    }
  },
  created() {
    this.initialize()
  },

  methods: {
    initialize() {
      const axios = require('axios')
      const page = this.pageNo - 1
      axios.get('games?&size=10&page=' + page).then(response => {
        this.setGames(response.data)
      })
    },
    setGames(page) {
      this.games = page.content
      this.totalPages = page.totalPages
      this.totalItems = page.totalElements
      this.pageNo = page.number + 1
      this.games.forEach(g => {
        if (g.participation !== undefined && g.participation !== null) {
          g.participationString = (g.participation ? 'Да' : 'Нет')
        }
        const date = new Date(g.dateStart)
        g.dateString = date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() +
            ' ' + date.getHours() + ':' + date.getMinutes()
      })
    },
    participateInGame(game) {
      const axios = require('axios')
      axios.put('games/participate', game).then(response => {
        this.initialize()
      })
    },
    cancelParticipation() {
      const axios = require('axios')
      axios.put('games/cancelParticipate').then(response => {
        this.initialize()
      })
    },
    showDeleteDialog(game) {
      this.selectedGame = game
      this.dialog = true
    },
    deleteGame() {
      const game = this.selectedGame
      const axios = require('axios')
      axios.delete('games/' + game.id).then(response => {
        this.cancelDelete()
        this.pageNo = 1
        this.initialize()
      })
    },
    cancelDelete() {
      this.selectedGame = {}
      this.dialog = false
    },
    switchPage(newPage) {
      const page = newPage - 1;

      const axios = require('axios')
      axios.get('/games?size=10&page=' + page).then(response => {
        this.setGames(response.data)
      })
    },
    updateGame(idGame){
      this.$router.push("/editGame/" + idGame);
    }
  }
}
</script>

<style scoped>

</style>