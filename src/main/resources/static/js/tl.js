class ItemStore {
    constructor() {
        this._items = [];
    }

    store(items) {
        this._items = this._items.concat(items);
    }

    get items() {
        return this._items;
    }
}


let source = creteEventSource();
let itemStore = new ItemStore();


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
        fetchItems();
    }, false);
    source.addEventListener('error', function (e) {
        if (e.readyState == EventSource.CLOSED) {
            connected = false;
            connect();
        }
    }, false);
    return source;
}

function fetchItems() {
    let lastId = itemStore.items.map(i => i.id).sort().reverse()[0] || "";
    fetch("/timeline/items?lastItemId=" + lastId, {
        credentials: 'include'
    })
    .then(res => res.json())
    .then(json => {
        console.log(json);
        itemStore.store(json.items);
    });

}

