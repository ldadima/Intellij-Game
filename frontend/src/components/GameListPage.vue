<template>
  <v-container>
    <v-btn v-if="profile.role === 'Admin'" class="my-3" @click="createGame">Создать игру</v-btn>
    <game-list :profile="profile"></game-list>
  </v-container>
</template>

<script>
import GameList from './GameList.vue'
export default {
  name: "GameListPage",
  components: {GameList},
  data() {
    return {
      profile: {}
    }
  },
  created() {
    const axios = require('axios')
    axios.get('/user').then(response => {
      this.profile = response.data
    })
  },
  methods:{
    createGame(){
      this.$router.push("/editGame");
    }
  }
}
</script>

<style scoped>

</style>