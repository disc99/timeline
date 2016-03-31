var source = creteEventSource();
document.addEventListener("visibilitychange", function() {
    if (!document.hidden && source.readyState == EventSource.CLOSED) {
        console.log("reconnect");
        source = creteEventSource();
    }
}, false);

function creteEventSource() {
    var source = new EventSource("/timeline/registNotification");
    source.addEventListener('open', function (e) {
        console.log('connected');
    });
    source.addEventListener('message', function (e) {
        console.log(e.data);
    }, false);
    source.addEventListener('error', function (e) {
        if (e.readyState == EventSource.CLOSED) {
            connected = false;
            connect();
        }
    }, false);
    return source;
}