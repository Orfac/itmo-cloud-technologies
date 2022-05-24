<template lang="pug">
.button-item
    label( v-if="name.length" )
      h5.button-item__name {{ name }}
    button.button-item__el {{ label }}
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps({
  label: {
    type: String,
    required: true,
    default: 'Поиск'
  },
  name: {
    type: String,
    required: false,
    default: ''
  },
})

</script>

<style lang="sass" scoped>
.button-item
  &__name
    margin-bottom: 10px
  &__el
    margin: 10px 0px
    padding: 10px 15px
    border: none
    border-radius: 3px
    background-color: #89c2c5
    color: #000000
    cursor: pointer
</style>
