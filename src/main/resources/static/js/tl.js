const DB_NAME = "timeline";
const DB_VERSION = 1;
const DB_STORE_NAME = 'items';

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


//
//class ItemStore {
//
//    constructor() {
//      let idbReq = indexedDB.open(DB_NAME, DB_VERSION);
//
//      idbReq.onupgradeneeded = event => {
//        console.log("[ItemStore] onupgradeneeded");
//        db = event.target.result;
//        db.createObjectStore(DB_STORE_NAME, {keyPath: "id"});
//      }
//
//      idbReq.onerror = event => console.log("[ItemStore] DB open error");
//      idbReq.onsuccess = event => {
//        console.log("[ItemStore] onsuccess");
//        this._db = idbReq.result;
//      }
//    }
//
//    store(items) {
////        this._items = this._items.concat(items);
////        this._changeEvents.forEach(e => e(items));
//
//      let tx = this._db.transaction(DB_STORE_NAME, "readwrite");
//      let store = tx.objectStore(DB_STORE_NAME);
//  	  var req = store.add(items)
//	  req.onsuccess = () => console.log("成功");
//	  req.onerror = () => console.log("エラー");
//	  tx.oncomplete = () => console.log("トランザクション完了");
//	  tx.onerror = () => console.log("トランザクション失敗");
//    }
//
//
////     onChange(fn) {
////         this._changeEvents.push(fn);
////     }
//
//
//    get items() {
//      let tx = this._db.transaction(DB_STORE_NAME, "readwrite");
//      let store = tx.objectStore(DB_STORE_NAME);
//      store.openCursor().onsuccess = function (event) {
//        var cursor = event.target.result;
//        if (cursor) {
//            console.log("id_str:" + cursor.key + " Text: " + cursor.value.Text);
//            cursor.continue();
//        }
//      };
//    }
//}




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

