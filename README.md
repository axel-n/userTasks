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



