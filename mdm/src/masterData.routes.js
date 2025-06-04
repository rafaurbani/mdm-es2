import express from 'express';
import controller from './masterData.controller.js';

const router = express.Router();

router.post('/', controller.create);
router.get('/', controller.findAll);
router.get('/:code', controller.findOne);
router.put('/:code', controller.update);
router.delete('/:code', controller.remove);

export default router;
