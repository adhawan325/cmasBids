import { BaseEntity } from './../../shared';

export class Candidate implements BaseEntity {
    constructor(
        public id?: number,
        public candidateName?: string,
        public meetsMQs?: boolean,
        public meetsDQs?: boolean,
        public resumeContentType?: string,
        public resume?: any,
        public ratePerHour?: number,
        public bids?: BaseEntity[],
    ) {
        this.meetsMQs = false;
        this.meetsDQs = false;
    }
}
