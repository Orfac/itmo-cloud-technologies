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
      .main-view__params
        .main-view__salary(
          v-for="param, idx in parameters"
          :key="idx"
          )
          InputItem(
            v-model:inputValue="param.value"
            :placeholder="param.placeholder"
            :name="param.name"
            :type="param.type"
          )
        ButtonItem(
          label="Очистить все"
          name="Очистить все возможные поля"
          @click="cleanParams"
        )
        ButtonItem(
          label="Поиск по параметрам"
          name="Поиск"
          :class="{disabled: isDisabled}"
          @click="getWorkers"
        )
      .warning( v-if="workersAbsence || hasMistake" ) По заданным параметрам работников не найдено
      TableItem(
        v-else
        :columns="tableColumns"
        :rows="workers"
        :selectedRowId="selectedWorkerId"
        height="560px"
        @rowClick="selectWorker"
        @editRow="editWorker"
        @deleteRow="deleteWorker"
        @filterRow="filterWorkers"
        @cleanTableFilter="cleanWorkersFilter"
        @sortRows="sortWorkers"
      )
      PaginationPanel(
        v-if="hasPagination && !workersAbsence && !hasMistake"
        @getPagWorkers="getPaginateWorkers"
        )
      .main-view__additional
        .main-view__salary
          InputItem(
            v-model:inputValue="searchedId"
            placeholder="Введите id"
            name="Поиск по id"
            type="number"
          )
          ButtonItem(
            label="Найти"
            @click="searchWorkerById"
          )
        .main-view__salary
          InputItem(
            v-model:inputValue="deletedRequestSalary"
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
          v-if="!hasPagination || hasMistake"
          label="Найти"
          name="Найти всех сотрудников"
          @click="getAllWorkers"
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
import SelectItem from '@/components/SelectItem.vue'
import WorkerItem from '@/components/WorkerItem.vue'
import PaginationPanel from '@/components/PaginationPanel.vue'
import { computed, reactive, ref, watch } from 'vue'
import store from '@/store'

const hasWorkers = ref( false )
const hasPagination = ref( true )

let parameters = reactive([
  {
    value: '',
    placeholder: 'Введите name',
    name: 'Поиск по name',
    type: 'string',
  },
  {
    value: '',
    placeholder: 'Введите salary',
    name: 'Поиск по salary',
    type: 'number',
  },
  {
    value: '',
    placeholder: 'Введите Coordinate X',
    name: 'Поиск по Coordinate X',
    type: 'number',
  },
  {
    value: '',
    placeholder: 'Введите Coordinate Y',
    name: 'Поиск по Coordinate Y',
    type: 'number',
  },
  {
    value: '1',
    placeholder: 'Введите номер страницы',
    name: 'Номер страницы',
    type: 'number',
  },
  {
    value: '5',
    placeholder: 'Введите количество работников на странице',
    name: 'Количество работников на странице',
    type: 'number',
  },
])

const filteredPosition = ref( '' )
const filteredStatus = ref( '' )
const filteredOrgType = ref( '' )
const sortedDirection = ref( '' )
const sortedColumn = ref( '' )
const searchedId = ref( '' )

const finalParams = computed( () => {
  return {
    name: parameters[0].value,
    salary: +parameters[1].value,
    position: filteredPosition.value,
    status: filteredStatus.value,
    organizationType: filteredOrgType.value,
    coordinateX: +parameters[2].value,
    coordinateY: +parameters[3].value,
    sortDirection: sortedDirection.value,
    sortedColumn: sortedColumn.value
  }
})

const isDisabled = computed( () => {
  return +parameters[1].value < 0 || +parameters[2].value > 509 || +parameters[2].value < 0 || +parameters[3].value < 0 || +parameters[4].value < 1 || +parameters[5].value < 1
})

const getWorkers = async () => {
  await store.dispatch( 'updatePage', +parameters[4].value - 1 )
  await store.dispatch( 'updatePageSize', parameters[5].value )
  await store.dispatch( 'getWorkers', finalParams.value )
  searchedId.value = ''
  hasWorkers.value = true
  hasPagination.value = true
}

const workersAbsence = computed( () => hasWorkers.value && !workers.value.length )

const getPaginateWorkers = async () => {
  parameters[4].value = ( store.getters.page + 1 ).toString()
  await store.dispatch( 'getWorkers', finalParams.value )
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
    width: '4%'
  },
  {
    label: 'Имя',
    id: 'name',
    width: '5%'
  },
  {
    label: 'Id',
    id: 'id',
    width: '4%'
  },
  {
    label: 'Координата Х',
    id: 'coordinateX',
    width: '8%',
    type: 'number'
  },
  {
    label: 'Координата Y',
    id: 'coordinateY',
    width: '10%',
    type: 'number'
  },
  {
    label: 'Должность',
    id: 'position',
    width: '10%',
    filtered: 'true',
    filteredValues: [ 'position', 'DIRECTOR', 'MANAGER', 'LABORER', 'DEVELOPER', 'BAKER' ]
  },
  {
    label: 'Зарплата',
    id: 'salary',
    width: '10%',
    type: 'number'
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
    width: '8%'
  },
  {
    label: '',
    id: 'fixers',
    width: '70px'
  },
]

const deleteWorker = async ( id: number ) => {
  await store.dispatch( 'deleteWorker', id )
  store.dispatch( 'getWorkers', finalParams.value )
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
const deletedRequestSalary = ref( '' )

const getSalaryWorkers = async () => {
  if ( !getRequestSalary.value.length ) return
  await store.dispatch( 'getSalaryWorkers', getRequestSalary.value )
  hasPagination.value = false
}
const deleteSalaryWorkers = async () => {
  if ( !deletedRequestSalary.value.length ) return
  await store.dispatch( 'deleteSalaryWorkers', deletedRequestSalary.value )
  store.dispatch( 'getWorkers', finalParams.value )
}

const getAllWorkers = async () => {
  parameters[4].value = '1'
  searchedId.value = ''
  store.dispatch( 'getWorkers', {})
  hasPagination.value = true
}

const cleanParams = () => {
  for ( const [ idx, param ] of parameters.entries() ) {
    idx > 3 ? 0 : param.value = ''
  }
}

const filterWorkers = ( filterParams: any ) => {
  switch ( filterParams.key ) {
    case 'position':
      filteredPosition.value = filterParams.value
      break
    case 'status':
      filteredStatus.value = filterParams.value
      break
    case 'organizationType':
      filteredOrgType.value = filterParams.value
  }
  store.dispatch( 'getWorkers', finalParams.value )
}

const cleanWorkersFilter = async ( key: string ) => {
  switch ( key ) {
    case 'position':
      filteredPosition.value = ''
      break
    case 'status':
      filteredStatus.value = ''
      break
    case 'organizationType':
      filteredOrgType.value = ''
  }
  await store.dispatch( 'getWorkers', finalParams.value )
}

const sortWorkers = async ( sortParams: any ) => {
  sortedDirection.value = sortParams.sortDirection
  sortedColumn.value = sortParams.sortedColumn
  await store.dispatch( 'getWorkers', finalParams.value )
}

const hasMistake = computed( () => store.getters.statusError  )

const searchWorkerById = async () => {
  if ( searchedId.value === '' ) return
  await store.dispatch( 'getWorkerById', +searchedId.value )
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
    display: flex
    justify-content: space-between
  &__salary
    margin-right: 50px
  &__params
    display: grid
    grid-template-columns: 1fr 1fr 1fr 1fr
    gap: 20px

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
