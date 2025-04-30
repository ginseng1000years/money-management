<template>
  <div class="categories">
    <h2>Categories</h2>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Image</th>
          <th>Parent Category</th>
          <th>Description</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="category in categories" :key="category.id">
          <td>{{ category.name }}</td>
          <td>
            <img v-if="category.image" :src="category.image" alt="Category Image" width="50" height="50" />
          </td>
          <td>{{ category.parentCategory ? category.parentCategory.name : '-' }}</td>
          <td>{{ category.description }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import apiClient from '../api/axios';

export default {
  name: 'Categories',
  data() {
    return {
      categories: []
    }
  },
  created() {
    this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await apiClient.get('/api/categories');
        this.categories = response.data;
      } catch (error) {
        console.error('Failed to fetch categories:', error);
      }
    },
    formatDate(dateString) {
      if (!dateString) return '-';
      const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    }
  }
}
</script>

<style scoped>
.categories {
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

img {
  display: block;
  max-width: 100%;
  height: auto;
}
</style>
