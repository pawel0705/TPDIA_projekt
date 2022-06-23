# Wykrywanie pomiarów OLAP

Założeniem projektu była analiza artykułu “Automatic Machine Learning-based OLAP Measure Detection for Tabular Data“ opracowanego przez Y. Yang, F. Abdelhédi, F. Ravat oraz O. Teste i implementacja własnego rozwiązania w języku Java. Artykuł przedstawia sposób na wykrywanie kolumn z tabel zawierających różne informacje, które przechowują pomiary z danej dziedziny problemowej. We własnej implementacji użyto języka Java i biblioteki Weka API oferującej implementacje algorytmów klasyfikacyjnych uczenia maszynowego i mechanizmy odczytywania plików o formacie CSV.

Github autorów artykułu: https://github.com/Implementation111/measure-detection

Wyniki były ustalane na podstawie miar opracowanych przez autorów artykułu: recall (R), precission (P), F-measure (F).

Użyto algorytmów: K*, SVM z kernelem RBF, k-najbliższych sąsiadów (KNN), random forest.

## Uzyskane wyniki

Tabela przedstawia wyniki poprawności klasyfikacji dla każdego z użytych algorytmów uczenia maszynowego, w wykrywaniu kolumny zawierającej pomiary, według trzech miar wydajności zdefiniowanych przez autorów.

<p align="center">
<br>
<img src="/images/Screenshot_1.png" width="50%"/>
</p>

## Współautor
Projekt współtworzony był wraz z:
- [OftenDeadKanji](https://github.com/OftenDeadKanji)
