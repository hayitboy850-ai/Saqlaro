const express = require('express');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
    res.json({
        app: 'SAQLARO',
        status: 'online',
        version: '1.0.0'
    });
});

app.get('/api/health', (req, res) => {
    res.json({
        success: true,
        message: 'SAQLARO server ishlayapti!'
    });
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`SAQLARO server ${PORT} portda ishlayapti`);
});
