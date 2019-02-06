package com.example.userTasks.service;

import com.example.userTasks.model.Task;
import com.example.userTasks.model.User;
import com.example.userTasks.repository.TaskRepository;
import com.example.userTasks.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PutTaskForUsers {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private TreeMap<Integer, ArrayList<Integer>> countTaskPerUser = new TreeMap<>();
    private ArrayList<Integer> currentListUsers = new ArrayList<>();
    private ArrayList<Integer> nextListUsers;
    private int currentCount;
    private int firstElement;

    public TreeMap<Integer, ArrayList<Integer>> putTasks(int offlineUserId) {
        int countUnsignedTask =  prepareData(offlineUserId);
        fillLists(countUnsignedTask);

        return countTaskPerUser;
    }

    private void fillLists(int countTasks) {

        for (int i = 1; i <= countTasks; i++) {
            if (countTaskPerUser.firstEntry().getValue().size() == 0) {
                countTaskPerUser.remove(currentCount);
            }
            initStep();

            // сохраняем текущую пару без 0 значения в списке
            countTaskPerUser.replace(currentCount, currentListUsers);

            // если следующая пара без элементов
            if (nextListUsers == null) {
                countTaskPerUser.put(currentCount + 1, new ArrayList<>(List.of(firstElement)));
            } else {
                nextListUsers.add(firstElement);
                countTaskPerUser.replace(currentCount + 1, nextListUsers);
            }
        }
        log.info("after processing unsigned tasks. receive map: " + countTaskPerUser);

    }


    private int prepareData(int offlineUserId) {

        List<Task> unsignedTasks = taskRepository.findByUserId(offlineUserId);
        List<User> usersOnline = userRepository.findByStatus(User.Status.Online);

        if (usersOnline.size() > 0 && unsignedTasks.size() > 0) {
            log.info("start assign tasks to another online users.");
            log.info("count unsigned tasks: {}, count another online users: {}",
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
            log.info("after prepare data, receive map tasks per user: {}", countTaskPerUser);
        }

        return unsignedTasks.size();
    }

    private void initStep() {
        Map.Entry<Integer, ArrayList<Integer>> entry = countTaskPerUser.firstEntry();

        // запоминаем (если есть) следующий список пользователей
        nextListUsers = countTaskPerUser.get(currentCount + 1);

        currentListUsers = entry.getValue();
        currentCount = entry.getKey();
        firstElement = currentListUsers.get(0);
        currentListUsers.remove(0);
    }
}
