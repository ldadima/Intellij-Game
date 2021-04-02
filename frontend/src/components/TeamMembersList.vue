<template>
  <v-layout column justify-center>
    <v-card>
      <team-member-item v-for="(item, index) in items"
                        :key="item.id"
                        :index="index"
                        :current-player-id="currentPlayerId"
                        :delete-player-action="deletePlayerAction"
                        :is-captain="isCaptain"
                        :player="item"
      ></team-member-item>
    </v-card>
    <v-btn @click="leaveTeamAction">{{ leaveTeamText }}</v-btn>
  </v-layout>
</template>

<script>
import TeamMemberItem from './TeamMemberItem.vue'

export default {
  name: "TeamMembersList",
  props: ['currentTeam', 'isCaptain', 'currentPlayerId', 'deletePlayerAction', 'leaveTeamAction'],
  components: {TeamMemberItem},
  computed: {
    items() {
      return this.currentTeam.map(p => {
        return {
          id: p.id,
          name: p.name
        }
      })
    },
    leaveTeamText() {
      if (this.isCaptain) {
        return 'Удалить команду'
      } else {
        return 'Покинуть команду'
      }
    }
  }
}
</script>

<style scoped>

</style>