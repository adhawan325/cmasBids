import { BaseEntity } from './../../shared';

export class Bid implements BaseEntity {
    constructor(
        public id?: number,
        public bidNumber?: string,
        public bidName?: string,
        public endDate?: any,
        public bidSOW?: any,
        public bidMQs?: any,
        public bidDQs?: any,
        public createdBy?: string,
        public createdOn?: any,
        public modifiedOn?: any,
        public department?: BaseEntity,
        public vendors?: BaseEntity[],
        public candidates?: BaseEntity[],
    ) {
    }
}
