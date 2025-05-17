<template>
  <div class="categories">
    <h2>Categories</h2>

    <button @click="openModal" class="add-category-btn">Add Category</button>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ editCategoryData ? 'Update Category' : 'Add New Category' }}</h3>
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

          <button type="submit" :disabled="loading">{{ editCategoryData ? 'Update Category' : 'Add Category' }}</button>
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
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="categories.length === 0">
          <td colspan="5" style="text-align: center; font-style: italic;">No category</td>
        </tr>
        <tr v-for="category in categories" :key="category.id">
          <td>{{ category.name }}</td>
          <td>
            <img v-if="category.image" :src="category.image" alt="Category Image" width="50" height="50" />
          </td>
          <td>{{ category.parentCategory ? category.parentCategory.name : '-' }}</td>
          <td>{{ category.description }}</td>
          <td>
            <button @click="editCategory(category)" class="edit-btn">Edit</button>
            <button @click="deleteCategory(category.id)" class="delete-btn">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteConfirm" class="delete-confirm-overlay" @click.self="cancelDelete">
      <div class="delete-confirm-modal">
        <p>Are you sure you want to delete this category?</p>
        <div class="delete-confirm-buttons">
          <button class="confirm-btn" @click="confirmDeleteCategory">Yes, Delete</button>
          <button class="cancel-btn" @click="cancelDelete">Cancel</button>
        </div>
      </div>
    </div>

    <!-- Authentication Error Modal -->
    <div v-if="showAuthError" class="auth-error-overlay" @click.self="closeAuthError">
      <div class="auth-error-modal">
        <p>Your session has expired. Please log in again to continue.</p>
        <button class="login-btn" @click="redirectToLogin">Login</button>
      </div>
    </div>
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
      showModal: false,
      editCategoryData: null,
      deleteConfirmCategoryId: null,
      showDeleteConfirm: false,
      showAuthError: false,
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
      this.editCategoryData = null;
    },
    async fetchCategories() {
      try {
        const response = await apiClient.get('/api/categories');
        this.categories = response.data;
        console.log(this.categories)
      } catch (error) {
        console.error('Failed to fetch categories:', error);
      }
    },
    async addCategory() {
      this.error = null;
      this.loading = true;
      try {
        const categoryData = {
          name: this.newCategory.name,
          type: this.newCategory.type,
          image: this.newCategory.image,
          description: this.newCategory.description,
          parentCategory: this.newCategory.parentCategoryId
            ? { id: this.newCategory.parentCategoryId }
            : null
        };

        if (this.editCategoryData) {
          // Update existing category
          const response = await apiClient.put(`/api/categories/${this.editCategoryData.id}`, categoryData);
          const index = this.categories.findIndex(cat => cat.id === this.editCategoryData.id);
          if (index !== -1) {
            this.categories.splice(index, 1, response.data);
          }
        } else {
          // Add new category
          const response = await apiClient.post('/api/categories', categoryData);
          this.categories.push(response.data);
        }

        this.closeModal();
      } catch (error) {
        this.error = (error.response && error.response.data) ? error.response.data : 'Failed to save category';
        console.error('Failed to save category:', error);
      } finally {
        this.loading = false;
      }
    },
    editCategory(category) {
      this.editCategoryData = category;
      this.newCategory = {
        name: category.name,
        type: category.type,
        image: category.image,
        parentCategoryId: category.parentCategory ? category.parentCategory.id : '',
        description: category.description
      };
      this.showModal = true;
      this.error = null;
    },
    deleteCategory(categoryId) {
      this.deleteConfirmCategoryId = categoryId;
      this.showDeleteConfirm = true;
    },
    async confirmDeleteCategory() {
      try {
        await apiClient.delete(`/api/categories/${this.deleteConfirmCategoryId}`);
        this.categories = this.categories.filter(cat => cat.id !== this.deleteConfirmCategoryId);
      } catch (error) {
        if (error.response && error.response.status === 401) {
          // Show authentication error modal
          this.showAuthError = true;
        } else {
          console.error('Failed to delete category:', error);
        }
      } finally {
        this.showDeleteConfirm = false;
        this.deleteConfirmCategoryId = null;
      }
    },
    cancelDelete() {
      this.showDeleteConfirm = false;
      this.deleteConfirmCategoryId = null;
    },
    closeAuthError() {
      this.showAuthError = false;
    },
    redirectToLogin() {
      window.location.href = process.env.VUE_APP_OAUTH_URL;
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
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.add-category-btn {
  margin-bottom: 30px;
  padding: 12px 24px;
  background-color: #1e88e5;
  border: none;
  color: white;
  font-weight: 600;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.add-category-btn:hover {
  background-color: #1976d2;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.edit-btn {
  margin-bottom: 15px;
  padding: 8px 16px;
  background-color: #f7ba2a; /* yellow */
  border: none;
  color: white;
  font-weight: 600;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.edit-btn:hover {
  background-color: #f9ca4d;
}

.delete-btn {
  margin-bottom: 15px;
  padding: 8px 16px;
  background-color: #f56c6c; /* red */
  border: none;
  color: white;
  font-weight: 600;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.delete-btn:hover {
  background-color: #f78989;
}

table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  background-color: white;
}

th, td {
  padding: 16px 24px;
  text-align: left;
  vertical-align: middle;
  border-bottom: 1px solid #f0f0f0;
}

th {
  background-color: #f8f9fa;
  color: #495057;
  font-weight: 600;
  font-size: 14px;
}

tbody tr {
  background-color: #f9f9f9;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

tbody tr:hover {
  background-color: #e6f0ff;
}

td img {
  border-radius: 4px;
  max-width: 50px;
  max-height: 50px;
  object-fit: cover;
}

.category-form {
  margin-bottom: 30px;
  padding: 24px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.category-form div {
  margin-bottom: 16px;
}

.category-form label {
  display: block;
  font-weight: 500;
  margin-bottom: 8px;
  color: #424242;
  font-size: 14px;
}

.category-form input,
.category-form select,
.category-form textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  box-sizing: border-box;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.category-form button {
  margin-right: 12px;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.category-form button[type="submit"] {
  background-color: #1e88e5;
  color: white;
}

.category-form button[type="submit"]:disabled {
  background-color: #90caf9;
  cursor: not-allowed;
}

.category-form .cancel-btn {
  background-color: #f44336;
  color: white;
}

.category-form .cancel-btn:hover {
  background-color: #e53935;
  transform: translateY(-1px);
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
/* Add styles for the custom delete confirmation modal */
.delete-confirm-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1100;
}

.delete-confirm-modal {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
  text-align: center;
}

.delete-confirm-buttons {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.delete-confirm-buttons button {
  padding: 8px 16px;
  font-weight: 600;
  border-radius: 4px;
  cursor: pointer;
  border: none;
}

.delete-confirm-buttons .confirm-btn {
  background-color: #f56c6c;
  color: white;
}

.delete-confirm-buttons .confirm-btn:hover {
  background-color: #f78989;
}

.delete-confirm-buttons .cancel-btn {
  background-color: #ccc;
  color: black;
}

.delete-confirm-buttons .cancel-btn:hover {
  background-color: #bbb;
}

/* Styles for authentication error modal */
.auth-error-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1200;
}

.auth-error-modal {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
  text-align: center;
}

.auth-error-modal p {
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 600;
}

.auth-error-modal .login-btn {
  padding: 8px 16px;
  background-color: #409eff;
  color: white;
  font-weight: 600;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.auth-error-modal .login-btn:hover {
  background-color: #66b1ff;
}
</style>
