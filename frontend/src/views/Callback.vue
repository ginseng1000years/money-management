<template>
  <div>Loading...</div>
</template>

<script>
import apiClient from '../api/axios';

export default {
  name: 'Callback',
  async mounted() {
    try {
      // Fetch user info from backend API
      const response = await apiClient.get('/api/userinfo');
      const user = response.data;
      localStorage.setItem('user', JSON.stringify(user));
      if (user.token) {
        localStorage.setItem('token', user.token);
      }
      this.$router.push({ name: 'Home' });
    } catch (error) {
      console.error('Failed to fetch user info:', error);
      this.$router.push({ name: 'Login' });
    }
  }
}
</script>
