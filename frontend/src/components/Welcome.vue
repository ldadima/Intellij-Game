<template>
  <div class="welcome">
    <img alt="Croup logo" :src=groupLogo />
    <h1>Приветствуем вас на сайте Клуба интеллектуальных игр НГУ!</h1>
    <h2>
      Собирай друзей. Участвуй в играх. Приноси победу своей команде!<br>
      Больше информации о клубе в
      <a href="https://vk.com/kii_nsu" target="_blank" rel="noopener">группе Вконтакте</a>.
    </h2>
  </div>
</template>

<script>
import GroupLogo from '../assets/GroupLogo.png'
export default {
  name: 'Welcome',
  data() {
    return {
      groupLogo: GroupLogo
    }
  }
}
</script>

<style>
a {
  color: #42b983;
}
</style>
