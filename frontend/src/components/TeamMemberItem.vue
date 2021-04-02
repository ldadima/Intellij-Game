<template>
  <v-list-item>
    <v-list-item-content dense>
      <v-list-item-title>
        <v-row>
          <v-col class="text-md-h6 text-center">{{index + 1}}
          </v-col>
          <v-col class="text-md-h6 text-center">
              {{ player.name }}
          </v-col>
          <v-col class="text-md-h6 text-center">
            <v-list-item-action v-if="isCaptain && !(player.id === currentPlayerId)">
              <v-btn class="text-md-h6 text-center" @click="deletePlayerAction(player.id)">Удалить</v-btn>
            </v-list-item-action>
          </v-col>
        </v-row>

      </v-list-item-title>
    </v-list-item-content>

  </v-list-item>
</template>

<script>
export default {
  name: "TeamMemberItem",
  props: ['isCaptain', 'player', 'currentPlayerId', 'index', 'deletePlayerAction']
}
</script>

<style scoped>

</style>