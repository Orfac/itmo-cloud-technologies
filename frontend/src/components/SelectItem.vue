<template lang="pug">
.select-item
    h5.select-item__name {{ name }}
    select.select-item__element(
      :required="isRequired"
      :value="selectValue"
      :placeholder="placeholder"
      @change="$emit('update:selectValue', $event.target.value)"
    )
      option.selecet-item__option(
        v-for="option, idx in options"
        :key="idx"
        ) {{ option }}
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps({
  placeholder: {
    type: String,
    required: false,
    default: 'Выберите значение'
  },
  name: {
    type: String,
    required: true,
    default: 'Введите значениеss'
  },
  isRequired: {
    type: Boolean,
    required: false,
    default: true
  },
  selectValue: {
    type: String,
    required: true,
    default: ''
  },
  options: {
    type: Array,
    required: false,
    default: () => []
  }
})

console.log( props.name )
console.log( props.selectValue )
console.log( props.options )

</script>

<style lang="sass" scoped>
.select-item
  &__name
    margin-bottom: 10px
  &__element
    width: 100%
    padding: 15px 8px
    outline: none
</style>
