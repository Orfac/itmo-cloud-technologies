<template lang="pug">
.app 
    .app__title( @click="test" ) Облачная разработка. Команда 4
    MainView
</template>

<script setup lang="ts">
import MainView from '@/views/MainView.vue'

</script>

<style lang="sass">
body
  margin: 0px
  padding: 0px
  font-family: Roboto, Arial, sans-serif
.app
  display: flex
  flex-direction: column
  align-items: center
  &__title
    text-align: center
    margin-top: 10px
</style>
