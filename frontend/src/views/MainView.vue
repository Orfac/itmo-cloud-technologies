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
      PaginationPanel( v-if="hasPagination" )
      .main-view__additional
        .main-view__salary
          InputItem(
            v-model:inputValue="deleteRequestSalary"
            placeholder="Введите значение salary"
            name="Удалить все объекты, заданному salary"
            type="number"
          )
          ButtonItem(
            label="Удалить"
            @click="deleteSalaryWorkers"
          )
        .main-view__salary
          InputItem(
            v-model:inputValue="getRequestSalary"
            placeholder="Введите ограничение"
            name="Получить объекты, ограниченные по полю salary"
            type="number"
          )
          ButtonItem(
            label="Получить"
            @click="getSalaryWorkers"
          )
        .main-view__status
          ButtonItem(
            label="Получить"
            name="Получить объект с максимальным поле Status"
            @click="getWorkerWithMaxStatus"
          )
      ButtonItem(
          label="Найти"
          name="Найти всех сотрудников"
          @click="getWorkers"
        )
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
import InputItem from '@/components/InputItem.vue'
import WorkerItem from '@/components/WorkerItem.vue'
import PaginationPanel from '@/components/PaginationPanel.vue'
import { computed, ref } from 'vue'
import store from '@/store'

const hasWorkers = ref( false )
const hasPagination = ref( true )

const getWorkers = async () => {
  await store.dispatch( 'getWorkers' )
  hasWorkers.value = true
  hasPagination.value = true
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
    width: '15%',
    filtered: 'true',
    filteredValues: [ 'position', 'DIRECTOR', 'MANAGER', 'LABORER', 'DEVELOPER', 'BAKER' ]
  },
  {
    label: 'Зарплата',
    id: 'salary',
    width: '10%',
  },
  {
    label: 'Статус',
    id: 'status',
    width: '10%',
    filtered: 'true',
    filteredValues: [ 'status', 'FIRED', 'HIRED', 'RECOMMENDED_FOR_PROMOTION', 'REGULAR', 'PROBATION' ]
  },
  {
    label: 'Тип организации',
    id: 'organizationType',
    width: '16%',
    filteredValues: [ 'organizationType', 'COMMERCIAL', 'GOVERNMENT', 'PRIVATE_LIMITED_COMPANY' ],
    filtered: 'true',
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

const getWorkerWithMaxStatus = async () => {
  await store.dispatch( 'getStatusWorker' )
  hasPagination.value = false
}

const getRequestSalary = ref( '' )
const deleteRequestSalary = ref( '' )

const getSalaryWorkers = async () => {
  await store.dispatch( 'getSalaryWorkers', getRequestSalary.value )
  hasPagination.value = false
}
const deleteSalaryWorkers = async () => {
  await store.dispatch( 'deleteSalaryWorkers', deleteRequestSalary.value )
  store.dispatch( 'getWorkers' )
}
</script>

<style lang="sass">
.main-view
  display: flex
  flex-direction: column
  align-items: center
  height: 100vh
  justify-content: space-between
  &__content
    padding: 0px 20px
    display: flex
    flex-direction: column
    align-items: center
  &__additional
    width: 80%
    display: flex
    justify-content: space-between

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
