import { WorkerPage, WorkerPerson } from '@/types'
import { createStore } from 'vuex'

export default createStore({
  state: {
    workers: [] as Array<WorkerPerson>,
    url: 'http://labvm-42-12.itmo-lab.cosm-lab.science:8080/workers',
    pages: 0,
    limit: 5,
    page: 0,
    workersCount: 0,
    counter: 1
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
    updateWorkersList( state, workers: WorkerPage ) {
      state.counter = 0
      workers.content.map( ( item ) => {
        item.index = ( state.page * state.limit + state.counter + 1 )
        state.counter++
      })
      state.workers = workers.content
      state.pages = workers.totalPages
      state.workersCount = workers.totalElements
    },
    updatePageNum( state, pageNum: number ) {
      state.page = pageNum
    }
  },
  actions: {
    async getWorkers( state, pageNum: number ) {
      const response = await fetch( this.state.url + `/?page=${this.state.page}` )
      const result = await response.json()
      this.commit( 'updateWorkersList', result )
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
    updatePage( state, pageNum: number ) {
      this.commit( 'updatePageNum', pageNum )
    },
  },
  modules: {},
})
