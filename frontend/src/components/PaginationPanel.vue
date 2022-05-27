<template lang="pug">
.pagination-panel
    .pagination-panel__pages {{ page + 1 }} из {{ pages }}
    .pagination-panel__arrows
      img.pagination-panel__arrow(
        src="@/assets/icons/controls/prev.svg"
        @click="toPrevPage"
        )
      img.pagination-panel__arrow(
        src="@/assets/icons/controls/next.svg"
        @click="toNextPage"
        )
</template>

<script setup lang="ts">
import { computed, defineEmits } from 'vue'
import store from '@/store'

const emit = defineEmits([ 'getPagWorkers' ])

const page = computed( () => store.getters.page )
const pages = computed( () => store.getters.pages )
const workersCount = computed( () => store.getters.workersCount )

const toPrevPage = async () => {
  if ( page.value === 0 ) {
    return
  } else {
    await store.dispatch( 'updatePage', page.value - 1 )
    emit( 'getPagWorkers' )
  }
}

const toNextPage = async () => {
  if ( page.value === pages.value - 1 ) {
    return
  } else {
    await store.dispatch( 'updatePage', page.value + 1 )
    emit( 'getPagWorkers' )
  } 
}


</script>

<style lang="sass" scoped>
.pagination-panel
  display: flex
  justify-content: right
  align-items: center
  width: 100%
  height: 45px
  border: 1px solid #c0cbd7
  border-top: none
  &__arrows
    cursor: pointer
    margin: 0px 10px
</style>