# E4 — isPalindrome Trace Table

Her satır **bir tur**. `left` ve `right` turun **başındaki** değerler.
"Sonuç" sütunu: o turda ne oldu → `devam` (left/right ilerler), `return false` veya `döngü biter → return true`.
Son satır: döngü koşulunun kontrol edildiği ve **sağlanmadığı** an ya da `return false` olan tur. Fazla satırları sil.

## Tablo 1 — `"racecar"`  (length = 7)

```
indeks:  0   1   2   3   4   5   6
        'r' 'a' 'c' 'e' 'c' 'a' 'r'
```

| Tur | left | right | left < right ? | charAt(left) | charAt(right) | Sonuç |
|---  |---   |---    |---             |---           |---            |---    |
| 1   |  0   |  6    |    true        |    r         |      r        | devam |
| 2   | 1    |   5   | true           | a            | a             |  devam|
| 3   | 2    |  4    |  true          |    c         |       c       | devam |
| 4   | 3    |   3   |  false         |  e           |     e         |  true |

## Tablo 2 — `"abca"`  (length = 4)

```
indeks:  0   1   2   3
        'a' 'b' 'c' 'a'
```

| Tur | left | right | left < right ? | charAt(left) | charAt(right) | Sonuç |
|---|---|---|---|---|---|---|
| 1 | 0|3 |true | a| a| devam|
| 2 | 1| 2| true| b| c| false|

## Sorular

1. Kodda `left` ve `right` ilk değerleri (`s.length()` cinsinden):
   - `left =s.length() - s.length()`
   - `right = s.length() - 1`
2. `"abca"`'da farklı karakteri bulduğun turdan sonra döngü devam etmeli mi? Neden?
   etmemeli çünkü bu noktadan sonra sonucun doğru çıkması mümkün değil, devam edilirse boşuna döngü çalışmış olur.
3. Tek karakterli (`"a"`) ve boş (`""`) String için metot ne döndürür? Döngüye hiç girer mi?
   true döndürür. döngüye girer bir kez çalışır ve true döner.
