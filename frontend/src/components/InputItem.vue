<template lang="pug">
.input-item
    label( :for="name" )
      h5.input-item__name {{ name }}
    input.input-item__element(
      :id="name"
      :placeholder="placeholder"
      :required="isRequired"
      :type="type"
      :value="inputValue"
      @input="$emit('update:inputValue', $event.target.value)"
    )
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  placeholder: {
    type: String,
    required: true,
    default: 'Введите значение'
  },
  name: {
    type: String,
    required: true,
    default: 'Введите значение'
  },
  isRequired: {
    type: Boolean,
    required: false,
    default: true
  },
  inputValue: {
    type: String,
    required: true,
    default: ''
  },
  type: {
    type: String,
    required: false,
    default: 'text'
  }
})

</script>

<style lang="sass" scoped>
.input-item
  &__name
    margin-bottom: 10px
  &__element
    width: 300px
    padding: 15px 8px
    outline: none
</style>
