import service from './masterData.service.js';
import { validateCountry } from './validator.js';

export default {
  async create(req, res) {
    const { error } = validateCountry(req.body);
    if (error) return res.status(400).json({ error: error.details[0].message });

    const result = await service.create(req.body);
    res.status(201).json(result);
  },

  async findAll(req, res) {
    const result = await service.findAll();
    res.json(result);
  },

  async findOne(req, res) {
    const result = await service.findOne(req.params.code);
    if (!result) return res.status(404).json({ error: 'Not found' });
    res.json(result);
  },

  async update(req, res) {
    const { error } = validateCountry(req.body);
    if (error) return res.status(400).json({ error: error.details[0].message });

    const result = await service.update(req.params.code, req.body);
    if (!result) return res.status(404).json({ error: 'Not found' });
    res.json(result);
  },

  async remove(req, res) {
    const result = await service.remove(req.params.code);
    if (!result) return res.status(404).json({ error: 'Not found' });
    res.json({ message: 'Deleted successfully' });
  }
};
