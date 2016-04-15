class ItemStore {
    constructor() {
        this._items = [];
        this._changeEvents = [];

        let idbReq = indexedDB.open("timeline", 1);
        this.idbReq.onupgradeneeded = event => {
            this.store = event.target.result.createObjectStore("items", {keyPath: "id"});
        };
        idbReq.onerror = event => console.log("DB open error");
        idbReq.onsuccess = event => {
            this.db = idbReq.result;

//            //"twitter", "pocket"2つのオブジェクトストアを読み書き権限付きで使用することを宣言
//            var transaction = db.transaction(["twitter", "pocket"], "readwrite");
//
//            //各オブジェクトストアの取り出し
//            var twitterStore = transaction.objectStore("twitter");
//            var pocketStore = transaction.objectStore("pocket");
//
//            //twitterオブジェクトストアから全データの取り出し
//            twitterStore.openCursor().onsuccess = function (event) {
//                var cursor = event.target.result;
//                if (cursor) {
//                    console.log("id_str:" + cursor.key + " Text: " + cursor.value.text);
//                    cursor.continue();
//                }
//            };
//            //pocketオブジェクトストアからの全データの取り出し
//            pocketStore.openCursor().onsuccess = function (event) {
//                var cursor = event.target.result;
//                if (cursor) {
//                    console.log("item_id:" + cursor.key + " url: " + cursor.value.url);
//                    cursor.continue();
//                }
//            };
        }

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



let source = creteEventSource();
let itemStore = new ItemStore();
itemStore.onChange(render);

