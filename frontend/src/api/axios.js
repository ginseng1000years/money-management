import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8180',
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

// Add a response interceptor to handle 401 Unauthorized responses
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      console.log('Unauthorized, redirecting to login...');
      // Redirect to OAuth2 login page
      window.location.href = 'http://localhost:8180/oauth2/authorization/google';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
