export interface IImage {
  id?: number;
  path?: string;
  pictureDescription?: string;
  fileType?: string;
  size?: number;
  isActive?: boolean;
}

export class Image implements IImage {
  constructor(
    public id?: number,
    public path?: string,
    public pictureDescription?: string,
    public fileType?: string,
    public size?: number,
    public isActive?: boolean
  ) {
    this.isActive = this.isActive || false;
  }
}
