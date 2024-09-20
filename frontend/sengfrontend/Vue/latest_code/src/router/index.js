import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/element',
    name: 'Element',
    component: () => import('../views/Element.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/forgetpassword',
    name: 'Forgetpassword',
    component: () => import('../views/Forgetpassword.vue')
  },
  {
    path: '/tenent',
    name: 'Tenent',
    component: () => import('../views/tenent.vue')
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
