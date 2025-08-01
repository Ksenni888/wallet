# Описание
Приложение, которое по REST принимает запрос вида

 POST api/v1/wallet
 
 {
 
 valletId: UUID,
 
 operationType: DEPOSIT or WITHDRAW,
 
 amount: 1000
 
 }
 
 после выполняет логику по изменению счета в базе данных.
 
 Также есть возможность получить баланс кошелька
 
 GET api/v1/wallets/{WALLET_UUID}

 Приложение запускается в докер контейнере, база данных тоже, вся система
 должна подниматься с помощью docker-compose.

 # Версии
 стек:
 java 11
 Spring 2
 Postgresql
 
 Написаны миграции для базы данных с помощью liquibase.
