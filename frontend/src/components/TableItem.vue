<template lang="pug">
.table
  .table-item
    table.table-fixed-head
      thead
        tr( :class="{ indent: hasMargin }" )
          th(
            v-for="(column, index) in columns"
            :key="index"
            :style="{'max-width': column.maxWidth, 'width': column.width}"
            :class="{ helper: index === 0 }"
          ) 
            .head( @click="sortRows(column.id, index)" )
              span {{ column.label }}
            FilterItem(
              v-if="column.filtered"
              :key="counter"
              :keyWord="column.id"
              :values="column.filteredValues"
              v-model:filteredRows="filteredValues"
              @updateFilteredValue="updateFilter"
              @cleanFilter="cleanTableFilter"
              )
      tbody.table__body
        tr.table-row(
          v-for="( row, idx ) in sortedRows"
          :class="{ active: selectedRowId === row.id, hovered: isHovered }"
          @click="rowClick(row.id, idx)"
        )
          td(
            v-for="(column, index) in columns"
            :key="index"
            :style="{'max-width': column.maxWidth, 'width': column.width, 'font-weight': column.fontWeight}"
          )
            span {{ row[ column.id ] }}
            .table-row__fixers(
              v-if="column.id === 'fixers'"
              :class="{ extracted: selectedRowId === row.id }"
            )
              img.edit(
                src="@/assets/icons/controls/edit.svg"
                @click.stop="editRow(row.id)"
              )
              .vertical
              img.delete(
                src="@/assets/icons/controls/trash.svg"
                @click.stop="deleteRow(row.id)"
              )
</template>

<script setup lang="ts">
import FilterItem from '@/components/FilterItem.vue'
import { defineProps, computed, defineEmits, ref, watch } from 'vue'

const emit = defineEmits([ 'rowClick', 'sortRows', 'deleteRow', 'editRow', 'repairRow', 'filterRow', 'cleanTableFilter' ])

const props = defineProps({
  rows: {
    type: Array,
    required: true,
    default: () => [],
  },
  columns: {
    type: Array,
    required: true,
    default: () => [],
  },
  hasNumeration: {
    type: Boolean,
    required: false,
    default: false
  },
  selectedRowId: {
    type: Number,
    required: false,
    default: -1,
  },
  height: {
    type: String,
    required: false,
    default: '0',
  },
  width: {
    type: String,
    required: false,
    default: '0',
  },
  isHovered: {
    type: Boolean,
    required: false,
    default: true,
  },
  label: {
    type: String,
    required: false,
    default: '',
  },
  searchedValue: {
    type: String,
    required: false,
    default: '',
  },
  filteredRow: {
    type: Number,
    required: false,
    default: 0,
  },
  expandedRows: {
    type: Array,
    required: false,
    default: () => [],
  }
})

const searchedRow = ref( 0 )
const counter = ref( 0 )

const hasMargin = computed( () =>  document.documentElement.clientHeight - +props.height.slice( 0, props.height.length - 2 ) < 33 * realRows.value.length )

const rowClick = ( id: number, index: number ) => {
  if ( id === props.selectedRowId ) {
    emit( 'rowClick', -1 )
  } else {
    emit( 'rowClick', id )
  }
}

const deleteRow = ( id: number ) => {
  emit( 'deleteRow', id )
}

const editRow = ( id: number ) => {
  emit( 'editRow', id )
}

const repairRow = ( id: number ) => {
  emit( 'repairRow', id )
}

const filteredValues = ref([''])

const updateFilter = ( params: string ) => {
  console.log( params )
  emit( 'filterRow', params )
}

const cleanTableFilter = ( key: string ) => {
  counter.value++
  emit( 'cleanTableFilter', key )
}
const filterRows = ( rows: Array<any> ) => {
  return rows
}

watch( () => filteredValues.value, () => {
  console.log( 'меняется', filteredValues.value )
})

