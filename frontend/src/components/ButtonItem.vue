<template lang="pug">
button.button-item {{ label }}
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps({
  label: {
    type: String,
    required: true,
    default: 'Поиск'
  }
})

</script>

<style lang="sass" scoped>
.button-item
  margin: 10px 0px
  padding: 10px 15px
  border: none
  border-radius: 3px
  background-color: #89c2c5
  color: #000000
  cursor: pointer
</style>
