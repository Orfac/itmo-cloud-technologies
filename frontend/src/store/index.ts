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
    counter: 1,
    positionFilter: '',
    statusFilter: '',
    organizationTypeFilter: '',
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
    updateWorkersBody( state, workers: WorkerPage ) {
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
    },
    updatePosFilter( state, filter: string ) {
      state.positionFilter = filter
    },
    updateStatusFilter( state, filter: string ) {
      state.statusFilter = filter
    },
    updateOrgFilter( state, filter: string ) {
      state.organizationTypeFilter = filter
    },
    updateWorker( state, worker: WorkerPerson ) {
      worker.index = 1
      state.workers = [ worker ]
    },
    updateWorkers( state, workers: Array<WorkerPerson> ) {
      state.workers = workers
      state.counter = 0
      workers.map( ( item ) => {
        item.index = ( state.page * state.limit + state.counter + 1 )
        state.counter++
      })
    },
  },
  actions: {
    async getWorkers( state, pageNum: number ) {
      const response = await fetch( this.state.url + `/?page=${this.state.page}` )
      const result = await response.json()
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
    updatePage( state, pageNum: number ) {
      this.commit( 'updatePageNum', pageNum )
    },
    async updatePositionFilter( state, filterValue: string ) {
      this.commit( 'updatePosFilter', filterValue )
    },
    async updateStatusFilter( state, filterValue: string ) {
      this.commit( 'updateStatusFilter', filterValue )
    },
    async updateOrganizationFilter( state, filterValue: string ) {
      this.commit( 'updateOrgFilter', filterValue )
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
