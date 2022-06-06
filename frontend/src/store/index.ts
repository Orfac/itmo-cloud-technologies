import { WorkerPage, WorkerPerson } from '@/types'
import { createStore } from 'vuex'
import axios from 'axios'

export default createStore({
  state: {
    workers: [] as Array<WorkerPerson>,
    url: 'http://labvm-42-12.itmo-lab.cosm-lab.science:8080/app/workers',
    pages: 0,
    pageSize: 5,
    page: 0,
    workersCount: 0,
    counter: 1,
    statusError: 0,
    validateError: ''
  },
  getters: {
    workers: ( state ) => {
      return state.workers
    },
    page: ( state ) => {
      return state.page
    },
    pages: ( state ) => {
      return state.pages
    },
    workersCount: ( state ) => {
      return state.workersCount
    },
    statusError: ( state ) => {
      if ( state.statusError < 300 ) return 0 
      else return state.statusError
    },
    valError: ( state ) => {
      return state.validateError
    }
  },
  mutations: {
    updateWorkersBody( state, workers: any ) {
      state.statusError = 0
      state.counter = 0
      workers.content.map( ( item: any ) => {
        item.index = ( state.page * state.pageSize + state.counter + 1 )
        item.coordinateX = item.coordinates.x
        item.coordinateY = item.coordinates.y
        state.counter++
      })
      state.workers = workers.content
      state.pages = workers.totalPages
      state.workersCount = workers.totalElements
    },
    updatePageNum( state, pageNum: number ) {
      state.page = pageNum
    },
    updateWorker( state, worker ) {
      worker.index = 1
      worker.coordinateX = worker.coordinates.x
      worker.coordinateY = worker.coordinates.y
      state.workers = [ worker ]
    },
    updateWorkers( state, workers: Array<any> ) {
      state.workers = workers
      state.counter = 0
      workers.map( ( item ) => {
        item.index = ( state.page * state.pageSize + state.counter + 1 )
        item.coordinateX = item.coordinates.x
        item.coordinateY = item.coordinates.y
        state.counter++
      })
    },
    updatePageSize( state, pageSize: number ) {
      state.pageSize = pageSize
    },
    setError( state, error: number ) {
      state.statusError = error
    },
    setValError( state, status ) {
      state.validateError = status.salary
    }
  },
  actions: {
    async getWorkers( state, parameters ) {
      const finalParams = Object.fromEntries( Object.entries( parameters ).filter( ( item ) => item[1]) )
      const pageParams = {
        page: this.state.page,
        pageSize: this.state.pageSize
      }
      const params = {
        params: Object.assign( pageParams, finalParams )
      }
      const response = await axios.get( this.state.url, params )
      const result = await response.data
      this.commit( 'updateWorkersBody', result )
    },
    async deleteWorker( state, id: number ) {
      const response = await fetch( this.state.url + '/' + id, {
        method: 'DELETE',
      })
    },
    async addWorker( state, body: any ) {
      const response = await fetch( this.state.url, {
        method: 'POST',
        body: JSON.stringify( body ),
        headers: {
          'Content-Type': 'application/json'
        }
      })
      const result = await response.json()
      console.log( result )
      if ( !response.ok ) this.commit( 'setValError', result )
    },
    async editWorker( state, body: any ) {
      const response = await fetch( this.state.url + '/' + body.id, {
        method: 'PUT',
        body: JSON.stringify( body ),
        headers: {
          'Content-Type': 'application/json',
          'accept': '*/*'
        }
      })
      if ( !response.ok ) this.commit( 'setValError', response.json() )
    },
    updatePage( state, pageNum: string ) {
      this.commit( 'updatePageNum', +pageNum )
    },
    updatePageSize( state, pageSize: number ) {
      this.commit( 'updatePageSize', pageSize )
    },
    async getStatusWorker( state ) {
      const response = await fetch( this.state.url + '/withBiggestStatus' )
      const result = await response.json()
      this.commit( 'updateWorker', result )
    },
    async getSalaryWorkers( state, salary: number ) {
      const response = await fetch( this.state.url + '/lessThan?' + `salary=${salary}` )
      const result = await response.json()
      this.commit( 'updateWorkers', result )
    },
    async deleteSalaryWorkers( state, salary: number ) {
      const response = await fetch( this.state.url + `?salary=${salary}`, {
        method: 'DELETE',
        headers: {
          'accept': '*/*'
        }
      })
    },
    async getWorkerById( state, id: number ) {
      const response = await fetch( this.state.url + `/${id}` )
      if ( response.ok ) {
        const result = await response.json()
        this.commit( 'updateWorker', result )
      } else {
        this.commit( 'setError', response.status )
      }
    }
  },
  modules: {},
})
