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
              <h1>АВТОРИЗАЦИЯ</h1>
            </v-layout>
            <form v-model="isFormValid">
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
                  <v-btn :disabled="!isFormValid" color="blue lighten-1" class="text-md-h7 white--text text-center" @click="loginUser">Войти</v-btn>
                </v-flex>
              </v-layout>
              <br>
              <v-layout row>
                <v-flex xs12>
                  <v-btn  color="blue lighten-1" class="text-md-h7 white--text text-center" @click="toRegister">Регистрация</v-btn>
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
  name: 'SignIn',
  data() {
    return {
      login: '',
      password: '',
      error: null,
      success: false,
      snackbar: false,
      text: 'Неверный логин или пароль',
      timeout: 2000,
    }
  },
  computed: {
    isFormValid() {
      return this.login !== '' && this.password !== '';
    }
  },
  methods: {
    loginUser() {
      const axios = require('axios');
      axios.get('/user', {
        auth: {
          username: this.login,
          password: this.password
        },
      }).then(response => {
        if (response.status === 200) {
          this.$router.push('/');
        }
      }).catch(error => {
        if (error.response.status === 401) {
          this.snackbar = true;
          this.password = '';
        }
      });
    },
    toRegister() {
      this.$router.push('/signUp');
    }
  }
}

</script>

<style scoped>

</style>