import { WorkerPage, WorkerPerson } from '@/types'
import { createStore } from 'vuex'
import axios from 'axios'

export default createStore({
  state: {
    workers: [] as Array<WorkerPerson>,
    url: 'http://labvm-42-12.itmo-lab.cosm-lab.science:8080/workers',
    pages: 0,
    pageSize: 5,
    page: 0,
    workersCount: 0,
    counter: 1,
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
  },
  mutations: {
    updateWorkersBody( state, workers: any ) {
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
    updateWorker( state, worker: WorkerPerson ) {
      worker.index = 1
      state.workers = [ worker ]
    },
    updateWorkers( state, workers: Array<WorkerPerson> ) {
      state.workers = workers
      state.counter = 0
      workers.map( ( item ) => {
        item.index = ( state.page * state.pageSize + state.counter + 1 )
        state.counter++
      })
    },
    updatePageSize( state, pageSize: number ) {
      state.pageSize = pageSize
    }
  },
  actions: {
    async getWorkers( state, parameters ) {
      console.log( 'pageNum', this.state.page )
      const pageParams = {
        page: this.state.page,
        pageSize: this.state.pageSize
      }
      const params = {
        params: Object.assign( pageParams, parameters )
      }
      console.log( 'pageParams', pageParams )
      const response = await axios.get( this.state.url, params )
      const result = await response.data
      console.log( result )
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
    }
  },
  modules: {},
})
