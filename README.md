
Руководство по подключению базы данных и запуску SUT

Откройте склонированный проект в среде разработки Intellij IDEA.


Чтобы запустить контейнеры с MySQL, PostgreSQL и Node.js, выполните команду:

docker-compose up -d --force-recreate


Запуск SUT:

Для MySQL выполните следующую команду в терминале:
 
 java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar

Для PostgreSQL используйте следующую команду в терминале:
 
 java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar

Запуск тестов (с использованием Allure):

Для выполнения тестов с MySQL выполните команду:
 
 gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app allureReport

Для запуска тестов с PostgreSQL выполните команду:
 
 gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app allureReport 



Откройте в вашем веб-браузере (например, Google Chrome) ссылку:

http://localhost:8080


Чтобы получить отчет Allure в браузере, выполните команду:

gradlew allureServe


По завершении тестов остановите работу приложения с помощью сочетания клавиш Ctrl+C и завершите работу контейнеров, выполнив команду:
docker-compose down