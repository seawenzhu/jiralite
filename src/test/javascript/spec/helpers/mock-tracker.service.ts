import { SpyObject } from './spyobject';
import { JlTrackerService } from '../../../../main/webapp/app/shared/tracker/tracker.service';

export class MockTrackerService extends SpyObject {

    constructor() {
        super(JlTrackerService);
    }

    connect() {}
}
