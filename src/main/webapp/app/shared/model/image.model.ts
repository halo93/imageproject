export interface IImage {
  id?: number;
  path?: string;
  description?: string;
  fileType?: string;
  size?: number;
}

export class Image implements IImage {
  constructor(public id?: number, public path?: string, public description?: string, public fileType?: string, public size?: number) {}
}
