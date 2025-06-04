import Joi from 'joi';

const schema = Joi.object({
  code: Joi.string().alphanum().min(2).max(5).required(),
  name: Joi.string().required(),
  numericCode: Joi.number().integer().min(0).required(),
  capitalCity: Joi.string().required(),
  population: Joi.number().integer().min(0).required(),
  area: Joi.number().positive().required()
});

export function validateCountry(data) {
  return schema.validate(data);
}
