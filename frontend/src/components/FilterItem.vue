<template lang="pug">
Popper
  img.filter( src="@/assets/icons/controls/filter.svg" )
  template( #content )
    .filter-item
      .filter-item__element( v-for="value, idx in displayedValues" )
        input.filter-item__checkbox.custom-checkbox(
          :id="idx"
          type="checkbox"
          v-model="filteredRows"
          :value="value"
          @change="changeFilter($event.target.value)"
          ) 
        label.filter-item__value( :for="idx" ) {{ value }}
</template>

<script setup lang="ts">
import { computed, defineProps, defineEmits } from 'vue'

const emit = defineEmits([ 'update:filteredRows' ])

const props = defineProps({
  keyWord: {
    type: String,
    required: false,
    default: ''
  },
  values: {
    type: Array,
    required: false,
    default: () => []
  },
  filteredRows: {
    type: Array,
    required: true,
    default: () => []
  }
})

const displayedValues = computed( () => new Set( props.values.map( ( item: any ) => item[props.keyWord]) ) )

const changeFilter = ( value: string ) => {
  emit( 'update:filteredRows', props.filteredRows )
}

</script>

<style lang="sass" scoped>
.filter
  cursor: pointer
  height: 17px
.filter-item
  background-color: white
  border: 1px solid red
  border-radius: 3px
  padding: 8px 15px
  box-shadow: 0px 0px 12px rgba(0,0,0,.12)
  &__element
    display: flex
    min-height: 25px
  &__value
    cursor: pointer
    width: 100%
    color: red
    font-weight: 300
    display: flex
    align-items: center
    user-select: none
</style>