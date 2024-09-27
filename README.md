
### Запуск тестов 
1) Открыть проект в IntelliJ IDEA;
2) Создать контейнеры в скопированном проекте `docker-compose up -d`;


#### Для проверки MySQL:
1) Запустить jar-файл с базой данных MySQL командой: `java "-Dspring.datasource.url=jdbc:mysql://185.119.57.126:3306/app" -jar artifacts/aqa-shop.jar`;

2) Убедиться в готовности системы. Приложение должно быть доступно по адресу: `http://localhost:8080/`;

3) В новой вкладке терминала запустить тесты командой: `./gradlew clean test "-Ddb.url=jdbc:mysql://185.119.57.126:3306/app"` ;

4) Для создания отчета запустить команду:`./gradlew allureServe`, `./gradlew allureReport`

#### Для проверки PostgreSQL:
1) В новой вкладке терминала запустить тестируемое приложение командой: `java "-Dspring.datasource.url=jdbc:postgresql://185.119.57.126:5432/app" -jar artifacts/aqa-shop.jar`;

2) Убедиться в готовности системы. Приложение должно быть доступно по адресу: `http://localhost:8080/`;

3) В новой вкладке терминала запустить тесты командой: `.\gradlew clean test "-Ddb.url=jdbc:postgresql://185.119.57.126:5432/app"`  ;

4) Для создания отчета запустить команду: `./gradlew allureServe`,`./gradlew allureReport`

 - Для остановки приложений использовать команду `Cntrl C`.
- Для удаления контейнеров использовать команду `docker-compose down`