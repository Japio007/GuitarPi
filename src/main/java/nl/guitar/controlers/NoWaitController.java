package nl.guitar.controlers;

abstract class NoWaitController implements Controller {

    public void start() {
        // no op
    }

    public void waitUntilTimestamp(long timeStamp) {
        // no op
    }

    public void waitMilliseconds(long waitTimeMS) {
        // no op
    }
}