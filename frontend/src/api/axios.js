import axios from 'axios';

const apiClient = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to include Authorization header with JWT token
apiClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    console.log('Axios interceptor token:', token);
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
      console.log('Authorization header set');
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response) {
      if (error.response.status === 401) {
        console.log('Unauthorized, clearing localStorage and redirecting to login...');
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        window.location.href = process.env.VUE_APP_LOGIN_URL;
      } else if (error.response.status === 440) {
        console.log('Session expired, clearing localStorage and redirecting to login...');
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        window.location.href = process.env.VUE_APP_LOGIN_URL;
      }
    }
    return Promise.reject(error);
  }
);



export default apiClient;
