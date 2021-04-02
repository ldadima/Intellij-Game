<template>
  <v-container fluid>
    <v-card-text class="text-md-h5 text-center" v-if="currentTeam.length === 0">
      {{ profile.name }}
    </v-card-text>
    <v-card-text class="text-md-h5 text-center" v-if="currentTeam.length > 0">
      {{ teamName }}
    </v-card-text>
    <v-snackbar
        content-class="text-md-h6 text-center"
        top
        color="red"
        v-model="snackbar"
        :timeout="timeout"
    >
      {{ text }}
    </v-snackbar>
    <v-row v-if="currentTeam.length === 0">
      <v-col>
        <create-team-form :error-message="setErrorMessage"></create-team-form>
      </v-col>
      <v-col>
        <join-team-form :error-message="setErrorMessage" :teams="teams"></join-team-form>
      </v-col>
    </v-row>

    <team-members-list v-if="currentTeam.length > 0"
                       :current-player-id="profile.id"
                       :current-team="currentTeam"
                       :delete-player-action="deletePlayer"
                       :is-captain="profile.role === 'Captain'"
                       :leave-team-action="leaveTeam"
    ></team-members-list>
  </v-container>
</template>

<script>
import CreateTeamForm from './CreateTeamForm.vue'
import JoinTeamForm from './JoinTeamForm.vue'
import {addHandler} from '../websocket'
import TeamMembersList from './TeamMembersList.vue'

export default {
  name: "TeamControlPage",
  components: {TeamMembersList, JoinTeamForm, CreateTeamForm},
  data() {
    return {
      teams: [],
      profile: {},
      currentTeam: [],
      snackbar: false,
      text: '',
      timeout: 2000,
      teamName: ''
    }
  },
  created() {
    addHandler(data => {
      if (data.objectType === 'TEAM') {
        switch (data.eventType) {
          case 'CREATE':
            this.updateTeams()
            this.updateCurrentTeam()
            this.updateProfile()
            this.updateTeamName()

            break
          case 'UPDATE':
            if (this.currentTeam.id === data.body.id) {
              this.currentTeam = data.body
            }
            this.updateTeamName()

            break
          case 'DELETE':
            this.updateCurrentTeam()
            this.updateTeamName()
            this.updateProfile()
            this.updateTeams()

            break
        }
      }
    })

    this.updateTeams()
    this.updateProfile()
    this.updateCurrentTeam()
    this.updateTeamName()

  },
  methods: {
    updateTeams() {
      const axios = require('axios')
      axios.get('/team').then(response => {
        this.teams = response.data
      })
    },
    updateProfile() {
      const axios = require('axios')
      axios.get('/user').then(response => {
        this.profile = response.data
      })
    },
    updateCurrentTeam() {
      const axios = require('axios')
      axios.get('team/teammates').then(response => {
        this.currentTeam = response.data
      })
    },
    updateTeamName() {
      const axios = require('axios')
      axios.get('team/name').then(response => {
        this.teamName = response.data
      }).catch(error => {
        this.teamName = '';
      })
    },
    deletePlayer(id) {
      const axios = require('axios')
      axios.put('team/delete/' + id)
      location.reload()
    },
    leaveTeam() {
      const axios = require('axios')
      if (this.profile.role === 'Captain') {
        axios.delete('team')
      } else {
        axios.put('team/leave')
      }
      location.reload()
    },
    setErrorMessage(message) {
      if (message !== '') {
        this.text = message
        this.snackbar = true
      }
    }
  }
}
</script>

<style scoped>

</style>