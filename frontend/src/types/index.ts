export interface CoordinateItem {
  x: number
  y: number
}

export interface WorkerPerson {
  id: number
  name: string
  coordinates: Array<CoordinateItem>
  salary: number
  position: string
  status: string
  organizationType: string
  creationDate: string
  index?: number
}

export interface WorkerPage {
  totalPages: number
  totalElements: number
  size: number
  content: Array<WorkerPerson>
  number: number
  pageable: PageableObject
  numberOfElements: number
  first: boolean
  last: boolean
  empty: boolean
}

export interface PageableObject {
  offset: number
  sort: Sort
  paged: boolean
  pageNumber: number
  pageSize: number
  unpaged: boolean
}

export interface Sort {
  empty: boolean
  usorted: boolean
  sorted: boolean
}
