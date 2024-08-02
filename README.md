# Banking Application API

## Описание

Это приложение представляет собой банковскую систему, позволяющую создавать учетные записи, вносить, снимать и переводить деньги между счетами, а также просматривать историю транзакций.

## Технологии

•  Java 17

•  Spring Boot

•  Spring Data JPA

•  Spring Security

•  PostgreSQL

•  Swagger

•  WireMock

•  Lombok

•  Flyway


## Установка и настройка

<details>
<summary>Шаг 1: Клонирование репозитория</summary>


git clone https://github.com/xJOHNJAx/BankProject.git
cd BankProject

</details>

<details>
<summary>Шаг 2: Настройка базы данных</summary>

Создайте базу данных PostgreSQL и настройте подключение в файле src/main/resources/application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/banking_app
spring.datasource.username=aston
spring.datasource.password=aston
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

</details>

<details>
<summary>Шаг 3: Заполнение базы данных исходными данными</summary>

Вариант 1: Использование data.sql
Создайте файл src/main/resources/data.sql и добавьте в него SQL-запросы для вставки исходных данных:

INSERT INTO account (id, account_number, balance, recipient_name, pin_code) VALUES
(1, '1234567890', 1000.00, 'Ivan Ivanov', '1234'),
(2, '0987654321', 2000.00, 'Den Krylov', '5678');

INSERT INTO transaction (id, account_id, amount, type, timestamp) VALUES
(1, 1, 1000.00, 'DEPOSIT', NOW()),
(2, 2, 2000.00, 'DEPOSIT', NOW());

Вариант 2: Использование сервиса инициализации данных
Создайте сервис для инициализации данных при запуске приложения:

package org.example.bankingapp.service;

import org.example.bankingapp.model.Account;
import org.example.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class DataInitializationService {

@Autowired
private AccountRepository accountRepository;

@PostConstruct
public void init() {
Account account1 = new Account("Ivan Ivanov", "1234");
account1.setBalance(new BigDecimal("1000.00"));
accountRepository.save(account1);

Account account2 = new Account("Den Krylov", "5678");
account2.setBalance(new BigDecimal("2000.00"));
accountRepository.save(account2);
}
}

</details>

<details>
<summary>Шаг 4: Запуск приложения</summary>

mvn spring-boot:run

</details>

<details>
<summary>Шаг 5: Доступ к документации Swagger
</summary>

После запуска приложения вы можете получить доступ к документации Swagger по адресу: http://localhost:8080/swagger-ui.html.
</details>

<details>
<summary>Использование API
Создание учетной записи</summary>

POST /api/accounts

{
"recipientName": "Ivan Ivanov",
"pinCode": "1234"
}

Внесение средств
POST /api/accounts/{id}/deposit

{
"amount": 100.00
}

Снятие средств
POST /api/accounts/{id}/withdraw

{
"amount": 50.00,
"pinCode": "1234"
}

Перевод средств
POST /api/accounts/{id}/transfer

{
"toAccountId": 2,
"amount": 25.00,
"pinCode": "1234"
}

Получение всех счетов
GET /api/accounts/all

Получение счета по ID
GET /api/accounts/{id}

Получение транзакций по ID счета
GET /api/accounts/{id}/transactions

Получение всех счетов (только для администраторов)
GET /api/accounts/admin/all</details>

<details>
<summary>WireMockServer и WireMockTest</summary>
WireMockServer
WireMock используется для создания мока HTTP-сервисов. Это позволяет тестировать взаимодействие с внешними сервисами без необходимости их реального вызова.

