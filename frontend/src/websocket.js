import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

let stompClient = null
const handlers = []

export function connect() {
    if (stompClient !== null) return
    const socket = new SockJS('gs-guide-websocket')
    stompClient = Stomp.over(() => socket)
    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame)
        stompClient.subscribe('/topic/activity', msg => {
            handlers.forEach(handler => handler(JSON.parse(msg.body)))
        })
    })
}

export function addHandler(handler) {
    handlers.push(handler)
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    console.log('Disconnected')
}

export function send(msg) {
    stompClient.send('/app/hello', {}, JSON.stringify(msg))
}