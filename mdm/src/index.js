import express from 'express';
import cors from 'cors';
import morgan from 'morgan';
import routes from './masterData.routes.js';

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(morgan('dev'));
app.use(express.json());
app.use('/api/mdm', routes);

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});

// eureka
app.get('/', (req, res) => {
  res.send('Hello from Node.js service!');
});
app.listen(PORT, () => {
  console.log(`Node.js service listening at http://localhost:${PORT}`);
});
// Import and start Eureka client
import('./eureka-client.cjs');