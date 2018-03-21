import { BaseEntity } from './../../shared';

export class BidDocument implements BaseEntity {
    constructor(
        public id?: number,
        public documentName?: string,
        public fileContentType?: string,
        public file?: any,
        public bid?: BaseEntity,
    ) {
    }
}
