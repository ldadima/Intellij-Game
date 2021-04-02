import Vue from 'vue'
import VueRouter from 'vue-router'
import Welcome from 'src/components/Welcome.vue'
import SignUp from 'src/components/SignUp.vue'
import SignIn from 'src/components/SignIn.vue'
import EditGame from 'src/components/EditGamePage.vue'
import TeamControlPage from "../components/TeamControlPage.vue";
import GameListPage from "../components/GameListPage";
Vue.use(VueRouter);

const routes = [
    { path: '/', component: Welcome },
    { path: '/signUp', component: SignUp },
    { path: '/signIn', component: SignIn },
    { path: '/myTeam', component: TeamControlPage },
    { path: '/editGame', component: EditGame },
    { path: '/gamesList', component: GameListPage},
    { path: '/editGame/:id', component: EditGame}
    ];

export default new VueRouter({
    mode: 'history',
    routes: routes
});
