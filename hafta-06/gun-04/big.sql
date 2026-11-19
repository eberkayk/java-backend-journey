-- Gün 35 · Görev D · hazır veri. Bu dosyayı DEĞİŞTİRME.
-- 500 bin satırlık "page_views" tablosu (yaklaşık 1 dakika sürer).
DROP TABLE IF EXISTS page_views;

CREATE TABLE page_views (
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id   INTEGER     NOT NULL,
    url       TEXT        NOT NULL,
    viewed_at TIMESTAMP   NOT NULL
);

INSERT INTO page_views (user_id, url, viewed_at)
SELECT (random() * 50000)::INTEGER + 1,
       '/page/' || (random() * 500)::INTEGER,
       TIMESTAMP '2026-01-01' + random() * INTERVAL '320 days'
FROM generate_series(1, 500000);

ANALYZE page_views;
