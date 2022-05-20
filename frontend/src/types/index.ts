export interface CoordinateItem {
  x: number;
  y: number;
}

export interface WorkerPerson {
  id: number;
  name: string;
  coordinates: Array<CoordinateItem>;
  salary: number;
  position: string;
  status: string;
  organizationType: string;
  creationDate: string;
}
