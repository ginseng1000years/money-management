<template>
  <div class="login">
    <h2>Login with Google</h2>
    <button class="google-btn" @click="signIn">
      <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google logo" />
      Sign in with Google
    </button>
    <p v-if="errorMessage" style="color: red; margin-top: 10px;">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from '@/api/axios.js';

export default {
  name: 'Login',
  data() {
    return {
      errorMessage: ''
    };
  },
  methods: {
    async signIn() {
      this.errorMessage = '';
      const healthUrl = '/health';
      const oauthUrl = 'http://localhost:8180/oauth2/authorization/google';
      try {
        const response = await axios.get(healthUrl);
        if (response.status === 200) {
          window.location.href = oauthUrl;
        } else {
          this.errorMessage = 'Technical issue, please wait';
        }
      } catch (error) {
        this.errorMessage = 'Technical issue, please wait';
      }
    }
  }
}
</script>

<style scoped>
.login {
  max-width: 400px;
  margin: 50px auto;
  text-align: center;
}

.google-btn {
  display: inline-flex;
  align-items: center;
  background-color: white;
  border: 1px solid #ddd;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border-radius: 4px;
  transition: box-shadow 0.3s ease;
}

.google-btn img {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.google-btn:hover {
  box-shadow: 0 0 5px rgba(0,0,0,0.3);
}
</style>
