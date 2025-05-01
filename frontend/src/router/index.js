import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'

import UserProfile from '../components/UserProfile.vue'
import Setting from '../components/Setting.vue'
import Categories from '../components/Categories.vue'
import Budget from '../components/Budget.vue'
import Transaction from '../components/Transaction.vue'
import Help from '../components/Help.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/home',
    name: 'HomeAlias',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/callback',
    name: 'Callback',
    component: () => import('../views/Callback.vue')
  },
  {
    path: '/user-profile',
    name: 'UserProfile',
    component: UserProfile,
    meta: { requiresAuth: true }
  },
  {
    path: '/setting',
    name: 'Setting',
    component: Setting,
    meta: { requiresAuth: true }
  },
  {
    path: '/categories',
    name: 'Categories',
    component: Categories,
    meta: { requiresAuth: true }
  },
  {
    path: '/budget',
    name: 'Budget',
    component: Budget,
    meta: { requiresAuth: true }
  },
  {
    path: '/transaction',
    name: 'Transaction',
    component: Transaction,
    meta: { requiresAuth: true }
  },
  {
    path: '/help',
    name: 'Help',
    component: Help,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// Navigation guard to check for auth
router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('user')
  if (to.meta.requiresAuth && !isAuthenticated) {
    window.location.href = process.env.VUE_APP_LOGIN_URL
  } else {
    next()
  }
})

export default router
