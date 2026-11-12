# 17 Haftalık Plan — Sıfırdan Junior Java Backend

> Omurga: `roadmap.sh/java` + `roadmap.sh/spring-boot`. `roadmap.sh/backend`'den sadece junior için gerekenler alındı.
> Tarihler hedeftir; hızlı gidersek öne çekeriz, zorlanırsak konu sağlam oturmadan ilerlemeyiz.
> **Revizyon (1 Kasım 2026):** Hafta 1–4 sınavları 58 / 78.5 / 64.5 / 62. Hedef %80+ olmadan SQL'e geçmiyoruz → Hafta 5 **Tekrar + Kapı Sınavı** eklendi, sonraki haftalar 1 hafta kaydı (16 → 17 hafta, kapı sınavı geçilemezse 18).
> **Revizyon (11 Kasım 2026):** 4–10 Kas ara verildi (Gün 26 algoritması 11 Kas’ta bitti). Gün 27’den itibaren her şey 1 hafta kaydı.

## Başlangıç Seviyesi (29 Eylül 2026)

- Java sözdizimi 2/5, diğer her şey 0/5. İngilizce iyi (okuma-yazma 4, konuşma 3).
- D1 (dizide max) doğru çözüldü → temel döngü/dizi mantığı var. Bunun üstüne kuracağız.
- En büyük risk: motivasyon kaybı (daha önce 3-4 kez bırakıldı). Önlem: günlük küçük hedef + her oturumda hesap sorma + görünür ilerleme (GitHub).

## Günlük Şablon (~6 saat)

