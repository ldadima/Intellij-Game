import Vue from 'vue'
import App from './App.vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import VueRouter from 'vue-router'
import SignUp from 'components/SignUp.vue'
import SignIn from 'components/SignIn.vue'
import Welcome from 'components/Welcome.vue'
import VueResource from 'vue-resource'
import Vuelidate from 'vuelidate'
import TeamControlPage from 'components/TeamControlPage.vue'
import {connect} from './websocket'
import EditGame from "./components/EditGamePage.vue";
import GameListPage from "./components/GameListPage.vue";

connect()

Vue.config.productionTip = false

Vue.use(Vuetify, {iconfont: 'mdiSvg'});
Vue.use(Vuelidate)
Vue.use(VueResource);
Vue.use(VueRouter);

const routes = [
    {path: '/', component: Welcome},
    {path: '/signUp', component: SignUp},
    {path: '/signIn', component: SignIn},
    {path: '/myTeam', component: TeamControlPage},
    {path: '/gamesList', component: GameListPage},
    {path: '/editGame/:id', component: EditGame},
    {path: '/editGame', component: EditGame},
];

const router = new VueRouter({
    mode: 'history',
    routes: routes
});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    router: router,
    render: h => h(App),
});
