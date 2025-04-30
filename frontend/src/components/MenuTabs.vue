<template>
  <nav class="menu-tabs">
    <ul>
      <li v-for="tab in tabs" :key="tab.name" :class="{ active: tab.name === activeTab }" @click="selectTab(tab.name)">
        <a href="#" @click.prevent>{{ tab.name }}</a>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'MenuTabs',
  data() {
    return {
      tabs: [
      { name: 'Home' },
      { name: 'Categories' },
      { name: 'Budget' },
      { name: 'Transaction' },
      { name: 'Help' },
      { name: 'Setting' },
      { name: 'User Profile' }
      ],
      activeTab: 'Home'
    }
  },
  methods: {
    selectTab(tabName) {
      this.activeTab = tabName;
      // Navigate to the route matching the tab name in kebab-case
      let routeName = tabName.toLowerCase().replace(/\s+/g, '-');
      if (routeName === 'user-profile') {
        this.$router.push({ name: 'UserProfile' });
      } else if (routeName === 'setting') {
        this.$router.push({ name: 'Setting' }); // Assuming Setting route exists or will be added
      } else {
        this.$router.push({ name: routeName.charAt(0).toUpperCase() + routeName.slice(1) });
      }
    }
  }
}
</script>

<style scoped>
.menu-tabs {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}
.menu-tabs ul {
  list-style: none;
  display: flex;
  margin: 0;
  padding: 0;
}
.menu-tabs li {
  margin: 0;
}
.menu-tabs li a {
  display: block;
  padding: 12px 20px;
  color: #495057;
  text-decoration: none;
  cursor: pointer;
  user-select: none;
}
.menu-tabs li.active a {
  font-weight: bold;
  border-bottom: 2px solid #007bff;
  color: #007bff;
}
.menu-tabs li a:hover {
  background-color: #e9ecef;
}
</style>
