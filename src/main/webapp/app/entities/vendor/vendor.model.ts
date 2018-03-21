import { BaseEntity } from './../../shared';

export class Vendor implements BaseEntity {
    constructor(
        public id?: number,
        public vendorName?: string,
        public vendorContact?: string,
        public contactEmail?: string,
        public contactPhone?: string,
        public vendorNotes?: any,
        public vendorEndDate?: any,
        public bids?: BaseEntity[],
    ) {
    }
}
