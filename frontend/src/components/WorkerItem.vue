<template lang="pug">
.worker-item
    .worker-item__headers
      h3.worker-item__header {{ workerLabel }}
      .worker-item__warning( v-if="hasWarning" ) Заполните все поля и попробуйте еще раз
    .worker-item__content
      form.worker-item__inputs(
        v-for="input, idx in inputs"
        :key="idx"
        )
        InputItem(
          v-model:inputValue="input.value"
          :placeholder="input.placeholder"
          :name="input.name"
          :type="input.type"
        )
      .worker-item__selects(
        v-for="select, idx in selects"
        :key="idx"
        )
        SelectItem(
          v-model:selectValue="select.value"
          :placeholder="select.placeholder"
          :name="select.name"
          :options="select.options"
        )
      .worker-item__buttons
        ButtonItem.worker-item__button(
          :label="workerButtonLabel"
          @click="updateWorkers"
          )
        ButtonItem.worker-item__button--exit(
          label="Отмена"
          @click="closeWorkerItem"
          )
</template>

<script setup lang="ts">
import ButtonItem from '@/components/ButtonItem.vue'
import InputItem from '@/components/InputItem.vue'
import SelectItem from '@/components/SelectItem.vue'
import { defineProps, defineEmits, reactive, computed, ref } from 'vue'
import store from '@/store'
import range from 'lodash'

const emit = defineEmits([ 'closeWorkerItem' ])

const props = defineProps({
  workerLabel: {
    type: String,
    required: true,
    default: 'Добавить сотрудника'
  },
  workerButtonLabel: {
    type: String,
    required: true,
    default: 'Добавить'
  },
  isEditing: {
    type: Boolean,
    required: true,
    default: false
  },
  editingId: {
    type: Number,
    required: true,
    default: -1
  }
})

const workers = computed( () => store.getters.workers )
const hasWarning = ref( false )
const inputs = reactive([
  {
    name: 'Имя',
    value: ''
  },
  {
    name: 'Зарплата',
    value: '',
    type: 'number'
  },
])

const selects = reactive([
  {
    name: 'Должность',
    options: [ 'DIRECTOR', 'MANAGER', 'LABORER', 'DEVELOPER', 'BAKER' ],
    value: 'DIRECTOR'
  },
  {
    name: 'Статус',
    options: [ 'FIRED', 'HIRED', 'RECOMMENDED_FOR_PROMOTION', 'REGULAR', 'PROBATION' ],
    value: 'FIRED'
  },
  {
    name: 'Тип организации',
    options: [ 'COMMERCIAL', 'GOVERNMENT', 'PRIVATE_LIMITED_COMPANY' ],
    value: 'COMMERCIAL'
  },
])

if ( props.isEditing ) {
  inputs[0].value = workers.value[workers.value.findIndex( ( item: any ) => item.id === props.editingId )].name
  inputs[1].value = workers.value[workers.value.findIndex( ( item: any ) => item.id === props.editingId )].salary
  selects[0].value = workers.value[workers.value.findIndex( ( item: any ) => item.id === props.editingId )].position
  selects[1].value = workers.value[workers.value.findIndex( ( item: any ) => item.id === props.editingId )].status
  selects[2].value = workers.value[workers.value.findIndex( ( item: any ) => item.id === props.editingId )].organizationType
}

const closeWorkerItem = () => {
  emit( 'closeWorkerItem' )
}

const updateWorkers = async () => {
  if ( inputs[0].value === '' || inputs[1].value === '' ) {
    hasWarning.value = true
    return
  }
  const date = new Date( Date.now() )
  const body = {
    name: inputs[0].value,
    id: 0,
    coordinates: {
      x: Math.round( Math.random() * 408 ),
      y: 0,
    },
    salary: +inputs[1].value,
    position: selects[0].value,
    status: selects[1].value,
    organizationType: selects[2].value,
    creationDate: '2022-05-22',
  }
  if ( props.isEditing ) {
    body.id = props.editingId
    await store.dispatch( 'editWorker', body )
  } else await store.dispatch( 'addWorker', body )
  await store.dispatch( 'getWorkers' )
  emit( 'closeWorkerItem' )
}

</script>

<style lang="sass" scoped>
.worker-item
  position: fixed
  top: 16%
  background-color: white
  border: 1px solid black
  padding: 50px 40px
  z-index: 999999
  &__buttons
    margin-top: 10px
  &__button
    margin-right: 10px
  &__warning
    color: red
    font-size: 10px

.invalid
  border: 1px solid red

h3
  margin: 10px 0px
</style>
