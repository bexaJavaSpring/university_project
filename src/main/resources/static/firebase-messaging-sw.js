// src/main/resources/static/firebase-messaging-sw.js

importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-messaging-compat.js');

firebase.initializeApp({
    apiKey: "AIzaSyBpBCO9zKrD76p_bI-CSfNtwL89bPsPq5g",
    authDomain: "university-app-a65e3.firebaseapp.com",
    projectId: "university-app-a65e3",
    storageBucket: "university-app-a65e3.firebasestorage.app",
    messagingSenderId: "87286361837",
    appId: "1:87286361837:web:7779cc7dffeac29314e6e4"
});

const messaging = firebase.messaging();
const analytics = firebase.analytics();
analytics.setAnalyticsCollectionEnabled(true);

// Background notification
messaging.onBackgroundMessage(function(payload) {
    console.log('Background xabar keldi:', payload);

    self.registration.showNotification(payload.notification.title, {
        body: payload.notification.body,
        icon: '/firebase-logo.png'
    });
});