| Blok    | Süre     | İçerik                                                                     |
| ------- | -------- | -------------------------------------------------------------------------- |
| Isınma  | 20 dk    | Dünkü kağıt notlarına bakmadan 3 şey yaz, sonra kontrol et (active recall) |
| Blok 1  | 1.5–2 sa | Yeni konu: `DERS.md` oku / Claude ile ders, "Kağıda Yaz" kutusunu yaz      |
| Blok 2  | 2–2.5 sa | Egzersizler — kendin kodla                                                 |
| Blok 3  | 1 sa     | Algoritma pratiği (Hafta 2'den itibaren)                                   |
| Kapanış | 20 dk    | `ILERLEME.md` doldur, commit + push                                        |

50 dk çalış / 10 dk mola. Telefon başka odada.
**Pazar:** 2-3 saat — haftalık tekrar, haftalık mini sınav (Claude hazırlar), gelecek haftaya bakış.

---

## FAZ 1 — Java Core (Hafta 1–4) · 30 Eyl – 27 Eki

| Hafta | Konular                                                                                                                                   | Hafta sonu çıktısı                                                                                    |
| ----- | ----------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| **1** | Terminal, Git temelleri, JDK/JVM, değişkenler, tipler, operatörler, if/switch, döngüler, metotlar, diziler, String                        | 30+ küçük egzersiz, ilk GitHub repo                                                                   |
| **2** | OOP: class/object, constructor, encapsulation, `static`, inheritance, polymorphism, abstract class, interface, `equals/hashCode/toString` | Konsol "Kütüphane Sistemi" (OOP tasarımlı)                                                            |
| **3** | Exceptions, Collections (List/Set/Map, iç yapıları), Generics, Comparable/Comparator, Iterator                                            | Kütüphane sistemini Collections + exception ile yeniden yaz                                           |
| **4** | Lambda, Stream API, Optional, record, enum, dosya I/O, `java.time`, temel concurrency kavramı. **Maven**                                  | **Proje 0:** Konsol "Gider Takip" uygulaması (dosyaya kaydeden, stream kullanan, Maven'lı, README'li) |

## TEKRAR — Hafta 5 · 2 – 15 Kas (Gün 25–31, 4–10 Kas ara)

**Neden:** Hafta 3 (Collections) ve Hafta 4 (modern Java) %50–60 seviyesinde, algoritmayı sıfırdan kurmak zayıf. Spring bunların üstüne kurulu; oturmadan geçmek ileride iki kat zaman kaybettirir.

**Günlük şablon (~5-6 saat, karışık):** Isınma 15 dk (sık kaçanlar, kodlu sorular) → Konu özeti 30 dk (yeni konu yok) → Çıktı tahmin seti 20 dk → Notsuz kod görevi 1.5–2 sa → Algoritma 1 sa (1–2 LeetCode: önce örnek girdiyle elle çöz, adımları chat'e yaz, sonra kodla) → Kapanış.

| Gün | Tarih  | Odak                                                                                                     | Algoritma              |
| --- | ------ | -------------------------------------------------------------------------------------------------------- | ---------------------- |
| 25  | 2 Kas  | Exceptions (checked/unchecked, custom, throw/throws) + `List` (dönüş değerleri, `remove(int)` tuzağı)    | Dizi + iki pointer     |
| 26  | 3 Kas  | `Map` / `Set`: `hashCode`/`equals`, sayma kalıpları (`getOrDefault`, `merge`, `putIfAbsent`)             | HashMap ile sayma      |
| 27  | 11 Kas | Generics + `Comparable` / `Comparator` zincirleri + `Iterator` / CME                                     | Sıralama tabanlı       |
| 28  | 12 Kas | Lambda + functional interface'ler + Stream (`filter/map/sorted/collect/groupingBy`), döngü ↔ stream      | String                 |
| 29  | 13 Kas | `Optional` (yazdırma!), `record`, `enum`, `java.time`, dosya I/O, exception isimleri                     | Karışık                |
| 30  | 14 Kas | **Notsuz mini proje** sıfırdan (Maven; collections + exception + stream + record/enum + dosya), 3-4 saat | —                      |
| 31  | 15 Kas | **Kapı sınavı** — Hafta 1–4 tamamı, 100 puan, konu bazında puan                                          | Sınav içinde 2 problem |

**Kapı kuralı:** Kapı sınavı **≥ 80** → Hafta 6 (SQL). **< 80** → Hafta 5b: sadece %80 altı kalan konular, 1 hafta, aynı formatta ikinci kapı sınavı; bütün tarihler 1 hafta daha kayar.

---

## FAZ 2 — Veritabanı ve Web Temelleri (Hafta 6–7) · 16 – 29 Kas

| Hafta | Konular                                                                                                                                                            | Çıktı                                                                   |
| ----- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------- |
| **6** | SQL (PostgreSQL): SELECT, WHERE, JOIN, GROUP BY, subquery, index, transaction, normalizasyon, ER diyagram. JDBC                                                    | 60+ SQL sorusu (pgexercises, SQLBolt). Gider Takip'i PostgreSQL'e bağla |
| **7** | HTTP (metodlar, status kodları, header, JSON), REST tasarımı, Postman, JUnit 5 ile unit test, SOLID, temel design patterns (Singleton, Factory, Builder, Strategy) | Proje 0'a unit testler (%70+)                                           |

## FAZ 3 — Spring Boot (Hafta 8–11) · 30 Kas – 27 Ara

| Hafta  | Konular                                                                                                                                   |
| ------ | ----------------------------------------------------------------------------------------------------------------------------------------- |
| **8**  | Spring IoC/DI, bean, `@Component/@Service/@Repository`, Spring Boot yapısı, `@RestController`, DTO, validation, global exception handling |
| **9**  | Spring Data JPA / Hibernate: entity, ilişkiler (1-N, N-N), lazy/eager, N+1 problemi, pagination, Flyway migration                         |
| **10** | Test: JUnit 5 + Mockito, `@WebMvcTest`, `@DataJpaTest`, Testcontainers. Spring Security + JWT                                             |
| **11** | Docker, docker-compose, GitHub Actions (CI), OpenAPI/Swagger, logging, deploy (Render/Railway vb.)                                        |

**Proje 1 (Hafta 8–11 boyunca yazılır):** Randevu/Rezervasyon REST API
- Kullanıcı kayıt/giriş (JWT), roller (USER/ADMIN), CRUD, pagination & filtreleme
- PostgreSQL + Flyway, Docker Compose ile tek komutla ayağa kalkar
- Unit + integration test, GitHub Actions CI, Swagger dokümantasyonu, canlı demo linki

> 🎯 **Hafta 11 sonunda:** yeni CV hazır → **başvurular başlar** (günde 3-5 hedefli başvuru, çalışma devam ederken).

## FAZ 4 — Derinleşme + İkinci Proje (Hafta 12–14) · 28 Ara – 17 Oca

- Proje 2: daha "kurumsal" bir sistem (ör. E-ticaret sipariş servisi): Redis cache, event/mesaj kuyruğu temeli (RabbitMQ veya Kafka tanıtım), transaction yönetimi, idempotency
- SOAP'a kısa bakış (Türk bankaları için), JSP/Thymeleaf'e 1 günlük bakış
- Git ileri: branch stratejisi, PR, rebase, conflict çözme
- Linux temelleri, temel ağ (TCP/IP, DNS, HTTPS)

## FAZ 5 — Mülakat Modu (Hafta 15–17) · 18 Oca – 7 Şub

- Java core mülakat soruları (100+ soru bankası, Claude ile sözlü prova — TR ve EN)
- Spring / JPA / SQL mülakat soruları
- Algoritma: NeetCode 150'den seçilmiş ~80 soru toplamı
- Temel sistem tasarımı (URL kısaltıcı, rate limiter vb. junior seviyesi)
- Mock mülakatlar (haftada 2), davranışsal sorular (STAR), "1.5 yıllık boşluk" sorusuna hazır cevap
- LinkedIn optimizasyonu, remote iş platformları

---

## Algoritma Pratiği Hattı (Hafta 2'den itibaren her gün 1 saat)

| Hafta | Konu                                                                        |
| ----- | --------------------------------------------------------------------------- |
| 2–5   | Diziler, String, HashMap ile sayma, iki pointer — LeetCode Easy             |
| 6–8   | Stack, Queue, Linked List, Recursion, Binary Search                         |
| 9–11  | Tree (BFS/DFS), sıralama algoritmaları, Big-O analizi                       |
| 12–17 | Sliding window, Heap, Graph temelleri, NeetCode 150 seçkisi, karışık tekrar |

## Kaynaklar (sadece bunlar — kaynak toplama tuzağına düşmüyoruz)

- **Ana ders:** Claude + bu klasördeki `DERS.md` dosyaları
- **Ek egzersiz bankası:** [Java Programming MOOC — Helsinki](https://java-programming.mooc.fi) (Part 1–7)
- **Resmî referans:** [dev.java/learn](https://dev.java/learn), [Baeldung](https://www.baeldung.com) (Spring için)
- **SQL:** [SQLBolt](https://sqlbolt.com), [pgexercises](https://pgexercises.com)
- **Algoritma:** LeetCode + [NeetCode](https://neetcode.io) videoları
- **Video (takıldığında):** Amigoscode, Java Brains (YouTube)

## Başarı Ölçütleri (işe hazır olduğumuzu nereden anlayacağız?)

- [ ] AI'sız, boş bir IntelliJ'de 1 saatte CRUD REST API yazabiliyorum
- [ ] 2 canlı, testli, dokümante projem var ve her satırını mülakatta savunabiliyorum
- [ ] LeetCode Easy'leri 20 dk içinde, Medium'ların yarısını 45 dk içinde çözebiliyorum
- [ ] Java/Spring/SQL soru bankasının %80'ine sözlü cevap verebiliyorum
- [ ] CV'deki her kelimeyi mülakatta açıklayabiliyorum
