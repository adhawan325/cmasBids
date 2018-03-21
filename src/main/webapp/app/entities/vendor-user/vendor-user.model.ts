import { BaseEntity } from './../../shared';

export class VendorUser implements BaseEntity {
    constructor(
        public id?: number,
        public userName?: string,
        public vendor?: BaseEntity,
    ) {
    }
}
