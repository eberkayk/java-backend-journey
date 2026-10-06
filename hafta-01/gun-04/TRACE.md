# E4 — reverseInPlace Trace Table

Her satır **bir tur**. `left` ve `right` turun **başındaki** değerler. "Dizi" sütunu o turdaki swap **bittikten sonraki** hali.
Son satır: döngü koşulunun kontrol edildiği ve **sağlanmadığı** an (swap yok).

## Tablo 1 — `{1, 2, 3, 4, 5}`  (length = 5)

| Tur | left | right | left < right ? | Dizi (swap sonrası) |
|---|---|---|---|---|
| 1 | 0 | 4 | t |5,2,3,4,1|
| 2 | 1 | 3 | t |5,4,3,2,1|
| 3 | 2 | 2 | f | (swap yok, döngü biter) |

## Tablo 2 — `{1, 2, 3, 4}`  (length = 4)

| Tur | left | right | left < right ? | Dizi (swap sonrası) |
|---|---|---|---|---|
| 1 | 0 | 3 | t |4,2,3,1|
| 2 | 1 | 2 | t |4,3,2,1|
| 3 | 2 | 1 | f | (swap yok, döngü biter) |

## Sorular

1. Kodda `left` ve `right` ilk değerleri (`arr.length` cinsinden):
   - `left = arr.length - arr.length`
   - `right = arr.length - 1`
2. Döngü koşulu `left <= right` olsaydı Tablo 1'de ne olurdu? Hata verir mi, sadece gereksiz bir iş mi yapar?
   tablo 1'de 3. turda da koşul sağlanırdı fakat index 2'deki elemanı indeks 2'ye koyacağı için bir değişiklik olmaz sadece bir tur fazladan gereksiz çalışır.
3. `reverseInPlace` neden `void` olabiliyor da Gün 3'teki `swap(int a, int b)` çalışmıyordu?
   reverseInPlace void olabiliyor çünkü o metota nesnenin adresini gönderiyoruz o yüzden o nesne üzerinde değişiklik yapabilir. fakat swap çalışmıyordu çünkü ona primitive değişkenlerin kopyasını gönderiyorduk ve metot bitince kopyalar da siliniyordu.

