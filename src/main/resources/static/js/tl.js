class ItemStore {
    constructor() {
        this._items = [];
        this._changeEvents = [];
    }

    store(items) {
        this._items = this._items.concat(items);
        this._changeEvents.forEach(e => e(items));
    }

    onChange(fn) {
        this._changeEvents.push(fn);
    }

    get items() {
        return this._items;
    }
}


let source = creteEventSource();
let itemStore = new ItemStore();
itemStore.onChange(render);


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
        console.log("[Catch message] "+e.data);
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
    let lastId = itemStore.items.map(i => i.id).sort((a, b) => (parseInt(a) > parseInt(b)) ? 1 : -1).reverse()[0] || "";
    fetch("/timeline/items?lastItemId=" + lastId, {
        credentials: 'include'
    })
    .then(res => res.json())
    .then(json => {
        console.log("[Fetched JSON] "+JSON.stringify(json.items));
        itemStore.store(json.items);
    });
}

function render(items) {
    const ul = document.getElementById("items");
    items.forEach(i => {
        const li = document.createElement("li");
        li.appendChild(document.createTextNode(JSON.stringify(i)));
        ul.insertBefore(li, ul.firstChild);
    });
}

