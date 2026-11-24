const express = require('express');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json({ limit: '16kb' }));

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

app.post('/api/check', (req, res) => {
    const { url } = req.body || {};

    if (!url || typeof url !== 'string') {
        return res.status(400).json({
            success: false,
            message: 'Havola kiritilmadi.'
        });
    }

    let parsed;

    try {
        parsed = new URL(url);
    } catch {
        return res.status(400).json({
            success: false,
            message: 'Havola noto‘g‘ri.'
        });
    }

    if (parsed.protocol !== 'https:') {
        return res.status(400).json({
            success: false,
            message: 'Faqat HTTPS havolalar qabul qilinadi.'
        });
    }

    const host = parsed.hostname.toLowerCase();

    const platforms = [
        { name: 'Instagram', domains: ['instagram.com'] },
        { name: 'TikTok', domains: ['tiktok.com'] },
        { name: 'YouTube', domains: ['youtube.com', 'youtu.be'] }
    ];

    const platform = platforms.find(item =>
        item.domains.some(domain =>
            host === domain || host.endsWith('.' + domain)
        )
    );

    res.json({
        success: true,
        accepted: true,
        platform: platform ? platform.name : 'Other',
        message: 'Havola tekshirildi. Yuklash xizmati hali ulanmagan.'
    });
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`SAQLARO server ${PORT} portda ishlayapti`);
});
