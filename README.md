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

License
=======


    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
