<template>
  <v-container>
    <v-snackbar
        content-class="text-md-h6 text-center"
        top
        color="red"
        v-model="snackbar"
        :timeout="timeout"
    >
      {{ text }}
    </v-snackbar>
    <v-container>
      <v-layout row>
        <v-flex xs12 sm6 offset-sm3>
          <v-container>
            <v-layout row>
              <h1>Игра</h1>
            </v-layout>
            <form v-model="isFormValid">
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="name"
                      label="Название"
                      id="name"
                      v-model="name"
                      type="name"
                      required
                      :rules="[this.name !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="date"
                      label="Дата проведения"
                      id="date"
                      type="date"
                      v-model="date"
                      required
                      :rules="[this.date !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="time"
                      label="Время проведения"
                      id="time"
                      type="time"
                      v-model="time"
                      required
                      :rules="[this.time !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="disc"
                      label="Время обсуждения вопроса"
                      id="disc"
                      v-model="secondsPerQuestion"
                      required
                      :rules="[this.secondsPerQuestion !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="amount"
                      label="Количество раундов"
                      id="amount"
                      v-model="numberOfLaps"
                      required
                      :rules="[this.numberOfLaps !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="between"
                      label="Время между раундами"
                      id="between"
                      v-model="minutesBetweenLaps"
                      required
                      :rules="[this.minutesBetweenLaps !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="questions"
                      label="Количество вопросов в раунде"
                      id="questions"
                      v-model="countQuestionsInLap"
                      required
                      :rules="[this.countQuestionsInLap !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-btn :disabled="!isFormValid" color="blue lighten-1" class="text-md-h7 white--text text-center"
                         @click="saveGame">Сохранить
                  </v-btn>
                </v-flex>
              </v-layout>
            </form>
          </v-container>
        </v-flex>
      </v-layout>
    </v-container>
    <v-container v-if="idGame !== null && idGame !== undefined">
      <questions-list :questions="questions"></questions-list>
      <br>
      <v-btn @click="submitQuestions">Сохранить и выйти</v-btn>
    </v-container>
  </v-container>
</template>

<script>
import QuestionsList from './QuestionsList.vue'
export default {
  name: "EditGamePage",
  components: {QuestionsList},
  data() {
    return {
      idGame: this.$route.params.id,
      name: '',
      date: '',
      time: '',
      secondsPerQuestion: '',
      numberOfLaps: '',
      minutesBetweenLaps: '',
      countQuestionsInLap: '',
      questions: [],
      snackbar: false,
      text: '',
      timeout: 2000,
      flag: false
    }
  },
  computed: {
    isFormValid() {
      return this.name !== '' && this.date !== '' && this.time !== '' &&
          this.secondsPerQuestion > 0 && this.numberOfLaps > 0 &&
          this.minutesBetweenLaps > 0 && this.countQuestionsInLap > 0;
    }
  },
  created() {
    this.getGame();
  },
  methods: {
    getGame() {
      if (this.idGame > 0) {
        const axios = require('axios');
        axios.get('/games/' + this.idGame).then(response => {
              if (response.status === 200) {
                this.name = response.data.name;
                const dateTime = response.data.dateStart.split('T');
                this.date = dateTime[0]
                this.time = dateTime[1]
                this.secondsPerQuestion = response.data.secondsPerQuestion;
                this.numberOfLaps = response.data.numberOfLaps;
                this.minutesBetweenLaps = response.data.minutesBetweenLaps;
                this.countQuestionsInLap = response.data.countQuestionsInLap;
                this.questions = response.data.questions
              }
            }
        ).catch(error => {
          if (error.response.status === 400) {
            //this.text = 'На эту дату уже назначена игра'
            alert(error.response.data.message);
            this.snackbar = true;
          }
        })
      }
    },
    saveGame() {
      const datetime = this.date + 'T' + this.time;
      const axios = require('axios');
      if (this.idGame === null || this.idGame === undefined || this.idGame <= 0) {
        axios.post('/games', {
          'name': this.name,
          'dateStart': datetime,
          'secondsPerQuestion': this.secondsPerQuestion,
          'numberOfLaps': this.numberOfLaps,
          'minutesBetweenLaps': this.minutesBetweenLaps,
          'countQuestionsInLap': this.countQuestionsInLap,
        }).then(response => {
              if (response.status === 201) {
                this.$router.push('editGame/' + response.data)
                location.reload()
              }
            }
        ).catch(error => {
          if (error.response.status === 400) {
            if(error.response.data.message.includes('already exists')){
              this.text = 'На эту дату уже назначена игра'
            } else {
              this.text = 'Дата и время игры должны быть позже текущих'
            }
            this.snackbar = true;
          }
        })
      } else {
        axios.put('/games', {
          'id': this.idGame,
          'name': this.name,
          'dateStart': datetime,
          'secondsPerQuestion': this.secondsPerQuestion,
          'numberOfLaps': this.numberOfLaps,
          'minutesBetweenLaps': this.minutesBetweenLaps,
          'countQuestionsInLap': this.countQuestionsInLap,
        }).then(response => {
              if (response.status === 200) {
                this.flag = false;
              }
            }
        ).catch(error => {
          if (error.response.status === 400) {
            if(error.response.data.message.includes('already exists')){
              this.text = 'На эту дату уже назначена игра'
            } else {
              this.text = 'Дата игры должна быть позже текущей'
            }
            this.snackbar = true;
            this.flag = true;
          }
        })
      }
    },
    submitQuestions() {
      this.saveGame()
      if (!this.flag) {
        const axios = require('axios')
        axios.put('/games/' + this.idGame, this.questions).then(response => {
          this.$router.push('/gamesList')
        }).catch(error => {
          if (error.response.status === 400) {
            if(error.response.data.message.includes('limit exceeded')) {
              this.text = 'Превышено допустимое количество вопросов'
            }
            this.snackbar = true;
          }
        })
      }
    }
  },
}
</script>

<style scoped>

</style>