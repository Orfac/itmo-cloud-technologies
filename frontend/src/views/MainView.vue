<template lang="pug">
.main-view
    .fake( v-if="hasAddingMode" )
    ButtonItem(
      v-if="!hasWorkers"
      :label="buttonLabel"
      @click="getWorkers"   
    )
    .main-view__content( v-else )
      ButtonItem(
        :label="buttonLabel"
        @click="doSomethingWithPerson"   
      )
      TableItem(
        :columns="tableColumns"
        :rows="workers"
        :selectedRowId="selectedWorkerId"
        @rowClick="selectWorker"
        @editRow="editWorker"
        @deleteRow="deleteWorker"
      )
      PaginationPanel
    WorkerItem(
      v-if="hasAddingMode"
      :workerLabel="workerLabel"
      :workerButtonLabel="workerButtonLabel"
      :isEditing="isEditing"
      :editingId="editingId"
      @closeWorkerItem="closeAddingMode"
      )
</template>

<script setup lang="ts">
import TableItem from '@/components/TableItem.vue'
import ButtonItem from '@/components/ButtonItem.vue'
import WorkerItem from '@/components/WorkerItem.vue'
import PaginationPanel from '@/components/PaginationPanel.vue'
import { computed, ref } from 'vue'
import store from '@/store'

const hasWorkers = ref( false )

const getWorkers = async () => {
  await store.dispatch( 'getWorkers' )
  hasWorkers.value = true
}

const buttonLabel = computed( () => {
  if ( !hasWorkers.value ) return 'Найти сотрудников'
  else return 'Добавить сотрудника'
})

const workers = computed( () => store.getters.workers )

const selectedWorkerId = ref( -1 )

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

const hasAddingMode = ref( false )

const doSomethingWithPerson = () => {
  hasAddingMode.value = true
}

const closeAddingMode = () => {
  hasAddingMode.value = false
  isEditing.value = false
}

const isEditing = ref( false )
const editingId = ref( -1 )
const workerLabel = computed( () => isEditing.value ? 'Редактировать сотрудника' : 'Добавить сотрудника' )
const workerButtonLabel = computed( () => isEditing.value ? 'Редактировать' : 'Добавить' )

const editWorker = ( id: number ) => {
  editingId.value = id
  isEditing.value = true
  hasAddingMode.value = true
}

</script>

<style lang="sass">
.main-view
  display: flex
  flex-direction: column
  align-items: center
  &__content
    padding: 0px 20px
    display: flex
    flex-direction: column
    align-items: center

.fake
  position: absolute
  top: 0px
  right: 0px
  bottom: 0px
  left: 0px
  background-color: black
  opacity: 0.4
  z-index: 9999
</style>
