<template>
  <div class="user-profile">
    <div class="user-info">
      <img :src="userImage" alt="User Image" class="user-image" />
      <div class="user-details">
        <p>Welcome, {{ userName }}</p>
        <p>Email: {{ userEmail }}</p>
      </div>
    </div>
    <button @click="logout" class="logout-button">Logout</button>
  </div>
</template>

<script>
export default {
  name: 'UserProfile',
  data() {
    const user = JSON.parse(localStorage.getItem('user')) || {};
    return {
      userName: user.name || '',
      userEmail: user.email || '',
      userImage: user.picture || '' // Get user image from user.picture in localStorage
    }
  },
  methods: {
    logout() {
      // Clear user data and redirect to login page
      localStorage.removeItem('user');
      this.$router.push({ name: 'Login' });
    }
  }
}
</script>

<style scoped>
.user-profile {
  max-width: 400px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  text-align: center;
}
.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
.user-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-right: 15px;
}
.user-details p {
  margin: 0;
  font-weight: 500;
}
.logout-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}
.logout-button:hover {
  background-color: #c82333;
}
</style>
