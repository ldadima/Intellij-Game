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
    <v-layout row>
      <v-flex xs12 sm6 offset-sm3>
        <v-card-text>
          <v-container>
            <v-layout row>
              <h1>РЕГИСТРАЦИЯ</h1>
            </v-layout>
            <form v-model="isFormValid">
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="name"
                      label="Имя"
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
                      name="login"
                      label="Логин"
                      id="login"
                      type="login"
                      v-model="login"
                      required
                      :rules="[this.login !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="password"
                      label="Пароль"
                      id="password"
                      v-model="password"
                      type="password"
                      required
                      :rules="[this.password !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="confirmPassword"
                      label="Повторите пароль"
                      id="confirmPassword"
                      v-model="confirmPassword"
                      type="password"
                      :rules="[comparePasswords]"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="SQ"
                      label="Секретный вопрос"
                      id="SQ"
                      type="SQ"
                      v-model="SQ"
                      required
                      :rules="[this.SQ !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-text-field
                      name="SA"
                      label="Ответ"
                      id="SA"
                      type="SA"
                      v-model="SA"
                      required
                      :rules="[this.SA !== '']"
                  ></v-text-field>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-flex xs12>
                  <v-btn :disabled="!isFormValid" color="blue lighten-1" class="text-md-h7 white--text text-center" @click="createUser">Зарегистрироваться</v-btn>
                </v-flex>
              </v-layout>
            </form>
          </v-container>
        </v-card-text>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
export default {
  name: "SignUp",

  data() {
    return {
      name: '',
      login: '',
      password: '',
      confirmPassword: '',
      SQ: '',
      SA: '',
      snackbar: false,
      text: 'Логин занят',
      timeout: 2000,
    }
  },
  computed: {
    comparePasswords() {
      return this.password !== this.confirmPassword ? 'Пароли не совпадают' : true
    },
    isFormValid() {
      return this.name !== '' && this.login !== '' && this.password !== '' &&
          this.confirmPassword !== '' && this.SA !== '' &&
          this.SQ !== '' && this.comparePasswords === true;
    }
  },

  methods: {
    createUser() {
      const axios = require('axios');
      axios.post('/signUp', {
        'login': this.login,
        'name': this.name,
        'password': this.password,
        'passwordConfirmation': this.confirmPassword,
        'question': this.SQ,
        'answer': this.SA
      }).then(response => {
            if (response.status === 201) {
              this.$router.push('/signIn');
            }
          }
      ).catch(error => {
        if(error.response.status === 400) {
          this.snackbar = true;
          this.password = '';
          this.confirmPassword = '';
        }
      })
    },
  },
}
</script>

<style scoped>

</style>