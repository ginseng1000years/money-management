<template>
  <div class="categories">
    <h2>Categories</h2>

    <button @click="openModal" class="add-category-btn">Add Category</button>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>Add New Category</h3>
        <form @submit.prevent="addCategory" class="category-form">
          <div>
            <label for="name">Name:</label>
            <input id="name" v-model="newCategory.name" required />
          </div>

          <div>
            <label for="type">Type:</label>
            <select id="type" v-model="newCategory.type" required>
              <option value="" disabled>Select type</option>
              <option value="income">Income</option>
              <option value="expense">Expense</option>
            </select>
          </div>

          <div>
            <label for="image">Icon (image):</label>
            <input id="image" v-model="newCategory.image" placeholder="Icon name or URL" />
          </div>

          <div>
            <label for="parentCategory">Parent Category:</label>
            <select id="parentCategory" v-model="newCategory.parentCategoryId">
              <option value="">None</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>

          <div>
            <label for="description">Description:</label>
            <textarea id="description" v-model="newCategory.description"></textarea>
          </div>

          <button type="submit" :disabled="loading">Add Category</button>
          <button type="button" @click="closeModal" class="cancel-btn">Cancel</button>
        </form>
      </div>
    </div>

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
      categories: [],
      newCategory: {
        name: '',
        type: '',
        image: '',
        parentCategoryId: '',
        description: ''
      },
      loading: false,
      error: null,
      showModal: false
    }
  },
  created() {
    this.fetchCategories();
  },
  methods: {
    openModal() {
      this.showModal = true;
      this.error = null;
    },
    closeModal() {
      this.showModal = false;
      this.error = null;
      this.newCategory = {
        name: '',
        type: '',
        image: '',
        parentCategoryId: '',
        description: ''
      };
    },
    async fetchCategories() {
      try {
        const response = await apiClient.get('/api/categories');
        this.categories = response.data;
      } catch (error) {
        console.error('Failed to fetch categories:', error);
      }
    },
    async addCategory() {
      this.error = null;
      this.loading = true;
      try {
        // Prepare category data
        const categoryData = {
          name: this.newCategory.name,
          type: this.newCategory.type,
          image: this.newCategory.image,
          description: this.newCategory.description,
          parentCategory: this.newCategory.parentCategoryId
            ? { id: this.newCategory.parentCategoryId }
            : null
        };

        const response = await apiClient.post('/api/categories', categoryData);
        this.categories.push(response.data);

        // Reset form and close modal
        this.closeModal();
      } catch (error) {
this.error = (error.response && error.response.data) ? error.response.data : 'Failed to add category';
        console.error('Failed to add category:', error);
      } finally {
        this.loading = false;
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

.add-category-btn {
  margin-bottom: 15px;
  padding: 8px 16px;
  background-color: #409eff;
  border: none;
  color: white;
  font-weight: 600;
  border-radius: 4px;
  cursor: pointer;
}

.add-category-btn:hover {
  background-color: #66b1ff;
}

.category-form {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background-color: #f9f9f9;
}

.category-form div {
  margin-bottom: 10px;
}

.category-form label {
  display: block;
  font-weight: 600;
  margin-bottom: 5px;
}

.category-form input,
.category-form select,
.category-form textarea {
  width: 100%;
  padding: 6px 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.category-form button {
  margin-right: 10px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-weight: 600;
  cursor: pointer;
}

.category-form button[type="submit"] {
  background-color: #67c23a;
  color: white;
}

.category-form button[type="submit"]:disabled {
  background-color: #a0d911;
  cursor: not-allowed;
}

.category-form .cancel-btn {
  background-color: #f56c6c;
  color: white;
}

.category-form .cancel-btn:hover {
  background-color: #f78989;
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

/* Modal styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
}
</style>