const realRows = computed( () => {
  let displayedRows = []
  if ( props.hasNumeration ) {
    displayedRows =  props.rows.map( ( v, idx ) => ({ ...v, index: idx + 1 }) )
  } else {
    displayedRows = props.rows
  }
  displayedRows = filterRows( displayedRows )
  const searchedElement = props.columns[searchedRow.value].id
  if ( props.searchedValue ) return displayedRows.filter( ( item: any ) => item[searchedElement].toLowerCase().includes( props.searchedValue.toLowerCase() ) )
  else return displayedRows
})

const sortedColumn = ref( '' )

const sortedRows = computed( () => {
  return [...realRows.value].sort( ( a: any, b: any ) => {
    if ( !a[sortedColumn.value]) return
    if ( typeof a[sortedColumn.value] === 'number' ) {
      if ( a[sortedColumn.value] > b[sortedColumn.value]) {
        return sortDirection.value
      }
      if ( a[sortedColumn.value] < b[sortedColumn.value]) {
        return - sortDirection.value
      }
    } else {
      if ( a[sortedColumn.value].toLowerCase() > b[sortedColumn.value].toLowerCase() ) {
        return sortDirection.value
      }
      if ( a[sortedColumn.value].toLowerCase() < b[sortedColumn.value].toLowerCase() ) {
        return - sortDirection.value
      }
    }
  })
})

const sortDirection = ref( 1 )

const sortRows = ( id: string, index: number ) => {
  sortedColumn.value = id
  sortDirection.value = - sortDirection.value
  emit( 'sortRows', { index: index, direction: sortDirection.value })
}

</script>

<style scoped lang="sass">
$height: v-bind(height)
.table-row
  background-color: white
  display: flex
  align-items: center
  cursor: default
  border-bottom: 1px solid #eff0f1
  min-height: 33px
  &__fixers
    display: none
  &:hover
    background-color: white
  &:hover &__fixers
    display: flex
    align-items: center
    align-self: center
    justify-content: space-between
  &__slot
    overflow: hidden
    text-overflow: ellipsis

.extracted
  display: flex

.active
  background-color: #eff0f1

.slotted
  padding: 0px
  height: auto
  border: none

.table-fixed-head
  width: 100%
  background-color: white
  border: 1px solid #e0e1e2
  border-spacing: 0px
  table-layout: fixed
  border-collapse: collapse

  & thead
    background-color: white
    & tr
      display: flex
      position: relative
      border-bottom: 1px solid #eff0f1
  & tbody
    display: block
    overflow-y: auto
    overflow-x: hidden
    text-overflow: ellipsis
    width: 100%
    max-height: calc(100vh - $height)

th
  padding: 10px 12px
  color: #030c0c
  font-size: 14px
  text-align: left
  overflow: hidden
  display: flex
  justify-content: space-between
  align-items: center
  border-left: 1px solid #eff0f1
  line-height: 30px
  user-select: none
td
  display: flex
  align-items: center
  padding: 8px 12px
  text-align: left
  font-size: 14px
  color: #4f5557
  white-space: nowrap
  border-left: 1px solid #eff0f1
  height: 33px
  line-height: 18px
  overflow: hidden
  text-overflow: ellipsis
.head
  border: none
  display: flex
  white-space: nowrap
  cursor: pointer
  &__sorter
    margin-left: 5px

.indent
  margin-right: 15px

.filter
  cursor: pointer
  margin-left: 5px
  &__img
    max-width: 16px

.hovered
  cursor: pointer
  &:hover
    background-color: #eff0f1

.label
  background-color: white
  color: #eff0f1
  padding: 8px 16px
  font-size: 15px

.count
  opacity: 0.5
  font-size: 14px

.edit, .delete
  padding: 0px 5px
  width: 23px
.edit
  border-right: 1px solid #eff0f1

.logo 
  margin-right: 5px
  width: 16px

.searcher
  display: flex
  align-items: center

.banned
  opacity: 0.5
  pointer-events: none

span
  text-overflow: ellipsis
  overflow: hidden

.expanded
  height: 135px
  display: flex
  align-items: flex-start
  white-space: normal
  overflow-y: auto
</style>
