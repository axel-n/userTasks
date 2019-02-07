package com.example.userTasks.service;

import com.example.userTasks.model.Task;
import com.example.userTasks.model.User;
import com.example.userTasks.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class PutTaskForUsers {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskRepository taskRepository;

    private TreeMap<Integer, ArrayList<Integer>> countTaskPerUser = new TreeMap<>();
    private TreeMap<Integer, ArrayList<Integer>> result = new TreeMap<>();
    private ArrayList<Integer> currentListAllUsers;

    private ArrayList<Integer> nextListModUsers;

    private int currentCount;
    private int currentResultKey;
    private int firstElement;

    /**
     * Управляем правильным распределением задач на других пользователей
     * Подготавливает данные в формате - количество задач и список пользователей
     *
     * Например, нужно положить 3 задачи.
     * подготовили такую карту {0=[2, 3], 1=[6], 2=[4], 5=[5]}
     * Должны получить на выходе такую карту (только изменные пользователи) {1=[2, 3], 2=[6]}
     *
     * @param unsignedTasks
     * @param usersOnline
     * @return карту с количеством задач и списком изменных пользователей.
     */
    public TreeMap<Integer, ArrayList<Integer>> putTasks(List<Task> unsignedTasks, List<User> usersOnline) {
        int countUnsignedTask =  prepareData(unsignedTasks, usersOnline);
        fillLists(countUnsignedTask);

        return result;
    }

    /**
     * @param countTasks количество задач, которые нужно обработать
     */
    private void fillLists(int countTasks) {

        for (int i = 1; i <= countTasks; i++) {
            if (countTaskPerUser.firstEntry().getValue().size() == 0) {
                countTaskPerUser.remove(currentCount);
            }
            initStep();

            // сохраняем текущую пару без 0 значения в списке
            countTaskPerUser.replace(currentCount, currentListAllUsers);

            // добавляем первый элемент (который уже удалил из списка)
            if (nextListModUsers == null) {
                result.put(currentResultKey, new ArrayList<>(List.of(firstElement)));
            } else {
                nextListModUsers.add(firstElement);
                result.replace(currentResultKey, nextListModUsers);
            }
        }
        log.info("после обработки неназначенных задач, получили карту (только для изменных пользователей): " + result);
    }

    /**
     * подготовка данных в обрабатываемый формат
     *
     * @param unsignedTasks
     * @param usersOnline
     * @return количество неназначенных задач
     */
    private int prepareData(List<Task> unsignedTasks, List<User> usersOnline) {

        if (usersOnline.size() > 0 && unsignedTasks.size() > 0) {
            log.info("начали обработку задач (пользователя, который вышел) для других пользователей (онлайн)");
            log.info("количество неназначенных задач: {}, количество других онлайн пользователей: {}",
                    unsignedTasks.size(), usersOnline.size());

            for (User user : usersOnline) {

                int userId = user.getId();
                int currentKey = taskRepository.findByUserId(userId).size();

                // если пара существует (запоминаем)
                if (countTaskPerUser.get(currentKey) != null) {
                    ArrayList<Integer> currentListUsers = countTaskPerUser.get(currentKey);
                    currentListUsers.add(userId);
                    countTaskPerUser.replace(currentKey, currentListUsers);
                } else {
                    countTaskPerUser.put(currentKey, new ArrayList<>(List.of(userId)));
                }
            }
            log.info("после подготовки, получили карту (количество задач и список пользователей): {}", countTaskPerUser);
        }

        return unsignedTasks.size();
    }

    /**
     * сохраняем текущие пары, удаляем первый элемент
     */
    private void initStep() {
        currentCount = countTaskPerUser.firstKey();
        currentListAllUsers = countTaskPerUser.get(currentCount);

        firstElement = currentListAllUsers.get(0);

        if (result.firstEntry() != null) {
            currentResultKey = countTaskPerUser.firstKey() + 1;
        } else {
            currentResultKey = 1;
        }
        nextListModUsers = result.get(currentResultKey);

        currentListAllUsers.remove(0);
    }
}
