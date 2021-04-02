<template>
  <v-app id="app">
    <v-app-bar app>
      <v-spacer></v-spacer>
      <v-toolbar-items>
        <v-btn @click="showWelcomePage">Клуб интеллектуальных игр НГУ <img height="80" alt="Logo" :src=KIILogo></v-btn>
        <v-btn v-if="role === 'Anonymous'" @click="showSignUpPage">Регистрация</v-btn>
        <v-btn v-if="role === 'Anonymous'" @click="showSignInPage">Вход</v-btn>
        <v-btn v-if="role === 'Player' || role === 'Captain' || role === 'Admin'" @click="showGamesListPage">Игры</v-btn>
        <v-btn v-if="role === 'Player' || role === 'Captain'" @click="showTeamPage">Моя команда</v-btn>
        <v-btn v-if="role === 'Player' || role === 'Captain' || role === 'Admin'" @click="showSignOutPage">Выход</v-btn>
      </v-toolbar-items>
    </v-app-bar>

    <v-main app>
      <router-view></router-view>
    </v-main>

    <yes-no-dialog-form :dialog="dialog"
                        :yes-action="logoutUser"
                        :no-action="closeDialog"
                        title="Выход"
                        question="Вы уверены, что хотите выйти?"
    ></yes-no-dialog-form>

    <v-footer fixed color="blue lighten-1" app>
      <v-card-text class="text-md-h5 white--text text-center"> &copy; {{ new Date().getFullYear() }} Copyright: КИИ
        НГУ
      </v-card-text>
    </v-footer>
  </v-app>
</template>

<script>
import KIILogo from 'assets/KIILogo.png'
import {connect} from './websocket'
import YesNoDialogForm from './components/YesNoDialogForm.vue'

export default {
  name: 'App',
  components: {YesNoDialogForm},
  data() {
    return {
      KIILogo: KIILogo,
      role: 'Anonymous',
      dialog: false
    }
  },
  beforeMount() {
    this.usersRole();
  },
  beforeUpdate() {
    connect()
    this.usersRole();
  },
  methods: {
    usersRole() {
      const axios = require('axios');
      axios.get('/user'
      ).then(response => {
        this.role = response.data.role;
      }).catch(error => {
        if (error.response.status === 401) {
          this.role = 'Anonymous';
        }
      });
    },
    showWelcomePage() {
      this.$router.push({path: '/'})
    },
    showSignUpPage() {
      this.$router.push({path: '/signUp'})
    },
    showSignInPage() {
      this.$router.push({path: '/signIn'})
    },
    showGamesListPage() {
      this.$router.push({path: '/gamesList'})
    },
    showGamePage() {
      this.$router.push({path: '/game'})
    },
    showTeamPage() {
      this.$router.push({path: '/myTeam'})
    },
    showSignOutPage() {
      this.dialog = true;
    },
    logoutUser() {
      const axios = require('axios');
      axios.get('/signOut').then(response => {
        if (response.status === 200) {
          this.$router.push('/signIn');
        }
      }).catch(error => {
        if (error.response.status === 401) {
          alert('Неверный логин или пароль');
        }
      });
      this.closeDialog()
    },
    closeDialog() {
      this.dialog = false
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
