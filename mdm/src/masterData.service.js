const store = new Map();

export default {
  async create(data) {
    store.set(data.code, data);
    return data;
  },

  async findAll() {
    return Array.from(store.values());
  },

  async findOne(code) {
    return store.get(code);
  },

  async update(code, data) {
    if (!store.has(code)) return null;
    store.set(code, data);
    return data;
  },

  async remove(code) {
    if (!store.has(code)) return null;
    store.delete(code);
    return true;
  }
};
