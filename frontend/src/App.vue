<template>
  <div id="app">
    <MenuTabs v-if="isAuthenticated" />
    <router-view />
  </div>
</template>

<script>
import MenuTabs from './components/MenuTabs.vue'

export default {
  name: 'App',
  components: {
    MenuTabs
  },
  data() {
    return {
      isAuthenticated: !!localStorage.getItem('user')
    }
  },
  watch: {
    $route() {
      this.isAuthenticated = !!localStorage.getItem('user');
    }
  }
}
</script>

<style>
/* Add global styles here */
</style>