WireMockTest
Пример теста с использованием WireMock:

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.awaitility.Awaitility.await;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Slf4j
public class WireMockTest {
private static GenericContainer<?> wireMockContainer;

@BeforeAll
public static void setUp() {
wireMockContainer = new GenericContainer<>(DockerImageName.parse("rodolpheche/wiremock"))
.withExposedPorts(8080)
.withCommand("--verbose")
.withStartupTimeout(Duration.ofMinutes(2))
.withLogConsumer(new Slf4jLogConsumer(log));
wireMockContainer.start();
log.info("WireMock container started at: {}:{}", wireMockContainer.getHost(), wireMockContainer.getMappedPort(8080));

WireMock.configureFor(wireMockContainer.getHost(), wireMockContainer.getMappedPort(8080));
stubFor(get(urlEqualTo("/hellowiremock"))
.willReturn(aResponse()
.withStatus(200)
.withBody("{\"message\": \"Hello, WireMock!\"}")));

await().atMost(10, TimeUnit.SECONDS).until(wireMockContainer::isRunning);
}

@AfterAll
public static void tearDown() {
if (wireMockContainer != null && wireMockContainer.isRunning()) {
wireMockContainer.stop();
log.info("WireMock container stopped.");
}
}

@Test
public void testWireMock() {
if (wireMockContainer == null || !wireMockContainer.isRunning()) {
throw new IllegalStateException("WireMock container is not running");
}

String wireMockUrl = "http://" + wireMockContainer.getHost() + ":" + wireMockContainer.getMappedPort(8080);

try (GenericContainer<?> ignored = wireMockContainer) {
given()
.when()
.get(wireMockUrl + "/hellowiremock")
.then()
.statusCode(200)
.body("message", equalTo("Hello, WireMock!"));
} catch (Exception e) {
log.error("Error during WireMock test", e);
}
}
}</details>


<details>
<summary>Использование Flyway</summary>
Для управления миграциями базы данных используется Flyway. Flyway автоматически применяет миграции при запуске приложения.

Настройка Flyway
1. 
Добавьте зависимость Flyway в pom.xml:

<dependency>
<groupId>org.flywaydb</groupId>
<artifactId>flyway-core</artifactId>
</dependency>

1. 
Создайте директорию для миграций:

mkdir -p src/main/resources/db/migration

1. 
Создайте файл миграции, например, V1__init.sql:

-- Создание таблицы account
CREATE TABLE account (
id SERIAL PRIMARY KEY,
account_number VARCHAR(20) NOT NULL,
balance DECIMAL(19, 2) NOT NULL,
recipient_name VARCHAR(100) NOT NULL,
pin_code VARCHAR(4) NOT NULL
);

-- Вставка исходных данных в таблицу account
INSERT INTO account (account_number, balance, recipient_name, pin_code) VALUES
('1234567890', 1000.00, 'Ivan Ivanov', '1234'),
('0987654321', 2000.00, 'Den Krylov', '5678');

-- Создание таблицы bank_transaction
CREATE TABLE bank_transaction (
id SERIAL PRIMARY KEY,
account_id BIGINT NOT NULL,
amount DECIMAL(19, 2) NOT NULL,
type VARCHAR(20) NOT NULL,
timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (account_id) REFERENCES account(id)
);

-- Вставка исходных данных в таблицу bank_transaction
INSERT INTO bank_transaction (account_id, amount, type) VALUES
(1, 1000.00, 'DEPOSIT'),
(2, 2000.00, 'DEPOSIT');

1. 
Настройте Flyway в application.properties:

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true</details>

Принятые решения при разработке
1. Использование Spring Boot и Spring Data JPA: Эти технологии были выбраны для упрощения разработки и управления базой данных.
2. PostgreSQL: Выбор PostgreSQL в качестве базы данных обусловлен его надежностью и широкими возможностями.
3. Spring Security: Добавлен уровень авторизации с разграничением по ролям для обеспечения безопасности.
4. Валидация PIN-кода: Все операции по списанию средств требуют правильного PIN-кода для обеспечения безопасности.
5. История транзакций: Все изменения баланса сохраняются в истории транзакций для обеспечения прозрачности и отслеживания операций.
6.  Заполнение базы данных исходными данными: Добавлены сценарии для заполнения базы данных исходными данными при запуске приложения.

Если у вас возникнут вопросы или потребуется дополнительная помощь, пишите: https://join.skype.com/zm3rWLuIjC3O
