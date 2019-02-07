# ТЗ 
## общее
- Есть система учета задач, в которой есть пользователи и задачи, назначенные на них.
- На одной задаче может быть только один ответственный. 
- Каждый пользователь может забрать неограниченное количество задач
- Если пользователь выходит из системы
    - его статус меняется на offline
    - нужно распределить его задачи на других онлайн пользователей с учетом их загруженности(максимально равномерно распределить).

## rest
Нужно реализовать rest сервис
- (online, offline)
- забирать на себя задачи и переводить их в различные статусы(завершено, отклонено)
- создавать задачи.

## Документация (api c примерами)
- [пользователь](https://documenter.getpostman.com/view/6588996/RztoMTwv)
- [задачи](https://documenter.getpostman.com/view/6588996/RztoMojU)

## Пример работы работы системы, когда пользователь выходит (ставит статус offline)
1. До начало работы
<details>
<summary>1.1 Список полозователей на текущий момент</summary>

Пользователь 4 не должен участвовать, т.к. он offline
Должны задачи взять пользовать 2 и 3
```json
[
    {
        "id": 1,
        "username": "user1",
        "email": "email1",
        "status": "Online"
    },
    {
        "id": 2,
        "username": "user2",
        "email": "email2",
        "status": "Online"
    },
    {
        "id": 3,
        "username": "user3",
        "email": "email3",
        "status": "Online"
    },
    {
        "id": 4,
        "username": "user4",
        "email": "email4",
        "status": "Offline"
    }
]
```
</details>

<detail>
<summary>1.2 Список всех задач</summary>

```json
[
    {
        "id": 5,
        "name": "name task1",
        "userId": 1,
        "status": "Open"
    },
    {
        "id": 6,
        "name": "name task2",
        "userId": 1,
        "status": "Open"
    }
]
```
</details>

2. Ставим статус offline пользователю 1
3. Смотрим, что изменилось

<detail>
<summary>3.1 Снова список всех задач</summary>

```json
[
    {
        "id": 5,
        "name": "name task1",
        "userId": 2,
        "status": "Open"
    },
    {
        "id": 6,
        "name": "name task2",
        "userId": 3,
        "status": "Open"
    }
]
```
</details>






