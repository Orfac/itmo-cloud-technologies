<template lang="pug">
Popper
  img.filter( src="@/assets/icons/controls/filter.svg" )
  template( #content )
    .filter-item
      .filter-item__element( v-for="value, idx in displayedValues" )
        input.filter-item__checkbox.custom-checkbox(
          :id="value"
          type="radio"
          v-model="filteredRows"
          :value="value"
          @change="changeFilter"
          ) 
        label.filter-item__value( :for="value" ) {{ value }}
</template>

<script setup lang="ts">
import { computed, defineProps, defineEmits } from 'vue'
import Popper from 'vue3-popper'
import store from '@/store'

const emit = defineEmits([ 'updateFilteredValue' ])

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

const displayedValues = computed( () => props.values.slice( 1 ) )

const changeFilter = ( value: string ) => {
  if ( props.values[0] === 'position' ) {
    store.dispatch( 'updatePositionFilter', props.filteredRows )
  } else if ( props.values[0] === 'status' ) {
    store.dispatch( 'updateStatusFilter', props.filteredRows )
  } else {
    store.dispatch( 'updateOrganizationFilter', props.filteredRows )
  }
  console.log( props.filteredRows )
  emit( 'updateFilteredValue', props.filteredRows )
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
    align-items: baseline
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