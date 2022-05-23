import { WorkerPerson } from '@/types'
import { createStore } from 'vuex'

export default createStore({
  state: {
    workers: [] as Array<WorkerPerson>,
    url: 'http://labvm-42-12.itmo-lab.cosm-lab.science:8080/workers'
  },
  getters: {
    workers: ( state ) => {
      return state.workers
    },
  },
  mutations: {
    updateWorkersList( state, workers: Array<WorkerPerson> ) {
      state.workers = workers
    },
  },
  actions: {
    async getWorkers( state, url: string ) {
      const response = await fetch( this.state.url )
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
  },
  modules: {},
})
