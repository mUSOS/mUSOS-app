mUSOS-app
=========

Mobilny klient USOS na system Android. 


Budowa projektu
====
1) Pobierz projekt
```
git clone https://github.com/mUSOS/mUSOS-app.git
```
2) Przejdź do folderu projektu
```
cd mUSOS-app
```
3) Dodaj własne klucze w plikach
```
mUSOS/src/main/AndroidManifest.xml
mUSOS/src/main/java/pl/edu/amu/usos/adapter/UniversityAdapter.java
```
4) Zbuduj projekt poleceniem
```
./gradlew clean assemble
```
5) Zbudowany program apk będzie dostępny w katalogu
```
USOS/build/outputs/apk
```
