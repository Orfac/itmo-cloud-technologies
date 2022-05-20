<template lang="pug">
.main-view
    .main-view__content( v-if="workers.length" )
      TableItem(
        :columns="tableColumns"
        :rows="workers"
        :hasNumeration="true"
        :selectedRowId="selectedWorkerId"
        @rowClick="selectWorker"
        @deleteRow="deleteWorker"
      )
    ButtonItem(
      v-else
      label="Найти сотрудников"
      @click="getWorkers"   
    )
</template>

<script setup lang="ts">
import TableItem from '@/components/TableItem.vue'
import ButtonItem from '@/components/ButtonItem.vue'
import { computed, ref } from 'vue'
import store from '@/store'

const getWorkers = () => {
  store.dispatch( 'getWorkers' )
}

const workers = computed( () => store.getters.workers )

const selectedWorkerId = ref( -1 )

const test = () => {
  console.log( workers.value )
}

const tableColumns = [
  {
    label: '№',
    id: 'index',
    width: '6%'
  },
  {
    label: 'Имя',
    id: 'name',
    width: '15%'
  },
  {
    label: 'Должность',
    id: 'position',
    width: '15%'
  },
  {
    label: 'Зарплата',
    id: 'salary',
    width: '10%'
  },
  {
    label: 'Статус',
    id: 'status',
    width: '10%'
  },
  {
    label: 'Тип организации',
    id: 'organizationType',
    width: '16%'
  },
  {
    label: 'Дата создания',
    id: 'creationDate',
    width: '13%'
  },
  {
    label: '',
    id: 'fixers',
    width: '70px'
  },
]

const deleteWorker = async ( id: number ) => {
  await store.dispatch( 'deleteWorker', id )
  store.dispatch( 'getWorkers' )
}

const selectWorker = ( id: number ) => {
  selectedWorkerId.value = id
}

</script>

<style lang="sass">
.main-view
  display: flex
  flex-direction: column
  align-items: center
  &__content
    padding: 20px
</style>
